package com.example.companyservice.instructor.domain;

import static com.example.companyservice.instructor.domain.InstructorStatus.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.companyservice.common.vo.EmailAddress;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(
	name = "ci_unique",
	columnNames = ("ci")
)})
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Instructor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Embedded
	private EmailAddress emailAddress;
	@Column(nullable = false)
	private String ci;
	@Column(nullable = false)
	private String di;
	@Column(nullable = false, columnDefinition = "varchar(255)")
	@Enumerated(EnumType.STRING)
	private InstructorStatus status;
	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private boolean consentToMarketingCommunications;
	@Column(nullable = false, columnDefinition = "tinyint(1)")
	private boolean consentToPersonalInformation;
	@Column(nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;
	@Column(nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;

	public Instructor(EmailAddress emailAddress, String ci, String di,
		boolean consentToMarketingCommunications, boolean consentToPersonalInformation) {

		this.emailAddress = emailAddress;
		this.ci = ci;
		this.di = di;
		this.status = ACTIVE;
		this.consentToMarketingCommunications = consentToMarketingCommunications;
		this.consentToPersonalInformation = consentToPersonalInformation;
	}

	public String getEmailAddress() {
		return emailAddress.getValue();
	}
}
