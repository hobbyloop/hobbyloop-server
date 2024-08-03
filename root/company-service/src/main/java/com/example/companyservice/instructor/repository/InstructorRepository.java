package com.example.companyservice.instructor.repository;

import com.example.companyservice.instructor.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    Optional<Instructor> findByProviderAndSubjectAndIsDeleteFalse(String provider, String subject);

    Optional<Instructor> findByNameAndBirthdayAndPhoneNumberAndIsDeleteFalse(String name, LocalDate birthday, String phoneNumber);
}
