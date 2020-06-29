package com.hhjang.springbatch.entity.classinformation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ClassInformation {

    private String teacherName;
    private int studentSize;

    public ClassInformation(String teacherName, int studentSize) {
        this.teacherName = teacherName;
        this.studentSize = studentSize;
    }
}
