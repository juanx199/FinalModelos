package com.mycompany.carmotor.model.patterns.creational;

import java.util.Arrays;
import java.util.List;

import com.mycompany.carmotor.model.domain.AxaColpatriaInsurer;
import com.mycompany.carmotor.model.domain.FalabellaInsurer;
import com.mycompany.carmotor.model.domain.IInsurer;
import com.mycompany.carmotor.model.domain.LibertyInsurer;
import com.mycompany.carmotor.model.domain.MapfreInsurer;
import com.mycompany.carmotor.model.domain.SuraInsurer;

public class InsuranceFactory {

    public static IInsurer getInsurer(String type) {
        switch (type.toUpperCase()) {
            case "FALABELLA", "BASIC" -> { return new FalabellaInsurer(); }
            case "SURA", "PREMIUM"    -> { return new SuraInsurer(); }
            case "MAPFRE"             -> { return new MapfreInsurer(); }
            case "LIBERTY"            -> { return new LibertyInsurer(); }
            case "AXA", "AXA COLPATRIA" -> { return new AxaColpatriaInsurer(); }
            default -> throw new IllegalArgumentException("Unknown insurer type: " + type);
        }
    }

    public static List<String> listAvailableInsurers() {
        return Arrays.asList("SURA", "MAPFRE", "LIBERTY", "AXA COLPATRIA", "FALABELLA");
    }
}