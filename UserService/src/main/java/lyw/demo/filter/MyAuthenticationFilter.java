package lyw.demo.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 自定义filter，使得能够用json传递前后端数据
 */
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
            || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authenticationToken = null;
            try (InputStream is = request.getInputStream()){
                Map<String, String> authenticationBean = mapper.readValue(is, Map.class);
                authenticationToken = new UsernamePasswordAuthenticationToken(authenticationBean.get("username"), authenticationBean.get("password"));
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                authenticationToken = new UsernamePasswordAuthenticationToken("", "");
            }finally {
                setDetails(request, authenticationToken);
                return this.getAuthenticationManager().authenticate(authenticationToken);
            }
        }else {
            return this.attemptAuthentication(request, response);
        }
    }

}
