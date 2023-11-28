package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Favourites;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Favouritesrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.serice.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class Favouritescontrol {
    @Autowired
    private Favouritesrepository Favouritesrepo;
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Registerrepository registerrepo;
    @Autowired
    private Advertiserepository adrepo;

    @PostMapping("/{userid}/{advertisementid}/addAdvetisementToFavourite")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Favourites saveadvertisement(@RequestBody Favourites favourite, @PathVariable("advertisementid") Long advertisementid, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> registermodel = registerservice.fetchId(userid);
        Optional<Advertise> advertisemodel= adrepo.findById(advertisementid);
        if (registermodel.isPresent()) {
            Register register = registermodel.get();
            Advertise advertise=advertisemodel.get();
            favourite.setUser(register);
            favourite.setAdvertiser(advertise.getAdvertiser());
            favourite.setAdvertisement(advertise);
            if(register.getFavourites().contains(advertisementid)){
                System.out.println("advertisementid exist in the register saved removing..");
                register.getFavourites().remove(advertisementid);
                registerrepo.save(register);
            }
            else{
                System.out.println("advertiserid not  exist saving the register saved adding..");
                register.getFavourites().add(advertisementid);
                registerrepo.save(register);
            }
        }
        System.out.println("addAdvetisementToFavourite->advertisment id not exits"+userid);
        Favouritesrepo.save(favourite);
        return favourite;
    }

    @GetMapping("{userid}/favouritegraph")
    @CrossOrigin(origins = "http://localhost:4200/")
    public List<Object[]> favouritegraph(@PathVariable("userid") Long userid) throws Exception {
        System.out.println(Favouritesrepo.favouritesgraph(userid));
        return Favouritesrepo.favouritesgraph(userid);
    }
}
