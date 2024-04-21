package com.example.companyservice.instructor.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

import com.example.companyservice.instructor.domain.InstructorOauth;

public interface InstructorOauthRepository extends CrudRepository<InstructorOauth, Long> {
}
