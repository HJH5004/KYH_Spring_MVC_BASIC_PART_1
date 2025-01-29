package hello.servlet.web.springmvc.old;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;


//HandlerMapping BeanUrl을 통해서 bean을 찾아옴
@Component("/springmvc/old-controller")
public class OldController implements Controller {
    //adpater에서 이를 찾아와줌
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OlcController 불러왔는가?");
        return new ModelAndView("new-form");
    }



}
