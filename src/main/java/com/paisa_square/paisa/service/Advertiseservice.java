package com.paisa_square.paisa.service;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Commentrepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Advertiseservice {
    @Autowired
    private Advertiserepository adrepo;
    @Autowired
    private Commentrepository commentrepo;
    @Autowired
    private Registerrepository registerrepo;

    public Optional<Advertise> fetchId(Long id){
        return adrepo.findById(id);
    }
    public Advertise savead(Advertise ad){
        return adrepo.save(ad);
    }
    public List<Advertise> findAlladvertisement(){
        return adrepo.findAll();
    }
    public List<Comments> findByadvertisementid(Integer advertisementid){
        return commentrepo.findByadvertiseId(advertisementid);
    }
    public List<Advertise> findAllByadvertiserId(Integer userid){
        Long registerId=registerrepo.findByUserId(Long.valueOf(userid)).get().getId();
        return adrepo.findByadvertiserId(Math.toIntExact(registerId));
    }

    public List<Advertise> searchAds(String query) {
        return adrepo.findByBrandnameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }

    public List<Advertise> getHashTagsAdvertisement(String hashtag) {
        return adrepo.getHashTagsAdvertisement(hashtag);
    }
    public List<Advertise> getPinCodesAdvertisement(String pincodes) {
        return adrepo.getPinCodesAdvertisement(pincodes);
    }
}
