package com.paisa_square.paisa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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
@ToString
public class Advertise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String brandname;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String url;
    private String backGroundColor;
    private BigDecimal pai;
    private BigDecimal paiperclick;
    private BigDecimal availablepai;
    private BigDecimal paisa;
    private BigDecimal paisaperclick;
    private BigDecimal availablepaisa;
    private String location=" ";
    private Date startDate;
    private Date endDate;
    private String country;
    private String state;
    private String status;
    private String district;
    private String hashtags;
    private String pincodes;
    private Integer likescount=0;
    private Integer commentscount=0;
    private Integer visitscount=0;
    @Getter
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn()
    private Register advertiser;
    @ElementCollection
    private List<Long> favourites = new ArrayList<>();
    @Getter
    @ElementCollection
    private List<Long> visiteduser = new ArrayList<>();
    @ElementCollection
    private List<Long> commenteduser = new ArrayList<>();
    @ElementCollection
    private List<Long> likes = new ArrayList<>();


    @CreationTimestamp
    private Date opendate;
    @UpdateTimestamp
    private Date lastupdate;

    public void setAdvertiser(Register advertiser) {
        this.advertiser = advertiser;
    }

    public void setVisiteduser(List<Long> visiteduser) {
        this.visiteduser = visiteduser;
    }

}
