package com.paisa_square.paisa.controller;

import com.paisa_square.paisa.model.Advertise;
import com.paisa_square.paisa.model.Comments;
import com.paisa_square.paisa.model.Report;
import com.paisa_square.paisa.repository.Advertiserepository;
import com.paisa_square.paisa.repository.Registerrepository;
import com.paisa_square.paisa.repository.Reportrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@RestController
public class Reportcontrol {

    @Autowired
    private Reportrepository Reportrepo;
    @PostMapping("/reportadvertisement")
    @CrossOrigin(origins = "http://localhost:4200/")
    public Report report(@RequestBody Report report) throws Exception {
        Reportrepo.save(report);
        System.out.println("report saved");
        return report;
    }
}
