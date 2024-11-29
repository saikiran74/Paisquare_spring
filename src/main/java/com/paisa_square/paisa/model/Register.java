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
    @Column(unique = true)
    private Long userId;
    private String username;
    private String advertiserName;
    private String email;
    private String country="IN";
    private Integer countryCode=91;
    private String mobileNumber;
    private String brandLocation;
    private BigDecimal pai= BigDecimal.valueOf(500.00);
    private BigDecimal paisa= BigDecimal.valueOf(0);
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
    private Number NoOfRating=BigDecimal.valueOf(0);
    private BigDecimal Rating=BigDecimal.valueOf(0);
    private Integer NoOfAdvertisements=0;
    private Integer NoOfLikes=0;
    private Integer NoOfComments=0;
    private Integer NoOfFollowers=0;
    private Integer NoOfSavedAds=0;
    private Integer NoOfVisit=0;
    private Integer NoOfProfileVisits=0;
    private String youtube;
    private String facebook;
    private String instagram;
    private String twitter;
    private String pinterest;
    private String brandCategory;
    private String brandTargetGender;
    private String brandEstablishedIn;
    private String brandCompanyEmployeeSize;
    private String pincode;
    private String accountType;
    private String[] brandHashTags;
    private String[] pinCodes;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profileImage;
    private String brandTargetAges;


    public static void main(String[] args) {
        // Create an instance of MyClass
        Register myInstance = new Register();
        String email = myInstance.getEmail(); // This is the correct way
    }

}
