package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Contactus;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.model.Visits;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.VisitorRepository;
import com.paisa_square.paisa.serice.Contactusservice;
import com.paisa_square.paisa.serice.Registerservice;
import com.paisa_square.paisa.serice.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
public class Visitorcontrol {
    @Autowired
    private VisitorService service;
    @Autowired
    private VisitorRepository visitorrepo;
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Advertiserepository adrepo;
    @PostMapping("{userid}/{advertisementid}/visit")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Visits visit(@RequestBody Visits visit, @PathVariable("userid") Long userid, @PathVariable("advertisementid") Long advertisementid) throws Exception {

        Optional<Advertise> advertismentmodel = adrepo.findById(advertisementid);
        Visits savevisitobj = null;
        if (advertismentmodel.isPresent()) {
            Advertise advertise = advertismentmodel.get();
            Optional<Register> registermodel = registerservice.fetchId(advertise.getAdvertiser().getId());
            Register register = registermodel.get();
            visit.setAdvertisement(advertise);
            visit.setAdvertiser(register);
            if(advertise.getVisiteduser().contains(userid)){
                System.out.println("user exist in the visiteduser");
            }
            else{
                System.out.println("user not  exist saving the visiteduser");
                advertise.getVisiteduser().add(userid);
                adrepo.save(advertise);
            }
            System.out.println("user not  exist saving the visit table"+advertisementid);
            savevisitobj = service.savevisitor(visit);
            System.out.println(savevisitobj);
            if (visit == null) {
                throw new Exception("Bad contactus details");
            }

        }
        System.out.println("advertisment id not exits"+advertisementid);
        return savevisitobj;
    }

    @GetMapping("{userid}/visitorgraph")
    @CrossOrigin(origins = "http://localhost:4200/")
    public List<Object[]> visitorgraph(@PathVariable("userid") Long userid) throws Exception {
        System.out.println(visitorrepo.visitorsgraph(userid));
        return visitorrepo.visitorsgraph(userid);
    }

}
