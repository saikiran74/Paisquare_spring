package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Favourites;
import com.paisa_square.paisa.model.Likes;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Favouritesrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.serice.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class Favouritescontrol {
    @Autowired
    private Favouritesrepository favouritesRepo;
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
            if(advertise.getFavourites().contains(userid)){
                System.out.println("advertisementid exist in the register saved removing..");
                advertise.getFavourites().remove(userid);
                registerrepo.save(register);
            }
            else{
                System.out.println("advertiserid not  exist saving the register saved adding..");
                advertise.getFavourites().add(userid);
                registerrepo.save(register);
            }
        }
        System.out.println("addAdvetisementToFavourite->advertisment id not exits"+userid);
        Advertise advertise2=advertisemodel.get();
        Optional<Favourites> favouritesModel = favouritesRepo.findByAdvertisementIdAndUserIdAndAdvertiserId(advertisementid, userid,advertise2.getAdvertiser().getId());
        if (favouritesModel.isPresent()) {
            Favourites favouritesobj = favouritesModel.get();
            if(favouritesobj.isFavourites()){
                favouritesobj.setFavourites(false);
                favouritesobj.setLastupdate(new Date());
                favouritesRepo.save(favouritesobj);
            }
            else{
                favouritesobj.setFavourites(true);
                favouritesobj.setLastupdate(new Date());
                favouritesRepo.save(favouritesobj);
            }
        }
        else{
            favourite.setFavourites(true);
            favouritesRepo.save(favourite);
        }
        return favourite;
    }

    @GetMapping("{userid}/favouritegraph")
    @CrossOrigin(origins = "http://localhost:4200/")
    public List<Object[]> favouritegraph(@PathVariable("userid") Long userid) throws Exception {
        System.out.println(favouritesRepo.favouritesgraph(userid));
        return favouritesRepo.favouritesgraph(userid);
    }
    @GetMapping("/{userid}/getfavouriteadvertisementslist")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getUserAdvertisements(@PathVariable("userid") Integer userid) {
        return adrepo.findByfavourites(userid);
    }
}
