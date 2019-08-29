package lyw.demo.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.mapper.AlternativeMapper;
import lyw.demo.mapper.Column_infoMapper;
import lyw.demo.mapper.Column_valueMapper;
import lyw.demo.mapper.ContestMapper;
import lyw.demo.pojo.Alternative;
import lyw.demo.pojo.Column_info;
import lyw.demo.pojo.Column_value;
import lyw.demo.service.Cache.CacheImpl.ColumnValueCache;
import lyw.demo.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private Column_valueMapper column_valueMapper;

    @Autowired
    private Column_infoMapper column_infoMapper;

    @Autowired
    private AlternativeMapper alternativeMapper;

    @Autowired
    private ContestMapper contestMapper;

    @Autowired
    @Qualifier("columnValueCache")
    private ColumnValueCache columnValueCache;

    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName = "Exception")
    @Override
    public void signUp(Column_value column_value) {
        column_valueMapper.delete(column_value);
        column_valueMapper.insert(column_value);

        Column_info column_info = column_infoMapper.selectByPrimaryKey(column_value.getCid());
        Integer contest = column_info.getCid();

        if(contestMapper.selectRelation(column_value.getUid(), contest)==null)
            contestMapper.insertRelation(column_value.getUid(),contest);
        isAddAlternative(column_value);
        columnValueCache.deleteCache(new Integer[]{column_value.getUid(),contest});
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName = "Exception")
    @Override
    public void keep(Column_value column_value) {
        Column_info column_info = column_infoMapper.selectByPrimaryKey(column_value.getCid());
        Integer contest = column_info.getCid();
        columnValueCache.setCache(column_value,new Integer[]{contest});
        isAddAlternative(column_value);
    }

    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName = "Exception")
    @Override
    public void update(Column_value column_value) {
        Example example = new Example(Column_value.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid",column_value.getCid()).andEqualTo("uid",column_value.getUid());
        column_valueMapper.updateByExampleSelective(column_value,example);

        isAddAlternative(column_value);
    }

    private void isAddAlternative(Column_value column_value) {
        if(column_infoMapper.selectByPrimaryKey(column_value.getCid()).getIcontype().equals("下拉框")){

            Example example = new Example(Alternative.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("value",column_value.getValue()).andEqualTo("cid",column_value.getCid());
            Alternative alternative = alternativeMapper.selectOneByExample(example);

            if(alternative==null){
                alternative = new Alternative();
                alternative.setCid(column_value.getCid());
                alternative.setValue(column_value.getValue());
                alternative.setUser_permit(true);
                alternativeMapper.insertSelective(alternative);
            }
        }
    }

}
