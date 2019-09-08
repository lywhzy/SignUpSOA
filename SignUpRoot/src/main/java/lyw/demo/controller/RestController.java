package lyw.demo.controller;

import lyw.demo.pojo.Alternative;
import lyw.demo.pojo.Column_info;
import lyw.demo.pojo.Contest;
import lyw.demo.service.AlternativeService;
import lyw.demo.service.AuditService;
import lyw.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;

    @Autowired
    private AlternativeService alternativeService;


    /**
     * 得到未通过审核的比赛列表
     * @return
     */
    @GetMapping("Audit")
    public List<Contest> findNotAudit(){
        return auditService.findNotAudit();
    }


    /**
     *  根据比赛id获得该比赛的栏目信息
     * @param cid 比赛id
     * @return
     */
    @GetMapping("findCI")
    public List<Column_info> findCI(int cid){
        return auditService.findByCid(cid);
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
     * @param cid
     * @return
     */
    @PutMapping("Display")
    public String updateDisplay(int cid){
        try{
            auditService.UpdateDisplay(cid);
        }catch (Exception e){
            return "false";
        }
        return "true";
    }


    /**
     * 删除管理员用户
     * @param id
     * @return
     */
    @DeleteMapping("user")
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
     * @param username
     * @param password
     * @return
     */
    @PostMapping("user")
    public String registerRoot(String username,String password){
        try{
            userService.registerRoot(username,password);
        }catch (Exception e){
            return "false";
        }
        return "true";
    }

    @GetMapping("alter")
    public List<Alternative> listAlters(int cid){
        List<Alternative> list = alternativeService.findAllByCid(cid);
        return list;
    }

    @DeleteMapping("alter")
    public String deleteAlter(int id){
        try{
            alternativeService.delete(id);
        }catch (RuntimeException e){
            return "false";
        }
        return "true";
    }

    @PostMapping("alter")
    public String insertAlter(Alternative alternative){
        try{
            alternative.setUser_permit(false);
            alternativeService.insert(alternative);
        }catch(RuntimeException e){
            return "false";
        }
        return "true";

    }
}
