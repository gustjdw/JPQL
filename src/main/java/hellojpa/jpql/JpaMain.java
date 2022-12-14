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

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            // inner join 시, inner는 생략 가능, left outer join 시, outer도 생략 가능
            String query = "select m from Member m left outer join m.team t";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            // 세타 조인
            String query2 = "select m from Member m, Team t where m.username = t.name";
            List<Member> result2 = em.createQuery(query2, Member.class)
                    .getResultList();
            System.out.println("result2.size() = " + result2.size());

            // 조인 대상 필터링
            String query3 = "select m from Member m left join m.team t on t.name = 'teamA'";
            List<Member> result3 = em.createQuery(query3, Member.class)
                    .getResultList();
            System.out.println("result3.size() = " + result3.size());

            // 연관관계 없는 엔티티 외부 조인
            String query4 = "select m from Member m left join Team t on m.username = t.name";
            List<Member> result4 = em.createQuery(query4, Member.class)
                    .getResultList();
            System.out.println("result4.size() = " + result4.size());

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
