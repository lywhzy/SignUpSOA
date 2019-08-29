package lyw.demo.service.Cache;

import java.util.List;

public interface StrongCacheService<T> extends CacheService<T>{

    void deleteCache(Object[] objs);

    List<T> selectListCache(Object[] objs);

    T selectCache(Object[] objs);

    void setCache(T Object,Object[] objs);

    void setListCache(List<T> list,Object[] objs);

}
