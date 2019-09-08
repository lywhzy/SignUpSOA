package lyw.demo.service;

import lyw.demo.pojo.Column_info;
import lyw.demo.pojo.Contest;

import java.util.List;

public interface AuditService {

    List<Contest> findNotAudit();
    //根据比赛id查询栏目信息
    List<Column_info> findByCid(int cid);

    void UpdateCheckStatus(int cid);

    void UpdateDisplay(int cid);
}
