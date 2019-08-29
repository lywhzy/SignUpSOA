package lyw.demo.service.Request.RequestImpl;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.pojo.Column_info;
import lyw.demo.service.Cache.CacheService;
import lyw.demo.service.ColumnService;
import lyw.demo.service.Request.Request;

@Slf4j
public class ColumnUpdateRequest implements Request {
    private ColumnService columnService;
    private CacheService cacheService;

    private Column_info column_info;

    private String type;

    public ColumnUpdateRequest(ColumnService columnService, CacheService cacheService, Column_info column_info,String type) {
        this.columnService = columnService;
        this.cacheService = cacheService;
        this.column_info = column_info;
        this.type = type;
    }

    @Override
    public void process() {
        log.info(column_info.getId() + "正在进行删除cache操作");
        cacheService.deleteCache(getContestId());
        columnService.update(column_info,type);
    }

    @Override
    public Integer getContestId() {
        return column_info.getCid();
    }
}
