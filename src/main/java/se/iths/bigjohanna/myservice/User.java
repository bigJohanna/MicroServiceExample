package se.iths.bigjohanna.myservice;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class User {

    @Id @GeneratedValue Integer id;
    String userName;
    String realName;
    String city;
    Float income;
    Boolean inRelationship;


    public User(Integer id, String userName, String realName, String city, Float income, Boolean inRelationship) {
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        this.city = city;
        this.income = income;
        this.inRelationship = inRelationship;
    }
}