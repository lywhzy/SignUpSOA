package lyw.demo.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lyw.demo.pojo.AjaxResponseBody;
import lyw.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setStatus("100");
        User userDetails = (User) authentication.getPrincipal();
        responseBody.setMsg(userDetails.getUsername() + " Logout Success!");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
        log.info(userDetails.getUsername() + "退出成功");

        // 需清除redis的。。。

        httpServletResponse.sendRedirect("http://192.168.43.88:3000/homePage");
    }
}
