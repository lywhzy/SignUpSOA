package lyw.demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BaseController {

    protected HttpServletRequest request;

    protected HttpSession httpSession;

    @Autowired
    private RedisTemplate redisTemplate;

    @ModelAttribute
    public void setUID(HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        String uid = request.getParameter("uuid");
        if(!StringUtils.isBlank(uid)){
            if(httpSession.getAttribute("uid") == null){
                httpSession.setAttribute("uid",redisTemplate.opsForValue().get("uuid"));
            }
        }
    }
}
