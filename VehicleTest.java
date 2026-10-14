package rentals;

/**
 * Tests the shared behavior in Vehicle: getters, validating setters, unique
 * ids, equals/hashCode and toString. Vehicle is abstract, so it is tested
 * through Car and Motorcycle objects.
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 */
public class VehicleTest
        extends student.TestCase
{
    // ~ Fields ................................................................

    private Vehicle civic;
    private Vehicle ninja;

    // ~ Setup .................................................................

    /**
     * Creates one car and one motorcycle.
     */
    public void setUp()
    {
        civic = new Car(2022, "Civic", "Honda", 4, 5, 15000.5, 24000.0, 12.4,
                "Gasoline", "Sedan", Car.AUTOMATIC, 2);
        ninja = new Motorcycle(2021, "Ninja 400", "Kawasaki", 2, 2, 3200.0,
                6500.0, 3.7, "Gasoline", "Sport");
    }

    // ~ Constructor / getters .................................................

    /**
     * The constructor stores every shared field.
     */
    public void testGetters()
    {
        assertEquals(2022, civic.getYear());
        assertEquals("Civic", civic.getName());
        assertEquals("Honda", civic.getMaker());
        assertEquals(4, civic.getCylinders());
        assertEquals(5, civic.getPax());
        assertEquals(15000.5, civic.getMileage(), 0.001);
        assertEquals(24000.0, civic.getPrice(), 0.001);
        assertEquals(12.4, civic.getTankSize(), 0.001);
        assertEquals("Gasoline", civic.getFuelType());
    }


    /**
     * Each new vehicle gets the next id, and ids are never shared.
     */
    public void testUniqueIds()
    {
        assertEquals(civic.getId() + 1, ninja.getId());
        Vehicle next = new Motorcycle(2020, "Rebel", "Honda", 1, 1, 0, 5000,
                3.0, "Gasoline", "Cruiser");
        assertEquals(ninja.getId() + 1, next.getId());
    }


    /**
     * The constructor uses the setters, so bad values are rejected at
     * creation time too.
     */
    public void testConstructorValidates()
    {
        Exception thrown = null;
        try
        {
            new Motorcycle(1800, "Old", "Maker", 1, 1, 0, 0, 0, "Gasoline",
                    "Sport");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * getVehicleType() is implemented by each subclass.
     */
    public void testGetVehicleType()
    {
        assertEquals("Car", civic.getVehicleType());
        assertEquals("Motorcycle", ninja.getVehicleType());
    }

    // ~ setYear ...............................................................

    /**
     * setYear() accepts MIN_YEAR through MAX_YEAR.
     */
    public void testSetYearValid()
    {
        civic.setYear(Vehicle.MIN_YEAR);
        assertEquals(Vehicle.MIN_YEAR, civic.getYear());
        civic.setYear(Vehicle.MAX_YEAR);
        assertEquals(Vehicle.MAX_YEAR, civic.getYear());
    }


    /**
     * setYear() rejects a year below MIN_YEAR.
     */
    public void testSetYearTooOld()
    {
        Exception thrown = null;
        try
        {
            civic.setYear(Vehicle.MIN_YEAR - 1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(2022, civic.getYear());
    }


    /**
     * setYear() rejects a year above MAX_YEAR.
     */
    public void testSetYearTooNew()
    {
        Exception thrown = null;
        try
        {
            civic.setYear(Vehicle.MAX_YEAR + 1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ~ setName / setMaker ....................................................

    /**
     * setName() stores the trimmed name.
     */
    public void testSetNameValid()
    {
        civic.setName("  Accord ");
        assertEquals("Accord", civic.getName());
    }


    /**
     * setName() rejects null.
     */
    public void testSetNameNull()
    {
        Exception thrown = null;
        try
        {
            civic.setName(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals("Civic", civic.getName());
    }


    /**
     * setName() rejects blank text.
     */
    public void testSetNameBlank()
    {
        Exception thrown = null;
        try
        {
            civic.setName("   ");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * setMaker() stores the trimmed maker.
     */
    public void testSetMakerValid()
    {
        civic.setMaker(" Toyota ");
        assertEquals("Toyota", civic.getMaker());
    }


    /**
     * setMaker() rejects null.
     */
    public void testSetMakerNull()
    {
        Exception thrown = null;
        try
        {
            civic.setMaker(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * setMaker() rejects an empty string.
     */
    public void testSetMakerEmpty()
    {
        Exception thrown = null;
        try
        {
            civic.setMaker("");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ~ setCylinders / setPax .................................................

    /**
     * setCylinders() accepts 0 (electric) and positive values.
     */
    public void testSetCylindersValid()
    {
        civic.setCylinders(0);
        assertEquals(0, civic.getCylinders());
        civic.setCylinders(8);
        assertEquals(8, civic.getCylinders());
    }


    /**
     * setCylinders() rejects negative values.
     */
    public void testSetCylindersNegative()
    {
        Exception thrown = null;
        try
        {
            civic.setCylinders(-1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(4, civic.getCylinders());
    }


    /**
     * setPax() accepts 1 or more.
     */
    public void testSetPaxValid()
    {
        civic.setPax(1);
        assertEquals(1, civic.getPax());
        civic.setPax(7);
        assertEquals(7, civic.getPax());
    }


    /**
     * setPax() rejects 0.
     */
    public void testSetPaxZero()
    {
        Exception thrown = null;
        try
        {
            civic.setPax(0);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(5, civic.getPax());
    }

    // ~ setMileage / setPrice / setTankSize ...................................

    /**
     * setMileage() accepts 0 and decimals.
     */
    public void testSetMileageValid()
    {
        civic.setMileage(0);
        assertEquals(0.0, civic.getMileage(), 0.001);
        civic.setMileage(45000.75);
        assertEquals(45000.75, civic.getMileage(), 0.001);
    }


    /**
     * setMileage() rejects negative values.
     */
    public void testSetMileageNegative()
    {
        Exception thrown = null;
        try
        {
            civic.setMileage(-0.5);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(15000.5, civic.getMileage(), 0.001);
    }


    /**
     * setMileage() rejects NaN.
     */
    public void testSetMileageNaN()
    {
        Exception thrown = null;
        try
        {
            civic.setMileage(Double.NaN);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * setPrice() accepts 0 and positive values.
     */
    public void testSetPriceValid()
    {
        civic.setPrice(0);
        assertEquals(0.0, civic.getPrice(), 0.001);
        civic.setPrice(19999.99);
        assertEquals(19999.99, civic.getPrice(), 0.001);
    }


    /**
     * setPrice() rejects negative values.
     */
    public void testSetPriceNegative()
    {
        Exception thrown = null;
        try
        {
            civic.setPrice(-1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * setTankSize() accepts 0 and decimals.
     */
    public void testSetTankSizeValid()
    {
        civic.setTankSize(0);
        assertEquals(0.0, civic.getTankSize(), 0.001);
        civic.setTankSize(15.5);
        assertEquals(15.5, civic.getTankSize(), 0.001);
    }


    /**
     * setTankSize() rejects negative values.
     */
    public void testSetTankSizeNegative()
    {
        Exception thrown = null;
        try
        {
            civic.setTankSize(-2.5);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ~ setFuelType ...........................................................

    /**
     * setFuelType() accepts any case and stores the canonical spelling.
     */
    public void testSetFuelTypeValid()
    {
        civic.setFuelType("hybrid");
        assertEquals("Hybrid", civic.getFuelType());
        civic.setFuelType(" ELECTRIC ");
        assertEquals("Electric", civic.getFuelType());
    }


    /**
     * setFuelType() rejects values not in FUEL_TYPES.
     */
    public void testSetFuelTypeInvalid()
    {
        Exception thrown = null;
        try
        {
            civic.setFuelType("Coal");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals("Gasoline", civic.getFuelType());
    }


    /**
     * setFuelType() rejects null.
     */
    public void testSetFuelTypeNull()
    {
        Exception thrown = null;
        try
        {
            civic.setFuelType(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ~ equals / hashCode / toString ..........................................

    /**
     * A vehicle equals itself.
     */
    public void testEqualsSelf()
    {
        assertTrue(civic.equals(civic));
    }


    /**
     * Vehicles with different ids are not equal, even with the same fields.
     */
    public void testEqualsDifferentId()
    {
        Vehicle twin = new Car(2022, "Civic", "Honda", 4, 5, 15000.5, 24000.0,
                12.4, "Gasoline", "Sedan", Car.AUTOMATIC, 2);
        assertFalse(civic.equals(twin));
        assertFalse(civic.equals(ninja));
    }


    /**
     * A vehicle is not equal to null or to another kind of object.
     */
    public void testEqualsNullAndOtherClass()
    {
        assertFalse(civic.equals(null));
        assertFalse(civic.equals("Civic"));
    }


    /**
     * hashCode() is the hash of the id.
     */
    public void testHashCode()
    {
        assertEquals(Integer.hashCode(civic.getId()), civic.hashCode());
        assertFalse(civic.hashCode() == ninja.hashCode());
    }


    /**
     * toString() starts with the type and id, then lists the shared fields.
     */
    public void testToString()
    {
        String text = ninja.toString();
        assertTrue(text.startsWith("Motorcycle #" + ninja.getId() + ": "));
        assertTrue(text.contains("year=2021"));
        assertTrue(text.contains("name=Ninja 400"));
        assertTrue(text.contains("maker=Kawasaki"));
        assertTrue(text.contains("cylinders=2"));
        assertTrue(text.contains("pax=2"));
        assertTrue(text.contains("mileage=3200.0"));
        assertTrue(text.contains("price=6500.0"));
        assertTrue(text.contains("tankSize=3.7"));
        assertTrue(text.contains("fuelType=Gasoline"));
    }
}
