package com.hhjang.springbatch.entity.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
