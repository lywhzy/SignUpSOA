package lyw.demo.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lyw.demo.pojo.User;
import lyw.demo.service.UserService;
import lyw.demo.util.JavaEmailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;

@org.springframework.web.bind.annotation.RestController
@Slf4j
@CrossOrigin
public class RestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 注册
     * @param
     * @return
     */
    @RequestMapping(value = "/insert")
    public String insert(String username, String phonenumber, String email, String password){
        User user = new User();
        //JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonStr);
        user.setUsername(username);
        user.setPhonenumber(phonenumber);
        user.setEmail(email);
        user.setPassword(password);
        // 默认用户角色
        user.setLevel("ROLE_USER");
        log.info(username + phonenumber + email + password);
        int flag = userService.insertUser(user);
        if (flag > 0){
            // 注册成功
            // ...
            log.info("注册成功:" + user.getUsername());
            return "true";
        }else{
            //...
            log.info("注册失败:" + user.getUsername());
            return "false";
        }

    }

    @RequestMapping(value = "/logoutUrl")
    public String logout(String uuid){
        //JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonStr);
        //String uuid = jsonObject.getString("uuid");
        // 清除redis，成功返回true字符串
        log.info("清除缓存中.." + uuid.toString());
        redisTemplate.delete(uuid.toString());
            return "{}";
    }

    @RequestMapping(value = "/checkUsername")
    public String checkUsername(String username){
        if (userService.selectByName(username) == null){
            return "true";
        }else {
            log.info("该用户名已被使用过");
            return "false";
        }
    }

    /**
     * 获取验证码
     * @param jsonStr
     * @return
     */
    @RequestMapping(value = "/sendEmailGetCode")
    public String emailcode(@RequestBody String jsonStr){
        JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonStr);
        String email = jsonObject.getString("email");
        String context = "您的验证码为";
        String code = null;
        log.info(email);
        try {
            code = JavaEmailSenderUtil.sendCodeEmail(email, context) + "";
            log.info("验证码：" + code);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return code;
    }

}
