package com.mycompany.carmotor.model.patterns.testdrive;

public interface TestDriveSubject {
    void subscribe(TestDriveObserver observer);
    void unsubscribe(TestDriveObserver observer);
    void notifyObservers();
}