package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MemberRepositoryTest {
    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void save(){
        Member hello = new Member("hello", 20);

        Member savedMember = memberRepository.save(hello);


        Member findMember = memberRepository.findById(savedMember.getId());

        Assertions.assertThat(findMember).isEqualTo(hello);

    }

    @Test
    void findAll() {

        Member member1 = new Member("hello", 20);
        Member member2 = new Member("hello2", 22);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> allMember = memberRepository.findAll();

        Assertions.assertThat(allMember.size()).isEqualTo(2);
        Assertions.assertThat(allMember).contains(member1, member2);
    }

}
