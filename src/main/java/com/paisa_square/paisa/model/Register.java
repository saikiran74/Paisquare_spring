package com.paisa_square.paisa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

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
    @ManyToOne
    @JoinColumn(name = "advertise_id")
    private Advertise advertise;



    public static void main(String[] args) {
        // Create an instance of MyClass
        Register myInstance = new Register();
        String email = myInstance.getEmail(); // This is the correct way
    }

}
