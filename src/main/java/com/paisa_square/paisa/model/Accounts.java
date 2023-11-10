package com.paisa_square.paisa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Number money;
    private Number pais;
    private String userid;
    private Number blockedmoney;
    private Number blockedpais;
    private Number bonusmoney;
    private Number bonuspais;
    @CreationTimestamp
    private Date opendate;
    @UpdateTimestamp
    private Date lastupdate;
}
