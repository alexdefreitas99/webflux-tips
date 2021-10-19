package com.alex.springtips;

import java.time.LocalDateTime;

public class Model {
    private String name;
    private Integer age;
    private LocalDateTime dateNasc;

    public Model(String name, Integer age, LocalDateTime dateNasc) {
        this.name = name;
        this.age = age;
        this.dateNasc = dateNasc;
    }

    public Model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public LocalDateTime getDateNasc() {
        return dateNasc;
    }

    public void setDateNasc(LocalDateTime dateNasc) {
        this.dateNasc = dateNasc;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", dateNasc=" + dateNasc +
                '}';
    }
}
