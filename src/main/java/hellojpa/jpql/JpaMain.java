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

            em.flush();
            em.clear();


            String query = "select m from Member m";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();
            for (Member member : result) {
                System.out.println("result = " + member.getUsername() + ", " + member.getTeam().getName());
                /* [N + 1 문제]
                * 회원1이 돌죠. 그때 팀A를 SQL 쿼리로 갖고 옴. 왜? 영속성 컨텍스트에 없기 때문에.
                * 회원2도 팀A 소속이었죠. 현재 1차 캐시에 팀A가 있기 때문에 SQL이 아닌 1차 캐시에서 갖고 옴.
                * 그래서 팀A에 대한 select 쿼리가 1번만 나간거고 회원2를 조회할 때는 select 쿼리가 나가지 않았음.
                * 동일함. 1차 캐시에 팀B가 없기 때문에 회원3을 조회하는 시점에 SQL 쿼리를 날림.
                *
                * fetch join을 사용하지 않아서 회원 100명의 팀을 조회할 때 쿼리가 100방이 나가는 참사가 생김
                * => N(100명에 대한) + 1(회원 갖고 오는 쿼리) => 첫번째 쿼리로 얻은 결과(1)만큼 N(100)번 날리는 것 => N + 1 문제 */
            }

            em.clear();
            System.out.println("=====================================================================");

            String query2 = "select m from Member m join fetch m.team";
            List<Member> result2 = em.createQuery(query2, Member.class)
                    .getResultList();
            for (Member member : result2) {
                System.out.println("result2 = " + member.getUsername() + ", " + member.getTeam().getName());
                /* 이때 member.getTeam()의 팀은 프록시가 아님
                * -> fetch join으로 회원과 팀을 함꼐 조회해서 지연 로딩X
                * result2에 결과가 담기는 시점에 프록시가 아닌 실제 데이터가 담기게 됨. */
            }


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
