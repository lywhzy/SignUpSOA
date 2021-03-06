package lyw.demo.service.Request.RequestImpl;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.pojo.Column_info;
import lyw.demo.service.Cache.CacheService;
import lyw.demo.service.ColumnService;
import lyw.demo.service.Request.Request;

import java.util.List;

@Slf4j
public class ColumnSelectRequest implements Request {

    private ColumnService columnService;
    private CacheService cacheService;
    private int contestId;
    private int uid;

    public ColumnSelectRequest(ColumnService columnService, CacheService cacheService, int contestId, int uid) {
        this.columnService = columnService;
        this.cacheService = cacheService;
        this.contestId = contestId;
        this.uid = uid;
    }

    @Override
    public void process() {
        List<Column_info> list = cacheService.selectListCache(getContestId());
        if(list==null||list.isEmpty()) {
            List<Column_info> clist = columnService.listByCIdForCache(getContestId(), uid);
            cacheService.setListCache(clist);
        }
    }

    public ColumnSelectRequest(Integer contestId) {
        this.contestId = contestId;
    }

    @Override
    public Integer getContestId() {
        return contestId;
    }
}
