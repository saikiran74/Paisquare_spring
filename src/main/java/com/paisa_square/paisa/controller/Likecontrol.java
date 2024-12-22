package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Followers;
import com.paisa_square.paisa.model.Likes;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Likerepository;
import com.paisa_square.paisa.repository.Registerrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class Likecontrol {
    @Autowired
    private Registerrepository registerRepo;
    @Autowired
    private Advertiserepository adrepo;
    @Autowired
    private Likerepository likerepo;
    @PostMapping("/like/{userid}/{advertisementid}")
    public Likes like(@RequestBody Likes like,@PathVariable("userid") Long userid,@PathVariable("advertisementid") Long advertisementid) throws Exception{
        Likes likeobj=null;
        Optional<Advertise> advertismentmodel = adrepo.findById(advertisementid);
        Optional<Register> registermodel = registerRepo.findByUserId(userid);
        if(advertismentmodel.isPresent() && registermodel.isPresent()){
            Advertise advertise=advertismentmodel.get();
            Register register =registermodel.get();
            Optional<Register> AdvertiserInRegisterModel = registerRepo.findByUserId(userid);
            Register advertiserInRegister =AdvertiserInRegisterModel.get();
            like.setAdvertisement(advertise);
            like.setUser(register);
            like.setAdvertiser(advertise.getAdvertiser());
            //Updating advertise_likes
            if(advertise.getLikes().contains(userid)) {
                advertise.getLikes().remove(userid);
                //updating advertise like count
                advertise.setLikescount(advertise.getLikescount()-1);
                advertiserInRegister.setNoOfLikes(advertiserInRegister.getNoOfLikes()-1);
                registerRepo.save(advertiserInRegister);
                adrepo.save(advertise);
            }
            else{
                advertise.getLikes().add(userid);
                //updating advertise like count
                advertise.setLikescount(advertise.getLikescount()+1);
                advertiserInRegister.setNoOfLikes(advertiserInRegister.getNoOfLikes()+1);
                registerRepo.save(advertiserInRegister);
                adrepo.save(advertise);
            }
            Advertise advertise2=advertismentmodel.get();
            Optional<Likes> likesModel = likerepo.findByAdvertisementIdAndUserIdAndAdvertiserId(advertisementid, userid,advertise2.getAdvertiser().getId());
            if (likesModel.isPresent()) {
                Likes likesobj = likesModel.get();
                if(likesobj.isLiked()){
                    likesobj.setLiked(false);
                    likesobj.setLastupdate(new Date());
                    likerepo.save(likesobj);
                }
                else{
                    likesobj.setLiked(true);
                    likesobj.setLastupdate(new Date());
                    likerepo.save(likesobj);
                }
            }
            else {
                like.setLiked(true);
                likeobj = likerepo.save(like);
            }
        }
        return likeobj;
    }
    @GetMapping("/likesgraph/{userid}/{period}")
    public List<Object[]> likegraph(@PathVariable("userid") Long userid,@PathVariable("period") String period) throws Exception {
        if(Objects.equals(period, "weekly")){
            return likerepo.weeklygraph(userid);
        } else if (Objects.equals(period, "lastmonth")) {
            return likerepo.lastmonth(userid);
        } else if (Objects.equals(period, "thismonth")) {
            return likerepo.thismonth(userid);
        }
        else{
            return likerepo.yearlygraph(userid);
        }
    }
    @GetMapping("/getlikedadvertisementslist/{userid}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getlikedadvertisementslist(@PathVariable("userid") Integer userid) {
        return adrepo.findAllBylikes(userid);
    }

    @GetMapping("/likelist/{userid}")
    public List<Likes> getlikeFromRemote(@PathVariable("userid") Long userid) {
        return likerepo.findAllByAdvertiserId(userid);
    }
}