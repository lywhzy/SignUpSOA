package lyw.demo.service.Impl;

import lyw.demo.mapper.UserMapper;
import lyw.demo.pojo.User;
import lyw.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void registerRoot(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setLevel("admin");
        userMapper.insertSelective(user);
    }

    @Override
    public void daleteRoot(int id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<User> findRoots() {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("level","admin");
        List<User> users = userMapper.selectByExample(example);
        return users;
    }

    @Override
    public User findUserByname(String username) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        User user = userMapper.selectOneByExample(example);
        return user;
    }


}
