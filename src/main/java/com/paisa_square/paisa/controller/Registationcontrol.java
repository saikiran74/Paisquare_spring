package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.ApiMessage;
import com.paisa_square.paisa.model.User;
import com.paisa_square.paisa.model.Role;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.repository.RoleRepository;
import com.paisa_square.paisa.repository.UserRepository;
import com.paisa_square.paisa.serice.Registerservice;
import com.paisa_square.paisa.model.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.*;

import com.paisa_square.paisa.config.JwtUtil;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class Registationcontrol {
    @Autowired
    private Registerservice registerService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Registerrepository registerRepo;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepo;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @PostMapping("/registeruser")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<ApiMessage> registerUser(@RequestBody User user) throws Exception {
        System.out.println("in register control");
        String tempEmailId = user.getEmail();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName("ROLE_USER");
        roles.add(role);
        user.setRoles(roles);
        if(tempEmailId!=null && !tempEmailId.isEmpty()) {
            Register existingUserInRegister = registerService.fetchUserByEmailId(tempEmailId);
            User existingUser = userRepo.findByEmail(tempEmailId);
            if (existingUser != null) {
                if (Objects.equals(existingUser.getEmailOTP(), "Verified")) {
                    System.out.println("email id is exist");
                    return ResponseEntity.ok(new ApiMessage("error", "emailExists", "Email ID already exists"));
                } else {
                    existingUser.setAccountType(user.getAccountType());
                    existingUser.setUsername(user.getUsername());
                    existingUserInRegister.setUsername(user.getUsername());
                    existingUser.setPincode(user.getPincode());
                    existingUser.setPassword(user.getPassword());
                    String savingExistingUserStatus = registerService.saveUser(existingUser);
                    registerService.saveUserInRegister(existingUserInRegister);
                    return statusMessageLogMethod(savingExistingUserStatus);
                }
            } else {
                Register registerUser = new Register();
                String saveUserInUser = registerService.saveUser(user);
                User saveUser = userRepo.findByEmail(user.getEmail());
                if(saveUser!=null){
                    registerUser.setUserId(saveUser.getId());
                    registerUser.setUsername(user.getUsername());
                    registerUser.setEmail(user.getEmail());
                    registerUser.setPincode(user.getPincode());
                    registerUser.setAccountType(user.getAccountType());
                    registerUser.setPai(new BigDecimal(500));
                    registerUser.setPaisa(new BigDecimal(0));
                    registerService.saveUserInRegister(registerUser);
                }
                return statusMessageLogMethod(saveUserInUser);
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
    public ResponseEntity<?> verifyOtp(@RequestBody User request) {
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
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User login) throws Exception {
        String tempEmailId=login.getEmail();
        String tempPassword=login.getPassword();
        Map<String, Object> response = new HashMap<>();
        String loginStatus="";
        if(tempEmailId!=null && tempPassword!=null){
            ApiMessage apiMessage;
            User user = null;
            loginStatus=registerService.fetchUserByEmailIdAndPassword(tempEmailId,tempPassword);
            System.out.println("loginStatus -->"+ loginStatus);
            if (Objects.equals(loginStatus, "validUser")) {
                apiMessage = new ApiMessage("success", "validUser", "Login success.");
                user = userRepo.findByEmail(tempEmailId);
            } else if (Objects.equals(loginStatus, "OTPNotVerified")) {
                apiMessage = new ApiMessage("error", "OTPNotVerified", "OTP Not verified.");
            } else if (Objects.equals(loginStatus, "inValidCredentials")) {
                apiMessage = new ApiMessage("error", "inValidCredentials", "Please enter valid credentials.");
            } else if (Objects.equals(loginStatus, "emailIdNotFound")) {
                apiMessage = new ApiMessage("error", "emailIdNotFound", "Email not found.");
            } else {
                apiMessage = new ApiMessage("error", "unKnown", "Please check email and password.");
            }
            System.out.println("apiMessage -->"+apiMessage);
            if(user!=null){
                String token = jwtUtil.generateToken(user.getEmail());
                response.put("token",token);
            }
            response.put("apiMessage", apiMessage);
            response.put("user", user);
            return ResponseEntity.ok(response);
        } else{
            ApiMessage apiMessage = new ApiMessage("error", "unKnown", "Please check email and password.");
            response.put("apiMessage", apiMessage);
            response.put("user", null);
            response.put("token","");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("profile/{userid}")
    @CrossOrigin(origins = "http://localhost:4200")
    public Optional<Register> getAllAdvertisements(@PathVariable("userid") Long userid) {
        return registerRepo.findByUserId(userid);
    }
    @PostMapping("updateProfile/brandInformation/{userid}")
    public Register brandInformation(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findByUserId(userid);
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

    @PostMapping("updateProfile/personalInformation/{userid}")
    public Register personalInformation(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findByUserId(userid);
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


    @PostMapping("updateProfile/password/{userid}")
    public User password(@RequestBody User profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<User> userProfile = userRepo.findById(userid);
        User userprofileobj = null;
        if (userProfile.isPresent()) {
            userprofileobj = userProfile.get();
            userprofileobj.setPassword(profile.getPassword());
            userRepo.save(userprofileobj);
        }
        return userprofileobj;
    }


    @PostMapping("updateProfile/BrandRecommendation/{userid}")
    public Register brandRecommendation(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findByUserId(userid);
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


    @PostMapping("updateProfile/socialMediaLinks/{userid}")
    public Register socialMediaLinks(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findByUserId(userid);
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
    @GetMapping("userdata/{userid}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Optional<Register>> getfollowers(@PathVariable("userid") Long userid){
        return Collections.singletonList(registerRepo.findByUserId(userid));
    }

}
