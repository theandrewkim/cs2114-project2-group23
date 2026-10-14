package rentals;

/**
 * Holds everything a Car and a Motorcycle have in common. Vehicle is never
 * created directly; every vehicle gets a unique id when it is built, and the
 * id never changes. The setters check their argument and throw
 * IllegalArgumentException for bad values, and the constructor sets every
 * field through its setter, so the same checks apply when a vehicle is built.
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 */
public abstract class Vehicle
{
    // ~ Fields ................................................................

    /** Oldest model year accepted. */
    public static final int MIN_YEAR = 1900;
    /** Newest model year accepted. */
    public static final int MAX_YEAR = 2027;
    /** Most cylinders accepted (0 means electric). */
    public static final int MAX_CYLINDERS = 16;
    /** Most passengers accepted. */
    public static final int MAX_PAX = 15;
    /** Highest mileage accepted. */
    public static final double MAX_MILEAGE = 500000.0;
    /** Highest price accepted. */
    public static final double MAX_PRICE = 1000000.0;
    /** Largest tank size accepted, in gallons. */
    public static final double MAX_TANK = 60.0;
    /** Allowed fuel types, in their canonical spelling. */
    public static final String[] FUEL_TYPES =
            { "Gasoline", "Diesel", "Electric", "Hybrid" };

    private static int nextId = 1;

    private final int id;
    private int year;
    private String name;
    private String maker;
    private int cylinders;
    private int pax;
    private double mileage;
    private double price;
    private double tankSize;
    private String fuelType;

    // ~ Constructors ..........................................................

    /**
     * Creates a vehicle with the next unique id. Every field is set through
     * its setter, so invalid values are rejected.
     *
     * @param year
     *            model year, MIN_YEAR to MAX_YEAR
     * @param name
     *            model name, e.g. "Civic"; not null or blank
     * @param maker
     *            manufacturer, e.g. "Honda"; not null or blank
     * @param cylinders
     *            number of cylinders, 0 for electric; not negative
     * @param pax
     *            number of passengers; at least 1
     * @param mileage
     *            mileage; not negative
     * @param price
     *            price; not negative
     * @param tankSize
     *            tank size in gallons; not negative
     * @param fuelType
     *            one of FUEL_TYPES (case-insensitive)
     * @throws IllegalArgumentException
     *             if any value is invalid
     */
    protected Vehicle(
            int year,
            String name,
            String maker,
            int cylinders,
            int pax,
            double mileage,
            double price,
            double tankSize,
            String fuelType)
    {
        this.id = nextId++;
        setYear(year);
        setName(name);
        setMaker(maker);
        setCylinders(cylinders);
        setPax(pax);
        setMileage(mileage);
        setPrice(price);
        setTankSize(tankSize);
        setFuelType(fuelType);
    }

    // ~ Public Methods ........................................................

    /**
     * @return this vehicle's unique id
     */
    public int getId()
    {
        return id;
    }


    /**
     * @return the model year
     */
    public int getYear()
    {
        return year;
    }


    /**
     * @param year
     *            the new model year, MIN_YEAR to MAX_YEAR
     * @throws IllegalArgumentException
     *             if year is out of range
     */
    public final void setYear(int year)
    {
        if (year < MIN_YEAR || year > MAX_YEAR)
        {
            throw new IllegalArgumentException(
                    "Year must be between " + MIN_YEAR + " and " + MAX_YEAR);
        }
        this.year = year;
    }


    /**
     * @return the model name
     */
    public String getName()
    {
        return name;
    }


    /**
     * @param name
     *            the new model name; stored trimmed
     * @throws IllegalArgumentException
     *             if name is null or blank
     */
    public final void setName(String name)
    {
        this.name = requireText(name, "Name");
    }


    /**
     * @return the manufacturer
     */
    public String getMaker()
    {
        return maker;
    }


    /**
     * @param maker
     *            the new manufacturer; stored trimmed
     * @throws IllegalArgumentException
     *             if maker is null or blank
     */
    public final void setMaker(String maker)
    {
        this.maker = requireText(maker, "Maker");
    }


    /**
     * @return the number of cylinders (0 for electric)
     */
    public int getCylinders()
    {
        return cylinders;
    }


    /**
     * @param cylinders
     *            the new number of cylinders
     * @throws IllegalArgumentException
     *             if cylinders is negative
     */
    public final void setCylinders(int cylinders)
    {
        if (cylinders < 0)
        {
            throw new IllegalArgumentException(
                    "Cylinders cannot be negative");
        }
        this.cylinders = cylinders;
    }


    /**
     * @return the number of passengers
     */
    public int getPax()
    {
        return pax;
    }


    /**
     * @param pax
     *            the new number of passengers
     * @throws IllegalArgumentException
     *             if pax is less than 1
     */
    public final void setPax(int pax)
    {
        if (pax < 1)
        {
            throw new IllegalArgumentException(
                    "A vehicle must seat at least 1 passenger");
        }
        this.pax = pax;
    }


    /**
     * @return the mileage
     */
    public double getMileage()
    {
        return mileage;
    }


    /**
     * @param mileage
     *            the new mileage
     * @throws IllegalArgumentException
     *             if mileage is negative or not a number
     */
    public final void setMileage(double mileage)
    {
        this.mileage = requireNonNegative(mileage, "Mileage");
    }


    /**
     * @return the price
     */
    public double getPrice()
    {
        return price;
    }


    /**
     * @param price
     *            the new price
     * @throws IllegalArgumentException
     *             if price is negative or not a number
     */
    public final void setPrice(double price)
    {
        this.price = requireNonNegative(price, "Price");
    }


    /**
     * @return the tank size in gallons
     */
    public double getTankSize()
    {
        return tankSize;
    }


    /**
     * @param tankSize
     *            the new tank size in gallons
     * @throws IllegalArgumentException
     *             if tankSize is negative or not a number
     */
    public final void setTankSize(double tankSize)
    {
        this.tankSize = requireNonNegative(tankSize, "Tank size");
    }


    /**
     * @return the fuel type, in its canonical spelling
     */
    public String getFuelType()
    {
        return fuelType;
    }


    /**
     * @param fuelType
     *            one of FUEL_TYPES (case-insensitive); stored in its canonical
     *            spelling
     * @throws IllegalArgumentException
     *             if fuelType is not one of FUEL_TYPES
     */
    public final void setFuelType(String fuelType)
    {
        this.fuelType = requireOption(fuelType, FUEL_TYPES, "fuel type");
    }


    /**
     * @return "Car" or "Motorcycle", depending on the subclass
     */
    public abstract String getVehicleType();


    /**
     * @return a one-line summary of the id and all shared fields
     */
    @Override
    public String toString()
    {
        return getVehicleType() + " #" + id
                + ": year=" + year
                + ", name=" + name
                + ", maker=" + maker
                + ", cylinders=" + cylinders
                + ", pax=" + pax
                + ", mileage=" + mileage
                + ", price=" + price
                + ", tankSize=" + tankSize
                + ", fuelType=" + fuelType;
    }


    /**
     * Two vehicles are equal when they are the same class and have the same
     * id. LinkedChain's contains, count and remove rely on this.
     *
     * @param object
     *            the object to compare with
     * @return true if object is a vehicle with the same id
     */
    @Override
    public boolean equals(Object object)
    {
        if (object == null || this.getClass() != object.getClass())
        {
            return false;
        }
        Vehicle other = (Vehicle)object;
        return this.id == other.id;
    }


    /**
     * @return a hash code consistent with equals
     */
    @Override
    public int hashCode()
    {
        return Integer.hashCode(id);
    }

    // ~ Protected Methods .....................................................

    /**
     * Finds value in options, ignoring case, so callers can store the
     * canonical spelling.
     *
     * @param value
     *            the text to look up
     * @param options
     *            the allowed values
     * @param label
     *            what the value is, used in the error message
     * @return the matching entry from options
     * @throws IllegalArgumentException
     *             if value is null or not in options
     */
    protected static String requireOption(
            String value,
            String[] options,
            String label)
    {
        if (value != null)
        {
            for (String option : options)
            {
                if (option.equalsIgnoreCase(value.trim()))
                {
                    return option;
                }
            }
        }
        throw new IllegalArgumentException("Invalid " + label + ": " + value
                + ". Options are: " + String.join(", ", options));
    }

    // ~ Private Methods .......................................................

    /**
     * @param value
     *            the text to check
     * @param label
     *            what the value is, used in the error message
     * @return value, trimmed
     * @throws IllegalArgumentException
     *             if value is null or blank
     */
    private static String requireText(String value, String label)
    {
        if (value == null || value.trim().isEmpty())
        {
            throw new IllegalArgumentException(label + " cannot be blank");
        }
        return value.trim();
    }


    /**
     * @param value
     *            the number to check
     * @param label
     *            what the value is, used in the error message
     * @return value, unchanged
     * @throws IllegalArgumentException
     *             if value is negative or NaN
     */
    private static double requireNonNegative(double value, String label)
    {
        if (Double.isNaN(value) || value < 0)
        {
            throw new IllegalArgumentException(label + " cannot be negative");
        }
        return value;
    }
}
