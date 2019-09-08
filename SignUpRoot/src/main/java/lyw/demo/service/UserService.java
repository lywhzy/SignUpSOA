package lyw.demo.service;

public interface UserService {

    void registerRoot(String username,String password);

//    根据用户id删除管理员
    void daleteRoot(int id);
}
