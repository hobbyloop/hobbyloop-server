package com.example.companyservice.instructor.infrastructure.persistence;

import org.springframework.data.repository.Repository;

import com.example.companyservice.instructor.domain.Instructor;

public interface InstructorRepository extends Repository<Instructor, Long> {
	<T extends Instructor> T save(T entity);
}
