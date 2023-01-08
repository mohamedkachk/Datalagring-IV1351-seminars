package se.kth.iv1351.jdbc.startup;

import se.kth.iv1351.jdbc.integration.*;
import se.kth.iv1351.jdbc.controller.*;
import se.kth.iv1351.jdbc.view.*;

/**
 * Starts the SoundGood client.
 */
public class Main {
    /**
     * @param args There are no command line arguments.
     */
    public static void main(String[] args) {
        try {
        new BlockingInterpreter(new Controller()).handleCmds();
        } catch (InstrumentDBException e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
}