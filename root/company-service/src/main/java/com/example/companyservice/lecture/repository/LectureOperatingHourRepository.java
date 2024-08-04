package com.example.companyservice.lecture.repository;

import com.example.companyservice.lecture.entity.Lecture;
import com.example.companyservice.lecture.entity.LectureOperatingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureOperatingHourRepository extends JpaRepository<LectureOperatingHour, Long> {

    List<LectureOperatingHour> findAllByLecture(Lecture lecture);
}