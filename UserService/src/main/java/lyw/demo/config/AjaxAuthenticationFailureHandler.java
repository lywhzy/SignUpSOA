package lyw.demo.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lyw.demo.pojo.AjaxResponseBody;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setStatus("400");
        responseBody.setMsg("Login Failure!");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
        log.info("登录失败");
        httpServletResponse.sendRedirect("http://192.168.43.56:8071/logfail.html");
        log.info("跳转失败页面中");
    }
}
