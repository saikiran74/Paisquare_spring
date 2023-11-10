package com.paisa_square.paisa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private String description;
    private String url;
    private Integer pai;
    private Integer paiperclick;
    private Integer paisa;
    private Integer paisaperclick;
    private String country;
    private String state;
    private String district;
    private Number likescount;
    private Number commentscount;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn()
    private Register advertiser;

    @ElementCollection
    private List<Long> visiteduser = new ArrayList<>();
    @ElementCollection
    private List<Long> commenteduser = new ArrayList<>();
    @ElementCollection
    private List<Long> likes = new ArrayList<>();

    public List<Long> getVisiteduser() {
        return visiteduser;
    }

    public void setVisiteduser(List<Long> visiteduser) {
        this.visiteduser = visiteduser;
    }


    @CreationTimestamp
    private Date opendate;
    @UpdateTimestamp
    private Date lastupdate;

    public Register getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(Register advertiser) {
        this.advertiser = advertiser;
    }
}
