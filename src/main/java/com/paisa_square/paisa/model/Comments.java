package com.paisa_square.paisa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer advertisementid;
    private String userid;
    private String comment;
    private String remark;
    private String temp1;
    @CreationTimestamp
    private Date opendate;
    @UpdateTimestamp
    private Date lastupdate;
    @ManyToOne()
    @JoinColumn()
    private Advertise advertise;

    public Advertise getAdvertise() {
        return advertise;
    }

    public void setAdvertise(Advertise advertise) {
        this.advertise = advertise;
    }
}
