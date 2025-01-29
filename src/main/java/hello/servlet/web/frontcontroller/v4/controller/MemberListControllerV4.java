package hello.servlet.web.frontcontroller.v4.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.List;
import java.util.Map;

public class MemberListControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paraMap, Map<String, Object> model) {
        //모델을 생성하여 주고 주고
        List<Member> members = memberRepository.findAll();
        //model의 value 값이 Object니까 어떤 타입이 들어와도 상관 없음.
        model.put("members", members);


        return "members";
    }
}
