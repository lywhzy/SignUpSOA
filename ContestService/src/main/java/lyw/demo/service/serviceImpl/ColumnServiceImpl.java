package lyw.demo.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.mapper.AlternativeMapper;
import lyw.demo.mapper.Column_infoMapper;
import lyw.demo.mapper.Column_valueMapper;
import lyw.demo.mapper.ContestMapper;
import lyw.demo.pojo.*;
import lyw.demo.service.Cache.CacheImpl.ColumnValueCache;
import lyw.demo.service.Cache.CacheService;
import lyw.demo.service.ColumnService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private Column_infoMapper column_infoMapper;

    @Autowired
    private Column_valueMapper column_valueMapper;

    @Autowired
    private AlternativeMapper alternativeMapper;

    @Autowired
    @Qualifier("alternativeCache")
    private CacheService cacheService;

    @Autowired
    private ColumnValueCache columnValueCache;

    static ConcurrentHashMap<String,String> keyName = null;

    static{
        keyName = new ConcurrentHashMap<String,String>();
        keyName.put("手机号","PhoneNumber");
        keyName.put("邮箱","Email");
        keyName.put("姓名","Name");
        keyName.put("学号","Number");
        keyName.put("院系","Faculty");
        keyName.put("班级","Clazz");
        keyName.put("专业","Profession");
        keyName.put("入学年份","Enrolment");
        keyName.put("照片","Picture");
    }

    @Override
    public Column_value getCvByUCId(int uid, int cid) {
        return null;
    }

    @Override
    public List<Column_info> listByCId(int cid,int uid) {
        List<Column_info> list = listByCIdForCache(cid,uid);

        setColumnListForValue(list,uid);
        setColumnListForAlters(list);
        return list;
    }

    @Override
    public List<Column_info> listByCIdForCache(int cid,int uid){
        Example example = new Example(Column_info.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid",cid);
        //使用索引优化查询速度
        criteria.andGreaterThan("id",0);
        example.setOrderByClause("sequence asc");
        List<Column_info> list = column_infoMapper.selectByExample(example);
        return list;
    }

    @Override
    public Column_info getInfo(int cid) {
        return column_infoMapper.selectByPrimaryKey(cid);
    }

    @Override
    public void delete(Column_value column_value) {

    }

    @Override
    public void update(Column_info column_info,String type) {
        switch (type){
            case "update":
                column_infoMapper.updateByPrimaryKey(column_info);
                break;
            case "delete":
                column_infoMapper.delete(column_info);
                break;
            case "insert":
                column_infoMapper.insert(column_info);
        }
    }

    @Override
    public void setColumnListForValue(List<Column_info> list,int uid){

        int type = 0; //未保存数据，未提交数据

        if(list==null||list.isEmpty()) return;

        Column_value column_value = columnValueCache.selectCache(new Integer[]{uid,list.get(0).getCid(),list.get(0).getId()});

        if(column_value!=null){
            type = -1;//保存数据
        }else if (contestMapper.selectRelation(uid,list.get(0).getCid())!=null){
            type = 1;//提交数据
        }

        if(type==1){
            list.forEach((column_info)->{
                setColumnForValue(column_info,uid);
            });
        }else if(type==0){
            list.forEach((column_info)->{
                try {
                    setColumnForTkValue(column_info,uid);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }else{
            list.forEach((column_info)->{
                setColumnForValueFromCache(column_info,uid);
            });
        }

    }

    private void setColumnForTkValue(Column_info column_info,int uid) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

//        User user = userService.get(uid);
//        User_info user_info = userService.getInfo(uid);
//        String key = column_info.getName();
//        if(user==null&&user_info==null) return;
//        Class clazz = null;
//        String value = null;
//        String methodName = null;
//        if(keyName.containsKey(key))
//            methodName = keyName.get(key);
//        else return;
//        methodName = "get" + methodName;
//        if(key.equals("手机号")||key.equals("邮箱")){
//            clazz = User.class;
//            Method method = clazz.getMethod(methodName,null);
//            value = (String) method.invoke(user,null);
//        }else{
//            clazz = User_info.class;
//            Method method = clazz.getMethod(methodName,null);
//            value = (String) method.invoke(user_info,null);
//        }
//        if(!StringUtils.isBlank(value))
//            column_info.setValue(value);
    }

    private void setColumnForValueFromCache(Column_info column_info,int uid){
        Column_value column_value = columnValueCache.selectCache(new Integer[]{uid,column_info.getCid(),column_info.getId()});
        if(column_value!=null&& !StringUtils.isBlank(column_value.getValue()))
            column_info.setValue(column_value.getValue());
    }

    private void setColumnForValue(Column_info column_info,int uid){
        Example example = new Example(Column_value.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid",column_info.getId()).andEqualTo("uid",uid);
        Column_value column_value = column_valueMapper.selectOneByExample(example);
        if(column_value!=null&& !StringUtils.isBlank(column_value.getValue()))
            column_info.setValue(column_value.getValue());
    }

    private void setColumnForAlters(Column_info column_info){

        List<Alternative> alternatives = cacheService.selectListCache(column_info.getId());

        if(alternatives==null||alternatives.isEmpty()){
            Example example = new Example(Alternative.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("cid",column_info.getId());
            alternatives = alternativeMapper.selectByExample(example);
            cacheService.setListCache(alternatives);
        }

        List<String> list = new ArrayList<>();
        for(Alternative alternative : alternatives){
            list.add(alternative.getValue());
        }
        column_info.setAlternatives(list);
    }

    @Override
    public void setColumnListForAlters(List<Column_info> list){
        list.forEach((column_info -> {
            setColumnForAlters(column_info);
        }));
    }



}
