package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Favourites;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Favouritesrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.service.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class Favouritescontrol {
    @Autowired
    private Favouritesrepository favouritesRepo;
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Registerrepository registerrepo;
    @Autowired
    private Advertiserepository adrepo;

    @PostMapping("/addAdvetisementToFavourite/{userid}/{advertisementid}")
    public Favourites saveadvertisement(@RequestBody Favourites favourite, @PathVariable("advertisementid") Long advertisementid, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> registermodel = registerrepo.findByUserId(userid);
        Optional<Advertise> advertisemodel= adrepo.findById(advertisementid);

        if (registermodel.isPresent() && advertisemodel.isPresent()) {
            Register register = registermodel.get();
            Advertise advertise=advertisemodel.get();
            Optional<Register> AdvertiserInregisterModel=registerrepo.findByUserId(advertise.getAdvertiser().getId());
            Register AdvertiserInregister =AdvertiserInregisterModel.get();
            favourite.setUser(register);
            favourite.setAdvertiser(advertise.getAdvertiser());
            favourite.setAdvertisement(advertise);
            if(advertise.getFavourites().contains(userid)){
                advertise.getFavourites().remove(userid);
                AdvertiserInregister.setNoOfSavedAds(AdvertiserInregister.getNoOfSavedAds()-1);
                registerrepo.save(AdvertiserInregister);
                registerrepo.save(register);
            }
            else{
                advertise.getFavourites().add(userid);
                AdvertiserInregister.setNoOfSavedAds(AdvertiserInregister.getNoOfSavedAds()+1);
                registerrepo.save(AdvertiserInregister);
                registerrepo.save(register);
            }
        }
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

    @GetMapping("/favouritegraph/{userid}/{period}")
    public List<Object[]> favouritegraph(@PathVariable("userid") Long userid,@PathVariable("period") String period) throws Exception {
        if(Objects.equals(period, "weekly")){
            return favouritesRepo.weeklygraph(userid);
        } else if (Objects.equals(period, "lastmonth")) {
            return favouritesRepo.lastmonth(userid);
        } else if (Objects.equals(period, "thismonth")) {
            return favouritesRepo.thismonth(userid);
        }
        else{
            return favouritesRepo.yearlygraph(userid);
        }
    }
    @GetMapping("/getfavouriteadvertisementslist/{userid}")
    public List<Advertise> getUserAdvertisements(@PathVariable("userid") Integer userid) {
        return adrepo.findByfavourites(userid);
    }
}
