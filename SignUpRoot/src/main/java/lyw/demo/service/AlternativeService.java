package lyw.demo.service;

import lyw.demo.pojo.Alternative;

import java.util.List;

public interface AlternativeService {
    //根据栏目id查询可选值
    List<Alternative> findAllByCid(int cid);

    void delete(int id);

    void insert(Alternative alternative);
}
