package lyw.demo;

import lyw.demo.Listener.Listener;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringCloudApplication
@EnableFeignClients
@MapperScan("lyw.demo.mapper")
public class ContestServiceApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ContestServiceApplication.class);
        application.addListeners(new Listener());
        application.run(args);
    }
}
