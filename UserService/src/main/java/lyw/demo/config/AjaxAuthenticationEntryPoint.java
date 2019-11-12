package lyw.demo.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import lyw.demo.pojo.AjaxResponseBody;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录时返回给前端的数据
 */
@Component
@Slf4j
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setStatus("000");
        responseBody.setMsg("未登录");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
        log.info("未登录 Need Authorities!");
    }
}
