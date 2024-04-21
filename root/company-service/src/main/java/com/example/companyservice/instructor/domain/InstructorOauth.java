package com.example.companyservice.instructor.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
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
public class InstructorOauth {
	@Id
	@Column(nullable = false)
	private Long instructorId;
	@Column(nullable = false)
	private String subject;
	@Column(nullable = false, columnDefinition = "varchar(255)")
	@Enumerated(EnumType.STRING)
	private OauthProvider provider;
	@Column(nullable = false)
	private String token;
	@Column(nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;
	@Column(nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;

	@OneToOne
	@MapsId
	@JoinColumn(nullable = false, name = "instructor_id")
	private Instructor instructor;

	public InstructorOauth(String subject, OauthProvider provider, String token, Instructor instructor) {
		this.subject = subject;
		this.provider = provider;
		this.token = token;
		this.instructor = instructor;
	}
}
