package com.example.companyservice.instructor.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

import com.example.companyservice.instructor.domain.InstructorProfile;

public interface InstructorProfileRepository extends CrudRepository<InstructorProfile, Long> {
}
