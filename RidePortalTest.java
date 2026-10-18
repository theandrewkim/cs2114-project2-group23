package rentals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Integration tests for RidePortal, following the Normal / Bad cases in our
 * test plan. Console input is supplied with setSystemIn() before each portal
 * is created, and console output is checked with systemOut().getHistory().
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 */
public class RidePortalTest
        extends student.TestCase
{
    // ~ Fields ................................................................

    private Car civic;
    private Car model3;
    private Car wrangler;
    private Motorcycle ninja;
    private Motorcycle glide;

    // ~ Setup .................................................................

    /**
     * Clears captured output and creates the sample vehicles.
     */
    public void setUp()
    {
        systemOut().clearHistory();
        civic = new Car(2022, "Civic", "Honda", 4, 5, 15000.5, 24000, 12.4,
                "Gasoline", "Sedan", Car.AUTOMATIC, 2);
        model3 = new Car(2023, "Model 3", "Tesla", 0, 5, 8000, 39000, 0,
                "Electric", "Sedan", Car.AUTOMATIC, 3);
        wrangler = new Car(2019, "Wrangler", "Jeep", 6, 4, 60000, 28000, 21.5,
                "Gasoline", "SUV", Car.MANUAL, 0);
        ninja = new Motorcycle(2021, "Ninja 400", "Kawasaki", 2, 2, 3200, 6500,
                3.7, "Gasoline", "Sport");
        glide = new Motorcycle(2022, "Street Glide", "Harley-Davidson", 2, 2,
                12000, 22000, 6.0, "Gasoline", "Touring");
    }

    // ~ Helpers ...............................................................

    /**
     * @param lines
     *            console input, one entry per line
     * @return a portal with an empty inventory that reads lines
     */
    private RidePortal portalWithInput(String... lines)
    {
        if (lines.length > 0)
        {
            setSystemIn(lines);
        }
        return new RidePortal();
    }


    /**
     * @param lines
     *            console input, one entry per line
     * @return a portal that reads lines and holds civic, model3, wrangler,
     *         ninja and glide, in that order
     */
    private RidePortal stockedPortal(String... lines)
    {
        RidePortal portal = portalWithInput(lines);
        portal.getInventory().add(civic);
        portal.getInventory().add(model3);
        portal.getInventory().add(wrangler);
        portal.getInventory().add(ninja);
        portal.getInventory().add(glide);
        return portal;
    }


    /**
     * @return everything printed to System.out during this test
     */
    private String output()
    {
        return systemOut().getHistory();
    }


    /**
     * @param text
     *            the text to search
     * @param target
     *            the text to count
     * @return how many times target appears in text
     */
    private int occurrences(String text, String target)
    {
        int count = 0;
        int index = text.indexOf(target);
        while (index >= 0)
        {
            count++;
            index = text.indexOf(target, index + target.length());
        }
        return count;
    }


    /**
     * Asserts that chain holds exactly the expected vehicles, in order.
     *
     * @param chain
     *            the chain to check
     * @param expected
     *            the vehicles it should hold
     */
    private void assertChain(LinkedChain<Vehicle> chain, Vehicle... expected)
    {
        assertNotNull(chain);
        assertEquals(expected.length, chain.size());
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(expected[i], chain.get(i));
        }
    }

    /**
     * @param rows
     *            the lines of an import file
     * @return a Scanner that reads rows, one per line
     */
    private Scanner fileLines(String... rows)
    {
        return new Scanner(String.join("\n", rows));
    }


    /**
     * @param rows
     *            the lines to write
     * @return a temporary file holding rows, deleted when the tests end
     * @throws IOException
     *             if the file cannot be created
     */
    private File tempFile(String... rows)
            throws IOException
    {
        File file = File.createTempFile("vehicles", ".csv");
        file.deleteOnExit();
        PrintWriter writer = new PrintWriter(file);
        for (String row : rows)
        {
            writer.println(row);
        }
        writer.close();
        return file;
    }


    // ~ main / run / displayMenu ..............................................

    /**
     * Normal: main() greets the user, shows the menu and exits on 0.
     */
    public void testMainExit()
    {
        setSystemIn("0");
        RidePortal.main(new String[0]);
        assertTrue(output().contains("Welcome to Ride Portal!"));
        assertTrue(output().contains("===== Ride Portal Menu ====="));
        assertTrue(output().contains("Goodbye!"));
    }


    /**
     * Normal: the user adds a car, selects Search, and the program searches
     * based on their input.
     */
    public void testMainAddThenSearch()
    {
        setSystemIn("1", // menu: add a vehicle
                "1", "2022", "Civic", "Honda", "4", "5", "15000.5", "24000",
                "12.4", "Gasoline", "Sedan", "0", "2", "n",
                "4", "3", "honda", "0", // menu: search by maker, then Done
                "0"); // menu: exit
        RidePortal.main(new String[0]);
        assertTrue(output().contains("Added: Car #"));
        assertTrue(output().contains("1 vehicle(s) match so far."));
        assertTrue(output().contains("===== Search Results ====="));
        assertTrue(output().contains("name=Civic"));
        assertTrue(output().contains("Goodbye!"));
    }


    /**
     * Bad: invalid menu options print a message and show the menu again.
     */
    public void testRunInvalidOption()
    {
        RidePortal portal = portalWithInput("9", "abc", "", "0");
        portal.run();
        assertEquals(3,
                occurrences(output(), "Invalid option. Please try again."));
        assertEquals(4, occurrences(output(), "===== Ride Portal Menu ====="));
    }


    /**
     * Normal: menu option 3 lists every vehicle.
     */
    public void testRunDisplayAll()
    {
        RidePortal portal = stockedPortal("3", "0");
        portal.run();
        assertTrue(output().contains(civic.toString()));
        assertTrue(output().contains(glide.toString()));
    }


    /**
     * Normal: menu option 2 removes a vehicle by id.
     */
    public void testRunRemove()
    {
        RidePortal portal = stockedPortal("2", "" + ninja.getId(), "0");
        portal.run();
        assertEquals(4, portal.getInventory().size());
        assertFalse(portal.getInventory().contains(ninja));
    }


    /**
     * Normal: menu option 5 updates a vehicle in place, and searches see the
     * change.
     */
    public void testRunUpdate()
    {
        RidePortal portal = stockedPortal("5", "" + civic.getId(), "2",
                "Accord", "0", "0");
        portal.run();
        assertEquals("Accord", civic.getName());
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(all, civic, model3, wrangler, ninja, glide);
        assertChain(portal.searchName(all, "Accord"), civic);
        assertChain(portal.searchName(all, "Civic"));
    }


    /**
     * Normal: menu option 1 with a bad vehicle type goes back to the menu.
     */
    public void testRunAddInvalidTypeReturnsToMenu()
    {
        RidePortal portal = portalWithInput("1", "5", "0");
        portal.run();
        assertTrue(output().contains("Invalid vehicle type."));
        assertEquals(2, occurrences(output(), "===== Ride Portal Menu ====="));
        assertTrue(portal.getInventory().isEmpty());
    }


    /**
     * displayMenu() prints every option.
     */
    public void testDisplayMenu()
    {
        portalWithInput().displayMenu();
        assertTrue(output().contains("1. Add a vehicle"));
        assertTrue(output().contains("2. Remove a vehicle"));
        assertTrue(output().contains("3. Display all vehicles"));
        assertTrue(output().contains("4. Search vehicles"));
        assertTrue(output().contains("5. Update a vehicle"));
        assertTrue(output().contains("6. Import vehicles from a file"));
        assertTrue(output().contains("0. Exit"));
    }

    // ~ addItem ...............................................................

    /**
     * A new portal starts with an empty inventory.
     */
    public void testNewPortalIsEmpty()
    {
        assertTrue(portalWithInput().getInventory().isEmpty());
    }


    /**
     * Normal: the user enters a car with valid data; the car is created and
     * added to the inventory.
     */
    public void testAddItemCar()
    {
        RidePortal portal = portalWithInput("1", "2022", "Civic", "Honda", "4",
                "5", "15000.5", "24000", "12.4", "gasoline", "sedan", "0", "2",
                "n");
        portal.addItem();

        assertEquals(1, portal.getInventory().size());
        Vehicle added = portal.getInventory().get(0);
        assertTrue(added instanceof Car);
        Car car = (Car)added;
        assertEquals(2022, car.getYear());
        assertEquals("Civic", car.getName());
        assertEquals("Honda", car.getMaker());
        assertEquals(4, car.getCylinders());
        assertEquals(5, car.getPax());
        assertEquals(15000.5, car.getMileage(), 0.001);
        assertEquals(24000.0, car.getPrice(), 0.001);
        assertEquals(12.4, car.getTankSize(), 0.001);
        assertEquals("Gasoline", car.getFuelType());
        assertEquals("Sedan", car.getCarType());
        assertEquals(Car.AUTOMATIC, car.getTransmission());
        assertEquals(2, car.getAutoPilot());
        assertTrue(output().contains("Added: " + car));
    }


    /**
     * Normal: the user enters a motorcycle with valid data.
     */
    public void testAddItemMotorcycle()
    {
        RidePortal portal = portalWithInput("0", "2021", "Ninja 400",
                "Kawasaki", "2", "2", "3200", "6500", "3.7", "Gasoline", "sport",
                "n");
        portal.addItem();

        assertEquals(1, portal.getInventory().size());
        Vehicle added = portal.getInventory().get(0);
        assertTrue(added instanceof Motorcycle);
        assertEquals("Ninja 400", added.getName());
        assertEquals("Sport", ((Motorcycle)added).getMotorcycleType());
    }


    /**
     * Bad: an invalid vehicle type adds nothing and returns.
     */
    public void testAddItemInvalidType()
    {
        RidePortal portal = portalWithInput("car");
        portal.addItem();
        assertTrue(portal.getInventory().isEmpty());
        assertTrue(output().contains(
                "Invalid vehicle type. Returning to the main menu."));
    }


    /**
     * Answering yes adds another vehicle; vehicles keep the order they were
     * entered. Answers are accepted in words and any case.
     */
    public void testAddItemAnother()
    {
        RidePortal portal = portalWithInput(
                "1", "2022", "Civic", "Honda", "4", "5", "15000", "24000", "12.4",
                "Gasoline", "Sedan", "0", "2", "maybe", "YES",
                "0", "2021", "Ninja 400", "Kawasaki", "2", "2", "3200", "6500",
                "3.7", "Gasoline", "Sport", "No");
        portal.addItem();

        assertEquals(2, portal.getInventory().size());
        assertTrue(portal.getInventory().get(0) instanceof Car);
        assertTrue(portal.getInventory().get(1) instanceof Motorcycle);
        assertEquals(1, occurrences(output(), "Please enter y or n."));
    }


    /**
     * Answering y and then giving a bad vehicle type keeps the first vehicle
     * and returns to the menu.
     */
    public void testAddItemAnotherThenInvalidType()
    {
        RidePortal portal = portalWithInput(
                "0", "2021", "Ninja 400", "Kawasaki", "2", "2", "3200", "6500",
                "3.7", "Gasoline", "Sport", "y",
                "bike");
        portal.addItem();
        assertEquals(1, portal.getInventory().size());
        assertTrue(output().contains("Invalid vehicle type."));
    }


    /**
     * Bad: every kind of bad input is rejected and re-prompted, and the car
     * is built from the first valid answer to each prompt.
     */
    public void testAddItemRepromptsBadInput()
    {
        RidePortal portal = portalWithInput("1",
                "hello", "2022.5", "1899", "2022", // year
                "", "Civic", // name
                "   ", "Honda", // maker
                "-4", "4", // cylinders
                "5.5", "0", "5", // pax
                "-1", "abc", "NaN", "15000.5", // mileage
                "-5", "24000", // price
                "-1", "12.4", // tank
                "Coal", "Gasoline", // fuel
                "Limo", "Sedan", // car type
                "2", "manual", "0", // transmission
                "-1", "2.5", "2", // autopilot
                "n");
        portal.addItem();

        assertEquals(1, portal.getInventory().size());
        Car car = (Car)portal.getInventory().get(0);
        assertEquals(2022, car.getYear());
        assertEquals("Civic", car.getName());
        assertEquals("Honda", car.getMaker());
        assertEquals(4, car.getCylinders());
        assertEquals(5, car.getPax());
        assertEquals(15000.5, car.getMileage(), 0.001);
        assertEquals(24000.0, car.getPrice(), 0.001);
        assertEquals(12.4, car.getTankSize(), 0.001);
        assertEquals("Gasoline", car.getFuelType());
        assertEquals("Sedan", car.getCarType());
        assertEquals(Car.AUTOMATIC, car.getTransmission());
        assertEquals(2, car.getAutoPilot());

        String out = output();
        assertEquals(4, occurrences(out, "Please enter a whole number."));
        assertEquals(1, occurrences(out, "Value must be at least 1900."));
        assertEquals(2, occurrences(out, "Input cannot be blank."));
        assertEquals(1, occurrences(out, "Value must be at least 1."));
        assertEquals(3, occurrences(out, "Value must be at least 0.0."));
        assertEquals(2, occurrences(out, "Please enter a number."));
        assertEquals(2, occurrences(out, "Invalid choice."));
        assertEquals(2, occurrences(out, "Please enter 0 or 1."));
    }


    /**
     * Bad: values above each limit are rejected and asked for again, and
     * values exactly at the limit are accepted.
     */
    public void testAddItemRejectsLargeValues()
    {
        RidePortal portal = portalWithInput("1",
                "3000", "2027", // year
                "Supra", "Toyota",
                "20", "16", // cylinders
                "40", "15", // pax
                "900000", "500000", // mileage
                "5000000", "1000000", // price
                "100", "60", // tank
                "Gasoline", "Coupe", "1",
                "9", "5", // autopilot
                "n");
        portal.addItem();

        Car car = (Car)portal.getInventory().get(0);
        assertEquals(Vehicle.MAX_YEAR, car.getYear());
        assertEquals(Vehicle.MAX_CYLINDERS, car.getCylinders());
        assertEquals(Vehicle.MAX_PAX, car.getPax());
        assertEquals(Vehicle.MAX_MILEAGE, car.getMileage(), 0.001);
        assertEquals(Vehicle.MAX_PRICE, car.getPrice(), 0.001);
        assertEquals(Vehicle.MAX_TANK, car.getTankSize(), 0.001);
        assertEquals(Car.MANUAL, car.getTransmission());
        assertEquals(Car.MAX_AUTOPILOT, car.getAutoPilot());
        assertEquals(7, occurrences(output(), "Value must be at most"));
    }

    // ~ removeItem ............................................................

    /**
     * Removing from an empty inventory just prints a message.
     */
    public void testRemoveItemEmpty()
    {
        RidePortal portal = portalWithInput();
        portal.removeItem();
        assertTrue(output().contains("No vehicles listed"));
    }


    /**
     * Normal: a known id removes exactly that vehicle.
     */
    public void testRemoveItemValid()
    {
        RidePortal portal = stockedPortal("" + wrangler.getId());
        portal.removeItem();
        assertChain(portal.getInventory(), civic, model3, ninja, glide);
        assertTrue(output().contains("Removed: " + wrangler));
    }


    /**
     * Removing the last vehicle, then adding, keeps the order right.
     */
    public void testRemoveItemLastThenAdd()
    {
        RidePortal portal = stockedPortal("" + glide.getId());
        portal.removeItem();
        portal.getInventory().add(glide);
        assertChain(portal.getInventory(), civic, model3, wrangler, ninja,
                glide);
    }


    /**
     * Bad: an unknown id prints "Vehicle not found".
     */
    public void testRemoveItemNotFound()
    {
        RidePortal portal = stockedPortal("" + (glide.getId() + 1000));
        portal.removeItem();
        assertEquals(5, portal.getInventory().size());
        assertTrue(output().contains("Vehicle not found"));
    }


    /**
     * Bad: an id that is not a number removes nothing.
     */
    public void testRemoveItemInvalidId()
    {
        RidePortal portal = stockedPortal("abc");
        portal.removeItem();
        assertEquals(5, portal.getInventory().size());
        assertTrue(output().contains("Invalid id."));
    }


    /**
     * Blank ids are re-prompted.
     */
    public void testRemoveItemBlankThenId()
    {
        RidePortal portal = stockedPortal("", "  ", "" + civic.getId());
        portal.removeItem();
        assertEquals(4, portal.getInventory().size());
        assertFalse(portal.getInventory().contains(civic));
        assertEquals(2, occurrences(output(), "Input cannot be blank."));
    }

    // ~ updateItem ............................................................

    /**
     * Updating with an empty inventory just prints a message.
     */
    public void testUpdateItemEmpty()
    {
        portalWithInput().updateItem();
        assertTrue(output().contains("No vehicles listed"));
        assertFalse(output().contains("Updating:"));
    }


    /**
     * Normal: every shared field can be changed, one after another. The
     * vehicle keeps its id and its place in the inventory.
     */
    public void testUpdateItemSharedFields()
    {
        int id = civic.getId();
        RidePortal portal = stockedPortal("" + id,
                "1", "2020",
                "2", "Accord",
                "3", "Acura",
                "4", "6",
                "5", "4",
                "6", "30000.5",
                "7", "18000",
                "8", "14.5",
                "9", "hybrid",
                "0");
        portal.updateItem();

        assertEquals(id, civic.getId());
        assertEquals(2020, civic.getYear());
        assertEquals("Accord", civic.getName());
        assertEquals("Acura", civic.getMaker());
        assertEquals(6, civic.getCylinders());
        assertEquals(4, civic.getPax());
        assertEquals(30000.5, civic.getMileage(), 0.001);
        assertEquals(18000.0, civic.getPrice(), 0.001);
        assertEquals(14.5, civic.getTankSize(), 0.001);
        assertEquals("Hybrid", civic.getFuelType());
        assertChain(portal.getInventory(), civic, model3, wrangler, ninja,
                glide);
        assertEquals(1, occurrences(output(), "Updating: "));
        assertEquals(9, occurrences(output(), "Updated: "));
    }


    /**
     * Normal: a car's own fields can be changed, and the car menu lists
     * them.
     */
    public void testUpdateItemCarFields()
    {
        RidePortal portal = stockedPortal("" + civic.getId(),
                "10", "coupe",
                "11", "1",
                "12", "4",
                "0");
        portal.updateItem();

        assertEquals("Coupe", civic.getCarType());
        assertEquals(Car.MANUAL, civic.getTransmission());
        assertEquals(4, civic.getAutoPilot());
        assertTrue(output().contains("12. Autopilot level"));
        assertFalse(output().contains("Motorcycle type"));
    }


    /**
     * A motorcycle's type can be changed. Car-only choices are not listed
     * for a motorcycle and are rejected if typed anyway.
     */
    public void testUpdateItemMotorcycleFields()
    {
        RidePortal portal = stockedPortal("" + ninja.getId(),
                "11", "12",
                "10", "cruiser",
                "0");
        portal.updateItem();

        assertEquals("Cruiser", ninja.getMotorcycleType());
        assertTrue(output().contains("10. Motorcycle type"));
        assertFalse(output().contains("11. Transmission"));
        assertEquals(2,
                occurrences(output(), "Invalid option. Please try again."));
    }


    /**
     * Bad: invalid field choices re-prompt, and bad new values are rejected
     * by the same prompt methods addItem uses.
     */
    public void testUpdateItemBadInput()
    {
        RidePortal portal = stockedPortal("" + civic.getId(),
                "99", "",
                "1", "1800", "abc", "2021",
                "2", "", "Integra",
                "0");
        portal.updateItem();

        assertEquals(2021, civic.getYear());
        assertEquals("Integra", civic.getName());
        String out = output();
        assertEquals(2, occurrences(out, "Invalid option. Please try again."));
        assertEquals(1, occurrences(out, "Value must be at least 1900."));
        assertEquals(1, occurrences(out, "Please enter a whole number."));
        assertEquals(1, occurrences(out, "Input cannot be blank."));
    }


    /**
     * Choosing Done right away changes nothing.
     */
    public void testUpdateItemDoneImmediately()
    {
        String before = civic.toString();
        RidePortal portal = stockedPortal("" + civic.getId(), "0");
        portal.updateItem();
        assertEquals(before, civic.toString());
        assertTrue(output().contains("Updating: " + before));
        assertFalse(output().contains("Updated: "));
    }


    /**
     * Bad: an unknown id prints "Vehicle not found".
     */
    public void testUpdateItemNotFound()
    {
        RidePortal portal = stockedPortal("" + (glide.getId() + 1000));
        portal.updateItem();
        assertTrue(output().contains("Vehicle not found"));
        assertFalse(output().contains("Updating:"));
    }


    /**
     * Bad: an id that is not a number updates nothing.
     */
    public void testUpdateItemInvalidId()
    {
        RidePortal portal = stockedPortal("abc");
        portal.updateItem();
        assertTrue(output().contains("Invalid id."));
        assertFalse(output().contains("Updating:"));
    }

    // ~ importItems / importVehicles ..........................................

    /**
     * Normal: car and motorcycle lines are imported in file order, and
     * comments and blank lines are ignored.
     */
    public void testImportVehiclesValid()
    {
        RidePortal portal = portalWithInput();
        int count = portal.importVehicles(fileLines(
                "# type,year,name,maker,cylinders,pax,mileage,price,tank,...",
                "",
                "Car,2022,Civic,Honda,4,5,15000.5,24000,12.4,Gasoline,Sedan,0,2",
                "Motorcycle,2021,Ninja,Kawasaki,2,2,3200,6500,3.7,Gasoline,Sport"));

        assertEquals(2, count);
        assertEquals(2, portal.getInventory().size());
        Car car = (Car)portal.getInventory().get(0);
        assertEquals(2022, car.getYear());
        assertEquals("Civic", car.getName());
        assertEquals("Honda", car.getMaker());
        assertEquals(4, car.getCylinders());
        assertEquals(5, car.getPax());
        assertEquals(15000.5, car.getMileage(), 0.001);
        assertEquals(24000.0, car.getPrice(), 0.001);
        assertEquals(12.4, car.getTankSize(), 0.001);
        assertEquals("Gasoline", car.getFuelType());
        assertEquals("Sedan", car.getCarType());
        assertEquals(Car.AUTOMATIC, car.getTransmission());
        assertEquals(2, car.getAutoPilot());
        Motorcycle bike = (Motorcycle)portal.getInventory().get(1);
        assertEquals("Ninja", bike.getName());
        assertEquals("Sport", bike.getMotorcycleType());
        assertTrue(output().contains(
                "Imported 2 vehicle(s), skipped 0 line(s)."));
    }


    /**
     * Spaces around values are ignored, and type names can be any case.
     */
    public void testImportVehiclesSpacesAndCase()
    {
        RidePortal portal = portalWithInput();
        portal.importVehicles(fileLines(
                " car , 2023 , Model 3 , Tesla , 0 , 5 , 8000 , 39000 , 0 , "
                        + "electric , sedan , 0 , 3 "));
        Car car = (Car)portal.getInventory().get(0);
        assertEquals("Model 3", car.getName());
        assertEquals("Electric", car.getFuelType());
        assertEquals("Sedan", car.getCarType());
    }


    /**
     * Bad: each bad line is skipped with a message saying why, and the good
     * lines are still imported.
     */
    public void testImportVehiclesBadLines()
    {
        RidePortal portal = portalWithInput();
        int count = portal.importVehicles(fileLines(
                "Boat,2020,Sea,Ray,4,8,10,20000,50,Gasoline,Sport",
                "Car,2022,Civic,Honda,4,5,15000,24000,12.4,Gasoline,Sedan,0",
                "Car,2022,Civic,Honda,four,5,15000,24000,12.4,Gasoline,Sedan,0,2",
                "Car,2022,Civic,Honda,4,5,NaN,24000,12.4,Gasoline,Sedan,0,2",
                "Car,2022,Civic,Honda,4,5,15000,-1,12.4,Gasoline,Sedan,0,2",
                "Car,2022,Civic,Honda,4,5,15000,24000,12.4,Coal,Sedan,0,2",
                "Car,2022,Civic,Honda,4,5,15000,24000,12.4,Gasoline,Sedan,2,2",
                "Motorcycle,1800,Old,Bike,1,1,0,0,1,Gasoline,Dirt",
                "Motorcycle,2021,,Kawasaki,2,2,3200,6500,3.7,Gasoline,Sport",
                "Motorcycle,2021,Ninja,Kawasaki,2,2,3200,6500,abc,Gasoline,Sport",
                "Motorcycle,2021,Ninja",
                "Motorcycle,2021,Ninja,Kawasaki,2,2,3200,6500,3.7,Gasoline,Sport"));

        assertEquals(1, count);
        assertEquals(1, portal.getInventory().size());
        String out = output();
        assertTrue(out.contains("Line 1 skipped: Type must be Car or "
                + "Motorcycle, not \"Boat\""));
        assertTrue(out.contains(
                "Line 2 skipped: A car needs 13 values, but this line has 12"));
        assertTrue(out.contains(
                "Line 3 skipped: Cylinders must be a whole number, not \"four\""));
        assertTrue(out.contains(
                "Line 4 skipped: Mileage must be a number, not \"NaN\""));
        assertTrue(out.contains("Line 5 skipped: Price cannot be negative"));
        assertTrue(out.contains("Line 6 skipped: Invalid fuel type: Coal"));
        assertTrue(out.contains("Line 7 skipped: Transmission must be 0"));
        assertTrue(out.contains("Line 8 skipped: Year must be between"));
        assertTrue(out.contains("Line 9 skipped: Name cannot be blank"));
        assertTrue(out.contains(
                "Line 10 skipped: Tank size must be a number, not \"abc\""));
        assertTrue(out.contains("Line 11 skipped: A motorcycle needs 11 "
                + "values, but this line has 3"));
        assertTrue(out.contains("Imported 1 vehicle(s), skipped 11 line(s)."));
    }


    /**
     * Bad: a line with a number above its limit is skipped instead of
     * capped. A line with every number exactly at its limit is accepted.
     */
    public void testImportVehiclesRejectsLargeValues()
    {
        RidePortal portal = portalWithInput();
        int count = portal.importVehicles(fileLines(
                "Car,3000,Supra,Toyota,6,4,1000,50000,15,Gasoline,Coupe,1,2",
                "Car,2020,Supra,Toyota,20,4,1000,50000,15,Gasoline,Coupe,1,2",
                "Car,2020,Supra,Toyota,6,40,1000,50000,15,Gasoline,Coupe,1,2",
                "Car,2020,Supra,Toyota,6,4,900000,50000,15,Gasoline,Coupe,1,2",
                "Car,2020,Supra,Toyota,6,4,1000,5000000,15,Gasoline,Coupe,1,2",
                "Car,2020,Supra,Toyota,6,4,1000,50000,100,Gasoline,Coupe,1,2",
                "Car,2020,Supra,Toyota,6,4,1000,50000,15,Gasoline,Coupe,1,9",
                "Car,2027,GR,Toyota,16,15,500000,1000000,60,Gasoline,Coupe,1,5"));

        assertEquals(1, count);
        String out = output();
        assertTrue(out.contains(
                "Line 1 skipped: Year must be at most 2027, not 3000"));
        assertTrue(out.contains(
                "Line 2 skipped: Cylinders must be at most 16, not 20"));
        assertTrue(out.contains(
                "Line 3 skipped: Passengers must be at most 15, not 40"));
        assertTrue(out.contains(
                "Line 4 skipped: Mileage must be at most 500000.0, not 900000"));
        assertTrue(out.contains(
                "Line 5 skipped: Price must be at most 1000000.0, not 5000000"));
        assertTrue(out.contains(
                "Line 6 skipped: Tank size must be at most 60.0, not 100"));
        assertTrue(out.contains(
                "Line 7 skipped: Autopilot level must be at most 5, not 9"));
        assertTrue(out.contains("Imported 1 vehicle(s), skipped 7 line(s)."));

        Car car = (Car)portal.getInventory().get(0);
        assertEquals(Vehicle.MAX_YEAR, car.getYear());
        assertEquals(Vehicle.MAX_CYLINDERS, car.getCylinders());
        assertEquals(Vehicle.MAX_PAX, car.getPax());
        assertEquals(Vehicle.MAX_MILEAGE, car.getMileage(), 0.001);
        assertEquals(Vehicle.MAX_PRICE, car.getPrice(), 0.001);
        assertEquals(Vehicle.MAX_TANK, car.getTankSize(), 0.001);
        assertEquals(Car.MAX_AUTOPILOT, car.getAutoPilot());
    }


    /**
     * An empty file imports nothing.
     */
    public void testImportVehiclesEmpty()
    {
        RidePortal portal = portalWithInput();
        assertEquals(0, portal.importVehicles(fileLines("")));
        assertTrue(portal.getInventory().isEmpty());
        assertTrue(output().contains(
                "Imported 0 vehicle(s), skipped 0 line(s)."));
    }


    /**
     * The invisible marker Excel puts at the start of some files is ignored.
     */
    public void testImportVehiclesExcelMarker()
    {
        RidePortal portal = portalWithInput();
        assertEquals(1, portal.importVehicles(fileLines(
                "﻿Car,2022,Civic,Honda,4,5,15000,24000,12.4,Gasoline,Sedan,"
                        + "0,2")));
    }


    /**
     * Normal: importItems() reads the file whose name the user types.
     *
     * @throws IOException
     *             if the test file cannot be written
     */
    public void testImportItemsFromFile()
            throws IOException
    {
        File file = tempFile(
                "Car,2019,Wrangler,Jeep,6,4,60000,28000,21.5,Gasoline,SUV,1,0",
                "Motorcycle,2022,Glide,Harley,2,2,12000,22000,6,Gasoline,Touring");
        RidePortal portal = stockedPortal(file.getPath());
        portal.importItems();
        assertEquals(7, portal.getInventory().size());
        assertEquals("Wrangler", portal.getInventory().get(5).getName());
        assertEquals("Glide", portal.getInventory().get(6).getName());
        assertTrue(output().contains(
                "Imported 2 vehicle(s), skipped 0 line(s)."));
    }


    /**
     * Bad: a file that does not exist imports nothing.
     */
    public void testImportItemsFileNotFound()
    {
        RidePortal portal = portalWithInput("no_such_file.csv");
        portal.importItems();
        assertTrue(portal.getInventory().isEmpty());
        assertTrue(output().contains("File not found: no_such_file.csv"));
    }


    /**
     * Normal: menu option 6 imports a file, and the vehicles can then be
     * listed.
     *
     * @throws IOException
     *             if the test file cannot be written
     */
    public void testRunImport()
            throws IOException
    {
        File file = tempFile(
                "Car,2022,Civic,Honda,4,5,15000,24000,12.4,Gasoline,Sedan,0,2");
        RidePortal portal = portalWithInput("6", file.getPath(), "3", "0");
        portal.run();
        assertEquals(1, portal.getInventory().size());
        assertTrue(output().contains(portal.getInventory().get(0).toString()));
    }

    // ~ displayAll ............................................................

    /**
     * An empty inventory prints "No vehicles listed".
     */
    public void testDisplayAllEmpty()
    {
        portalWithInput().displayAll();
        assertTrue(output().contains("No vehicles listed"));
    }


    /**
     * Every vehicle is printed, in the order it was added.
     */
    public void testDisplayAllInOrder()
    {
        stockedPortal().displayAll();
        String out = output();
        int civicAt = out.indexOf(civic.toString());
        int model3At = out.indexOf(model3.toString());
        int wranglerAt = out.indexOf(wrangler.toString());
        int ninjaAt = out.indexOf(ninja.toString());
        int glideAt = out.indexOf(glide.toString());
        assertTrue(civicAt >= 0);
        assertTrue(civicAt < model3At);
        assertTrue(model3At < wranglerAt);
        assertTrue(wranglerAt < ninjaAt);
        assertTrue(ninjaAt < glideAt);
    }

    // ~ searchResults .........................................................

    /**
     * Normal: matching vehicles exist and are returned and printed.
     */
    public void testSearchResultsMatch()
    {
        RidePortal portal = stockedPortal("3", "Honda", "0");
        assertChain(portal.searchResults(), civic);
        assertTrue(output().contains("1 vehicle(s) match so far."));
        assertTrue(output().contains("===== Search Results ====="));
        assertTrue(output().contains(civic.toString()));
    }


    /**
     * Bad: nothing matches, so an empty chain is returned and the user is
     * not asked for more filters.
     */
    public void testSearchResultsNoMatch()
    {
        RidePortal portal = stockedPortal("1", "Mustang");
        LinkedChain<Vehicle> result = portal.searchResults();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertTrue(output().contains("0 vehicle(s) match so far."));
        assertTrue(output().contains("No matching vehicles found"));
        assertEquals(1, occurrences(output(), "===== Search Filters ====="));
    }


    /**
     * Filters stack: each one narrows the previous result.
     */
    public void testSearchResultsStacked()
    {
        RidePortal portal = stockedPortal("9", "Gasoline", "7", "25000",
                "10", "Sedan", "0");
        assertChain(portal.searchResults(), civic);
        assertTrue(output().contains("4 vehicle(s) match so far."));
        assertTrue(output().contains("3 vehicle(s) match so far."));
        assertTrue(output().contains("1 vehicle(s) match so far."));
        assertChain(portal.getInventory(), civic, model3, wrangler, ninja,
                glide);
    }


    /**
     * Choosing Done right away returns every vehicle, in a new chain that is
     * not the inventory itself.
     */
    public void testSearchResultsDoneImmediately()
    {
        RidePortal portal = stockedPortal("0");
        LinkedChain<Vehicle> result = portal.searchResults();
        assertChain(result, civic, model3, wrangler, ninja, glide);
        assertNotSame(portal.getInventory(), result);
        result.clear();
        assertEquals(5, portal.getInventory().size());
    }


    /**
     * Searching an empty inventory returns an empty chain without showing
     * the filter menu.
     */
    public void testSearchResultsEmptyInventory()
    {
        LinkedChain<Vehicle> result = portalWithInput().searchResults();
        assertTrue(result.isEmpty());
        assertTrue(output().contains("No vehicles listed"));
        assertFalse(output().contains("===== Search Filters ====="));
    }

    // ~ searchBy ..............................................................

    /**
     * Choosing Done returns null.
     */
    public void testSearchByDone()
    {
        assertNull(stockedPortal("0").searchBy(civicOnly()));
    }


    /**
     * Bad: an invalid filter choice re-prompts.
     */
    public void testSearchByInvalidChoice()
    {
        RidePortal portal = stockedPortal("99", "abc", "0");
        assertNull(portal.searchBy(portal.getInventory()));
        assertEquals(2,
                occurrences(output(), "Invalid option. Please try again."));
    }


    /**
     * Normal: each general filter (1-9) runs the matching search.
     */
    public void testSearchByGeneralFilters()
    {
        RidePortal portal = stockedPortal(
                "1", "Civic",
                "2", "2022",
                "3", "Kawasaki",
                "4", "0",
                "5", "4",
                "6", "10000",
                "7", "7000",
                "8", "20",
                "9", "Electric");
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchBy(all), civic);
        assertChain(portal.searchBy(all), civic, glide);
        assertChain(portal.searchBy(all), ninja);
        assertChain(portal.searchBy(all), model3);
        assertChain(portal.searchBy(all), wrangler);
        assertChain(portal.searchBy(all), model3, ninja);
        assertChain(portal.searchBy(all), ninja);
        assertChain(portal.searchBy(all), wrangler);
        assertChain(portal.searchBy(all), model3);
    }


    /**
     * Normal: each car / motorcycle filter (10-13) runs the matching search.
     */
    public void testSearchBySpecificFilters()
    {
        RidePortal portal = stockedPortal(
                "10", "SUV",
                "11", "1",
                "12", "3",
                "13", "Touring");
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchBy(all), wrangler);
        assertChain(portal.searchBy(all), wrangler);
        assertChain(portal.searchBy(all), model3);
        assertChain(portal.searchBy(all), glide);
    }


    /**
     * Bad: a year like 2022.5 is rejected (NumberFormatException is caught)
     * and the user is asked again.
     */
    public void testSearchByYearDecimal()
    {
        RidePortal portal = stockedPortal("2", "2022.5", "2022");
        assertChain(portal.searchBy(portal.getInventory()), civic, glide);
        assertTrue(output().contains("Please enter a whole number."));
    }


    /**
     * Bad: a search value above the limit is asked for again instead of
     * being capped (searching for year 2050 used to search for 2027).
     */
    public void testSearchByTooLargeValue()
    {
        RidePortal portal =
                stockedPortal("2", "2050", "2022", "5", "40", "4");
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchBy(all), civic, glide);
        assertChain(portal.searchBy(all), wrangler);
        assertTrue(output().contains("Value must be at most 2027."));
        assertTrue(output().contains("Value must be at most 15."));
    }


    /**
     * Bad: a filter with no matches returns an empty chain, not null.
     */
    public void testSearchByEmptyResult()
    {
        RidePortal portal = stockedPortal("1", "Mustang");
        LinkedChain<Vehicle> result = portal.searchBy(portal.getInventory());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ~ General search methods ................................................

    /**
     * searchName(): case-insensitive match; unknown names give an empty
     * chain.
     */
    public void testSearchName()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchName(all, "civic"), civic);
        assertChain(portal.searchName(all, " Model 3 "), model3);
        assertChain(portal.searchName(all, "Mustang"));
        assertChain(portal.searchName(all, null));
        assertChain(portal.searchName(null, "Civic"));
    }


    /**
     * searchYear(): exact year match.
     */
    public void testSearchYear()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchYear(all, 2022), civic, glide);
        assertChain(portal.searchYear(all, 1999));
        assertChain(portal.searchYear(null, 2022));
    }


    /**
     * searchMaker(): case-insensitive match; unknown makers give an empty
     * chain.
     */
    public void testSearchMaker()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchMaker(all, "honda"), civic);
        assertChain(portal.searchMaker(all, "Ferrari"));
        assertChain(portal.searchMaker(all, null));
        assertChain(portal.searchMaker(null, "Honda"));
    }


    /**
     * searchCylinder(): exact cylinder count.
     */
    public void testSearchCylinder()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchCylinder(all, 2), ninja, glide);
        assertChain(portal.searchCylinder(all, 12));
        assertChain(portal.searchCylinder(null, 2));
    }


    /**
     * searchPax(): exact seat count.
     */
    public void testSearchPax()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchPax(all, 5), civic, model3);
        assertChain(portal.searchPax(all, 9));
        assertChain(portal.searchPax(null, 5));
    }


    /**
     * searchMileage(): keeps mileage at or below the limit.
     */
    public void testSearchMileage()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchMileage(all, 12000), model3, ninja, glide);
        assertChain(portal.searchMileage(all, 100));
        assertChain(portal.searchMileage(null, 12000));
    }


    /**
     * searchPrice(): keeps prices at or below the limit.
     */
    public void testSearchPrice()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchPrice(all, 24000), civic, ninja, glide);
        assertChain(portal.searchPrice(all, 1000));
        assertChain(portal.searchPrice(null, 24000));
    }


    /**
     * searchTank(): keeps tank sizes at or above the minimum.
     */
    public void testSearchTank()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchTank(all, 12.4), civic, wrangler);
        assertChain(portal.searchTank(all, 50));
        assertChain(portal.searchTank(null, 12.4));
    }


    /**
     * searchFuel(): case-insensitive fuel type match.
     */
    public void testSearchFuel()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchFuel(all, "electric"), model3);
        assertChain(portal.searchFuel(all, "Diesel"));
        assertChain(portal.searchFuel(all, null));
        assertChain(portal.searchFuel(null, "Electric"));
    }


    /**
     * Searching never changes the chain being searched.
     */
    public void testSearchDoesNotModifySource()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        LinkedChain<Vehicle> result = portal.searchFuel(all, "Gasoline");
        result.clear();
        portal.searchName(all, "Civic");
        portal.searchCarType(all, "Sedan");
        assertChain(all, civic, model3, wrangler, ninja, glide);
    }

    // ~ Car / Motorcycle specific search methods ..............................

    /**
     * searchCarType(): only matching cars remain; motorcycles are filtered
     * out.
     */
    public void testSearchCarType()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchCarType(all, "sedan"), civic, model3);
        assertChain(portal.searchCarType(all, "Van"));
        assertChain(portal.searchCarType(all, null));
        assertChain(portal.searchCarType(null, "Sedan"));
    }


    /**
     * searchTrans(): only cars with that transmission remain.
     */
    public void testSearchTrans()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchTrans(all, Car.AUTOMATIC), civic, model3);
        assertChain(portal.searchTrans(all, Car.MANUAL), wrangler);
        assertChain(portal.searchTrans(bikesOnly(), Car.AUTOMATIC));
        assertChain(portal.searchTrans(null, Car.AUTOMATIC));
    }


    /**
     * searchAuto(): only cars with exactly that autopilot level remain.
     */
    public void testSearchAuto()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchAuto(all, 2), civic);
        assertChain(portal.searchAuto(all, 0), wrangler);
        assertChain(portal.searchAuto(all, 5));
        assertChain(portal.searchAuto(null, 2));
    }


    /**
     * searchMotorcycleType(): only matching motorcycles remain; cars are
     * filtered out.
     */
    public void testSearchMotorcycleType()
    {
        RidePortal portal = stockedPortal();
        LinkedChain<Vehicle> all = portal.getInventory();
        assertChain(portal.searchMotorcycleType(all, "sport"), ninja);
        assertChain(portal.searchMotorcycleType(all, "Dirt"));
        assertChain(portal.searchMotorcycleType(all, null));
        assertChain(portal.searchMotorcycleType(null, "Sport"));
    }

    // ~ Prompt methods ........................................................

    /**
     * promptYear() Normal: a valid year is returned.
     */
    public void testPromptYear()
    {
        assertEquals(2022, portalWithInput("2022").promptYear());
    }


    /**
     * promptYear() Bad: text, decimals, blanks and old years re-prompt.
     */
    public void testPromptYearBadInput()
    {
        RidePortal portal =
                portalWithInput("hello", "2022.5", "", "1899", "2022");
        assertEquals(2022, portal.promptYear());
        assertEquals(2, occurrences(output(), "Please enter a whole number."));
        assertEquals(1, occurrences(output(), "Input cannot be blank."));
        assertEquals(1, occurrences(output(), "Value must be at least 1900."));
    }


    /**
     * promptYear() Bad: years after MAX_YEAR are rejected, not capped.
     */
    public void testPromptYearTooNew()
    {
        assertEquals(2022, portalWithInput("2050", "2022").promptYear());
        assertTrue(output().contains("Value must be at most 2027."));
    }


    /**
     * promptName() Normal: the trimmed name is returned.
     */
    public void testPromptName()
    {
        assertEquals("Civic", portalWithInput(" Civic ").promptName());
    }


    /**
     * promptName() Bad: empty input is rejected.
     */
    public void testPromptNameEmpty()
    {
        assertEquals("Civic",
                portalWithInput("", "   ", "Civic").promptName());
        assertEquals(2, occurrences(output(), "Input cannot be blank."));
    }


    /**
     * promptMaker() Normal and Bad (empty input is rejected).
     */
    public void testPromptMaker()
    {
        assertEquals("Honda", portalWithInput("", "Honda").promptMaker());
        assertEquals(1, occurrences(output(), "Input cannot be blank."));
    }


    /**
     * promptCylinder() Normal: a non-negative count is returned.
     */
    public void testPromptCylinder()
    {
        assertEquals(4, portalWithInput("4").promptCylinder());
        assertEquals(0, portalWithInput("0").promptCylinder());
    }


    /**
     * promptCylinder() Bad: negatives, text and values above MAX_CYLINDERS
     * are rejected.
     */
    public void testPromptCylinderBadInput()
    {
        assertEquals(4,
                portalWithInput("-4", "four", "4").promptCylinder());
        assertTrue(output().contains("Value must be at least 0."));
        assertTrue(output().contains("Please enter a whole number."));
        assertEquals(8, portalWithInput("20", "8").promptCylinder());
        assertTrue(output().contains("Value must be at most 16."));
    }


    /**
     * promptPax() Normal: a valid passenger count is returned.
     */
    public void testPromptPax()
    {
        assertEquals(5, portalWithInput("5").promptPax());
    }


    /**
     * promptPax() Bad: 5.5 (NumberFormatException), 0 and values above
     * MAX_PAX are rejected.
     */
    public void testPromptPaxBadInput()
    {
        assertEquals(5, portalWithInput("5.5", "0", "5").promptPax());
        assertTrue(output().contains("Please enter a whole number."));
        assertTrue(output().contains("Value must be at least 1."));
        assertEquals(5, portalWithInput("40", "5").promptPax());
        assertTrue(output().contains("Value must be at most 15."));
    }


    /**
     * promptMileage() Normal: a mileage under the limit is returned.
     */
    public void testPromptMileage()
    {
        assertEquals(45000.0, portalWithInput("45000").promptMileage(), 0.001);
    }


    /**
     * promptMileage() Bad: a mileage over MAX_MILEAGE is rejected, not
     * capped.
     */
    public void testPromptMileageTooHigh()
    {
        assertEquals(45000.0,
                portalWithInput("900000", "45000").promptMileage(), 0.001);
        assertTrue(output().contains("Value must be at most 500000.0."));
    }


    /**
     * promptMileage() Bad: negatives, text and NaN are rejected.
     */
    public void testPromptMileageBadInput()
    {
        assertEquals(100.5,
                portalWithInput("-1", "abc", "NaN", "100.5").promptMileage(),
                0.001);
        assertEquals(2, occurrences(output(), "Please enter a number."));
        assertTrue(output().contains("Value must be at least 0.0."));
    }


    /**
     * promptPrice() Normal: a positive price is returned.
     */
    public void testPromptPrice()
    {
        assertEquals(25000.0, portalWithInput("25000").promptPrice(), 0.001);
    }


    /**
     * promptPrice() Bad: negatives and values above MAX_PRICE are rejected.
     */
    public void testPromptPriceBadInput()
    {
        assertEquals(25000.0,
                portalWithInput("-5", "25000").promptPrice(), 0.001);
        assertTrue(output().contains("Value must be at least 0.0."));
        assertEquals(30000.0,
                portalWithInput("5000000", "30000").promptPrice(), 0.001);
        assertTrue(output().contains("Value must be at most 1000000.0."));
    }


    /**
     * promptTank() Normal: a decimal tank size like 15.5 is returned.
     */
    public void testPromptTank()
    {
        assertEquals(15.5, portalWithInput("15.5").promptTank(), 0.001);
    }


    /**
     * promptTank() Bad: negatives and values above MAX_TANK are rejected.
     */
    public void testPromptTankBadInput()
    {
        assertEquals(17.5, portalWithInput("-1", "17.5").promptTank(), 0.001);
        assertTrue(output().contains("Value must be at least 0.0."));
        assertEquals(15.5, portalWithInput("100", "15.5").promptTank(), 0.001);
        assertTrue(output().contains("Value must be at most 60.0."));
    }


    /**
     * promptFuel() Normal: a valid fuel type is returned in canonical form.
     */
    public void testPromptFuel()
    {
        assertEquals("Diesel", portalWithInput("diesel").promptFuel());
    }


    /**
     * promptFuel() Bad: an invalid fuel type is rejected.
     */
    public void testPromptFuelInvalid()
    {
        assertEquals("Electric",
                portalWithInput("Coal", "Electric").promptFuel());
        assertTrue(output().contains(
                "Invalid choice. Options are: Gasoline, Diesel, Electric, Hybrid"));
    }


    /**
     * promptCarType() Normal and Bad (invalid type is rejected).
     */
    public void testPromptCarType()
    {
        assertEquals("SUV", portalWithInput("suv").promptCarType());
        assertEquals("Van", portalWithInput("Limo", "Van").promptCarType());
        assertEquals(1, occurrences(output(), "Invalid choice."));
    }


    /**
     * promptMotorcycleType() Normal and Bad (invalid type is rejected).
     */
    public void testPromptMotorcycleType()
    {
        assertEquals("Cruiser",
                portalWithInput("cruiser").promptMotorcycleType());
        assertEquals("Dirt",
                portalWithInput("Scooter", "Dirt").promptMotorcycleType());
        assertEquals(1, occurrences(output(), "Invalid choice."));
    }


    /**
     * promptTrans() Normal: 0 and 1 are accepted.
     */
    public void testPromptTrans()
    {
        assertEquals(Car.MANUAL, portalWithInput("1").promptTrans());
        assertEquals(Car.AUTOMATIC, portalWithInput("0").promptTrans());
    }


    /**
     * promptTrans() Bad: anything but 0 or 1 is rejected.
     */
    public void testPromptTransInvalid()
    {
        assertEquals(Car.AUTOMATIC,
                portalWithInput("2", "manual", "0").promptTrans());
        assertEquals(2, occurrences(output(), "Please enter 0 or 1."));
    }


    /**
     * promptAuto() Normal: a level from 0-5 is returned.
     */
    public void testPromptAuto()
    {
        assertEquals(3, portalWithInput("3").promptAuto());
    }


    /**
     * promptAuto() Bad: negatives, text and levels above 5 are rejected.
     */
    public void testPromptAutoBadInput()
    {
        assertEquals(4, portalWithInput("-1", "x", "4").promptAuto());
        assertTrue(output().contains("Value must be at least 0."));
        assertTrue(output().contains("Please enter a whole number."));
        assertEquals(5, portalWithInput("8", "5").promptAuto());
        assertTrue(output().contains("Value must be at most 5."));
    }

    // ~ Private fixtures ......................................................

    /**
     * @return a chain holding only civic
     */
    private LinkedChain<Vehicle> civicOnly()
    {
        LinkedChain<Vehicle> chain = new LinkedChain<Vehicle>();
        chain.add(civic);
        return chain;
    }


    /**
     * @return a chain holding only the motorcycles
     */
    private LinkedChain<Vehicle> bikesOnly()
    {
        LinkedChain<Vehicle> chain = new LinkedChain<Vehicle>();
        chain.add(ninja);
        chain.add(glide);
        return chain;
    }
}
