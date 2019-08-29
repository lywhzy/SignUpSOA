package lyw.demo.service.Cache.CacheImpl;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.pojo.Column_value;
import lyw.demo.service.Cache.CacheService;
import lyw.demo.service.Cache.StrongCacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ColumnValueCache implements StrongCacheService<Column_value> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void deleteCache(int id) {
        redisTemplate.delete(id+"keepColumnValue");
    }

    @Override
    public List<Column_value> selectListCache(int id) {
        return redisTemplate.opsForList().range(id+"keepColumnValue",0,-1);
    }

    @Override
    public Column_value selectCache(int id) {
        return null;
    }

    @Override
    public void setCache(Column_value object) {
        redisTemplate.opsForList().rightPush(object.getUid()+"keepColumnValue",object);
    }

    @Override
    public void setListCache(List<Column_value> list) {
        if(list==null||list.isEmpty()) return;
        redisTemplate.opsForList().rightPushAll(list.get(0).getUid() + "keepColumnValue",list);
    }








    @Override
    public void deleteCache(Object[] objs) {
        redisTemplate.delete(objs[0].toString()+"key"+objs[1].toString());
    }

    @Override
    public List<Column_value> selectListCache(Object[] objs) {
        return null;
    }

    @Override
    public Column_value selectCache(Object[] objs) {
        String str = (String) redisTemplate.opsForHash().get(objs[0].toString()+"key"+objs[1].toString(),"columnValue"+objs[2].toString());
        if(StringUtils.isBlank(str)){
            return null;
        }
        Column_value column_value = new Column_value();
        column_value.setValue(str);
        return column_value;
    }

    @Override
    public void setCache(Column_value Object, Object[] objs) {
        redisTemplate.opsForHash().put(Object.getUid()+"key"+objs[0],"columnValue"+Object.getCid(),Object.getValue());
    }

    @Override
    public void setListCache(List<Column_value> list, Object[] objs) {

    }
}
