package hello.servlet.web.frontcontroller.v5_1.adapater;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v5_1.MyHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV3HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV3);
    }

    @Override
    public String handler(HttpServletResponse response, HttpServletRequest request, Map<String, Object> model, Object handler) throws ServletException, IOException {
        ControllerV3 controller = (ControllerV3) handler;

        Map<String, String> paraMap = createParamMap(request);

        ModelView modelView = controller.process(paraMap);

//        model = modelView.getModel();

        modelView.getModel().forEach((k,v) -> model.put(k,v));

        return modelView.getViewName();
    }

    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paraMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paraname ->paraMap.put(paraname, request.getParameter(paraname)));
        return paraMap;
    }
}
