package com.example.prog4.controller;


import com.example.prog4.conf.company.CompanyConf;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PopulateController {


    @ModelAttribute("companyConf")
    private CompanyConf populateCompanyConf(){
        return new CompanyConf();
    }
}