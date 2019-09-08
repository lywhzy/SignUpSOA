package lyw.demo.service.Impl;

import lyw.demo.mapper.UserMapper;
import lyw.demo.pojo.User;
import lyw.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void registerRoot(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setLevel(1);
        userMapper.insertSelective(user);
    }

    @Override
    public void daleteRoot(int id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
