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

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setAge(10);
            member1.setType(MemberType.ADMIN);
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();


            String query = "select concat('a', 'b') from Member m";
            List<String> result = em.createQuery(query, String.class)
                    .getResultList();
            for (String s : result) {
                System.out.println("s = " + s);
            }

            String query2 = "select locate('ce', 'abcdef') from Member m";
            List<Integer> result2 = em.createQuery(query2, Integer.class)
                    .getResultList();
            for (Integer i : result2) {
                System.out.println("i = " + i);
            }

            String query3 = "select function('group_concat', m.username) from Member m";
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
