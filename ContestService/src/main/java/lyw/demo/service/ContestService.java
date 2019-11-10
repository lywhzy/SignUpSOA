package lyw.demo.service;


import lyw.demo.pojo.Contest;

import java.util.List;

public interface ContestService extends BaseService<Contest>{
    String SIGNING = "报名中";
    String SIGNED = "报名截止";
    String NotOpen = "暂未开放";
    List<Contest> listByUId(int uid);
    List<Contest> getTopCharacterization();
    Contest getById(int cid);
    Boolean CheckSign(int uid,int cid);
}
