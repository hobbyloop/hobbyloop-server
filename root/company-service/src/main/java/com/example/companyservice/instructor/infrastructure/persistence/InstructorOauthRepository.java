package com.example.companyservice.instructor.infrastructure.persistence;

import org.springframework.data.repository.Repository;

import com.example.companyservice.instructor.domain.InstructorOauth;

public interface InstructorOauthRepository extends Repository<InstructorOauth, Long> {
	<T extends InstructorOauth> T save(T entity);
}
