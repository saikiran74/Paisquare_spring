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
public class Advertisementtransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn()
    private Advertise advertisement;
    @ManyToOne()
    @JoinColumn()
    private Register advertiser;
    private BigDecimal advertisementpai;
    private BigDecimal advertisementpaisa;
    private BigDecimal availablepai;
    private BigDecimal availablepaisa;
    @CreationTimestamp
    private Date opendate;
    @UpdateTimestamp
    private Date lastupdate;

    public void save(Advertisementtransaction trans) {
    }
}