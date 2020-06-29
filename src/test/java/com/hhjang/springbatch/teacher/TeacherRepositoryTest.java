package com.hhjang.springbatch.teacher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TeacherRepositoryTest {
    @Autowired
    TeacherRepository repository;

    @Test
    public void create() {
        // Given
        Teacher teacher = new Teacher();
        teacher.setName("hyeonha");
        teacher.setSubject("math");

        // When
        Teacher savedTeacher = repository.save(teacher);

        // Then
        assertThat(savedTeacher.getName()).isEqualTo("hyeonha");
        assertThat(savedTeacher.getSubject()).isEqualTo("math");
    }
}