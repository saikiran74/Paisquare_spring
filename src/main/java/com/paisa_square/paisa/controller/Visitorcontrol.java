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
@CrossOrigin(origins = "${cors.allowedOrigins}")
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
    public Visits visit(@RequestBody Visits visit, @PathVariable("userid") Long userid, @PathVariable("advertisementid") Long advertisementid) throws Exception {

        Optional<Advertise> advertismentmodel = adrepo.findById(advertisementid);
        List<Visits> existingVisit = visitorrepo.findByUseridAndAdvertisement_Id(String.valueOf(userid), advertisementid);
        Visits savevisitobj = null;
        if (advertismentmodel.isPresent()) {
            Advertise advertise = advertismentmodel.get();
            Optional<Register> registermodel = registerRepo.findById(advertise.getAdvertiser().getId());
            Optional<Register> registermodelOfUser = registerRepo.findByUserId(userid);
            Register register = registermodel.get();
            Register registerOfUser = registermodelOfUser.get();
            //Saving data into register table
            register.setNoOfVisit(register.getNoOfVisit()+1);

            //Saving data into visitor table
            visit.setAdvertisement(advertise);
            visit.setAdvertiser(register);
            //Saving data into advertise table visited count increment
            advertise.setVisitscount(advertise.getVisitscount()+1);
            // Saving data into advertise_visiteduser table
            if(existingVisit.isEmpty()){
                if(advertise.getPaiperclick().compareTo(BigDecimal.ZERO)>=0 && (advertise.getAvailablepai().subtract(advertise.getPaiperclick())).compareTo(BigDecimal.ZERO)>=0){
                    advertise.setAvailablepai(advertise.getAvailablepai().subtract(advertise.getPaiperclick()));
                    registerOfUser.setPai(registerOfUser.getPai().add(advertise.getPaiperclick()));
                }
                if(advertise.getPaisaperclick().compareTo(BigDecimal.ZERO)>=0 && (advertise.getAvailablepaisa().subtract(advertise.getPaisaperclick())).compareTo(BigDecimal.ZERO)>=0){
                    advertise.setAvailablepaisa(advertise.getAvailablepaisa().subtract(advertise.getPaisaperclick()));
                    registerOfUser.setPaisa(registerOfUser.getPaisa().add(advertise.getPaisaperclick()));
                }
            }
            advertise.getVisiteduser().add(userid);
            adrepo.save(advertise);
            registerRepo.save(register);
            registerRepo.save(registerOfUser);
            savevisitobj = visitorService.savevisitor(visit);
            if (visit == null) {
                throw new Exception("Bad contactus details");
            }
        }
        return savevisitobj;
    }

    @GetMapping("/visitorgraph/{userid}/{period}")
    public List<Object[]> visitorgraph(@PathVariable("userid") Long userid,@PathVariable("period") String period) throws Exception {
        Long registerUserId=registerRepo.findByUserId(userid).get().getId();
        if(Objects.equals(period, "weekly")){
            return visitorrepo.weeklygraph(registerUserId);
        } else if (Objects.equals(period, "lastmonth")) {
            return visitorrepo.lastmonth(registerUserId);
        } else if (Objects.equals(period, "Today")) {
            return visitorrepo.today(registerUserId);
        } else if (Objects.equals(period, "thismonth")) {
            return visitorrepo.thismonth(registerUserId);
        }else{
            return visitorrepo.yearlygraph(registerUserId);
        }
    }
    @GetMapping("/getvisitedadvertisementslist/{userid}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Advertise> getvisitedadvertisementslist(@PathVariable("userid") Integer userid) {
        return adrepo.findAllByvisiteduser(userid);
    }
}
