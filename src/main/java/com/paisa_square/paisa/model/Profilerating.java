package com.paisa_square.paisa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profilerating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn()
    private Register user;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn()
    private Register advertiser;
    private BigDecimal rating;
    @CreationTimestamp
    private Date opendate;
    @UpdateTimestamp
    private Date lastupdate;
}

