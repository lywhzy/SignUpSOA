package lyw.demo.config;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.filter.MyAuthenticationFilter;
import lyw.demo.mapper.UserMapper;
import lyw.demo.pojo.MyUserDetails;
import lyw.demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 此注解会启动spring security
 * 核心配置
 * 分别是标识该类是配置类、开启security服务、开启全局security注解
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static List<String> DEFAULT_IGNORED = Arrays.asList("/Css/**", "/Js/**", "/static/**", "/webjars/**", "/**/favicon.ico");

    @Autowired
    private AjaxAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AjaxAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private AjaxLogoutSuccessHandler logoutSuccessHandler;

    @Override
    public void configure(WebSecurity webSecurity) throws Exception{
        webSecurity.ignoring().antMatchers("/static/**");
    }

    /**
     * 配置策略
     * 定义受保护的URL和不受保护的URL
     * 以及登录URL，登录失败URL，登录成功URL
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 关闭csrf
                .csrf().disable()
                // 关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 用重写的filter替换掉原有的usernamePasswordAuthenticationFilter
        //http
        //        .addFilterAt(myAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        /*http.authorizeRequests()
                .antMatchers("/**")
                .fullyAuthenticated()
                .and()
                .httpBasic();
                    //.loginPage("/login").permitAll();*/

        http
                .authorizeRequests()
                    .antMatchers("/homePage.html").permitAll()//开放homePage,使其不受保护
                    .antMatchers("/sendEmailGetCode").permitAll()
                    .antMatchers("/insert").permitAll()
                    .antMatchers("/checkUsername").permitAll()
                    .antMatchers("/logoutUrl").permitAll()
                    .antMatchers("/resources/static/**").permitAll()
                    //.antMatchers("/user/**").hasRole("USER")
                    .anyRequest().authenticated() //任何请求，登录后访问
                    .and()
                .formLogin()
                    .loginPage("/login")//.loginProcessingUrl("/login")
                    .loginPage("/logfail")
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler).permitAll();
                    //.and()
                    // 关闭拦截未登录自动跳转，改为返回json信息
                    //.exceptionHandling().authenticationEntryPoint(new AjaxAuthenticationEntryPoint())
                    //.and()
                //.logout()
                    //.logoutUrl("/logout")
                //   .deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler)
                //    .and()
                //    .sessionManagement().maximumSessions(10);
                    //.logoutUrl("/logout")
                    //.logoutSuccessHandler(logoutSuccessHandler()) // 注销成功处理器

        log.info("正在授权");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }



    /**
     * 注册自定义的UsernamePasswordAuthenticationFilter
     */
    @Bean
    MyAuthenticationFilter myAuthenticationFilter() throws Exception {
        MyAuthenticationFilter filter = new MyAuthenticationFilter();
        //filter.setAuthenticationSuccessHandler(new SuccessHandler());//登录成功处理
        //filter.setAuthenticationFailureHandler(new FailureHandler());//登录失败处理
        //filter.setFilterProcessesUrl("");//

        // 这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    /**
     * BCrypt加密
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 登入处理
     * @return
     */
    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler(){
        return new SavedRequestAwareAuthenticationSuccessHandler(){
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
                User userDetails = (User) authentication.getPrincipal();
                logger.info("USER :" + userDetails.getUsername() + " LOGIN SUCCESS!");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }

    /**
     * 登出处理
     * @return
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                try {
                    MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
                    log.info("USER: " + userDetails.getUsername() + " LOGOUT SUCCESS!");
                }catch (Exception e){
                    log.info("LOGOUT EXCEPTION, e :" + e.getMessage());
                }
                httpServletResponse.sendRedirect("/login");
            }
        };
    }

    /**
     * 用户登录实现
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {

            @Autowired
            private UserMapper userMapper;

            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                log.info("用户名" + s);
                if (StringUtils.isEmpty(s)){
                    throw new RuntimeException("用户名不能为空！");
                }
                User user = userMapper.selLogin(s);
                if (user == null){
                    throw new UsernameNotFoundException("Username " + s + "not found");
                }

                MyUserDetails userDetails = new MyUserDetails(user);
                log.info("用户： " + user.getUsername() + "权限为 " + user.getLevel() + userDetails.getAuthorities());
                return userDetails;
            }
        };
    }



}
