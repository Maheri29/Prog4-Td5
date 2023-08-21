package com.example.prog4.conf.company;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaxIdentityConf {
    private String nif;
    private String stat;
    private String rcs;

}
