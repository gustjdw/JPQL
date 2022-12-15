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
            member.setUsername("관리자");
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();


            String query1 = "select " +
                    "case when m.age <= 10 then '학생 요금' " +
                    "when m.age >= 60 then '경로 요금' " +
                    "else '일반 요금' " +
                    "end " +
                    "from Member m";
            List<String> result1 = em.createQuery(query1, String.class)
                    .getResultList();
            for (String s : result1) {
                System.out.println("s = " + s);
            }

            String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m";
            List<String> result2 = em.createQuery(query2, String.class)
                    .getResultList();
            for (String s : result2) {
                System.out.println("s = " + s);
            }

            String query3 = "select nullif(m.username, '관리자') from Member m";
            List<String> result3 = em.createQuery(query3, String.class)
                    .getResultList();
            for (String s : result3) {
                System.out.println("s = " + s);
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
