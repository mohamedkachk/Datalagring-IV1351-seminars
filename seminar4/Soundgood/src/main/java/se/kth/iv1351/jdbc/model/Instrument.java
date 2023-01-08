package se.kth.iv1351.jdbc.model;

public class Instrument implements InstrumentDTO {
    private int id;
    private String name;
    private String brand;
    private int price;


    /**
     * Creates an instrument with the specified id, name, brand and price.
     *
     * @param id    The id of instrument.
     * @param name  The name of instrument.
     * @param brand The brand of instrument.
     * @param price The price for rent of instrument.
     */
    public Instrument(int id, String name, String brand, int price) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    /**
     * @return The instrument's id.
     */
    public int getInstrumentId() {
        return id;
    }

    /**
     * @return The instrument's name.
     */
    public String getInstrumentName() {
        return name;
    }

    /**
     * @return The instrument's brand.
     */
    public String getInstrumentBrand(){
        return brand;
    }

    /**
     * @return The instrument's price.
     */
    public int getInstrumentPrice() {
        return id;
    }


    /**
     * @return A string representation of all fields in this object.
     */
    @Override
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();
        stringRepresentation.append(" instrument id: ");
        stringRepresentation.append(id);
        stringRepresentation.append(",  name: ");
        stringRepresentation.append(name);
        stringRepresentation.append(", brand: ");
        stringRepresentation.append("["+brand+"]");
        stringRepresentation.append(", price: ");
        stringRepresentation.append(price);
        return stringRepresentation.toString();
    }
}

