package com.example.studentform_with_fragments;

import java.io.Serializable;

/**
 * Created by dghag on 3/19/2018.
 */

public class Student implements Serializable {
    String name;
    String email;
    String department;
    int mood;

    public Student(String name, String email, String department, int mood){
        this.name = name;
        this.email = email;
        this.department = department;
        this.mood = mood;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setDepartment(String department){
        this.department = department;
    }
    public void setMood(int mood) {
        this.mood = mood;
    }

    public String toString(){
        return "Student{" +
                "name='" + name + '\'' +
                ", emailAddress='" + email + '\'' +
                ", Department='" + department + '\''  +
                ", mood=" + mood +
                '}';
    }
}