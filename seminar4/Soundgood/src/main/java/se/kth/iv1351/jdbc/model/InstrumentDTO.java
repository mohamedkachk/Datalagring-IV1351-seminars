package se.kth.iv1351.jdbc.model;

/**
 * Specifies a read-only view of an instrument.
 */
public interface InstrumentDTO {
    /**
     * @return The instrument's id.
     */
    public int getInstrumentId();

    /**
     * @return The name of instrument.
     */
    public String getInstrumentName();

    /**
     * @return The instrument's brand.
     */
    public String getInstrumentBrand();

    /**
     * @return The instrument's price.
     */
    public int getInstrumentPrice();
}
