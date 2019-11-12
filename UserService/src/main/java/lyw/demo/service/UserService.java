package lyw.demo.service;

import lyw.demo.pojo.User;

public interface UserService {

    User selLogin(String name, String password);

    int insertUser(User record);

    User selectByName(String name);

}
