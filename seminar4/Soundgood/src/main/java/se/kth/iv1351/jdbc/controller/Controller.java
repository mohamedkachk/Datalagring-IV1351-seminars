package se.kth.iv1351.jdbc.controller;

import se.kth.iv1351.jdbc.integration.*;
import se.kth.iv1351.jdbc.model.*;
import java.util.*;

public class Controller {
    private final InstrumentDAO instrumentDb;

    /**
     * Creates a new instance, and retrieves a connection to the database.
     *
     * @throws InstrumentDBException If unable to connect to the database.
     */
    public Controller() throws InstrumentDBException {
        instrumentDb = new InstrumentDAO();
    }

    /**
     * Lists all available instruments in the whole database.
     *
     * @return A list containing all available instruments for rent.
     * The list is empty if there are no available instruments.
     *
     * @throws InstrumentException If unable to retrieve instruments.
     */
    public List<? extends InstrumentDTO> getAllInstruments() throws InstrumentException {
        try {
            return instrumentDb.findAllInstruments();
        } catch (Exception e) {
            throw new InstrumentException("Unable to list instruments.", e);
        }
    }

    /**
     * Lists all available instruments of specified name.
     *
     * @param name The name of instrument that shall be listed.
     * @return A list with all instruments of specified name. The list is
     *         empty if there are no available instruments of specified name,
     *         or if there is no such instrument.
     * @throws InstrumentException If unable to retrieve the instruments.
     */
    public List<? extends InstrumentDTO> getInstrumentsByName(String name)
            throws InstrumentException {
        if (name == null) {
            return new ArrayList<>();
        }
        try {
            return instrumentDb.findInstrumentsByName(name);
        } catch (Exception e) {
            throw new InstrumentException("Could not search for instrument.", e);
        }
    }

    /**
     * rents the instrument that has a specified id for the student
     * with specified id for a specified number of month.
     *
     * @param instr_id The id of the instrument which will be rented.
     * @param student_id The id of the student who will rent the instrument.
     * @param no_month The number of month for rental.
     *
     * @throws RejectedException If not allowed to rent the specified instrument.
     * @throws InstrumentException If failed to search for instrument's id.
     * @throws Exception If failed to search for student's id.
     */
    public void rentInstrument(Integer instr_id, Integer student_id, Integer no_month) throws Exception {
        String failureMsg = "Could not find instrument: " + instr_id;

        Integer std_id = instrumentDb.findStudentPKById(student_id);
        if (student_id == null || std_id != student_id) {
            throw new Exception("Could not find student's id: " + student_id);
        }

        Instrument instrmnt = instrumentDb.findInstrumentByID(instr_id);
        if (instrmnt == null || instr_id != instrmnt.getInstrumentId()) {
            throw new InstrumentException(failureMsg);
        }
        Integer NumberOfRent = instrumentDb.findNumberOfRentalsByStudentID(student_id);
        if (2 <= NumberOfRent) {
            throw new RejectedException("Student have already rented: "+NumberOfRent+" instruments");
        }
        try {
             instrumentDb.createRental(instrmnt, student_id, no_month);
        } catch (InstrumentDBException e) {
            throw new InstrumentException(failureMsg, e);
        } catch (Exception e) {
            commitOngoingTransaction(failureMsg);
            throw e;
        }
    }


    private void commitOngoingTransaction(String failureMsg) throws InstrumentException {
        try {
            instrumentDb.commit();
        } catch (InstrumentDBException e) {
            throw new InstrumentException(failureMsg, e);
        }
    }

    /**
     * Terminates an ongoing rental with the specified instrument's id.
     *
     * @param instrumentId The id of the instrument.
     *
     * @throws InstrumentException If failed to Terminate the rental of the specified id.
     * @throws InstrumentDBException If failed to find the specified instrument's id.
     */
    public void terminateRent(Integer instrumentId)
            throws InstrumentException, InstrumentDBException {
        String failureMsg = "Could not terminate instrument Id: " + instrumentId;
         Integer rented_id = instrumentDb.findRentedInstrumentByID(instrumentId);
        if (instrumentId==null||rented_id!= instrumentId) {
            throw new InstrumentException("Could not find instrument's id: "+instrumentId);
        }
        try {
            instrumentDb.updateRental(instrumentId);
        } catch (Exception e) {
            throw new InstrumentException(failureMsg, e);
        }
    }
}
