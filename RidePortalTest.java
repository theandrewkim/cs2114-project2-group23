package rentals;

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
     * Bad: values above each limit are capped to the limit.
     */
    public void testAddItemCapsLargeValues()
    {
        RidePortal portal = portalWithInput("1", "3000", "Supra", "Toyota",
                "20", "40", "900000", "5000000", "100", "Gasoline", "Coupe", "1",
                "9", "n");
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
        assertEquals(7, occurrences(output(), "Value above limit; capped at"));
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
     * promptYear(): years after MAX_YEAR are capped.
     */
    public void testPromptYearCapped()
    {
        assertEquals(Vehicle.MAX_YEAR, portalWithInput("2050").promptYear());
        assertTrue(output().contains("Value above limit; capped at 2027."));
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
     * promptCylinder() Bad: negatives and text are rejected; large values
     * are capped.
     */
    public void testPromptCylinderBadInput()
    {
        assertEquals(4,
                portalWithInput("-4", "four", "4").promptCylinder());
        assertTrue(output().contains("Value must be at least 0."));
        assertTrue(output().contains("Please enter a whole number."));
        assertEquals(Vehicle.MAX_CYLINDERS,
                portalWithInput("20").promptCylinder());
    }


    /**
     * promptPax() Normal: a valid passenger count is returned.
     */
    public void testPromptPax()
    {
        assertEquals(5, portalWithInput("5").promptPax());
    }


    /**
     * promptPax() Bad: 5.5 (NumberFormatException) and 0 are rejected; large
     * values are capped.
     */
    public void testPromptPaxBadInput()
    {
        assertEquals(5, portalWithInput("5.5", "0", "5").promptPax());
        assertTrue(output().contains("Please enter a whole number."));
        assertTrue(output().contains("Value must be at least 1."));
        assertEquals(Vehicle.MAX_PAX, portalWithInput("40").promptPax());
    }


    /**
     * promptMileage() Normal: a mileage under the limit is returned.
     */
    public void testPromptMileage()
    {
        assertEquals(45000.0, portalWithInput("45000").promptMileage(), 0.001);
    }


    /**
     * promptMileage() Bad: a mileage over MAX_MILEAGE is capped.
     */
    public void testPromptMileageCapped()
    {
        assertEquals(Vehicle.MAX_MILEAGE,
                portalWithInput("900000").promptMileage(), 0.001);
        assertTrue(output().contains("capped at 500000.0"));
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
     * promptPrice() Bad: negatives are rejected; large values are capped.
     */
    public void testPromptPriceBadInput()
    {
        assertEquals(25000.0,
                portalWithInput("-5", "25000").promptPrice(), 0.001);
        assertTrue(output().contains("Value must be at least 0.0."));
        assertEquals(Vehicle.MAX_PRICE,
                portalWithInput("5000000").promptPrice(), 0.001);
    }


    /**
     * promptTank() Normal: a decimal tank size like 15.5 is returned.
     */
    public void testPromptTank()
    {
        assertEquals(15.5, portalWithInput("15.5").promptTank(), 0.001);
    }


    /**
     * promptTank() Bad: negatives are rejected; large values are capped.
     */
    public void testPromptTankBadInput()
    {
        assertEquals(17.5, portalWithInput("-1", "17.5").promptTank(), 0.001);
        assertTrue(output().contains("Value must be at least 0.0."));
        assertEquals(Vehicle.MAX_TANK,
                portalWithInput("100").promptTank(), 0.001);
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
     * promptAuto() Bad: negatives and text are rejected; levels above 5 are
     * capped.
     */
    public void testPromptAutoBadInput()
    {
        assertEquals(4, portalWithInput("-1", "x", "4").promptAuto());
        assertTrue(output().contains("Value must be at least 0."));
        assertTrue(output().contains("Please enter a whole number."));
        assertEquals(Car.MAX_AUTOPILOT, portalWithInput("8").promptAuto());
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
