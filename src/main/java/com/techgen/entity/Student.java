package com.techgen.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NamedQueries({
		@NamedQuery(name = "Student.getStudentAndAssociatedGuidesByGuideId", query = "select student from Student student where student.guide.id = :id"),
		@NamedQuery(name = "Student.getStudentAndAssociatedGuides", query = "select student from Student student where student.guide = :guide") })
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "enrollment_id")
	private String enrollmentId;

	private String name;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY) // by default fetch types for @ManyToOne and @OneToOne mappings are EAGER
	@JoinColumn(name = "guide_id")
	private Guide guide;

	public Student(String enrollmentId, String name, Guide guide) {
		super();
		this.enrollmentId = enrollmentId;
		this.name = name;
		this.guide = guide;
	}

	public Student(String enrollmentId, String name) {
		super();
		this.enrollmentId = enrollmentId;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", enrollmentId=" + enrollmentId + ", name=" + name + ", guide=" + guide + "]";
	}

}
