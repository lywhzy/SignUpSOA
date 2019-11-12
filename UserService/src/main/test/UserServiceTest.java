import lyw.demo.UserServiceApplication;
import lyw.demo.mapper.UserMapper;
import lyw.demo.pojo.User;
import lyw.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(classes = UserServiceApplication.class)
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;


    @Test
    public void testLogin(){
        userService.selLogin("罗运帷", "123456");
    }

    @Test
    public void testMapper(){
        /*Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username","罗运帷").andEqualTo("password","123456");
        List<User> user = (List<User>) userMapper.selectByExample(example);*/

        User user = userMapper.selLogin("罗运帷");
        System.out.println(user);
    }

}
