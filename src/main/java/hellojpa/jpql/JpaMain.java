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
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            String query = "select t.members.size from Team t";
            Integer result = em.createQuery(query, Integer.class)
                    .getSingleResult();
            System.out.println("result = " + result);


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
