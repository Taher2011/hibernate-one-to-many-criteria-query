package com.techgen.client;

import java.util.List;

import com.techgen.entity.Guide;
import com.techgen.entity.Student;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public class Client {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = null;
		EntityManager entityManager = null;
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("student-guide");

			entityManager = entityManagerFactory.createEntityManager();

			EntityTransaction transaction = entityManager.getTransaction();

			// getGuide(entityManager, transaction);
			// getGuideName(entityManager, transaction);
			// getGuideNameAndSalary(entityManager, transaction);
			// getGuideWhoseSalaryIs1000(entityManager, transaction);
			// getGuideUsingDynamiceQuery(entityManager, transaction);
			// getGuideUsingWildcard(entityManager, transaction);
			// getGuidesCount(entityManager, transaction);
			// getStudentsJoinGuides(entityManager, transaction);
			// getStudentsLeftJoinGuides(entityManager, transaction);
			// getGuidesJoinStudents(entityManager, transaction);
			// getGuidesJoinStudentsEager(entityManager, transaction);
			// getGuidesLeftJoinStudentsEager(entityManager, transaction);
			// getStudentsJoinGuides1(entityManager, transaction);
			// getStudentsJoinGuides2(entityManager, transaction);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (entityManager != null && entityManager.isOpen()) {
				entityManager.close();
			}
			if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
				entityManagerFactory.close();
			}
		}
	}

	private static void getGuide(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Guide> criteria = criteriaBuilder.createQuery(Guide.class);
		Root<Guide> root = criteria.from(Guide.class);
		criteria.select(root);

		TypedQuery<Guide> query = entityManager.createQuery(criteria);
		List<Guide> guides = query.getResultList();
		for (Guide guide : guides) {
			System.out.println(guide);
		}

		transaction.commit();
	}

	private static void getGuideName(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<String> criteria = criteriaBuilder.createQuery(String.class);
		Root<Guide> root = criteria.from(Guide.class);
		Path<String> name = root.get("name");
		criteria.select(name);

		Query query = entityManager.createQuery(criteria);
		List<String> guideNames = query.getResultList();
		for (String guideName : guideNames) {
			System.out.println(guideName);
		}

		transaction.commit();
	}

	private static void getGuideNameAndSalary(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteria = criteriaBuilder.createQuery(Object[].class);
		Root<Guide> root = criteria.from(Guide.class);
		Path<String> name = root.get("name");
		Path<Integer> salary = root.get("salary");
		criteria.multiselect(name, salary);

		Query query = entityManager.createQuery(criteria);
		List<Object[]> guideNamesAndSalaries = query.getResultList();
		for (Object[] guideNameAndSalary : guideNamesAndSalaries) {
			System.out.println(guideNameAndSalary[0] + "  " + guideNameAndSalary[1]);
		}

		transaction.commit();
	}

	private static void getGuideWhoseSalaryIs1000(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Guide> criteria = criteriaBuilder.createQuery(Guide.class);
		Root<Guide> root = criteria.from(Guide.class);
		Path<Integer> salary = root.get("salary");
		criteria.where(criteriaBuilder.equal(salary, 1000));
		criteria.select(root);

		TypedQuery<Guide> query = entityManager.createQuery(criteria);
		List<Guide> guides = query.getResultList();
		for (Guide guide : guides) {
			System.out.println(guide);
		}

		transaction.commit();
	}

	private static void getGuideUsingDynamiceQuery(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Guide> criteria = criteriaBuilder.createQuery(Guide.class);
		Root<Guide> root = criteria.from(Guide.class);
		criteria.where(criteriaBuilder.equal(root.get("name"), criteriaBuilder.parameter(String.class, "name")));
		criteria.select(root);
		String name = "Ian Lamb";

		TypedQuery<Guide> query = entityManager.createQuery(criteria).setParameter("name", name);
		List<Guide> guides = query.getResultList();
		for (Guide guide : guides) {
			System.out.println(guide);
		}

		transaction.commit();
	}

	private static void getGuideUsingWildcard(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Guide> criteria = criteriaBuilder.createQuery(Guide.class);
		Root<Guide> root = criteria.from(Guide.class);
		Path<String> staffId = root.get("staffId");
		criteria.where(criteriaBuilder.like(staffId, "2000%"));
		criteria.select(root);

		TypedQuery<Guide> query = entityManager.createQuery(criteria);
		List<Guide> guides = query.getResultList();
		for (Guide guide : guides) {
			System.out.println(guide);
		}

		transaction.commit();
	}

	private static void getGuidesCount(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<Guide> root = criteria.from(Guide.class);
		criteria.select(criteriaBuilder.count(root));

		TypedQuery<Long> query = entityManager.createQuery(criteria);
		Long guideCount = query.getSingleResult();
		System.out.println(guideCount);

		transaction.commit();
	}

	// below method is for inner join
	// jpql query will be "select student from Student student join student.guide guide"
	// sql query will be "select * from student s join guide g on s.guide_id = g.id"
	private static void getStudentsJoinGuides(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
		Root<Student> root = criteria.from(Student.class);
		Join<Student, Guide> join = root.join("guide");
		criteria.select(root);

		TypedQuery<Student> query = entityManager.createQuery(criteria);
		List<Student> students = query.getResultList();
		for (Student student : students) {
			System.out.println(student);
		}

		transaction.commit();
	}

	// below method is for left inner join
	// jpql query will be "select student from Student student left join student.guide guide"
	// sql query will be "select * from student s left join guide g on s.guide_id = g.id"
	private static void getStudentsLeftJoinGuides(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
		Root<Student> root = criteria.from(Student.class);
		Join<Student, Guide> join = root.join("guide", JoinType.LEFT);
		criteria.select(root);

		TypedQuery<Student> query = entityManager.createQuery(criteria);
		List<Student> students = query.getResultList();
		for (Student student : students) {
			System.out.println(student);
		}

		transaction.commit();
	}

	// below method is for inner join
	// jpql query will be "select guide from Guide guide join guide.students student"
	// sql query will be "select * from guide g join student s on g.id = s.guide_id"
	private static void getGuidesJoinStudents(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Guide> criteria = criteriaBuilder.createQuery(Guide.class);
		Root<Guide> root = criteria.from(Guide.class);
		Join<Guide, Student> join = root.join("students");
		criteria.select(root);

		TypedQuery<Guide> query = entityManager.createQuery(criteria);
		List<Guide> guides = query.getResultList();
		for (Guide guide : guides) {
			System.out.println(guide);
			System.out.println(guide.getStudents());
		}

		transaction.commit();
	}

	// below method is for inner join
	// jpql query will be "select guide from Guide guide join fetch guide.students student"
	// sql query will be "select * from guide g join student s on g.id = s.guide_id"
	private static void getGuidesJoinStudentsEager(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Guide> criteria = criteriaBuilder.createQuery(Guide.class);
		Root<Guide> root = criteria.from(Guide.class);
		Fetch<Guide, Student> join = root.fetch("students");
		criteria.select(root);

		TypedQuery<Guide> query = entityManager.createQuery(criteria);
		List<Guide> guides = query.getResultList();
		for (Guide guide : guides) {
			System.out.println(guide);
			System.out.println(guide.getStudents());
		}

		transaction.commit();
	}

	// below method is for left inner join
	// jpql query will be "select guide from Guide guide left join fetch guide.students student"
	// sql query will be "select * from guide g left join student s on g.id = s.guide_id"
	private static void getGuidesLeftJoinStudentsEager(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Guide> criteria = criteriaBuilder.createQuery(Guide.class);
		Root<Guide> root = criteria.from(Guide.class);
		Fetch<Guide, Student> join = root.fetch("students", JoinType.LEFT);
		criteria.select(root);

		TypedQuery<Guide> query = entityManager.createQuery(criteria);
		List<Guide> guides = query.getResultList();
		for (Guide guide : guides) {
			System.out.println(guide);
			System.out.println(guide.getStudents());
		}

		transaction.commit();
	}

	private static void getStudentsJoinGuides1(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		TypedQuery<Student> query = entityManager.createNamedQuery("Student.getStudentGuide1", Student.class)
				.setParameter("id", 2);
		List<Student> students = query.getResultList();
		for (Student student : students) {
			System.out.println(student);
		}

		transaction.commit();
	}

	private static void getStudentsJoinGuides2(EntityManager entityManager, EntityTransaction transaction) {
		transaction.begin();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Guide> criteria = criteriaBuilder.createQuery(Guide.class);
		Root<Guide> root = criteria.from(Guide.class);
		Path<Integer> id = root.get("id");
		criteria.where(criteriaBuilder.equal(id, 2));
		criteria.select(root);

		TypedQuery<Guide> query = entityManager.createQuery(criteria);
		Guide guide = query.getSingleResult();

		TypedQuery<Student> query1 = entityManager.createNamedQuery("Student.getStudentGuide2", Student.class)
				.setParameter("guide", guide);
		List<Student> students = query1.getResultList();
		for (Student student : students) {
			System.out.println(student);
		}

		transaction.commit();
	}

}
