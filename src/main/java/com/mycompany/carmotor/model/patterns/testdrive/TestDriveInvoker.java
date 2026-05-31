package com.mycompany.carmotor.model.patterns.testdrive;

import java.util.ArrayList;
import java.util.List;

public class TestDriveInvoker {

    private final List<TestDriveCommand> history;
    private TestDriveCommand current;

    public TestDriveInvoker() {
        this.history = new ArrayList<>();
    }

    public void setCommand(TestDriveCommand command) {
        this.current = command;
    }

    public void executeCommand() {
        if (current != null) {
            current.execute();
            history.add(current);
            System.out.println("[Invoker] Comando ejecutado. Historial: " + history.size());
        } else {
            System.out.println("[Invoker] No hay comando configurado.");
        }
    }

    public void undoLast() {
        if (!history.isEmpty()) {
            TestDriveCommand last = history.remove(history.size() - 1);
            System.out.println("[Invoker] Deshaciendo: " + last.getClass().getSimpleName());
            last.undo();
        } else {
            System.out.println("[Invoker] No hay comandos para deshacer.");
        }
    }

    public int getHistorySize()              { return history.size(); }
    public List<TestDriveCommand> getHistory(){ return new ArrayList<>(history); }
}