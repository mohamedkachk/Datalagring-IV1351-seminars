package se.kth.iv1351.jdbc.integration;

import se.kth.iv1351.jdbc.model.*;
import java.time.temporal.*;
import java.time.*;
import java.util.*;
import java.sql.*;

public class InstrumentDAO {

    private Connection connection;

    /**##########################################################################**/
    private static final String INSTRUMENT_TABLE_NAME = "instrument";
    private static final String INSTRUMENT_PK_COLUMN_NAME = "instrument_id";
    private static final String INSTRUMENT_COLUMN_NAME = "name";
    private static final String INSTRUMENT_COLUMN_BRAND = "brand";
    private static final String INSTRUMENT_COLUMN_PRICE = "price";

    private PreparedStatement findAllInstrumentsStmt;
    private PreparedStatement findInstrumentByNameStmt;
    private PreparedStatement findInstrumentByIdStmtLockingForUpdate ;
    /**##########################################################################**/
    private static final String STUDENT_TABLE_NAME = "student";
    private static final String STUDENT_PK_COLUMN_NAME = "student_id";
    private PreparedStatement findStudentPKStmt;
    /**##########################################################################**/
    private static final String CONTRACT_OF_RENTAL_TABLE_NAME = "contract_of_rental";

    private static final String CONTRACT_OF_RENTAL_COLUMN_PRICE = " monthly_price";

    private static final String CONTRACT_OF_RENTAL_COLUMN_START_TIME = "start_time";
    private static final String CONTRACT_OF_RENTAL_COLUMN_END_TIME = "end_time";
    private static final String NO_OF_RENTED_INSTRUMENT = "no_of_rented_instrument";

    private static final String STUDENT_FK_COLUMN_NAME = STUDENT_PK_COLUMN_NAME;

    private static final String INSTRUMENT_FK_COLUMN_NAME = INSTRUMENT_PK_COLUMN_NAME;

    private PreparedStatement countNumberOfRentedInstrument;

    private PreparedStatement CreateRentalStmt;
    private PreparedStatement findRentedInstrumentIdStmt;
    private PreparedStatement updateEndTimeOfRentalStmt;
/**##########################################################################**/

    /**
     * Constructs a new DAO object connected to the database.
     */
    public InstrumentDAO() throws InstrumentDBException {
        try {
            connectToDB();
            prepareStatements();
        } catch (ClassNotFoundException | SQLException exception) {
            throw new InstrumentDBException("Could not connect to datasource.", exception);
        }
    }


    /**
     * Retrieves all available instruments.
     *
     * @return A list with all available instruments.
     * The list is empty if there are no available instruments.
     *
     * @throws InstrumentDBException If failed to search for instruments.
     */
    public List<Instrument> findAllInstruments() throws InstrumentDBException {
        String failureMsg = "Could not list instruments.";
        List<Instrument> instruments = new ArrayList<>();
        try (ResultSet result = findAllInstrumentsStmt.executeQuery()) {
            while (result.next()) {
                instruments.add(createObject(result));
            }
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        }
        return instruments;
    }


    /**
     * Searches for all instruments by the specified name.
     *
     * @param instrumName The instrument's name
     * @return A list with all instruments which have the specified name,
     *         the list is empty if there are no such instrument.
     * @throws InstrumentDBException If failed to search for instruments.
     */
    public List<Instrument> findInstrumentsByName(String instrumName) throws InstrumentDBException {
        String failureMsg = "Could not search for the specified instrument: "+instrumName;
        ResultSet result = null;
        List<Instrument> instruments = new ArrayList<>();
        try {
            findInstrumentByNameStmt.setString(1, instrumName);
            result = findInstrumentByNameStmt.executeQuery();
            while (result.next()) {
                instruments.add(createObject(result));
            }
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        } finally {
            closeResultSet(failureMsg, result);
        }
        return instruments;
    }

    /**
     * Searches for the student with the specified id number.
     *
     * @param student_id to search.
     * @return An id of the student, or <code>null</code> if
     * there is no such student's id.
     *
     * @throws InstrumentDBException If failed to search the specified id.
     */
    public Integer findStudentPKById(Integer student_id) throws InstrumentDBException {
        String failureMsg = "Could not find the student's id: " + student_id;
        ResultSet result = null;
        try {
            findStudentPKStmt.setInt(1, student_id);
            result = findStudentPKStmt.executeQuery();
            if (result.next()) {
                return result.getInt(STUDENT_PK_COLUMN_NAME);
            }
            connection.commit();
        }
        catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        } finally {
            closeResultSet(failureMsg, result);
        }
        return null;
    }

    /**
     * Searches for the instrument with the specified id number.
     *
     *  @param instrument_id The id of the instrument which will be searched.
     *                      It will not be possible to perform UPDATE
     *                      or DELETE statements on the selected row in the
     *                      current transaction. Also, the transaction will not
     *                      be committed when this method returns.
     *                      If no row is founded the transaction will
     *                      be committed when this method returns.
     * @return The instrument with the specified instrument's id,
     * or <code>null</code> if  there is no such instrument's id.
     *
     * @throws InstrumentDBException If failed to search for the instrument's id.
     */
    public Instrument findInstrumentByID(Integer instrument_id) throws InstrumentDBException {
        String failureMsg = "Could not search for specified instrument's id: "+instrument_id;
        PreparedStatement stmtToExecute = findInstrumentByIdStmtLockingForUpdate;
        ResultSet result = null;
        try {
            stmtToExecute.setInt(1, instrument_id);
            result = stmtToExecute.executeQuery();
            if (result.next()) {
                return createObject(result);
            }
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        } finally {
            closeResultSet(failureMsg, result);
        }
        return null;
    }

    /**
     * Counts the number of all rentals for the student with
     * the specified id number.
     *
     * @param student_id The id of the student which has rented instrument.
     *
     * @return A number of rentals for the specified student's id,
     * returns zero if no rental found for this student's id.
     *
     * @throws InstrumentDBException If failed to count the rentals.
     */
    public Integer findNumberOfRentalsByStudentID(Integer student_id) throws InstrumentDBException {
        String failureMsg = "Could not count the number of rental " +
                "for this specified student's id: " + student_id;
        ResultSet result = null;
        try {
            countNumberOfRentedInstrument.setInt(1, student_id);
            result = countNumberOfRentedInstrument.executeQuery();
            if (result.next()) {
                return result.getInt(NO_OF_RENTED_INSTRUMENT);
            }
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        } finally {
            closeResultSet(failureMsg, result);
        }
        return 0;
    }

    /**
     * Searches for an ongoing rental for the instrument 
     * with the specified id number.
     *
     *  @param instrumentId The id of the instrument for an ongoing rental 
     *                      which will be searched.
     *                      It will not be possible to perform UPDATE
     *                      or DELETE statements on the selected row in the
     *                      current transaction. Also, the transaction will not
     *                      be committed when this method returns.
     *                      If no row is founded the transaction will
     *                      be committed when this method returns.
     * @return The id of the instrument for an ongoing rental,
     * or <code>null</code> if there is no such rental.
     *
     * @throws InstrumentDBException If failed to search for an ongoing rental
     *  with the specified instrument's id.
     */
    public Integer findRentedInstrumentByID(Integer instrumentId) 
             throws InstrumentDBException {
        String failureMsg = "Could not find the rented instrument's id: "+instrumentId;
        ResultSet result = null;
        try {
            findRentedInstrumentIdStmt.setInt(1, instrumentId);
            result = findRentedInstrumentIdStmt.executeQuery();
            if (result.next()) {
                return result.getInt(INSTRUMENT_FK_COLUMN_NAME);
            }
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        } finally {
            closeResultSet(failureMsg, result);
        }
        return null;
    }

    /**
     * Terminates an ongoing rental with the specified instrument's id.
     *
     * @param instrumentId The id of the rented instrument.
     *                     
     * @throws InstrumentDBException If unable to Terminate the 
     * ongoing rental with the specified instrument's id.
     */
    public void updateRental(Integer instrumentId) throws InstrumentDBException {
        String failureMsg = "Could not terminate instrument's id: " + instrumentId;
        LocalDateTime localDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        try {
            updateEndTimeOfRentalStmt.setTimestamp(1, Timestamp.valueOf(localDate));
            updateEndTimeOfRentalStmt.setInt(2, instrumentId);
            int updatedRows = updateEndTimeOfRentalStmt.executeUpdate();
            if (updatedRows != 1) {
                handleException(failureMsg, null);
            }
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        }
    }

    /**
     * Creates an object of Instrument.
     *
     * <code>ResultSet</code> SQL object.
     *
     * @return Instrument object.
     * @throws SQLException If failed to create the object.
     */
    private Instrument createObject(ResultSet result) throws SQLException {
        int id = result.getInt(INSTRUMENT_PK_COLUMN_NAME);
        String name = result.getString(INSTRUMENT_COLUMN_NAME);
        String brand = result.getString(INSTRUMENT_COLUMN_BRAND);
        int price = result.getInt(INSTRUMENT_COLUMN_PRICE);
        return new Instrument(id, name, brand, price);
    }

    /**
     * Creates a new rental for a student.
     *
     * <code>Instrument</code> object. The instrument which will be rented
     * in the specified <code>Instrument</code>.
     * @param student_id The id of the student who will rent the instrument.
     * @param no_month The number of month for rental.
     * @throws InstrumentDBException If failed to create the specified rental.
     */
    public void createRental(Instrument instrmnt, Integer student_id, Integer no_month)
            throws InstrumentDBException {
        String failureMsg = "Could not rent the instrument: " + instrmnt;
        LocalDateTime localDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime plusMonth = localDate.plusMonths(no_month);

        try {
            CreateRentalStmt.setInt(1, student_id);
            CreateRentalStmt.setInt(2, instrmnt.getInstrumentId());
            CreateRentalStmt.setInt(3, instrmnt.getInstrumentPrice());
            CreateRentalStmt.setTimestamp(4, Timestamp.valueOf(localDate));
            CreateRentalStmt.setTimestamp(5, Timestamp.valueOf(plusMonth));
            int updatedRows = CreateRentalStmt.executeUpdate();
            if (updatedRows != 1) {
                handleException(failureMsg, null);
            }
            connection.commit();
        } catch (SQLException sqle) {
            handleException(failureMsg, sqle);
        }
    }


    /**
     * Commits the current transaction.
     *
     * @throws InstrumentDBException If unable to commit the current transaction.
     */
    public void commit() throws InstrumentDBException {
        try {
            connection.commit();
        } catch (SQLException e) {
            handleException("Failed to commit", e);
        }
    }

    private void connectToDB() throws ClassNotFoundException, SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "postgres");
        connection.setAutoCommit(false);
    }

     /**
     * Prepares the statements for the different queries.
     *
     * @throws SQLException If unable to access the database.
     */
    private void prepareStatements() throws SQLException {
        findAllInstrumentsStmt = connection.prepareStatement("SELECT * FROM " + INSTRUMENT_TABLE_NAME
                + " i WHERE NOT EXISTS " + " (SELECT * FROM " + CONTRACT_OF_RENTAL_TABLE_NAME
                + " c WHERE " + " c." + INSTRUMENT_FK_COLUMN_NAME +"=" + "i."
                + INSTRUMENT_PK_COLUMN_NAME + " AND c.end_time > NOW())");


        findInstrumentByNameStmt = connection.prepareStatement("SELECT * FROM "
                + INSTRUMENT_TABLE_NAME + " i WHERE NOT EXISTS " + " (SELECT * FROM "
                + CONTRACT_OF_RENTAL_TABLE_NAME + " c WHERE "
                + " c." + INSTRUMENT_FK_COLUMN_NAME +" = " + "i." + INSTRUMENT_PK_COLUMN_NAME
                + " AND c.end_time > NOW()) " + " AND i." + INSTRUMENT_COLUMN_NAME + " = ?");


        findInstrumentByIdStmtLockingForUpdate = connection.prepareStatement("SELECT * FROM "+
                INSTRUMENT_TABLE_NAME + " i WHERE NOT EXISTS "+" (SELECT * FROM " + CONTRACT_OF_RENTAL_TABLE_NAME +
                " c WHERE  c." + INSTRUMENT_FK_COLUMN_NAME +" =  i." + INSTRUMENT_PK_COLUMN_NAME
                +" AND c.end_time > NOW()) " + " AND i." + INSTRUMENT_PK_COLUMN_NAME + " = ? FOR UPDATE");


        CreateRentalStmt = connection.prepareStatement("INSERT INTO "+ CONTRACT_OF_RENTAL_TABLE_NAME +
                "("+ STUDENT_FK_COLUMN_NAME +","+ INSTRUMENT_FK_COLUMN_NAME+","+ CONTRACT_OF_RENTAL_COLUMN_PRICE +","
                +CONTRACT_OF_RENTAL_COLUMN_START_TIME +","+CONTRACT_OF_RENTAL_COLUMN_END_TIME+
                ") VALUES (?, ?, ?, ?, ?)");


        countNumberOfRentedInstrument = connection.prepareStatement("SELECT COUNT(*) AS "+
                NO_OF_RENTED_INSTRUMENT +" FROM "+ CONTRACT_OF_RENTAL_TABLE_NAME +
                " WHERE "+ STUDENT_FK_COLUMN_NAME +" = ? AND (end_time > NOW())");


        findStudentPKStmt = connection.prepareStatement("SELECT "
                + STUDENT_PK_COLUMN_NAME + " FROM " + STUDENT_TABLE_NAME + " WHERE "
                + STUDENT_PK_COLUMN_NAME +" = ?");


        findRentedInstrumentIdStmt = connection.prepareStatement("SELECT * FROM "+
                CONTRACT_OF_RENTAL_TABLE_NAME + " WHERE " + INSTRUMENT_FK_COLUMN_NAME + " = ? AND " +
                "(end_time > NOW()) FOR UPDATE");


        updateEndTimeOfRentalStmt = connection.prepareStatement("UPDATE "
                + CONTRACT_OF_RENTAL_TABLE_NAME  + " SET "
                + CONTRACT_OF_RENTAL_COLUMN_END_TIME + " = ? WHERE "
                + INSTRUMENT_FK_COLUMN_NAME + " = ?"
                + " AND (end_time > NOW())" );

    }

    private void handleException(String failureMsg, Exception cause) throws InstrumentDBException {
        String completeFailureMsg = failureMsg;
        try {
            connection.rollback();
        } catch (SQLException rollbackExc) {
            completeFailureMsg = completeFailureMsg +
                    ". Also failed to rollback transaction because of: " + rollbackExc.getMessage();
        }

        if (cause != null) {
            throw new InstrumentDBException(failureMsg, cause);
        } else {
            throw new InstrumentDBException(failureMsg);
        }
    }

    private void closeResultSet(String failureMsg, ResultSet result) throws InstrumentDBException {
        try {
            result.close();
        } catch (Exception e) {
            throw new InstrumentDBException(failureMsg + " Could not close result set.", e);
        }
    }

}
