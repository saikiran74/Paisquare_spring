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
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
    private String advertisement_type;
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
    private String slug;
    /** Auto-generate slug before saving */
    @PrePersist
    @PreUpdate
    public void generateSlug() {
        if (this.slug == null || this.slug.isEmpty()) {
            // List of common stop words to ignore
            List<String> stopWords = Arrays.asList("the", "is", "and", "your", "to", "for", "with", "on", "of", "a", "in", "by", "this", "an");

            // Convert description to lowercase and split into words
            String[] words = description.toLowerCase().split("\\s+");

            // Filter out stop words and limit to 6 keywords
            String slugBase = Arrays.stream(words)
                    .filter(word -> !stopWords.contains(word))
                    .limit(6)  // Keep only 6 meaningful words
                    .collect(Collectors.joining("-")); // Join with "-"

            // Normalize and remove special characters
            slugBase = Normalizer.normalize(slugBase, Normalizer.Form.NFD);
            slugBase = slugBase.replaceAll("[^a-z0-9-]", "");

            // Ensure uniqueness by appending ID
            this.slug = slugBase;
        }
    }


    public void setAdvertiser(Register advertiser) {
        this.advertiser = advertiser;
    }

    public void setVisiteduser(List<Long> visiteduser) {
        this.visiteduser = visiteduser;
    }

}
