package rentals;

/**
 * A rental motorcycle. Adds the motorcycle type to the fields every Vehicle
 * has.
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 */
public class Motorcycle
        extends Vehicle
{
    // ~ Fields ................................................................

    /** Allowed motorcycle types, in their canonical spelling. */
    public static final String[] MOTORCYCLE_TYPES =
            { "Sport", "Cruiser", "Touring", "Standard", "Dirt" };

    private String motorcycleType;

    // ~ Constructors ..........................................................

    /**
     * Creates a motorcycle. Shared fields are checked by Vehicle; the
     * motorcycle type is set through its setter so the same check applies.
     *
     * @param year
     *            model year, MIN_YEAR to MAX_YEAR
     * @param name
     *            model name, e.g. "Ninja 400"
     * @param maker
     *            manufacturer, e.g. "Kawasaki"
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
     * @param motorcycleType
     *            one of MOTORCYCLE_TYPES
     * @throws IllegalArgumentException
     *             if any value is invalid
     */
    public Motorcycle(
            int year,
            String name,
            String maker,
            int cylinders,
            int pax,
            double mileage,
            double price,
            double tankSize,
            String fuelType,
            String motorcycleType)
    {
        super(year, name, maker, cylinders, pax, mileage, price, tankSize,
                fuelType);
        setMotorcycleType(motorcycleType);
    }

    // ~ Public Methods ........................................................

    /**
     * @return the motorcycle type, in its canonical spelling
     */
    public String getMotorcycleType()
    {
        return motorcycleType;
    }


    /**
     * @param motorcycleType
     *            one of MOTORCYCLE_TYPES (case-insensitive)
     * @throws IllegalArgumentException
     *             if motorcycleType is not one of MOTORCYCLE_TYPES
     */
    public final void setMotorcycleType(String motorcycleType)
    {
        this.motorcycleType = requireOption(motorcycleType, MOTORCYCLE_TYPES,
                "motorcycle type");
    }


    /**
     * @return "Motorcycle"
     */
    @Override
    public String getVehicleType()
    {
        return "Motorcycle";
    }


    /**
     * @return the shared vehicle summary plus the motorcycle type
     */
    @Override
    public String toString()
    {
        return super.toString() + ", motorcycleType=" + motorcycleType;
    }
}
