package rentals;

/**
 * Tests the motorcycle-only field: motorcycle type.
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 */
public class MotorcycleTest
        extends student.TestCase
{
    // ~ Fields ................................................................

    private Motorcycle ninja;

    // ~ Setup .................................................................

    /**
     * Creates a sport motorcycle.
     */
    public void setUp()
    {
        ninja = new Motorcycle(2021, "Ninja 400", "Kawasaki", 2, 2, 3200.0,
                6500.0, 3.7, "Gasoline", "sport");
    }

    // ~ Tests .................................................................

    /**
     * The constructor stores the canonical motorcycle type.
     */
    public void testGetMotorcycleType()
    {
        assertEquals("Sport", ninja.getMotorcycleType());
    }


    /**
     * The constructor rejects a bad motorcycle type.
     */
    public void testConstructorValidates()
    {
        Exception thrown = null;
        try
        {
            new Motorcycle(2021, "Vespa", "Piaggio", 1, 2, 0, 0, 2.0,
                    "Gasoline", "Scooter");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * A motorcycle reports its type as "Motorcycle".
     */
    public void testGetVehicleType()
    {
        assertEquals("Motorcycle", ninja.getVehicleType());
    }


    /**
     * setMotorcycleType() accepts every MOTORCYCLE_TYPES value in any case.
     */
    public void testSetMotorcycleTypeValid()
    {
        for (String type : Motorcycle.MOTORCYCLE_TYPES)
        {
            ninja.setMotorcycleType(type.toUpperCase());
            assertEquals(type, ninja.getMotorcycleType());
        }
    }


    /**
     * setMotorcycleType() rejects a type not in MOTORCYCLE_TYPES.
     */
    public void testSetMotorcycleTypeInvalid()
    {
        Exception thrown = null;
        try
        {
            ninja.setMotorcycleType("Scooter");
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
        assertEquals("Sport", ninja.getMotorcycleType());
    }


    /**
     * setMotorcycleType() rejects null.
     */
    public void testSetMotorcycleTypeNull()
    {
        Exception thrown = null;
        try
        {
            ninja.setMotorcycleType(null);
        }
        catch (Exception e)
        {
            thrown = e;
        }
        assertNotNull(thrown);
        assertTrue(thrown instanceof IllegalArgumentException);
    }


    /**
     * toString() ends with the motorcycle type.
     */
    public void testToString()
    {
        String text = ninja.toString();
        assertTrue(text.startsWith("Motorcycle #" + ninja.getId() + ": "));
        assertTrue(text.endsWith(", motorcycleType=Sport"));
    }
}
