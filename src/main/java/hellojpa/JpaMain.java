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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);


            // Query query2 = em.createQuery("select m.username, m.age from Member m", Member.class);
            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();

            System.out.println("result = " + result.getUsername());

            // team 조회해야해서 inner 조인 쿼리나감
            List<Team> result2 = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();

            // 이럴 때는 join 쿼리로 써서 조인 쿼리가 나가는걸 알 수 있게 한다
            List<Team> result3 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();
            

            // 타입 2개일 때
            List<MemberDTO> resultList = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m, MemberDTO.class")
                    .getResultList();

            MemberDTO memberDTO = resultList.get(0);
            System.out.println("memberDTO = " + memberDTO.getUsername());
            System.out.println("memberDTO = " + memberDTO.getAge());

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
