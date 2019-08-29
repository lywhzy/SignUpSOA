package lyw.demo.service.Cache.CacheImpl;

import lyw.demo.pojo.Alternative;
import lyw.demo.service.Cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AlternativeCache implements CacheService<Alternative> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void deleteCache(int id) {
        redisTemplate.delete("alternative" + id);
    }

    @Override
    public List<Alternative> selectListCache(int id) {
        List<Alternative> alternatives = redisTemplate.opsForList().range("alternative" + id,0,-1);
        return alternatives;
    }

    @Override
    public Alternative selectCache(int id) {
        return null;
    }

    @Override
    public void setCache(Alternative object) {

    }

    @Override
    public void setListCache(List<Alternative> list) {
        if(list==null||list.isEmpty()) return;
        redisTemplate.opsForList().rightPushAll("alternative" + list.get(0).getCid(),list);
        redisTemplate.expire("alternative" + list.get(0).getCid(),1, TimeUnit.HOURS);
    }
}
