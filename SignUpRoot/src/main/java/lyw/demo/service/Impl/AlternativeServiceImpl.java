package lyw.demo.service.Impl;

import lyw.demo.mapper.AlternativeMapper;
import lyw.demo.pojo.Alternative;
import lyw.demo.service.AlternativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AlternativeServiceImpl implements AlternativeService {

    @Autowired
    private AlternativeMapper alternativeMapper;

    @Override
    public List<Alternative> findAllByCid(int cid) {
        Example example = new Example(Alternative.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid",cid);
        List<Alternative> list = alternativeMapper.selectByExample(example);
        return list;
    }

    @Override
    public void delete(int id) {
        alternativeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void insert(Alternative alternative) {
        alternativeMapper.insert(alternative);
    }
}
