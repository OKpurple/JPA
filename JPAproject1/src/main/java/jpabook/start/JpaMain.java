package jpabook.start;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
		
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		
		try {
			//항상 트랜잭션 안에서 데이터를 변경해야 한다.
			tx.begin();
			logic(em);
			tx.commit();
		}catch(Exception e) {
			tx.rollback();
		}finally {
			em.close();
		}
		emf.close();
	}

	private static void logic(EntityManager em) {
		String id = "id1";
		Member  member = new Member();
		member.setId(id);
		member.setUsername("우희");
		member.setAge(2);
		
		//영속성 등록
		em.persist(member);
		//수정 (영속성 내에서 추적하여 수정한다)
		member.setAge(30);
		
		//한건 조회 영속성 된 것들 중 찾는다.
		Member findMember = em.find(Member.class, id);
		System.out.println("findMember="+findMember.getUsername()+", age= "+findMember.getAge());
		
		//List<Member> members =em.createQuery("select m from Member m",Member.class).getResultList();
		
		//System.out.println("member.size = "+members.size());
		
		em.remove(findMember);
		
		//여기까지 persistence-context에서만 일어나는일 후에 commit을 하면 
		//쓰기지연 SQL저장소에서 데이터베이스로 데이터를 보내기 때문에 성능을 좋게 만들 수 있다.
		
	}
}
