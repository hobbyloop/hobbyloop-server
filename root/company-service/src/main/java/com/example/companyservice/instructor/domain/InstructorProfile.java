package com.example.companyservice.instructor.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InstructorProfile {
	@Id
	@Column(nullable = false)
	private Long instructorId;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private LocalDate dateOfBirth;
	@Column(nullable = false)
	private String phoneNumber;
	@Column(nullable = false, columnDefinition = "varchar(255)")
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Column(nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;

	@OneToOne
	@MapsId
	@JoinColumn(nullable = false, name = "instructor_id")
	private Instructor instructor;

	public InstructorProfile(
		String name, LocalDate dateOfBirth, String phoneNumber, Gender gender, Instructor instructor) {

		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.instructor = instructor;
	}
}
