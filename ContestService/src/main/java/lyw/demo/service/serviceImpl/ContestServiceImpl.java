package lyw.demo.service.serviceImpl;

import lyw.demo.mapper.ContestMapper;
import lyw.demo.pojo.Contest;
import lyw.demo.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class ContestServiceImpl implements ContestService {
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public List<Contest> listByUId(int uid) {
        List<Contest> list = contestMapper.selectByUserId(uid);
        setContestStatus(list);
        return list;
    }

    @Override
    public List<Contest> getTopCharacterization() {

        List<Contest> list = redisTemplate.opsForList().range("top",0,-1);
        if(!list.isEmpty()){
            return list;
        }
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        readLock.lock();
        try{
            list = contestMapper.getTopCharacterization();
            while (list.size() < 3) {
                list.add(new Contest());
            }
            redisTemplate.opsForList().rightPushAll("top",list);
            redisTemplate.expire("top",1, TimeUnit.DAYS);
        }finally {
            readLock.unlock();
        }
        return list;
    }

    @Override
    public Contest getById(int cid) {
        return contestMapper.selectByPrimaryKey(cid);
    }

    @Override
    public Contest get(int id) {
        return contestMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Contest obj) {
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        try{
            redisTemplate.delete("top");
            contestMapper.updateByPrimaryKey(obj);
        }finally {
            writeLock.unlock();
        }
    }


    //需与栏目信息的缓存联系起来
    @Override
    public void delete(int id) {
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        try{
            redisTemplate.delete("top");
            contestMapper.deleteByPrimaryKey(id);
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    public void insert(Contest obj) {
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        try{
            redisTemplate.delete("top");
            contestMapper.insert(obj);
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    public List<Contest> getAll() {

        List<Contest> list = redisTemplate.opsForList().range("allContest",0,-1);
        if(!list.isEmpty()){
            return list;
        }
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        readLock.lock();
        try{
            list = list = contestMapper.selectAll();
            setContestStatus(list);
            redisTemplate.opsForList().rightPushAll("allContest",list);
            redisTemplate.expire("allContest",1, TimeUnit.DAYS);
        }finally {
            readLock.unlock();
        }
        return list;
    }

    private void setContestStatus(List<Contest> list){
        String status = null;
        for(Contest contest : list){
            status = contest.getStatus();
            if(status!=null&&status.equals("已报名")) continue;
            if(new Date().before(contest.getBegindate())){
                contest.setStatus(this.NotOpen);
            }else if(new Date().before(contest.getEnddate())){
                contest.setStatus(this.SIGNING);
            }else{
                contest.setStatus(this.SIGNED);
            }
        }
    }
}
