package sunghs.java.utils.validation.model;

import lombok.Data;

@Data
public class StudentInfo {

    private long studentNo;

    private String name;

    private int age;

    public StudentInfo(long studentNo, String name, int age) {
        this.studentNo = studentNo;
        this.name = name;
        this.age = age;
    }
}
