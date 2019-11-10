package lyw.demo.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lyw.demo.pojo.Column_info;
import lyw.demo.pojo.Column_value;
import lyw.demo.service.Cache.CacheService;
import lyw.demo.service.ColumnService;
import lyw.demo.service.Request.Request;
import lyw.demo.service.Request.RequestImpl.ColumnSelectRequest;
import lyw.demo.service.Route.RouteService;
import lyw.demo.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@Slf4j
public class SignUpController extends BaseController{

    @Autowired
    private ColumnService columnService;
    @Autowired
    private SignUpService signUpService;

    @Autowired
    @Qualifier("columnInfoCache")
    private CacheService cacheService;

    @Autowired
    private RouteService routeService;


    @GetMapping("getInfoList")
    public List<Column_info> getciList(int cid){
        long startTime = System.currentTimeMillis();
        long time = 0L;
        long endTime = 0L;

        Integer uid = super.uid;
        if(uid==null) return null;

        Request request = new ColumnSelectRequest(columnService,cacheService,cid,uid);
        routeService.process(request);

        while(true){
            if(time > 200L){
                break;
            }
            try{
                List<Column_info> list = cacheService.selectListCache(cid);
                if(list==null||list.isEmpty()){
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    time = endTime - startTime;
                }else{
                    columnService.setColumnListForValue(list,uid);
                    columnService.setColumnListForAlters(list);
                    return list;
                }
            }catch (Exception e){

            }
        }
        return columnService.listByCId(cid,uid);
    }

    @GetMapping(value = "update")
    public String update(String jsonStr){
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            for(Map.Entry entry : jsonObject.entrySet()){
                Column_value column_value = getColumn_value(entry);
                signUpService.update(column_value);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
//        for(Column_value column_value : list){
//            column_value.setUid(1);
//            signUpService.update(column_value);
//        }
        return "success";
    }

    @GetMapping(value = "keep")
    public String keep(String jsonStr){
        try{
            System.out.println(jsonStr);
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            for(Map.Entry entry : jsonObject.entrySet()){
                Column_value column_value = getColumn_value(entry);
                signUpService.keep(column_value);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
//        for(Column_value column_value : list){
//            column_value.setUid(1);
//            signUpService.update(column_value);
//        }
        return "success";
    }

    @GetMapping(value = "signup")
    public String signup(String jsonStr){
        try{
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            for(Map.Entry entry : jsonObject.entrySet()){
                Column_value column_value = getColumn_value(entry);
                log.info(column_value.getValue());
                signUpService.signUp(column_value);
            }
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
//        for(Column_value column_value : list){
//            column_value.setUid(1);
//            signUpService.update(column_value);
//        }
        return "success";
    }

    private Column_value getColumn_value(Map.Entry entry) {
        Integer key = Integer.parseInt(entry.getKey().toString());
        if(super.uid==null) return null;
        String value = (String) entry.getValue();
        Column_value column_value = new Column_value();
        column_value.setUid(super.uid);
        column_value.setCid(key);
        column_value.setValue(value);
        return column_value;
    }

}
