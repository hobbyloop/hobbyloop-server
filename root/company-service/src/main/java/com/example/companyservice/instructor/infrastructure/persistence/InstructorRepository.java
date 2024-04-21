package com.example.companyservice.instructor.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

import com.example.companyservice.instructor.domain.Instructor;

public interface InstructorRepository extends CrudRepository<Instructor, Long> {
}
