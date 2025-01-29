package hello.servlet.web.frontcontroller.v5_1;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5_1.adapater.ControllerV4HandlerAdapter;
import hello.servlet.web.frontcontroller.v5_1.adapater.ControllerV3HandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "frontControllerServletV5_1", urlPatterns = "/front-controller/v599/*")
public class FrontControllerServletV5 extends HttpServlet {


    /*
    * 2. 그러나 여기에 와서는 value에 해당 하는 값이 v3가 아니라 object로 설정되어 있어 value에 해당하는 값이 유연하게 설정되어 있다. = 아무 컨트롤러나 들어가게 할 수 있도록 하기 위해 이렇게 하였다.
    * */
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    /*
    * 1. 기존 Map 타입
    *   기존 V3 Controller에서는 Value에 해당하는 값이 v3로 고정 되어있다.
    * */
//    private Map<String, ControllerV3> handlerMappingMap = new HashMap<>();

    private List<MyHandlerAdapter> myHandlerAdapterList = new ArrayList<>();

    public FrontControllerServletV5(){
        inintHandlerMappingMap();

        initHandelrAdapters();
    }

    /*
    * Controller 초기화
    * 목적 : 너 이 URI에 어떤 핸들러 던져줄꺼야?
    * */
    private void inintHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());


        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }
    
    /*
    * 어뎁터를 초기화 해서 던져준다.
    *
    * 목적 : Handler 찾아오는 것 까지 알겠어. 그러면 이거 무슨 버전에 적용시킬꺼야?
    * */
    private void initHandelrAdapters() {
        myHandlerAdapterList.add(new ControllerV3HandlerAdapter());
        myHandlerAdapterList.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("V4 리펙토링 버전으로 들어옴!");

        /**
         * 그럼 다르게 이번엔 V4 Controller가 메인인 솔루션에 v3를 사용하고 싶은 상황
         * */
        Object handler = getHandler(request);

        if(handler == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //헨들러 지금 돌리는 로직을 몇번 Controller에 붙일 지 정할 수 있음.
        //결국엔 코드는 WET해지는 것 같아.
        MyHandlerAdapter handlerAdapter = getHandlerAdpter(handler);

        //결국엔 ModelView를 만들어서 주는 것 아닌가?
        //이 메인 로직을 건드는게 없다.
        //model에 대한 벨류 추가
        Map<String, Object> model = new HashMap<>();

        String viewName = handlerAdapter.handler(response, request, model, handler);

        //비즈니스 로직

        //view 위치 파싱 함수
        MyView myView = viewResolver(viewName);


        //렌더러 함수
        myView.render(model, request, response);

    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private MyHandlerAdapter getHandlerAdpter(Object handler) {
        for (MyHandlerAdapter adapter : myHandlerAdapterList) {
            if(adapter.supports(handler)){
                return adapter;
            }
        }
        throw new IllegalArgumentException("Handler adapter를 찾을 수 없습니다.");
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }
}
