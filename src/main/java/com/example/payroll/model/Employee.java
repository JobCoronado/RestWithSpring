package com.example.payroll.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class Employee {
    @Id @GeneratedValue
    private  Long id;
    private  String firstName;
    private  String lastName;
    private  String role;

    public Employee() {
    }

    public Employee(String firstName,String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public void setName(String name){
        String [] partName = name.split(" ");
        this.firstName = partName[0];
        this.lastName = partName[1];
    }

    public String getName(){
        return this.firstName + " " + this.lastName;
    }

}