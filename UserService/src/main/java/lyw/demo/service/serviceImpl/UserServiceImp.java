package lyw.demo.service.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.mapper.UserMapper;
import lyw.demo.pojo.User;
import lyw.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImp implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selLogin(String name, String password) {
        User user = null;
        if (name != null && password != null && !"".equals(name) && !"".equals(password)){
            try {
                user = userMapper.selLogin(name);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return user;
    }

    @Override
    public int insertUser(User record) {
        return userMapper.insert(record);
    }

    @Override
    public User selectByName(String name) {
        return userMapper.selLogin(name);
    }
}
