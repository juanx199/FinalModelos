package com.mycompany.carmotor.model.patterns.testdrive;

public interface TestDriveCommand {
    void execute();
    void undo();
}