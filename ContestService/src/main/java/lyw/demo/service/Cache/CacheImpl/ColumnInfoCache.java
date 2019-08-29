package lyw.demo.service.Cache.CacheImpl;

import lyw.demo.pojo.Column_info;
import lyw.demo.service.Cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

@org.springframework.stereotype.Service
public class ColumnInfoCache implements CacheService<Column_info> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void deleteCache(int id) {
        redisTemplate.delete("column_infos"+id);
    }

    @Override
    public List<Column_info> selectListCache(int id) {
        return redisTemplate.opsForList().range("column_infos"+id,0,-1);
    }

    @Override
    public Column_info selectCache(int id) {
        return null;
    }

    @Override
    public void setCache(Column_info object) {

    }

    @Override
    public void setListCache(List<Column_info> list) {
        int id;
        if(list != null && !list.isEmpty()){
            id = list.get(0).getCid();
            redisTemplate.opsForList().rightPushAll("column_infos"+id,list);
            redisTemplate.expire("column_infos" + id,1, TimeUnit.DAYS);
        }
    }
}
