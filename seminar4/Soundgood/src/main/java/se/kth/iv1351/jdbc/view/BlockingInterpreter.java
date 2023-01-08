package se.kth.iv1351.jdbc.view;

import se.kth.iv1351.jdbc.controller.Controller;
import se.kth.iv1351.jdbc.model.InstrumentDTO;

import java.util.*;

/**
 * Reads and interprets user commands. This command interpreter is blocking, the user
 * interface does not react to user input while a command is being executed.
 */
public class BlockingInterpreter {
    private static final String PROMPT = "> ";
    private final Scanner console = new Scanner(System.in);
    private Controller ctrl;
    private boolean keepReceivingCmds = false;

    /**
     * Creates a new instance that will use the specified controller for all operations.
     *
     * @param ctrl The controller used by this instance.
     */
    public BlockingInterpreter(Controller ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Stops the commend interpreter.
     */
    public void stop() {
        keepReceivingCmds = false;
    }

    /**
     * Interprets and performs user commands. This method will not return until the
     * UI has been stopped. The UI is stopped either when the user gives the
     * "quit" command, or when the method <code>stop()</code> is called.
     */
    public void handleCmds() {
        keepReceivingCmds = true;
        while (keepReceivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                switch (cmdLine.getCmd()) {
                    case HELP:
                        for (Command command : Command.values()) {
                            if (command == Command.ILLEGAL_COMMAND) {
                                continue;
                            }
                            System.out.println(command.toString().toLowerCase());
                        }
                        break;

                    case QUIT:
                        keepReceivingCmds = false;
                        break;

                    case LIST:
                        List<? extends InstrumentDTO> instruments;
                        if (cmdLine.getParameter(0).equals("")) {
                            instruments = ctrl.getAllInstruments();
                        } else {
                            instruments = ctrl.getInstrumentsByName(cmdLine.getParameter(0));
                        }
                        for (InstrumentDTO instrument : instruments) {
                            System.out.println(instrument);
                        }
                        break;

                    case RENT:
                        Integer instrument_id = Integer.parseInt(cmdLine.getParameter(0));
                        Integer student_id = Integer.parseInt(cmdLine.getParameter(1));
                        Integer no_of_month = Integer.parseInt(cmdLine.getParameter(2));
                        if(instrument_id > 0 && student_id > 0 && no_of_month > 0) {
                            ctrl.rentInstrument(instrument_id, student_id, no_of_month);
                            System.out.println("Rental is done successfully");
                           }
                          break;

                    case TERMINATE:
                        Integer instrum_id = Integer.parseInt(cmdLine.getParameter(0));
                        if(instrum_id > 0) {
                            ctrl.terminateRent(instrum_id);
                            System.out.println("terminated rental successfully");
                        }
                        break;

                    default:
                        System.out.println("illegal command");
                }
            } catch (Exception e) {
                System.out.println("Operation failed");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String readNextLine() {
        System.out.print(PROMPT);
        return console.nextLine();
    }
}
