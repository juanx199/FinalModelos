package com.mycompany.carmotor.model.patterns.creational;

import com.mycompany.carmotor.model.patterns.structural.BranchComposite;

public class BranchSingleton {

    private static BranchSingleton instance;

    private BranchComposite rootBranch;
    private final String companyName;

    private BranchSingleton(String companyName) {
        this.companyName = companyName;
        this.initSystem();
    }

    public static BranchSingleton getInstance(String companyName) {
        if (instance == null) {
            instance = new BranchSingleton(companyName);
            System.out.println("[Singleton] -> Unique instance of BranchSingleton created.");
        }
        return instance;
    }

    public BranchComposite getRootBranch() {
        return rootBranch;
    }

    public final void initSystem() {
        System.out.println("[Singleton] -> Initializing branch system for: " + companyName);
        this.rootBranch = new BranchComposite("Main Central Branch");
        System.out.println("[Singleton] -> System initialized successfully.");
    }
}