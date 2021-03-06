package lyw.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(value = {"lyw.demo.mapper"})
public class SignUpRootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignUpRootApplication.class);
    }
}
