package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.*;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Followerrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.service.Followerservice;
import com.paisa_square.paisa.service.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class Followercontrol {
    @Autowired
    private Followerservice service;
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Followerrepository followersrepo;
    @Autowired
    private Registerrepository Registerrepo;
    @Autowired
    private Advertiserepository adrepo;

    @PostMapping("/follow/{userid}/{advertiserid}")
    public Followers comment(@RequestBody Followers follow, @PathVariable("advertiserid") Long advertiserid,@PathVariable("userid") Long userid) throws Exception {
        Optional<Register> registermodel = Registerrepo.findByUserId(userid);
        Optional<Register> advertisermodel = Registerrepo.findByUserId(advertiserid);
        if (registermodel.isPresent()) {
            Register register = registermodel.get();
            Register advertiser = advertisermodel.get();
            follow.setAdvertiser(advertiser);
            follow.setUser(register);
            if(register.getFollowing().contains(advertiserid)){
                register.getFollowing().remove(advertiserid);
            }
            else{
                register.getFollowing().add(advertiserid);
            }
            if(advertiser.getFollowers().contains(userid)){
                advertiser.getFollowers().remove(userid);
            }
            else{
                advertiser.getFollowers().add(userid);
            }
            Registerrepo.save(register);
        }
        Optional<Followers> followersmodel = followersrepo.findByAdvertiserIdAndUserId(advertiserid, userid);
        if (followersmodel.isPresent()) {
            Followers followersobj = followersmodel.get();
            if(followersobj.isFollowing()){
                followersobj.setFollowing(false);
                followersobj.setLastupdate(new Date());
                followersrepo.save(followersobj);
            }
            else{
                followersobj.setFollowing(true);
                followersobj.setLastupdate(new Date());
                followersrepo.save(followersobj);
            }
        }
        else
            followersrepo.save(follow);
        return follow;
    }
    @GetMapping("/followersgraph/{userid}/{period}")
    public List<Object[]> followersgraph(@PathVariable("userid") Long userid,@PathVariable("period") String period) throws Exception {
        if(Objects.equals(period, "weekly")){
            return followersrepo.weeklygraph(userid);
        } else if (Objects.equals(period, "lastmonth")) {
            return followersrepo.lastmonth(userid);
        } else if (Objects.equals(period, "thismonth")) {
            return followersrepo.thismonth(userid);
        }
        else{
            return followersrepo.yearlygraph(userid);
        }
    }
    @GetMapping("/getfollowingadvertisementslist/{userid}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getfollowingadvertisementslist(@PathVariable("userid") Long userid) {
        Optional<Register> registermodel = Registerrepo.findByUserId(userid);
        if (registermodel.isPresent()) {
           return adrepo.findAdvertiseByUserFollowing(userid);
        } else {
            return Collections.emptyList();
        }
    }
    @GetMapping("/UserFollowingProfiles/{userid}")
    public List<Register> getUserFollowingProfiles(@PathVariable("userid") Long userid) {
        List<Register> followersModel=Registerrepo.findAllProfilesUserFollowing(userid);
        if (!followersModel.isEmpty()) {
            return Registerrepo.findAllProfilesUserFollowing(userid);
        } else {
            return Collections.emptyList();
        }
    }
}
