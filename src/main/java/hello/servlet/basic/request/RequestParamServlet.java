package hello.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 *
 * 1. 파라미터 전송 기능
 * http://localhost:8080/request-param?username=hello
 *
 * */
@WebServlet(name = "requestParamServlet" , urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("RequestParamServlet.service");

        System.out.println("전체 파라티머 조회 start");

        req.getParameterNames().asIterator().forEachRemaining(
                paraName -> System.out.println(paraName + " = " + req.getParameter(paraName))
        );
        System.out.println("전체 파라티머 조회 end");


        System.out.println("단일 파라티머 조회 staRT");
        //이 방식은 get 뿐만 아니라 post에서도 사용할 수 있다.
        String userName = req.getParameter("username");
        String age  = req.getParameter("age");

        System.out.println("userName = " + userName);
        System.out.println("age = " + age);
        System.out.println("단일 파라티머 조회 end");




        System.out.println("이름이 같은 복수 파라미터 조회");
        String[] userNames = req.getParameterValues("username");
        for(String name : userNames){
            System.out.println("name = " + name);
        }




    }
}
