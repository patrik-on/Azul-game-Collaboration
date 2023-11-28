package sk.uniba.fmph.dcs;

public class ConsoleGameObserver implements ObserverInterface{

    @Override
    public void notify(String newState) {
        System.out.println(newState);
    }
}
