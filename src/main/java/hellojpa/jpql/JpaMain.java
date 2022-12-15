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
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(10);
            member1.setType(MemberType.ADMIN);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);


            // 이 시점에 flush
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();
            System.out.println("resultCount = " + resultCount);

            // 아래와 같은 이유로 벌크 연산 후에는 영속성 컨텍스트 초기화
            em.clear();

            // update 쿼리는 DB에 곧바로 반영이 됐지만 1차 캐시에는 아직 세팅이 안 되어있음.
            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember.getAge() = " + findMember.getAge());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
            System.out.println("em.close()============================================");
        }
        emf.close();
        System.out.println("emf.close()============================================");
    }
}
