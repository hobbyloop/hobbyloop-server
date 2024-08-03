package com.example.companyservice.instructor.repository.instructorCenter;

import com.example.companyservice.instructor.entity.InstructorCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorCenterRepository extends JpaRepository<InstructorCenter, Long>, InstructorCenterRepositoryCustom {
}
