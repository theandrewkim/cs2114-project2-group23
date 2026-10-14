package rentals;

/**
 * Tests the car-only fields: car type, transmission (0 = automatic, 1 =
 * manual) and autopilot level (0-5).
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 */
public class CarTest
        extends student.TestCase
{
    // ~ Fields ................................................................

    private Car civic;
    private Car wrangler;

    // ~ Setup .................................................................

    /**
     * Creates an automatic sedan and a manual SUV.
     */
    public void setUp()
    {
        civic = new Car(2022, "Civic", "Honda", 4, 5, 15000.5, 24000.0, 12.4,
                "Gasoline", "Sedan", Car.AUTOMATIC, 2);
        wrangler = new Car(2019, "Wrangler", "Jeep", 6, 4, 60000, 28000, 21.5,
                "Gasoline", "suv", Car.MANUAL, 0);
    }

    // ~ Constructor / getters .................................................

    /**
     * The constructor stores the car fields, with the canonical car type.
     */
    public void testGetters()
    {
        assertEquals("Sedan", civic.getCarType());
        assertEquals(Car.AUTOMATIC, civic.getTransmission());
        assertEquals(2, civic.getAutoPilot());
        assertEquals("SUV", wrangler.getCarType());
        assertEquals(Car.MANUAL, wrangler.getTransmission());
        assertEquals(0, wrangler.getAutoPilot());
    }


    /**
     * The constructor rejects a bad car field.
     */
    public void testConstructorValidates()
    {
        Exception thrown = null;
        try
        {
            new Car(2022, "Civic", "Honda", 4, 5, 0, 0, 0, "Gasoline",
                    "Limo", Car.AUTOMATIC, 0);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * A car reports its type as "Car".
     */
    public void testGetVehicleType()
    {
        assertEquals("Car", civic.getVehicleType());
    }

    // ~ setCarType ............................................................

    /**
     * setCarType() accepts every CAR_TYPES value in any case.
     */
    public void testSetCarTypeValid()
    {
        for (String type : Car.CAR_TYPES)
        {
            civic.setCarType(type.toLowerCase());
            assertEquals(type, civic.getCarType());
        }
    }


    /**
     * setCarType() rejects a type not in CAR_TYPES.
     */
    public void testSetCarTypeInvalid()
    {
        Exception thrown = null;
        try
        {
            civic.setCarType("Limo");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals("Sedan", civic.getCarType());
    }


    /**
     * setCarType() rejects null.
     */
    public void testSetCarTypeNull()
    {
        Exception thrown = null;
        try
        {
            civic.setCarType(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ~ setTransmission .......................................................

    /**
     * setTransmission() accepts 0 and 1.
     */
    public void testSetTransmissionValid()
    {
        civic.setTransmission(Car.MANUAL);
        assertEquals(1, civic.getTransmission());
        civic.setTransmission(Car.AUTOMATIC);
        assertEquals(0, civic.getTransmission());
    }


    /**
     * setTransmission() rejects 2.
     */
    public void testSetTransmissionTooHigh()
    {
        Exception thrown = null;
        try
        {
            civic.setTransmission(2);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(Car.AUTOMATIC, civic.getTransmission());
    }


    /**
     * setTransmission() rejects -1.
     */
    public void testSetTransmissionNegative()
    {
        Exception thrown = null;
        try
        {
            civic.setTransmission(-1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ~ setAutoPilot ..........................................................

    /**
     * setAutoPilot() accepts 0 through MAX_AUTOPILOT.
     */
    public void testSetAutoPilotValid()
    {
        civic.setAutoPilot(0);
        assertEquals(0, civic.getAutoPilot());
        civic.setAutoPilot(Car.MAX_AUTOPILOT);
        assertEquals(5, civic.getAutoPilot());
    }


    /**
     * setAutoPilot() rejects levels above MAX_AUTOPILOT.
     */
    public void testSetAutoPilotTooHigh()
    {
        Exception thrown = null;
        try
        {
            civic.setAutoPilot(Car.MAX_AUTOPILOT + 1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals(2, civic.getAutoPilot());
    }


    /**
     * setAutoPilot() rejects negative levels.
     */
    public void testSetAutoPilotNegative()
    {
        Exception thrown = null;
        try
        {
            civic.setAutoPilot(-1);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }

    // ~ toString ..............................................................

    /**
     * toString() adds the car fields, showing the transmission by name.
     */
    public void testToString()
    {
        String text = civic.toString();
        assertTrue(text.startsWith("Car #" + civic.getId() + ": "));
        assertTrue(text.contains("name=Civic"));
        assertTrue(text.endsWith(
                ", carType=Sedan, transmission=Automatic, autoPilot=2"));
        assertTrue(wrangler.toString().contains("transmission=Manual"));
    }
}
