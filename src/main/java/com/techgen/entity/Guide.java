package com.techgen.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Guide {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	private Integer salary;

	@Column(name = "staff_id")
	private String staffId;

	@OneToMany(mappedBy = "guide", cascade = CascadeType.PERSIST) // by default fetch types for @OneToMany and @ManyToMany mappings are LAZY
	private Set<Student> students = new HashSet<>();

	public void addStudent(Student student) {
		students.add(student);
		student.setGuide(this);
	}

	public Guide(String name, Integer salary, String staffId) {
		super();
		this.name = name;
		this.salary = salary;
		this.staffId = staffId;
	}

	@Override
	public String toString() {
		return "Guide [id=" + id + ", name=" + name + ", salary=" + salary + ", staffId=" + staffId + "]";
	}

}
