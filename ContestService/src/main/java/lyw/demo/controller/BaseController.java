package lyw.demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BaseController {

    protected HttpServletRequest request;

    protected Integer uid;


    @Autowired
    private RedisTemplate redisTemplate;

    @ModelAttribute
    public void setUID(HttpServletRequest request){
        String uid = request.getParameter("uuid");
        if(!StringUtils.isBlank(uid)){
            this.uid = (Integer) redisTemplate.opsForValue().get(uid);
        }
    }
}
