package lyw.demo;

import lyw.demo.Listener.Listener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("lyw.demo.mapper")
public class ContestServiceApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ContestServiceApplication.class);
        application.addListeners(new Listener());
        application.run(args);
    }

    @Bean
    public Integer setRfc()    {
        // 指定jre系统属性，允许特殊符号， 如{} 做入参，其他符号按需添加。见 tomcat的HttpParser源码。
         System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow", "|{}");
         return 0;
        // }
    }

}
