package hellojpa;

import jakarta.persistence.*;

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
            member.setUsername("member1");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String query =
                    "select " +
                            "case when m.age <= 10 then '학생요금' " +
                            "     when m.age >= 60 then '경로요금' " +
                            "     else '일반요금' " +
                            "end " +
                            "from Member m";

            String query2 = "select coalesce(m.username, '이름 없는 회원') as username " +
                    "from Member m";

            String query3 = "select nullif(m.username, '이름 없는 회원') as username " +
                    "from Member m";

            String query4 = "select m.team.name From Member m";

            String query5 = "select t.members from Team t";

            List<String> result = em.createQuery(query, String.class)
                    .getResultList();

            for(String s : result) {
                System.out.println("s = " + s);
            }

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
