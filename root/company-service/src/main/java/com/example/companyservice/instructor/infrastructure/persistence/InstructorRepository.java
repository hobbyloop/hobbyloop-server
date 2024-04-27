package com.example.companyservice.instructor.infrastructure.persistence;

import org.springframework.data.repository.CrudRepository;

import com.example.companyservice.instructor.domain.Instructor;

import java.util.Optional;

public interface InstructorRepository extends CrudRepository<Instructor, Long> {

    Optional<Instructor> findByEmail(String email);
}
