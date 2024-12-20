package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Register;
import com.paisa_square.paisa.model.Visits;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.repository.VisitorRepository;
import com.paisa_square.paisa.service.Registerservice;
import com.paisa_square.paisa.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@RestController
public class Visitorcontrol {
    @Autowired
    private VisitorService visitorService;
    @Autowired
    private VisitorRepository visitorrepo;
    @Autowired
    private Registerrepository registerRepo;
    @Autowired
    private Registerservice registerservice;
    @Autowired
    private Advertiserepository adrepo;
    @PostMapping("/visit/{userid}/{advertisementid}")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Visits visit(@RequestBody Visits visit, @PathVariable("userid") Long userid, @PathVariable("advertisementid") Long advertisementid) throws Exception {

        Optional<Advertise> advertismentmodel = adrepo.findById(advertisementid);
        Visits savevisitobj = null;
        if (advertismentmodel.isPresent()) {
            Advertise advertise = advertismentmodel.get();
            Optional<Register> registermodel = registerRepo.findByUserId(advertise.getAdvertiser().getId());
            Register register = registermodel.get();
            //Saving data into register table
            register.setNoOfVisit(register.getNoOfVisit()+1);
            registerRepo.save(register);
            //Saving data into visitor table
            visit.setAdvertisement(advertise);
            visit.setAdvertiser(register);
            //Saving data into advertise table visited count increment
            advertise.setVisitscount(advertise.getVisitscount()+1);
            // Saving data into advertise_visiteduser table

            if(advertise.getVisiteduser().contains(userid)){
                System.out.println("user exist in the visiteduser");
            }
            else{
                if(advertise.getPaiperclick().compareTo(BigDecimal.ZERO)>=0 && (advertise.getAvailablepai().subtract(advertise.getPaiperclick())).compareTo(BigDecimal.ZERO)>=0){
                    advertise.setAvailablepai(advertise.getAvailablepai().subtract(advertise.getPaiperclick()));
                }
                if(advertise.getPaisaperclick().compareTo(BigDecimal.ZERO)>=0 && (advertise.getAvailablepaisa().subtract(advertise.getPaisaperclick())).compareTo(BigDecimal.ZERO)>=0){
                    advertise.setAvailablepaisa(advertise.getAvailablepaisa().subtract(advertise.getPaisaperclick()));
                }
                System.out.println("user not  exist saving the visiteduser");
                advertise.getVisiteduser().add(userid);
                adrepo.save(advertise);
            }
            System.out.println("user not  exist saving the visit table"+advertisementid);
            savevisitobj = visitorService.savevisitor(visit);
            if (visit == null) {
                throw new Exception("Bad contactus details");
            }
        }
        System.out.println("advertisment id not exits"+advertisementid);
        return savevisitobj;
    }

    @GetMapping("/visitorgraph/{userid}/{period}")
    @CrossOrigin(origins = "http://localhost:4200/")
    public List<Object[]> visitorgraph(@PathVariable("userid") Long userid,@PathVariable("period") String period) throws Exception {
        if(Objects.equals(period, "weekly")){
            return visitorrepo.weeklygraph(userid);
        } else if (Objects.equals(period, "lastmonth")) {
            return visitorrepo.lastmonth(userid);
        } else if (Objects.equals(period, "thismonth")) {
            return visitorrepo.thismonth(userid);
        } else{
            return visitorrepo.yearlygraph(userid);
        }
    }
    @GetMapping("/getvisitedadvertisementslist/{userid}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getvisitedadvertisementslist(@PathVariable("userid") Integer userid) {
        return adrepo.findAllByvisiteduser(userid);
    }
}
