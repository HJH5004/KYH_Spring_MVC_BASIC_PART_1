package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/*
* 이거 왜 쓰는건가?
* */
public interface MyHandlerAdapter {
    /**
     *
     * 해당 Handler가 이 버전의 컨트롤러를 지원하는지에 대해 확인
     *
     * */
    boolean supports(Object handler);

    /**
     * Handler를 실질적으로 돌려주는 역할을 수행하는...부분?
     *
     * */
    ModelView handler(HttpServletResponse response, HttpServletRequest request, Object handler) throws ServletException, IOException;
}
