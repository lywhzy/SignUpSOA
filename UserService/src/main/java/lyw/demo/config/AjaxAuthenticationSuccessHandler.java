package lyw.demo.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lyw.demo.pojo.AjaxResponseBody;
import lyw.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 登录成功时返回给前端的数据
 */
@Component
@Slf4j
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setStatus("200");
        responseBody.setMsg("Login Success!");
        User userDetails = (User) authentication.getPrincipal();
        responseBody.setResult(userDetails);
        //httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));

        // 将uuid发送给前端
        UUID uuid = UUID.randomUUID();
        responseBody.setUuid(uuid.toString());
        // 存放uuid和userDetails于redis中
        redisTemplate.opsForValue().set(uuid.toString(), userDetails.getId());
        redisTemplate.expire(uuid.toString(), 1, TimeUnit.DAYS);

        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
        log.info(userDetails.getUsername() + " 成功跳转中...");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", uuid.toString());
        jsonObject.put("username", userDetails.getUsername());
        jsonObject.put("level", userDetails.getLevel());

        if (userDetails.getLevel().equals("ROLE_USER")){
            log.info("192.168.43");
            httpServletResponse.sendRedirect("http://192.168.43.88:3000/homePage?jsonObject=" + URLEncoder.encode(jsonObject.toJSONString()));
        }else{
            // 管理员请求到后台
            httpServletResponse.sendRedirect("");
        }


        /*CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://59.69.12.2:8071/doGetTest");
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            System.out.println("响应状态为 " + response.getStatusLine());
            if (entity != null){
                System.out.println("响应内容长度为：" + entity.getContentLength());
                System.out.println("响应内容为：" + entity.getContent());

            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (httpClient != null){
                    httpClient.close();
                }
                if (response != null){
                    response.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }*/

    }
}
