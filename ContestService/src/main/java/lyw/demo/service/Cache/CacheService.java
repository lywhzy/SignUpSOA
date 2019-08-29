package lyw.demo.service.Cache;


import java.util.List;

public interface CacheService<T> {

    void deleteCache(int id);

    List<T> selectListCache(int id);

    T selectCache(int id);

    void setCache(T Object);

    void setListCache(List<T> list);

}
