package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.ApiMessage;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.serice.Registerservice;
import com.paisa_square.paisa.model.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class Registationcontrol {
    @Autowired
    private Registerservice registerService;
    @Autowired
    private Registerrepository registerRepo;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @PostMapping("/registeruser")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<ApiMessage> registerUser(@RequestBody Register user) throws Exception {
        System.out.println("in register control");
        Register createAccount=null;
        String tempEmailId = user.getEmail();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        System.out.println("hashedPassword "+hashedPassword);
        System.out.println(tempEmailId);
        if(tempEmailId!=null && !tempEmailId.isEmpty()){
            System.out.println("going to register serve1");
            Register existingUser=registerService.fetchUserByEmailId(tempEmailId);
            if(existingUser!=null){
                if(Objects.equals(existingUser.getEmailOTP(), "Verified")){
                    System.out.println("email id is exist");
                    return ResponseEntity.ok(new ApiMessage("error","emailExists", "Email ID already exists"));
                } else{
                    existingUser.setAccountType(user.getAccountType());
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPincode(user.getPincode());
                    existingUser.setPassword(user.getPassword());
                    String savingExistingUserStatus=registerService.saveUser(existingUser);
                    return statusMessageLogMethod(savingExistingUserStatus);
                }
            } else{
                System.out.println("going to register serve");
                String statusMessageLog=registerService.saveUser(user);
                return statusMessageLogMethod(statusMessageLog);
            }
        }
        System.out.println("Issue while creating account!");
        return ResponseEntity.ok(new ApiMessage("error","issueInCreating", "Issue while creating account"));
    }
    public ResponseEntity<ApiMessage> statusMessageLogMethod(String savingUserStatus) {
        if(Objects.equals(savingUserStatus, "emailSent")){
            return ResponseEntity.ok(new ApiMessage("success", "OTPSent", "User registered successfully! please enter OTP"));
        } else if (Objects.equals(savingUserStatus, "invalidEmailAddress")) {
            return ResponseEntity.ok(new ApiMessage("error", "emailAddressNotFound", "Email address not found!"));
        } else {
            return ResponseEntity.ok(new ApiMessage("error", "IssueInSaving", "issue in saving user! please try later"));
        }
    }
    @PostMapping("/verifyOTP")
    public ResponseEntity<?> verifyOtp(@RequestBody Register request) {
        System.out.println("In verifyOTP page"+request.getEmail()+ request.getEmailOTP());
        boolean isVerified = registerService.verifyOtp(request.getEmail(), request.getEmailOTP());
        if (isVerified) {
            //return ResponseEntity.status(400).body(new ApiMessage("success", "OTP verified."));
            return ResponseEntity.ok(new ApiMessage("success","OTPVerified", "OTP verified."));
        } else {
            return ResponseEntity.ok(new ApiMessage("error","invalidOTP", "Invalid OTP."));
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:4200/")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Register login) throws Exception {
        String tempEmailId=login.getEmail();
        String tempPassword=login.getPassword();
        Map<String, Object> response = new HashMap<>();
        String loginStatus="";
        if(tempEmailId!=null && tempPassword!=null){
            ApiMessage apiMessage;
            Register user = null;
            loginStatus=registerService.fetchUserByEmailIdAndPassword(tempEmailId,tempPassword);
            if (Objects.equals(loginStatus, "validUser")) {
                apiMessage = new ApiMessage("success", "validUser", "Login success.");
                user = registerService.fetchUserByEmailId(tempEmailId);
            } else if (Objects.equals(loginStatus, "OTPNotVerified")) {
                apiMessage = new ApiMessage("error", "OTPNotVerified", "OTP Not verified.");
            } else if (Objects.equals(loginStatus, "inValidCredentials")) {
                apiMessage = new ApiMessage("error", "inValidCredentials", "Please enter valid credentials.");
            } else if (Objects.equals(loginStatus, "emailIdNotFound")) {
                apiMessage = new ApiMessage("error", "emailIdNotFound", "Email not found.");
            } else {
                apiMessage = new ApiMessage("error", "unKnown", "Please check email and password.");
            }

            response.put("apiMessage", apiMessage);
            response.put("user", user);

            return ResponseEntity.ok(response);
        } else{
            ApiMessage apiMessage = new ApiMessage("error", "unKnown", "Please check email and password.");
            response.put("apiMessage", apiMessage);
            response.put("user", null);
            return ResponseEntity.ok(response);
        }
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
