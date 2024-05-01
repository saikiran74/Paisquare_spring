package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Followers;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.serice.Registerservice;
import com.paisa_square.paisa.model.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class Registationcontrol {
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Registerrepository registerRepo;
    @PostMapping("/registeruser")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Register registerUser(@RequestBody Register user) throws Exception {
        String tempEmailId = user.getEmail();
        if(tempEmailId!=null && !"".equals(tempEmailId)){
            Register userobj=registerservice.fetchUserByEmailId(tempEmailId);
            if(userobj!=null){
                throw new Exception("emailid is exist");
            }
        }
        Register userobj=null;
        userobj=registerservice.saveUser(user);
        return userobj;
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Register loginUser(@RequestBody Register login) throws Exception {
        String tempEmailId=login.getEmail();
        String tempPass=login.getPassword();
        Register userObj=null;
        if(tempEmailId!=null && tempPass!=null){
            userObj=registerservice.fetchUserByEmailIdAndPassword(tempEmailId,tempPass);
        }
        if(userObj==null){
            throw new Exception("Bad email and password"+tempEmailId+tempPass);
        }
        return userObj;
    }

    @GetMapping("/{userid}/profile")
    @CrossOrigin(origins = "http://localhost:4200")
    public Optional<Register> getAllAdvertisements(@PathVariable("userid") Long userid) {
        return registerRepo.findById(userid);
    }
    @PostMapping("{userid}/updateProfile/brandInformation")
    public Register brandInformation(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findById(userid);
        Register userprofileobj = null;
        if (userProfile.isPresent()) {
            userprofileobj = userProfile.get();
            userprofileobj.setBrandName(profile.getBrandName());
            userprofileobj.setBrandDescription(profile.getBrandDescription());
            userprofileobj.setBrandTagLine(profile.getBrandTagLine());
            userprofileobj.setWebsite(profile.getWebsite());
            registerRepo.save(userprofileobj);
        }
        return userprofileobj;
    }

    @PostMapping("{userid}/updateProfile/personalInformation")
    public Register personalInformation(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findById(userid);
        Register userprofileobj = null;
        if (userProfile.isPresent()) {
            userprofileobj = userProfile.get();
            userprofileobj.setAdvertiserName(profile.getAdvertiserName());
            userprofileobj.setEmail(profile.getEmail());
            userprofileobj.setMobileNumber(profile.getMobileNumber());
            userprofileobj.setBrandLocation(profile.getBrandLocation());
            registerRepo.save(userprofileobj);
        }
        return userprofileobj;
    }


    @PostMapping("{userid}/updateProfile/password")
    public Register password(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findById(userid);
        Register userprofileobj = null;
        if (userProfile.isPresent()) {
            userprofileobj = userProfile.get();
            userprofileobj.setPassword(profile.getPassword());
            registerRepo.save(userprofileobj);
        }
        return userprofileobj;
    }


    @PostMapping("{userid}/updateProfile/BrandRecommendation")
    public Register brandRecommendation(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findById(userid);
        Register userprofileobj = null;
        if (userProfile.isPresent()) {
            userprofileobj = userProfile.get();
            userprofileobj.setBrandCategory(profile.getBrandCategory());
            userprofileobj.setBrandTargetGender(profile.getBrandTargetGender());
            userprofileobj.setCountry(profile.getCountry());
            userprofileobj.setCountryCode(profile.getCountryCode());
            userprofileobj.setBrandEstablishedIn(profile.getBrandEstablishedIn());
            userprofileobj.setBrandCompanyEmployeeSize(profile.getBrandCompanyEmployeeSize());
            userprofileobj.setBrandHashTags(profile.getBrandHashTags());
            userprofileobj.setPinCodes(profile.getPinCodes());
            userprofileobj.setBrandTargetAges(profile.getBrandTargetAges());
            registerRepo.save(userprofileobj);
        }
        return userprofileobj;
    }


    @PostMapping("{userid}/updateProfile/socialMediaLinks")
    public Register socialMediaLinks(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findById(userid);
        Register userprofileobj = null;
        if (userProfile.isPresent()) {
            userprofileobj = userProfile.get();
            userprofileobj.setYoutube(profile.getYoutube());
            userprofileobj.setFacebook(profile.getFacebook());
            userprofileobj.setInstagram(profile.getInstagram());
            userprofileobj.setTwitter(profile.getTwitter());
            userprofileobj.setPinterest(profile.getPinterest());
            registerRepo.save(userprofileobj);
        }
        return userprofileobj;
    }
    @GetMapping("{userid}/userdata")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Register> getfollowers(@PathVariable("userid") Long userid){
        return Collections.singletonList(registerRepo.findById(userid).orElse(null));
    }

}
