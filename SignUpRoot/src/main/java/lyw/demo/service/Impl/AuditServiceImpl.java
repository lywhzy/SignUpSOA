package lyw.demo.service.Impl;

import lyw.demo.mapper.Column_InfoMapper;
import lyw.demo.mapper.ContestMapper;
import lyw.demo.pojo.Column_info;
import lyw.demo.pojo.Contest;
import lyw.demo.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private Column_InfoMapper column_infoMapper;

    @Autowired
    private ContestMapper contestMapper;

    @Override
    public List<Contest> findNotAudit() {

        List<Contest> contests = contestMapper.findNotAudit();

        return contests;
    }

    @Override
    public List<Column_info> findByCid(int cid) {
        Example example = new Example(Column_info.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid",cid);
        List<Column_info> list = column_infoMapper.selectByExample(example);
        return list;
    }

    @Override
    public void UpdateCheckStatus(int cid) {
        contestMapper.updateCheckStatusByCid(cid);
    }

    @Override
    public void UpdateDisplay(int cid) {
        Boolean display = contestMapper.selectDisplayByCid(cid);
        contestMapper.updateDisplayByCid(cid,display);
    }
}
