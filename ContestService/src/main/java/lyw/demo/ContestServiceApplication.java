package lyw.demo;

import lyw.demo.Listener.Listener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("lyw.demo.mapper")
public class ContestServiceApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ContestServiceApplication.class);
        application.addListeners(new Listener());
        application.run(args);
    }
}
