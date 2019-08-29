package lyw.demo.service;

import java.util.List;

public interface BaseService<T> {
    T get(int id);
    void update(T obj);
    void delete(int id);
    void insert(T obj);
    List<T> getAll();
}
