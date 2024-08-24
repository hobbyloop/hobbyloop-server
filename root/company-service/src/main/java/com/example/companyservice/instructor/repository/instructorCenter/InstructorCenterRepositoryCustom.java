package com.example.companyservice.instructor.repository.instructorCenter;

import com.example.companyservice.instructor.entity.InstructorCenter;

import java.util.List;

public interface InstructorCenterRepositoryCustom {

    List<InstructorCenter> getInstructorCenterList(long centerId, int sort);
}
