package hello.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

// 서블릿 자동 등록
@ServletComponentScan
@SpringBootApplication
public class SpringBasicMvcPart1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBasicMvcPart1Application.class, args);
    }

}
