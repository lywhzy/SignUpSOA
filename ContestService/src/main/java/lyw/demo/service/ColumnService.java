package lyw.demo.service;

import lyw.demo.pojo.Column_info;
import lyw.demo.pojo.Column_value;

import java.util.List;

public interface ColumnService {
    Column_value getCvByUCId(int uid, int cid);
    List<Column_info> listByCId(int cid/*比赛id*/,int uid);
    List<Column_info> listByCIdForCache(int cid,int uid);
    Column_info getInfo(int cid);
    void delete(Column_value column_value);
    void update(Column_info column_info,String type);
    void setColumnListForAlters(List<Column_info> list);
    void setColumnListForValue(List<Column_info> list,int uid);
}
