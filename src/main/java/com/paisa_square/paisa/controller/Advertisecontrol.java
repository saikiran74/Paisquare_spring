package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Advertisementtransaction;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.repository.Advertisementtransactionrepository;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.service.Advertiseservice;
import com.paisa_square.paisa.service.GoogleSitemapPingService;
import com.paisa_square.paisa.service.Registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.math.BigDecimal.valueOf;

@RestController
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class Advertisecontrol {
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Registerrepository registerRepo;
    @Autowired
    private Advertiseservice service;
    @Autowired
    private Advertisementtransactionrepository adtransRepo;

    @Autowired
    private GoogleSitemapPingService googleSitemapPingService;
    @Autowired
    private SitemapController sitemapController;
    @Autowired
    private Advertiserepository adrepo;
    @GetMapping("/advertisements")
    public List<Advertise> getAllAdvertisements() {
        return service.findAlladvertisement();
    }
    @GetMapping("/idadvertisements/{advertisementid}")
    public List<Advertise> getIDAdvertisements(@PathVariable("advertisementid") Integer advertisementid) {
        return adrepo.findByadvertisementId(advertisementid);
    }
    @GetMapping("/singleadvertisement/{advertisementid}")
    public Advertise singleadvertisement(@PathVariable("advertisementid") Integer advertisementid) {
        return adrepo.singleadvertisement(advertisementid);
    }
    @GetMapping("/useradvertisements/{userid}")
    public List<Advertise> getUserAdvertisements(@PathVariable("userid") Integer userid) {
        return service.findAllByadvertiserId(userid);
    }

    @PostMapping("/advertise/{userid}")
    public Advertise advertise(@RequestBody Advertise ad,@PathVariable("userid") Long userid) throws Exception {
        Optional<Register> registermodel = registerRepo.findByUserId(userid);
        Advertise trans=null;
        if (registermodel.isPresent()) {
            Register register = registermodel.get();
            ad.setAdvertiser(register);
            if(ad.getPaisa()==null){
                ad.setPaisa(BigDecimal.ZERO);
            }
            if(ad.getPai()==null){
                ad.setPai(BigDecimal.ZERO);
            }
            if(ad.getPaiperclick()==null){
                ad.setPaiperclick(BigDecimal.ZERO);
            }
            if(ad.getPaisaperclick()==null){
                ad.setPaisaperclick(BigDecimal.ZERO);
            }
            if(register.getPai().compareTo(ad.getPai())>=0 || register.getPaisa().compareTo(ad.getPaisa())>=0){
                    register.setPai(register.getPai().subtract(ad.getPai()));
                    register.setPaisa(register.getPaisa().subtract(ad.getPaisa()));
                    register.setNoOfAdvertisements(register.getNoOfAdvertisements()+1);
                    ad.setAvailablepai(ad.getPai());
                    ad.setAvailablepaisa(ad.getPaisa());
                    ad.setHashtags(ad.getHashtags());
                    ad.setPincodes(ad.getPincodes());
                    ad.setUserid(Math.toIntExact(userid));
                    registerRepo.save(register);
                    trans=service.savead(ad);
                    Advertisementtransaction transaction=new Advertisementtransaction();
                    transaction.setAdvertisement(trans);
                    transaction.setAdvertiser(trans.getAdvertiser());
                    transaction.setAdvertisementpai(trans.getPai());
                    transaction.setAdvertisementpaisa(trans.getPaisa());
                    transaction.setAvailablepaisa(register.getPaisa());
                    transaction.setAvailablepai(register.getPai());
                    adtransRepo.save(transaction);
                }
            else
                throw new Exception("emailid is exist");
        }
        else {
            throw new IllegalArgumentException("userId not found with id: " + userid);
        }
        sitemapController.getSitemap();
        return ad;
    }

    @GetMapping("/commentslist/{advertisementid}")
    public List<Comments> getAllCommentList(@PathVariable("advertisementid") Integer advertisementid) {
        return service.findByadvertisementid(advertisementid);
    }
    @GetMapping("/search")
    public List<Advertise> searchAds(@RequestParam("query") String query) {
        return service.searchAds(query);
    }

    @GetMapping("/getHashTags")
    public List<String> getHashTags() {
        List<String> allHashtags = adrepo.findTopHashtags();
        Map<String, Integer> hashtagCount = new HashMap<>();

        // Split each hashtag string by commas and count occurrences
        for (String hashtags : allHashtags) {
            if (hashtags == null || hashtags.trim().isEmpty()) {
                continue;
            }
            String[] hashtagArray = hashtags.split(",");  // Assuming comma as separator
            for (String hashtag : hashtagArray) {
                hashtag = hashtag.trim();  // Remove any extra spaces
                hashtagCount.put(hashtag, hashtagCount.getOrDefault(hashtag, 0) + 1);
            }
        }
        List<String> result=hashtagCount.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        // Sort hashtags by count in descending order
        return result;
    }

    @GetMapping("/getHashTagsAdvertisement/{query}")
    public List<Advertise> getHashTagsAdvertisement(@PathVariable("query") String query) {

        return service.getHashTagsAdvertisement(query);
    }

    @GetMapping("/getpincodesadvertisement/{query}")
    public List<Advertise> getPinCodesAdvertisement(@PathVariable("query") String query) {
        if (Objects.equals(query, "all")) {
            return service.findAlladvertisement();
        }
        return service.getPinCodesAdvertisement(query);
    }
}
