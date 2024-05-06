package com.example.companyservice.instructor.infrastructure.persistence;

import org.springframework.data.repository.Repository;

import com.example.companyservice.instructor.domain.InstructorProfile;

public interface InstructorProfileRepository extends Repository<InstructorProfile, Long> {
	<T extends InstructorProfile> T save(T entity);
}
