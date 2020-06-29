package com.hhjang.springbatch.entity.teacher;

import com.hhjang.springbatch.entity.student.Student;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Setter
@NoArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String subject;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();

    @Builder
    public Teacher(String name, String subject) {
        this.name = name;
        this.subject = subject;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.setTeacher(this);
    }
}
