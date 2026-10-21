package rentals;

/**
 * A rental car. Adds the car type, the transmission (0 = automatic, 1 =
 * manual) and the autopilot level (0-5) to the fields every Vehicle has.
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 */
public class Car
        extends Vehicle
{
    // ~ Fields ................................................................

    /** Allowed car types, in their canonical spelling. */
    public static final String[] CAR_TYPES =
            { "Sedan", "SUV", "Coupe", "Hatchback", "Truck", "Van" };
    /** Transmission value for an automatic car. */
    public static final int AUTOMATIC = 0;
    /** Transmission value for a manual car. */
    public static final int MANUAL = 1;
    /** Display names for each transmission value, indexed by that value. */
    public static final String[] TRANSMISSIONS = { "Automatic", "Manual" };
    /** Highest autopilot level (0 = none, 5 = fully self-driving). */
    public static final int MAX_AUTOPILOT = 5;

    private String carType;
    private int transmission;
    private int autoPilot;

    // ~ Constructors ..........................................................

    /**
     * Creates a car. Shared fields are checked by Vehicle; car fields are set
     * through their setters so the same checks apply.
     *
     * @param year
     *            model year, MIN_YEAR to MAX_YEAR
     * @param name
     *            model name, e.g. "Civic"
     * @param maker
     *            manufacturer, e.g. "Honda"
     * @param cylinders
     *            number of cylinders, 0 for electric
     * @param pax
     *            number of passengers, at least 1
     * @param mileage
     *            mileage, not negative
     * @param price
     *            price, not negative
     * @param tankSize
     *            tank size in gallons, not negative
     * @param fuelType
     *            one of FUEL_TYPES
     * @param carType
     *            one of CAR_TYPES
     * @param transmission
     *            AUTOMATIC (0) or MANUAL (1)
     * @param autoPilot
     *            autopilot level, 0 to MAX_AUTOPILOT
     * @throws IllegalArgumentException
     *             if any value is invalid
     */
    public Car(
            int year,
            String name,
            String maker,
            int cylinders,
            int pax,
            double mileage,
            double price,
            double tankSize,
            String fuelType,
            String carType,
            int transmission,
            int autoPilot)
    {
        super(year, name, maker, cylinders, pax, mileage, price, tankSize,
                fuelType);
        setCarType(carType);
        setTransmission(transmission);
        setAutoPilot(autoPilot);
    }

    // ~ Public Methods ........................................................

    /**
     * @return the car type, in its canonical spelling
     */
    public String getCarType()
    {
        return carType;
    }


    /**
     * @param carType
     *            one of CAR_TYPES (case-insensitive)
     * @throws IllegalArgumentException
     *             if carType is not one of CAR_TYPES
     */
    public final void setCarType(String carType)
    {
        this.carType = requireOption(carType, CAR_TYPES, "car type");
    }


    /**
     * @return AUTOMATIC (0) or MANUAL (1)
     */
    public int getTransmission()
    {
        return transmission;
    }


    /**
     * @param transmission
     *            AUTOMATIC (0) or MANUAL (1)
     * @throws IllegalArgumentException
     *             if transmission is not 0 or 1
     */
    public final void setTransmission(int transmission)
    {
        if (transmission != AUTOMATIC && transmission != MANUAL)
        {
            throw new IllegalArgumentException(
                    "Transmission must be 0 (automatic) or 1 (manual)");
        }
        this.transmission = transmission;
    }


    /**
     * @return the autopilot level, 0 to MAX_AUTOPILOT
     */
    public int getAutoPilot()
    {
        return autoPilot;
    }


    /**
     * @param autoPilot
     *            the autopilot level, 0 to MAX_AUTOPILOT
     * @throws IllegalArgumentException
     *             if autoPilot is out of range
     */
    public final void setAutoPilot(int autoPilot)
    {
        if (autoPilot < 0 || autoPilot > MAX_AUTOPILOT)
        {
            throw new IllegalArgumentException(
                    "Autopilot level must be between 0 and " + MAX_AUTOPILOT);
        }
        this.autoPilot = autoPilot;
    }


    /**
     * @return "Car"
     */
    @Override
    public String getVehicleType()
    {
        return "Car";
    }


    /**
     * @return the shared vehicle summary plus the car fields
     */
    @Override
    public String toString()
    {
        return super.toString()
                + ", carType=" + carType
                + ", transmission=" + TRANSMISSIONS[transmission]
                + ", autoPilot=" + autoPilot;
    }
}