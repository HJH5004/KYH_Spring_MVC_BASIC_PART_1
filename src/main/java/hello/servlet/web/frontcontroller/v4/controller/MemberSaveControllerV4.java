package hello.servlet.web.frontcontroller.v4.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paraMap, Map<String, Object> model) {

        String username = paraMap.get("username");
        int age = Integer.parseInt(paraMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        //model에 대한 정보를 하나만 넣어줌으로써 viewModel에 대한 부분을 신경쓰지 않고  model만 신경쓰면 됨.
        model.put("member", member);

        //단순하게 viewPath만 던져주고 있다.
        return "save-result";
    }
}
