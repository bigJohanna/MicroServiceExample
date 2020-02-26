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

    @Id @GeneratedValue int id;
    String userName;
    String realName;
    String city;
    float income;
    boolean inRelationship;


}
