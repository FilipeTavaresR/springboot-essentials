package br.com.devdojo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
public class Student extends AbstractEntity {
    @NotEmpty(message = "Name cannot be null")
    @Column(nullable = false)
    private String name;
    @Email
    @NotEmpty(message = "Email cannot be null")
    private String email;
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public Student(String name, String email, Long id) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Student() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
