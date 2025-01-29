package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapater.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapater.ControllerV4HandlerAdapter;
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

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
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

        /**
         * 총 정리
         *
         * 지금 이 서비스에서는 Controller3이나 4를 골라서 사용할 때 비즈니스 로직적인 부분에서 수정이 발생하지 않는다.
         * 서로 각기 다른 종류의 Controller를 사용하기 위해 Adapter 라는 인터페이스를 구축하였고 이러한 인터페이스가 각기 다른 controoler가 적용될 수 있도록 맞춰 주고 있다.
         *
         * 1.Problem
         * Controller3의 경우 ModelView를 생성하여 이를 render러에 던져줘서 dispatch 되도록 구성 되어있다.
         * 그러나 ControllerV4의 경우 ModelView를 생성하지 않고 viewPath만 전달하고 이 viewPath를 처리하는 로직을 추가 하여 처리 하도록 하고 있다.
         *
         * 이렇게 서로 각기 다른 처리 방식 때문에 만약 이러한 로직을 둘다 사용 하고자 한다면 Fontcontroller쪽에선 이를 벨리데이션 하는 로직을 추가해야 하고 새로운 종류의 컨트롤러가 늘어날 때 마다 이를 벨리데이션 코드의 수정이 발생하게 된다.
         *
         * 2.Solution
         * 그래서 여기에서는 어뎁터를 활용 하여 다른 종류의 컨트롤러를 사용하고자 하더라도 Frontcontroller, Controller(비즈니스로직)의 코드 수정 없이도 동일하게 돌아가도록 처리해주고 있다.
         *
         * 여기서 adapter는 modelView를 생성하지 않는 기존 ControllerV4 로직에서 modelView를 만들어서 던져주도록 하고있다.
         *
         * 이렇게 함으로써 얻는 이점은 무엇인가? FrontController, handler쪽에선 어떠한 수정도 발생하지 않고 로직을 돌릴 수 있다.
         *      frontController 입장 : v3를 하던 v4를 하던 결국 return값은 ModelView로 돌아오기 때문에 로직이 어떤 컨트롤러를 사용하던 로직이 변하지 않음
         *      handler 입장(controllerV3 || conotrllerV4) : 마찬가지로 어디서 자기들을 호출하던 return 값은 변화하지 않음.
         *
         * 따라서? 여기서 adapter는 기존 비즈니스 로직을 수정 하지 않도록 서비스 자체가 잘 돌아갈 수 있도록 FrontController와 Handler 사이에서 Handler의 Controller를 선택하고 어떤 Controller가 선택 되어도 서비스는 정상적으로 작동 시킬 수 있도록 연결 다리를 하는 역할을 하고 있다.
         *
         * */
        Object handler = getHandler(request);

        if(handler == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //헨들러 지금 돌리는 로직을 몇번 Controller에 붙일 지 정할 수 있음.
        //결국엔 코드는 WET해지는 것 같아.
        MyHandlerAdapter handlerAdapter =  getHandlerAdpter(handler);

        //결국엔 ModelView를 만들어서 주는 것 아닌가?
        //이 메인 로직을 건드는게 없다.
        ModelView modelView = handlerAdapter.handler(response, request, handler);

        //비즈니스 로직

        //view 위치 파싱 함수
        String viewName = modelView.getViewName();
        MyView myView = viewResolver(viewName);


        //렌더러 함수
        myView.render(modelView.getModel(), request, response);

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
