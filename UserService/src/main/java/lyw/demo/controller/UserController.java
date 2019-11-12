package lyw.demo.controller;

import lombok.extern.slf4j.Slf4j;
import lyw.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping(value = "/test")
    public String test(){
        return "test successful!";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logfail")
    public String logfail(){
        return "logfail";
    }


    @RequestMapping("admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin(){
        return "您拥有ROLE_ADMIN角色";
    }

    @RequestMapping("user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String printUser(){
        return "您拥有ROLE_USER角色";
    }

    @RequestMapping("/doGetTest")
    public String doGetTest(){
        System.out.println("发送成功");
        return "doGetTest Success";
    }

/*    @RequestMapping(value = "/insert")
    public void insert(@RequestBody String jsonStr){
        User user = new User();
        JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonStr);
        user.setUsername(jsonObject.getString("username"));
        user.setPhonenumber(jsonObject.getString("phonenumber"));
        user.setEmail(jsonObject.getString("email"));
        user.setPassword(jsonObject.getString("password"));
        // 默认用户角色
        user.setLevel("ROLE_USER");
        int flag = userService.insertUser(user);
        if (flag > 0){
            // 注册成功
            // ...
            log.info("注册成功:" + user.getUsername());
        }else{
            //...
            log.info("注册失败:" + user.getUsername());
        }

    }

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
            log.info("这里错了");
            e.printStackTrace();
        }

        return code;
    }*/


    /*@PostMapping(value = "isLogin", produces = "application/json; utf-8")
    public String isLogin() throws IOException {
        // 验证session中是否存在此用户
        User user = (User) session.getAttribute("user");
        // 验证是否包含自动登录的cookie
        Cookie[] cookies = request.getCookies();
        //String username = request.getParameter("userName");
        String[] str = new String[10];
        User user1 = null;
        if (null != cookies) {
            for (Cookie c : cookies) {
                if ("loginCookie".equals(c.getName())) {
                    // 取出用户名和密码
                    str = c.getValue().split("@");
                    if (null != str && null != str[0] && str[1] != null) {
                        try {
                            // 验证登录信息
                            user1 = userService.selLogin(str[0], str[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
        if (null != user) {
            System.out.println("session");
        }
        if (null != user1) {
            System.out.println("cookie");
        }
        if (null != user || null != user1) {
            System.out.println("已登录");
//            PrintWriter out = response.getWriter();
            user = (user == null ? user1 : user);
            System.out.println(user.getName());
            session.setAttribute("user",user);
//            out.println(user.getName());
//            out.flush();
//            out.close();
            return user.getName();
            // response.sendRedirect(request.getContextPath() + "");
        } else {
            System.out.println("未登录");
            // response.sendRedirect(request.getContextPath() + "/index.jsp");
            return "FAIL";
        }
    }


    @PostMapping(value = "selLogin", produces = "application/json; utf-8")
    public String Login(@RequestBody String jsonStr){
        JSONObject jsonObject = (JSONObject) JSONObject.parse(jsonStr);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        System.out.println(username + password);

        User user = null;
        try { // 验证登录
            user = userService.selLogin(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            // shiro spring整合异常，暂弃之
            *//*Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated() && subject.isRemembered()){
                Object principal = subject.getPrincipal();
                if (null != principal){
                    UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
                    token.setRememberMe(true);
                    subject.login(token);//登录
                }
            }*//*

            ServletContext application = session.getServletContext();
            @SuppressWarnings("unchecked")
            Map<String, Object> loginMap = (Map<String, Object>) application.getAttribute("loginMap");
            if (null == loginMap) {
                loginMap = new HashMap<String, Object>();
            }
            // 查看application里的session id是否一致
            for (String key : loginMap.keySet()) {
                if (user.getId() == Integer.parseInt(key)) {
                    if (session.getId().equals(loginMap.get(key))) {
                        System.out.println("同一地点已登录");
                        //return request.getContextPath() + "/homePage.html?msg=" + URLEncoder.encode(username + "在同一地点重复登录", "UTF-8").toString();
                    } else {
                        System.out.println("异地登录");
                        // 执行。。。
                        //return request.getContextPath() + "/homePage.html?msg=" + URLEncoder.encode(username + "异地登录", "UTF-8").toString();
                    }
                }
            }
            loginMap.put(user.getId().toString(), session.getId());
            application.setAttribute("loginMap", loginMap);
            // 将用户保存在session
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(10 * 60);

            // 生成cookie保存登录信息
            Cookie cookie = new Cookie("loginCookie", username + "@" + password);
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setPath(request.getContextPath());
            response.addCookie(cookie);

            System.out.println("登陆成功!");
            // 跳转到主页,返回字符串success
            // ...
            //response.sendRedirect(request.getContextPath() + "/homePage.html");
            return user.getName();

        } else {
            session.setAttribute("msg", "用户名或密码错误!");
            System.out.println("用户名或密码错误!");
            // 跳转到失败页面
            // ...
            request.getRequestDispatcher("/").forward(request, response);
            return null;
        }
        return "";
    }*/

}
