package lyw.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lyw.demo.mapper.AlternativeMapper;
import lyw.demo.mapper.Column_InfoMapper;
import lyw.demo.mapper.Column_valueMapper;
import lyw.demo.mapper.ContestMapper;
import lyw.demo.pojo.*;
import lyw.demo.service.AlternativeService;
import lyw.demo.service.AuditService;
import lyw.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
@CrossOrigin
public class RestController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;

    @Autowired
    private AlternativeService alternativeService;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    private Column_InfoMapper column_infoMapper;

    @Autowired
    private AlternativeMapper alternativeMapper;

    @Autowired
    private Column_valueMapper column_valueMapper;


    /**
     * 得到未通过审核的比赛列表
     * @return
     */
    @GetMapping("Audit")
    public List<Contest> findNotAudit(){
        return auditService.findNotAudit();
    }

    /**
     * 得到该管理员已审核的比赛
     * @param uid
     * @return
     */
    @GetMapping("getMyContest")
    public List<Contest> getContestByUid(int uid){
        List<Contest> list = auditService.selectByUid(uid);
        return  list;
    }

    @GetMapping("getAllCVByCid")
    public String getAllCVByCid(int cid){
        return null;
    }

    @PostMapping("editContest")
    public String editContest(@RequestBody String jsonStr){
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONObject c = jsonObject.getJSONObject("contest");
        Contest contest = JSONObject.parseObject(c.toJSONString(),Contest.class);
        JSONArray array = jsonObject.getJSONArray("list");

        System.out.println(contest);
        System.out.println(array.toJSONString());

        contestMapper.updateByPrimaryKeySelective(contest);

        //删除该比赛所有可选值和数据
        List<Column_info> list = auditService.findByCid(contest.getId());
        list.forEach(column_info -> {
            List<Alternative> list1 = alternativeService.findAllByCid(column_info.getId());
            list1.forEach(alternative -> alternativeService.delete(alternative.getId()));

            Example example = new Example(Column_value.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("cid",column_info.getId());
            column_valueMapper.deleteByExample(example);
        });

        //删除该比赛所有栏目
        Example example = new Example(Column_info.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid",contest.getId());
        column_infoMapper.deleteByExample(example);

        //删除报名用户关系
        column_valueMapper.deleteContestRelation(contest.getId());


        for(int i = 0;i < array.size(); ++i){
            JSONObject object = (JSONObject) array.get(i);
            Column_info column_info = new Column_info();
            column_info.setName(object.getString("name"));
            column_info.setIcontype(object.getString("type"));
            column_info.setCid(contest.getId());
            column_info.setChoose(true);
            column_info.setSequence(i+1);
            column_infoMapper.insert(column_info);

            column_info = column_infoMapper.selectOne(column_info);

            JSONArray jsonArray = (JSONArray) object.get("options");
            for(int j = 0;j < jsonArray.size();++j){
                String value = jsonArray.getString(j);
                Alternative alternative = new Alternative();
                alternative.setValue(value);
                alternative.setUser_permit(false);
                alternative.setCid(column_info.getId());
                alternativeMapper.insert(alternative);
            }
        }

        return "true";
    }


    @GetMapping("getContest")
    public Contest getContest(int cid){
        Contest contest = auditService.getContest(cid);
        return contest;
    }

    /**
     * 得到该管理员未上线的比赛
     * @param uid
     * @return
     */
    @GetMapping("getMyContestDis")
    public List<Contest> getContestByUidDis(int uid){
        List<Contest> list = auditService.selectDis(uid);
        return  list;
    }

    /**
     *  根据比赛id获得该比赛的栏目信息
     * @param cid 比赛id
     * @return
     */
    @GetMapping("findCI")
    public List<Column_info> findCI(int cid){
        List<Column_info> list = auditService.findByCid(cid);
        for (Column_info column_info : list) {
            List<String> slist = new ArrayList<>();
            List<Alternative> alternatives = alternativeService.findAllByCid(column_info.getId());
            alternatives.forEach(alternative -> slist.add(alternative.getValue()));
            column_info.setAlternatives(slist);
        }
        return list;
    }

    @GetMapping("findAlter")
    public List<Column_info> findAlter(int cid){
        List<Column_info> list = auditService.findByCid(cid);

        List<Column_info> realList = new ArrayList<>();

        list.forEach(column_info -> {
            if(column_info.getIcontype().equals("下拉框")){
                realList.add(column_info);
            }
        });

        for (Column_info column_info : realList) {
            List<Alternative> alternatives = alternativeService.findAllByCid(column_info.getId());
            column_info.setAlternativeList(alternatives);
        }
        return realList;
    }


    /**
     * 修改比赛审核状态以及可见状态
     * @param cid
     * @return
     */
    @PutMapping("CheckStatus")
    public String updateCheckStatus(int cid){

        try{
            auditService.UpdateCheckStatus(cid);
        }catch (Exception e){
            return "false";
        }
        return "true";
    }


    /**
     * 修改可见状态
     * @param cid 比赛id
     * 调用格式为： Display?cid=??
     * method :get
     * @return
     */
    @GetMapping("Display")
    public String updateDisplay(int cid){
        try{
            auditService.UpdateDisplay(cid);
        }catch (Exception e){
            return "false";
        }
        return "true";
    }

    @GetMapping("user")
    public List<User> findRoots(){
        return userService.findRoots();
    }


    /**
     * 删除管理员用户
     * @param id
     * @return
     */
    @GetMapping("deleteUser")
    public String deleteUser(int id){
        try{
            userService.daleteRoot(id);
        }catch (Exception e){
            return "false";
        }
        return "true";
    }

    /**
     * 注册管理员用户
     * @return
     */
    @PostMapping("user")
    public String registerRoot(@RequestBody Map map){
        System.out.println(123);
        try{
            String username = (String) map.get("username");
            String password = (String) map.get("password");
            if(userService.findUserByname(username)!=null) return "该用户已注册";
            userService.registerRoot(username,password);
        }catch (Exception e){
            return "注册异常";
        }
        return "注册成功";
    }

    @GetMapping("alter")
    public List<Alternative> listAlters(int cid){
        List<Alternative> list = alternativeService.findAllByCid(cid);
        return list;
    }

    @GetMapping("deleteAlter")
    public String deleteAlter(int id){
        try{
            alternativeService.delete(id);
        }catch (RuntimeException e){
            return "false";
        }
        return "true";
    }

    @PostMapping(value = "alter")
    public String insertAlter(@RequestBody String jsonStr){

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        int cid = Integer.parseInt(jsonObject.getString("cid"));
        String name = (String) jsonObject.get("name");
        String optionname = (String) jsonObject.get("optionname");

        Alternative alternative = new Alternative();
        alternative.setValue(optionname);
        alternative.setUser_permit(false);


        Example example = new Example(Column_info.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid",cid);
        criteria.andEqualTo("name",name);
        Column_info column_info = column_infoMapper.selectOneByExample(example);

        alternative.setCid(column_info.getId());

        try{
            alternativeService.insert(alternative);
        }catch(RuntimeException e){
            return "false";
        }
        return "true";
    }
}
