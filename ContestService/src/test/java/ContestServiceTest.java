import lyw.demo.ContestServiceApplication;
import lyw.demo.mapper.Column_infoMapper;
import lyw.demo.pojo.Column_value;
import lyw.demo.pojo.Contest;
import lyw.demo.service.Cache.CacheImpl.ColumnValueCache;
import lyw.demo.service.ContestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContestServiceApplication.class)
public class ContestServiceTest {

    @Autowired
    private ContestService contestService;

    @Autowired
    private Column_infoMapper column_infoMapper;

    @Autowired
    private ColumnValueCache columnValueCache;

    @Test
    public void testUpdate(){
        Contest contest = contestService.get(2);
        contest.setAttachment(2);
        contestService.update(contest);
    }

    @Test
    public void testmapper(){
        System.out.println(column_infoMapper.selectByPrimaryKey(1));
    }

    @Test
    public void testCache(){
        Column_value column_value = new Column_value();
        column_value.setUid(1);
        column_value.setCid(1);
        column_value.setValue("123");
        columnValueCache.setCache(column_value,new Integer[]{1});
    }

}
