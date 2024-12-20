package com.paisa_square.paisa.service;

import com.paisa_square.paisa.model.*;
import com.paisa_square.paisa.repository.Profileratingrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import java.util.Random;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import static java.lang.Long.sum;

@Service
public class Registerservice {
    @Autowired
    private Registerrepository registerRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${paiSquareOfcEmail}")
    private String paiSquareOfcEmail;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private Profileratingrepository profileratingrepo;
    private final Random random = new Random();
    public Register saveUserInRegister(Register user ){
        return registerRepository.save(user);
    }
    public String saveUser(User user) throws MessagingException {
        System.out.println("in register service");
        String otp=generateOtp();
        user.setEmailOTP(otp);
        String sendOtpEmailResponse=sendOtpEmail(user.getEmail(),otp,user.getUsername());
        String emailResponse="none";
        System.out.println("user.getEmail()->"+user.getEmail());
        System.out.println("sendOtpEmailResponse "+sendOtpEmailResponse);
        if(Objects.equals(sendOtpEmailResponse, "emailSent")){
            userRepo.save(user);
            emailResponse="emailSent";
        } else if (Objects.equals(sendOtpEmailResponse, "invalidEmailAddress")) {
            emailResponse="invalidEmailAddress";
        }
        return emailResponse;
    }
    private String generateOtp() {
        System.out.println("generate OTP");
        return String.valueOf(100000 + random.nextInt(900000)); // 6-digit OTP
    }


    private String sendOtpEmail(String to, String otp,String username) {
        System.out.println("In sendotpemail page ->");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String emailResponse="";
        try {
            helper.setTo(to);
            helper.setSubject("Your Pai Square Account OTP - Action Required");
            helper.setText(
                    "Dear "+username+",\n\n" +
                            "Thank you for choosing Pai Square!\n\n" +
                            "To complete your account creation, please use the One-Time Password (OTP) provided below:\n\n" +
                            "Your OTP: "+otp+"\n\n" +
                            "This OTP is valid for the next 10 minutes. Please do not share it with anyone for security reasons.\n\n" +
                            "If you did not initiate this request, please contact our support team immediately.\n\n" +
                            "Thank you for joining Pai Square. We look forward to serving you!\n\n" +
                            "Best regards,\n" +
                            "Pai Square Team\n" +
                            paiSquareOfcEmail+"\n" +
                            "www.paisquare.in"
            );
            mailSender.send(message);
            emailResponse="emailSent";
            System.out.println("Email sent " + to);
        } catch (MailException | MessagingException e) {
            // Log the exception for further investigation
            System.err.println("Failed to send email to " + to);
            System.err.println("Error message: " + e.getMessage());
            // You may want to notify the user about the issue
            emailResponse="invalidEmailAddress";
        }
        return emailResponse;
    }
    public boolean verifyOtp(String email, String otp) {
        System.out.println("verify otp service");
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(email));
        System.out.println(userOptional);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("user.getEmailOTP() "+user.getEmailOTP());
            System.out.println("otp "+otp);
            if(Objects.equals(user.getEmailOTP(), otp)){
                user.setEmailOTP("Verified");
                userRepo.save(user);
                accountCreationEmail(user.getEmail(),user.getUsername());
                System.out.println("valid OTP");
                return true;
            }
            else{
                System.out.println("Invalid OTP");
                return false;
            }
        } else{
            System.out.println("email not present");
        }
        return false;
    }
    private void accountCreationEmail(String to,String username) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(to);
            helper.setSubject("Your Pai Square Account OTP - Action Required");
            helper.setText(
                "Dear "+username+",\n\n" +
                "Congratulations! Your Pai Square account has been successfully created.\n\n" +
                "You can now log in using your email and password to explore our platform and enjoy all the features we offer.\n\n" +
                "If you ever need assistance, feel free to reach out to our support team.\n\n" +
                "Thank you for choosing Pai Square. We’re excited to have you with us!\n\n" +
                "Best regards,\n" +
                "Pai Square Team\n" +
                paiSquareOfcEmail+"\n" +
                "www.paisquare.in"
            );
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            System.err.println("Failed to send email to " + to);
            System.err.println("Error message: " + e.getMessage());
        }
    }
    public Register fetchUserByEmailId(String email){
        return registerRepository.findByEmail(email);
    }
    public String fetchUserByEmailIdAndPassword(String email,String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println(hashedPassword);
        Optional<User> checkingEmailIdOptional = Optional.ofNullable(userRepo.findByEmail(email));

        if(checkingEmailIdOptional.isPresent()){
            boolean passwordMatching=passwordEncoder.matches(password, checkingEmailIdOptional.get().getPassword());
            System.out.println("passwordMatching  "+passwordMatching);
            if (passwordMatching) {
                User user = checkingEmailIdOptional.get();
                if(Objects.equals(user.getEmailOTP(), "Verified")){
                    return "validUser";
                } else {
                    return "OTPNotVerified";
                }
            } else{
                return "inValidCredentials";
            }
        } else {
            return "emailIdNotFound";
        }

    }

    public void saveProfileImage(Long id, MultipartFile image) throws IOException {
        // Fetch the Register object by id
        Register register = registerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the image bytes from MultipartFile
        byte[] imageBytes = image.getBytes();

        // Save the image bytes to the Register entity
        register.setProfileImage(imageBytes);

        // Save the Register entity with the new image
        registerRepository.save(register);
    }


    public byte[] getProfileImage(Long id) {
        Register register = registerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return register.getProfileImage();
    }


    public Boolean saveRating(Profilerating rating, Long userid, Long advertiserid) throws Exception {
        System.out.println("In saveRating: " + rating);
        System.out.println("In saveRating: userId = " + userid);
        System.out.println("In saveRating: advertiserId = " + advertiserid);
        Optional<Register> registerModel = registerRepository.findByUserId(userid);
        Optional<Register> registerModelAdvertiser = registerRepository.findByUserId(advertiserid);
        Optional<Profilerating> ratingModel = profileratingrepo.findByUserIdAndAdvertiserId(userid, advertiserid);

        rating.setAdvertiser(registerModelAdvertiser.get());
        rating.setUser(registerModel.get());
        System.out.println("ratingobj"+rating);
        if (ratingModel.isPresent() && registerModel.isPresent()) {
            // Update existing rating
            Profilerating existingRating = ratingModel.get();
            existingRating.setRating(rating.getRating());

            profileratingrepo.save(existingRating);

            // Update rating in Register
            Register register = updateRatingInRegister(rating, registerModel.get());
            registerRepository.save(register);

        } else {
            // Save new rating
            profileratingrepo.save(rating);

            // If register model is present, update it
            registerModel.ifPresent(register -> {
                Register updatedRegister = updateRatingInRegister(rating, register);
                registerRepository.save(updatedRegister);
            });
        }

        return true;
    }

    private Register updateRatingInRegister(Profilerating rating, Register register) {
        System.out.println("Updating rating for register: " + rating);

        BigDecimal userExistingRating = register.getRating();
        int noOfUserRating = (int) register.getNoOfRating();

        // Calculate new rating
        BigDecimal updatedRating;
        if (noOfUserRating > 0) {
            BigDecimal newRating = rating.getRating();
            updatedRating = userExistingRating
                    .multiply(BigDecimal.valueOf(noOfUserRating))
                    .add(newRating)
                    .divide(BigDecimal.valueOf(noOfUserRating + 1), RoundingMode.HALF_UP);
        } else {
            // If no ratings exist, directly set the new rating
            updatedRating = rating.getRating();
        }

        // Update register object
        register.setRating(updatedRating);
        register.setNoOfRating(noOfUserRating + 1);

        return register;
    }
}
