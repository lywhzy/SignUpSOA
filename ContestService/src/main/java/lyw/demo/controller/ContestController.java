package lyw.demo.controller;

import lyw.demo.pojo.Contest;
import lyw.demo.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContestController {

    @Autowired
    private ContestService contestService;

    @GetMapping("getCharacterization")
    public List<Contest> getTopContest(){
        List<Contest> list = contestService.getTopCharacterization();
        return list;
    }

    @GetMapping("getMyContest")
    public List<Contest> getContestByUid(int uid){
        List<Contest> list = contestService.listByUId(uid);
        return  list;
    }

    @GetMapping("getAllContest")
    public List<Contest> getAllContest(){
        List<Contest> list = contestService.getAll();
        return list;
    }

    @GetMapping("updateContest")
    public void updateContest(){
        Contest contest = contestService.get(3);
        contest.setAttachment(1);
        contestService.update(contest);
    }
}
