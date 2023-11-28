package com.paisa_square.paisa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    @CreationTimestamp
    private Date opendate;
    @UpdateTimestamp
    private Date lastupdate;
    @ElementCollection
    private List<Long> following = new ArrayList<>();
    @ElementCollection
    private List<Long> blocked = new ArrayList<>();
    @ElementCollection
    private List<Long> Favourites = new ArrayList<>();
    private String brandname;
    private String bio;
    private Number no_followers;
    private Number no_ads;
    private Number mobilenumber;
    private String country;
    private Number countrycode;
    private String address;
    private String website;
    private String youtube;
    private String facebook;
    private String instagram;
    private String twitter;
    private String pinterest;


    public static void main(String[] args) {
        // Create an instance of MyClass
        Register myInstance = new Register();
        String email = myInstance.getEmail(); // This is the correct way
    }

}
