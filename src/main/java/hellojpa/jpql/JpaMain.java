package hellojpa.jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            Member findMember = result.get(0);
            findMember.setAge(20);  // update query가 나갔으므로 영속성 컨텍스트에서 관리중이라는 것을 알 수 있음.

            List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();  // join 쿼리가 나감. join은 성능에 영향을 주기 때문에 SQL과 유사하도록 아래와 같이 하는게 좋음.
            List<Team> result3 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            /**
             * 프로젝션 - 여러 값 조회
             */
            // 가장 깔끔한 방법. 패키지명이 길어지면 별로라는 단점. QueryDSL에서 해결됨.
            List<MemberDto> resultList = em.createQuery("select new hellojpa.jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                    .getResultList();
            MemberDto memberDto = resultList.get(0);
            System.out.println("memberDto.getUsername() = " + memberDto.getUsername());
            System.out.println("memberDto.getAge() = " + memberDto.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
