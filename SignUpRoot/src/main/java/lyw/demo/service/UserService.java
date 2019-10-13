package lyw.demo.service;

import lyw.demo.pojo.User;

import java.util.List;

public interface UserService {

    void registerRoot(String username,String password);

//    根据用户id删除管理员
    void daleteRoot(int id);


    List<User> findRoots();

    User findUserByname(String username);

}
