package com.example.companyservice.lecture.repository;

import com.example.companyservice.lecture.entity.Lecture;
import com.example.companyservice.lecture.entity.LectureBreakHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureBreakHourRepository extends JpaRepository<LectureBreakHour, Long> {

    List<LectureBreakHour> findAllByLecture(Lecture lecture);
}
