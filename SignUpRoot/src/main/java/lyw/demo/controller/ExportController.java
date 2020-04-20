package lyw.demo.controller;


import lyw.demo.mapper.Column_valueMapper;
import lyw.demo.pojo.Column_info;
import lyw.demo.pojo.Column_value;
import lyw.demo.pojo.Contest;
import lyw.demo.pojo.vo.LanQiao;
import lyw.demo.service.AuditService;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 需更改数据库连接driver、url、sql语句等，固定写死，仅做工具类使用，不可反复使用
 * 还需更改数据库接收的实体javabean（如user），替换user即可
 */

@Controller
public class ExportController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private Column_valueMapper column_valueMapper;

    @RequestMapping(value = "/download", produces = { "application/json;charset=UTF-8" }, method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView downloadFile(HttpServletResponse response,int cid) {
        List<List<Column_value>> userlList = new ArrayList<>();

        List<Column_info> list = auditService.findByCid(cid);

        List<String> strings = new ArrayList<>();
        list.forEach(column_info -> strings.add(column_info.getName()));

        Contest contest = auditService.getContest(cid);


        // 数据库表信息更改头部信息，只需在此更改
        String[] headers = strings.toArray(new String[0]);


        Example example = new Example(Column_value.class);
        Example.Criteria criteria = example.createCriteria();

        for (Column_info column_info : list) {
            criteria = criteria.orEqualTo("cid",column_info.getId());
        }
        example.setOrderByClause("id asc");

        List<Column_value> list1 = column_valueMapper.selectByExample(example);


        List<Column_value> stringList = null;
        for(int i = 0;i < list1.size();++i){
            if(i % headers.length == 0){
                stringList = new ArrayList<>();
                stringList.add(list1.get(i));
            }else if(i % headers.length == headers.length-1){
                stringList.add(list1.get(i));
                stringList.sort(Comparator.comparing(Column_value::getCid));
                userlList.add(stringList);
                System.out.println(Arrays.toString(stringList.toArray()));
            }else{
                stringList.add(list1.get(i));
            }
        }

        // 可更改文件名
        String fileName = contest.getName() + "报名信息表";
        //ExportExcelUtil<User> ee= new ExportExcelUtil<User>();
        if (userlList != null){
            exportExcel(headers,userlList,fileName,response);
        }else {
            System.out.println("无结果集");
        }

        return null;
    }


    //产生输出
    public void getExportedFile(XSSFWorkbook workbook, String name, HttpServletResponse response) throws Exception {
        BufferedOutputStream fos = null;
        try {
            String fileName = name + ".xlsx";
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String( fileName.getBytes("ISO8859-1"), "UTF-8" ));
            fos = new BufferedOutputStream(response.getOutputStream());
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public void exportExcel(String[] headers, Collection dataset, String fileName, HttpServletResponse response){
        // 声明一个工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(fileName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 产生表格标题行
        XSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; ++i){
            XSSFCell cell = row.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        try{
            // 遍历集合数据，产生数据行
            Iterator it = dataset.iterator();
            int index = 0;
            while (it.hasNext()){
                index++;
                row = sheet.createRow(index);
                List<Column_value> lanQiao= (List<Column_value>) it.next();
                // 利用反射，根据javabean属性的先后顺序，动态调用get()方法得到属性
//                Field[] fields = lanQiao.getClass().getDeclaredFields();
                for (short i = 0; i < headers.length; ++i){
                    XSSFCell cell = row.createCell(i);
//                    Field field = fields[i];
//                    String fieldName = field.getName();
//                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//                    Class tcls = lanQiao.getClass();
//                    Method getMethod = tcls.getMethod(getMethodName, new Class[] {});
//                    Object value = getMethod.invoke(lanQiao, new Object[] {});
                    Object value = lanQiao.get(i).getValue();
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    // 其它数据类型都当做字符串简单处理
                    if (value != null && value != ""){
                        textValue = value.toString();
                    }
                    if (textValue != null){
                        XSSFRichTextString richTextString = new XSSFRichTextString(textValue);
                        cell.setCellValue(richTextString);
                    }
                }
            }
            getExportedFile(workbook, fileName, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
