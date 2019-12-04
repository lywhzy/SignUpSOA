package lyw.demo.controller;

import com.alibaba.fastjson.JSONObject;
import lyw.demo.pojo.Contest;
import lyw.demo.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class ContestController extends BaseController{

    @Autowired
    private ContestService contestService;


    @GetMapping("getCharacterization")
    public List<Contest> getTopContest(){
        List<Contest> list = contestService.getTopCharacterization();
        return list;
    }

    @GetMapping("getMyContest")
    public List<Contest> getContestByUid(){
        Integer uid = super.uid;
        if(uid == null) return null;
        List<Contest> list = contestService.listByUId(uid);
        return  list;
    }

    @GetMapping("getAllContest")
    public List<Contest> getAllContest(){
        List<Contest> list = contestService.getAll();
        return list;
    }

    @GetMapping("getContestById")
    public String getContest(int cid){
        String name = contestService.getById(cid).getName();

        Map<String,Object> map = new HashMap<>();

        map.put("name",name);

        JSONObject jsonObject = new JSONObject(map);

        return jsonObject.toJSONString();
    }

    @GetMapping("checkSignUp")
    public String CheckSignUp(int cid){
        Integer uid = super.uid;
        if(uid == null) return null;
        if(contestService.CheckSign(uid,cid)) return "true";
        else return "false";
    }
}
