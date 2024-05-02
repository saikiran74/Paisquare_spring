package com.paisa_square.paisa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
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
    private String advertiserName;
    private String email;
    private String country;
    private Integer countryCode;
    private String mobileNumber;
    private String brandLocation;
    private String password;
    private BigDecimal pai;
    private BigDecimal paisa;
    @CreationTimestamp
    private Date opendate;
    @UpdateTimestamp
    private Date lastupdate;
    @ElementCollection
    private List<Long> following = new ArrayList<>();
    @ElementCollection
    private List<Long> followers = new ArrayList<>();
    @ElementCollection
    private List<Long> blocked = new ArrayList<>();
    private String brandName;
    private String brandDescription;
    private String brandTagLine;
    private String website;
    private Integer NoOfAdvertisements;
    private Integer NoOfLikes;
    private Integer NoOfComments;
    private Integer NoOfFollowers;
    private Integer NoOfSavedAds;
    private Integer NoOfVisit;
    private Integer NoOfProfileVisits;
    private String youtube;
    private String facebook;
    private String instagram;
    private String twitter;
    private String pinterest;
    private String brandCategory;
    private String brandTargetGender;
    private String brandEstablishedIn;
    private String brandCompanyEmployeeSize;
    private String[] brandHashTags;
    private String[] pinCodes;
    private String brandTargetAges;


    public static void main(String[] args) {
        // Create an instance of MyClass
        Register myInstance = new Register();
        String email = myInstance.getEmail(); // This is the correct way
    }

}
