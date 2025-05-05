package com.paisa_square.paisa.controller;
import com.paisa_square.paisa.model.*;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.repository.RoleRepository;
import com.paisa_square.paisa.repository.UserRepository;
import com.paisa_square.paisa.service.Registerservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.*;

import com.paisa_square.paisa.config.JwtUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "${cors.allowedOrigins}")
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
    private static final Logger logger = LoggerFactory.getLogger(Registationcontrol.class);

    @GetMapping("/")
    @CrossOrigin(origins = "${cors.allowedOrigins}")
    public String home() {
        return "Welcome to PaiSquare API!";
    }
    @PostMapping("/registeruser")
    @CrossOrigin(origins = "${cors.allowedOrigins}")
    public ResponseEntity<ApiMessage> registerUser(@RequestBody User user) throws Exception {

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
                    registerUser.setPai(new BigDecimal(1000));
                    registerUser.setPaisa(new BigDecimal(0));
                    registerService.saveUserInRegister(registerUser);
                }
                return statusMessageLogMethod(saveUserInUser);
            }
        }
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
    @CrossOrigin(origins = "${cors.allowedOrigins}")
    public ResponseEntity<?> verifyOtp(@RequestBody User request) {
        boolean isVerified = registerService.verifyOtp(request.getEmail(), request.getEmailOTP());
        if (isVerified) {
            //return ResponseEntity.status(400).body(new ApiMessage("success", "OTP verified."));
            return ResponseEntity.ok(new ApiMessage("success","OTPVerified", "OTP verified."));
        } else {
            return ResponseEntity.ok(new ApiMessage("error","invalidOTP", "Invalid OTP."));
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "${cors.allowedOrigins}")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User login) throws Exception {
        String tempEmailId=login.getEmail();
        String tempPassword=login.getPassword();
        Map<String, Object> response = new HashMap<>();
        String loginStatus="";
        if(tempEmailId!=null && tempPassword!=null){
            ApiMessage apiMessage;
            User user = null;
            loginStatus=registerService.fetchUserByEmailIdAndPassword(tempEmailId,tempPassword);
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
            String hashedPassword = passwordEncoder.encode(profile.getPassword());
            userprofileobj.setPassword(hashedPassword);
            userRepo.save(userprofileobj);
        }
        return profile;
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
    /**@PostMapping("updateProfile/accounttype/{userid}")
    public Register brandAccountType(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> userProfile = registerRepo.findByUserId(userid);
        Register userprofileobj = null;
        if (userProfile.isPresent()) {
            userprofileobj = userProfile.get();
            userprofileobj.setAccountType(profile.getAccountType());
            registerRepo.save(userprofileobj);
        }
        return userprofileobj;
    } **/
    @PostMapping("updateProfile/accounttype/{userid}")
    public ResponseEntity brandAccountType(@RequestBody Register profile, @PathVariable("userid") Long userid) throws Exception {
        Optional<Register> registerProfile = registerRepo.findByUserId(userid);
        Optional<User> userProfile = userRepo.findById(userid);
        Register registerProfileObj = null;
        User userProfileObj = null;

        Map<String, Object> response = new HashMap<>();
        if (registerProfile.isPresent() && userProfile.isPresent()) {
            registerProfileObj = registerProfile.get();
            userProfileObj = userProfile.get();
            registerProfileObj.setAccountType(profile.getAccountType());
            userProfileObj.setAccountType(profile.getAccountType());
            registerRepo.save(registerProfileObj);
            userRepo.save(userProfileObj);

            String token = jwtUtil.generateToken(userProfileObj.getEmail());
            response.put("token",token);

            System.out.println("Account type updated to: " + registerProfileObj.getAccountType()); // ✅ Add this line
        }
        response.put("user", registerProfile);
        return ResponseEntity.ok(response);
        // ✅ Return updated profile
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
    public Optional<Register> findUserProfile(@PathVariable("userid") Long userId){
        return registerRepo.findByUserId(userId);
    }
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    @PostMapping("updateProfile/upload-image/{id}")
    public ResponseEntity<String> uploadProfileImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        try {
            if (image.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No image file provided.");
            }

            // Check file size (optional)
            if (image.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("File size exceeds the limit.");
            }

            registerService.saveProfileImage(id, image);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("updateProfile/profile-image/{id}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id) {
        byte[] image = registerService.getProfileImage(id);

        if (image != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) // or IMAGE_JPEG based on the image type
                    .body(image);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/rating/{userid}/{advertiserid}")
    public Optional<Register> rating(@RequestBody Profilerating rating, @PathVariable("userid") Long userid, @PathVariable("advertiserid") Long advertiserid) throws Exception{
        registerService.saveRating(rating,userid,advertiserid);
        return registerRepo.findByUserId(userid);
    }
}
