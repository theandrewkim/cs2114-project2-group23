package rentals;

import java.util.Scanner;

/**
 * Console front end for Ride Match. Users add cars and motorcycles to one
 * inventory, remove them by id, list them, and stack search filters to find
 * the right rental. All vehicles live in one LinkedChain; the search methods
 * never modify it and always return a new chain of matches.
 *
 * @author Andrew Kim, Nikitha Nivas, Alper Sheker
 * @version Oct 14, 2026
 */
public class RidePortal
{
    // ~ Fields ................................................................

    private LinkedChain<Vehicle> inventory;
    private Scanner scanner;

    // ~ Constructors ..........................................................

    /**
     * Creates a portal with an empty inventory that reads from System.in.
     */
    public RidePortal()
    {
        inventory = new LinkedChain<Vehicle>();
        scanner = new Scanner(System.in);
    }

    // ~ Public Methods ........................................................

    /**
     * Program entry point. Creates a RidePortal and runs the main menu.
     *
     * @param args
     *            not used
     */
    public static void main(String[] args)
    {
        System.out.println("Welcome to Ride Portal! "
                + "Enter available options of rental vehicles.");
        System.out.println("We will help you find the right option for you.");
        RidePortal portal = new RidePortal();
        portal.run();
    }


    /**
     * @return the chain holding every vehicle in this portal
     */
    public LinkedChain<Vehicle> getInventory()
    {
        return inventory;
    }


    /**
     * Main menu loop: shows the menu, reads a choice and runs it, until the
     * user chooses 0. An invalid option prints a message and shows the menu
     * again.
     */
    public void run()
    {
        boolean running = true;
        while (running)
        {
            displayMenu();
            String choice = scanner.nextLine().trim();
            switch (choice)
            {
                case "1":
                    addItem();
                    break;
                case "2":
                    removeItem();
                    break;
                case "3":
                    displayAll();
                    break;
                case "4":
                    searchResults();
                    break;
                case "0":
                    System.out.println(
                            "Thank you for using Ride Portal. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    /**
     * Prints the numbered main-menu options.
     */
    public void displayMenu()
    {
        System.out.println();
        System.out.println("===== Ride Portal Menu =====");
        System.out.println("1. Add a vehicle");
        System.out.println("2. Remove a vehicle");
        System.out.println("3. Display all vehicles");
        System.out.println("4. Search vehicles");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }


    /**
     * Asks whether the user is entering a car or a motorcycle, prompts for
     * every field, builds the vehicle and adds it to the inventory. Repeats
     * while the user wants to add another. An invalid vehicle type returns to
     * the main menu.
     */
    public void addItem()
    {
        boolean adding = true;
        while (adding)
        {
            System.out.println("Are you entering a motorcycle or car?");
            System.out.print("Enter 0 for motorcycle and 1 for car: ");
            String type = scanner.nextLine().trim();
            if (!type.equals("0") && !type.equals("1"))
            {
                System.out.println(
                        "Invalid vehicle type. Returning to the main menu.");
                return;
            }

            int year = promptYear();
            String name = promptName();
            String maker = promptMaker();
            int cylinders = promptCylinder();
            int pax = promptPax();
            double mileage = promptMileage();
            double price = promptPrice();
            double tankSize = promptTank();
            String fuelType = promptFuel();

            Vehicle vehicle;
            if (type.equals("0"))
            {
                String motorcycleType = promptMotorcycleType();
                vehicle = new Motorcycle(year, name, maker, cylinders, pax,
                        mileage, price, tankSize, fuelType, motorcycleType);
            }
            else
            {
                String carType = promptCarType();
                int transmission = promptTrans();
                int autoPilot = promptAuto();
                vehicle = new Car(year, name, maker, cylinders, pax, mileage,
                        price, tankSize, fuelType, carType, transmission,
                        autoPilot);
            }
            inventory.add(vehicle);
            System.out.println("Added: " + vehicle);
            adding = readYesNo("Add another vehicle?");
        }
    }


    /**
     * Prompts for a vehicle id and removes that vehicle from the inventory.
     * An unknown id prints "Vehicle not found".
     */
    public void removeItem()
    {
        if (inventory.isEmpty())
        {
            System.out.println("No vehicles listed");
            return;
        }
        String input = readNonEmpty("Enter the id of the vehicle to remove: ");
        int id;
        try
        {
            id = Integer.parseInt(input);
        }
        catch (NumberFormatException e)
        {
            System.out.println("Invalid id. Returning to the main menu.");
            return;
        }

        Vehicle match = null;
        for (Vehicle vehicle : inventory)
        {
            if (vehicle.getId() == id)
            {
                match = vehicle;
            }
        }
        if (match == null)
        {
            System.out.println("Vehicle not found");
            return;
        }
        inventory.remove(match);
        System.out.println("Removed: " + match);
    }


    /**
     * Prints every vehicle in the inventory, or "No vehicles listed" if it is
     * empty.
     */
    public void displayAll()
    {
        if (inventory.isEmpty())
        {
            System.out.println("No vehicles listed");
            return;
        }
        for (Vehicle vehicle : inventory)
        {
            System.out.println(vehicle);
        }
    }


    /**
     * Starts from the whole inventory and lets the user stack filters with
     * searchBy() until they choose "Done" (or nothing is left to filter).
     * Prints and returns the final matches. The inventory itself is never
     * changed.
     *
     * @return a new chain of the matching vehicles; empty if nothing matches
     */
    public LinkedChain<Vehicle> searchResults()
    {
        LinkedChain<Vehicle> results = new LinkedChain<Vehicle>();
        if (inventory.isEmpty())
        {
            System.out.println("No vehicles listed");
            return results;
        }
        for (Vehicle vehicle : inventory)
        {
            results.add(vehicle);
        }

        LinkedChain<Vehicle> filtered = searchBy(results);
        while (filtered != null)
        {
            results = filtered;
            System.out.println(results.size() + " vehicle(s) match so far.");
            filtered = results.isEmpty() ? null : searchBy(results);
        }

        System.out.println("===== Search Results =====");
        if (results.isEmpty())
        {
            System.out.println("No matching vehicles found");
        }
        for (Vehicle vehicle : results)
        {
            System.out.println(vehicle);
        }
        return results;
    }


    /**
     * Shows the filter menu, reads a filter choice, prompts for its value and
     * applies the matching search method to source. An invalid choice
     * re-prompts.
     *
     * @param source
     *            the vehicles to filter; not modified
     * @return a new chain of matches, or null if the user chose "Done"
     */
    public LinkedChain<Vehicle> searchBy(LinkedChain<Vehicle> source)
    {
        while (true)
        {
            System.out.println();
            System.out.println("===== Search Filters =====");
            System.out.println("1. Name");
            System.out.println("2. Year");
            System.out.println("3. Maker");
            System.out.println("4. Cylinders");
            System.out.println("5. Passengers");
            System.out.println("6. Max mileage");
            System.out.println("7. Max price");
            System.out.println("8. Min tank size");
            System.out.println("9. Fuel type");
            System.out.println("10. Car type");
            System.out.println("11. Transmission");
            System.out.println("12. Autopilot level");
            System.out.println("13. Motorcycle type");
            System.out.println("0. Done");
            System.out.print("Choose a filter: ");
            String choice = scanner.nextLine().trim();

            switch (choice)
            {
                case "0":
                    return null;
                case "1":
                    return searchName(source, promptName());
                case "2":
                    return searchYear(source, promptYear());
                case "3":
                    return searchMaker(source, promptMaker());
                case "4":
                    return searchCylinder(source, promptCylinder());
                case "5":
                    return searchPax(source, promptPax());
                case "6":
                    System.out.println(
                            "Vehicles at or below this mileage will be kept.");
                    return searchMileage(source, promptMileage());
                case "7":
                    System.out.println(
                            "Vehicles at or below this price will be kept.");
                    return searchPrice(source, promptPrice());
                case "8":
                    System.out.println(
                            "Vehicles with at least this tank size will be kept.");
                    return searchTank(source, promptTank());
                case "9":
                    return searchFuel(source, promptFuel());
                case "10":
                    return searchCarType(source, promptCarType());
                case "11":
                    return searchTrans(source, promptTrans());
                case "12":
                    return searchAuto(source, promptAuto());
                case "13":
                    return searchMotorcycleType(source, promptMotorcycleType());
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /*
     * General search methods. Each returns a new chain of the vehicles in
     * source that match, in the same order; source is never modified. A null
     * source or value gives an empty chain.
     */

    /**
     * @param source
     *            the vehicles to filter
     * @param name
     *            the model name to match (case-insensitive)
     * @return a new chain of vehicles whose name equals name
     */
    public LinkedChain<Vehicle> searchName(
            LinkedChain<Vehicle> source,
            String name)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || name == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (name.trim().equalsIgnoreCase(vehicle.getName()))
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param year
     *            the model year to match
     * @return a new chain of vehicles whose year equals year
     */
    public LinkedChain<Vehicle> searchYear(
            LinkedChain<Vehicle> source,
            int year)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle.getYear() == year)
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param maker
     *            the manufacturer to match (case-insensitive)
     * @return a new chain of vehicles whose maker equals maker
     */
    public LinkedChain<Vehicle> searchMaker(
            LinkedChain<Vehicle> source,
            String maker)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || maker == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (maker.trim().equalsIgnoreCase(vehicle.getMaker()))
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param cylinders
     *            the exact number of cylinders to match
     * @return a new chain of vehicles with that many cylinders
     */
    public LinkedChain<Vehicle> searchCylinder(
            LinkedChain<Vehicle> source,
            int cylinders)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle.getCylinders() == cylinders)
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param pax
     *            the exact number of passengers to match
     * @return a new chain of vehicles that seat exactly pax passengers
     */
    public LinkedChain<Vehicle> searchPax(LinkedChain<Vehicle> source, int pax)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle.getPax() == pax)
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param maxMileage
     *            the highest mileage to keep
     * @return a new chain of vehicles with mileage at or below maxMileage
     */
    public LinkedChain<Vehicle> searchMileage(
            LinkedChain<Vehicle> source,
            double maxMileage)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle.getMileage() <= maxMileage)
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param maxPrice
     *            the highest price to keep
     * @return a new chain of vehicles priced at or below maxPrice
     */
    public LinkedChain<Vehicle> searchPrice(
            LinkedChain<Vehicle> source,
            double maxPrice)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle.getPrice() <= maxPrice)
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param minTank
     *            the smallest tank size to keep, in gallons
     * @return a new chain of vehicles with tank size at least minTank
     */
    public LinkedChain<Vehicle> searchTank(
            LinkedChain<Vehicle> source,
            double minTank)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle.getTankSize() >= minTank)
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param fuelType
     *            the fuel type to match (case-insensitive)
     * @return a new chain of vehicles that use fuelType
     */
    public LinkedChain<Vehicle> searchFuel(
            LinkedChain<Vehicle> source,
            String fuelType)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || fuelType == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (fuelType.trim().equalsIgnoreCase(vehicle.getFuelType()))
            {
                result.add(vehicle);
            }
        }
        return result;
    }

    /*
     * Car / Motorcycle specific search methods. Vehicles of the other kind
     * are always filtered out.
     */

    /**
     * @param source
     *            the vehicles to filter
     * @param carType
     *            the car type to match (case-insensitive)
     * @return a new chain of cars whose type equals carType
     */
    public LinkedChain<Vehicle> searchCarType(
            LinkedChain<Vehicle> source,
            String carType)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || carType == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle instanceof Car
                    && carType.trim().equalsIgnoreCase(
                    ((Car)vehicle).getCarType()))
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param transmission
     *            Car.AUTOMATIC (0) or Car.MANUAL (1)
     * @return a new chain of cars with that transmission
     */
    public LinkedChain<Vehicle> searchTrans(
            LinkedChain<Vehicle> source,
            int transmission)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle instanceof Car
                    && ((Car)vehicle).getTransmission() == transmission)
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param autoPilot
     *            the autopilot level to match, 0 to Car.MAX_AUTOPILOT
     * @return a new chain of cars with exactly that autopilot level
     */
    public LinkedChain<Vehicle> searchAuto(
            LinkedChain<Vehicle> source,
            int autoPilot)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle instanceof Car
                    && ((Car)vehicle).getAutoPilot() == autoPilot)
            {
                result.add(vehicle);
            }
        }
        return result;
    }


    /**
     * @param source
     *            the vehicles to filter
     * @param type
     *            the motorcycle type to match (case-insensitive)
     * @return a new chain of motorcycles whose type equals type
     */
    public LinkedChain<Vehicle> searchMotorcycleType(
            LinkedChain<Vehicle> source,
            String type)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || type == null)
        {
            return result;
        }
        for (Vehicle vehicle : source)
        {
            if (vehicle instanceof Motorcycle
                    && type.trim().equalsIgnoreCase(
                    ((Motorcycle)vehicle).getMotorcycleType()))
            {
                result.add(vehicle);
            }
        }
        return result;
    }

    /*
     * Prompt methods, used by addItem and searchBy. Each one keeps asking
     * until it gets a usable value. Numbers below the minimum are re-prompted;
     * numbers above the maximum are capped to the maximum.
     */

    /**
     * Reads a whole-number year. Text or decimals (e.g. 2022.5) and years
     * below MIN_YEAR re-prompt; years above MAX_YEAR are capped.
     *
     * @return the year entered
     */
    public int promptYear()
    {
        return readInt("Enter the model year (" + Vehicle.MIN_YEAR + "-"
                + Vehicle.MAX_YEAR + "): ", Vehicle.MIN_YEAR, Vehicle.MAX_YEAR);
    }


    /**
     * Reads a model name. Blank input re-prompts.
     *
     * @return the name entered, trimmed
     */
    public String promptName()
    {
        return readNonEmpty("Enter the model name (e.g. Civic): ");
    }


    /**
     * Reads a manufacturer. Blank input re-prompts.
     *
     * @return the maker entered, trimmed
     */
    public String promptMaker()
    {
        return readNonEmpty("Enter the manufacturer (e.g. Honda): ");
    }


    /**
     * Reads a cylinder count (0 for electric). Negative or non-integer input
     * re-prompts; values above MAX_CYLINDERS are capped.
     *
     * @return the number of cylinders entered
     */
    public int promptCylinder()
    {
        return readInt("Enter the number of cylinders (0 for electric): ", 0,
                Vehicle.MAX_CYLINDERS);
    }


    /**
     * Reads a passenger count. Below 1 or non-integer input re-prompts;
     * values above MAX_PAX are capped.
     *
     * @return the number of passengers entered
     */
    public int promptPax()
    {
        return readInt("Enter the number of passengers: ", 1, Vehicle.MAX_PAX);
    }


    /**
     * Reads a mileage. Negative or non-number input re-prompts; values above
     * MAX_MILEAGE are capped.
     *
     * @return the mileage entered
     */
    public double promptMileage()
    {
        return readDouble("Enter the mileage: ", 0, Vehicle.MAX_MILEAGE);
    }


    /**
     * Reads a price. Negative or non-number input re-prompts; values above
     * MAX_PRICE are capped.
     *
     * @return the price entered
     */
    public double promptPrice()
    {
        return readDouble("Enter the price: ", 0, Vehicle.MAX_PRICE);
    }


    /**
     * Reads a tank size in gallons (decimals allowed). Negative or non-number
     * input re-prompts; values above MAX_TANK are capped.
     *
     * @return the tank size entered
     */
    public double promptTank()
    {
        return readDouble("Enter the tank size in gallons (e.g. 15.5): ", 0,
                Vehicle.MAX_TANK);
    }


    /**
     * Reads a fuel type from Vehicle.FUEL_TYPES (case-insensitive). Anything
     * else re-prompts.
     *
     * @return the fuel type, in its canonical spelling
     */
    public String promptFuel()
    {
        return readChoice("Enter the fuel type", Vehicle.FUEL_TYPES);
    }


    /**
     * Reads a car type from Car.CAR_TYPES (case-insensitive). Anything else
     * re-prompts.
     *
     * @return the car type, in its canonical spelling
     */
    public String promptCarType()
    {
        return readChoice("Enter the car type", Car.CAR_TYPES);
    }


    /**
     * Reads a motorcycle type from Motorcycle.MOTORCYCLE_TYPES
     * (case-insensitive). Anything else re-prompts.
     *
     * @return the motorcycle type, in its canonical spelling
     */
    public String promptMotorcycleType()
    {
        return readChoice("Enter the motorcycle type",
                Motorcycle.MOTORCYCLE_TYPES);
    }


    /**
     * Reads a transmission: 0 for automatic or 1 for manual. Anything else
     * re-prompts.
     *
     * @return Car.AUTOMATIC or Car.MANUAL
     */
    public int promptTrans()
    {
        while (true)
        {
            String input =
                    readNonEmpty("Enter 0 for automatic, 1 for manual: ");
            if (input.equals("0") || input.equals("1"))
            {
                return Integer.parseInt(input);
            }
            System.out.println("Please enter 0 or 1.");
        }
    }


    /**
     * Reads an autopilot level. Negative or non-integer input re-prompts;
     * values above Car.MAX_AUTOPILOT are capped.
     *
     * @return the autopilot level entered
     */
    public int promptAuto()
    {
        return readInt("Enter the autopilot level (0-" + Car.MAX_AUTOPILOT
                + "): ", 0, Car.MAX_AUTOPILOT);
    }

    // ~ Private Methods (input helpers shared by all prompt methods) ..........

    /**
     * Prints msg and reads lines until one is not blank.
     *
     * @param msg
     *            the prompt to show
     * @return the line entered, trimmed
     */
    private String readNonEmpty(String msg)
    {
        System.out.print(msg);
        String input = scanner.nextLine().trim();
        while (input.isEmpty())
        {
            System.out.println(
                    "Input cannot be blank. Please enter valid data.");
            System.out.print(msg);
            input = scanner.nextLine().trim();
        }
        return input;
    }


    /**
     * Reads a whole number. Non-integers and values below min re-prompt;
     * values above max are capped to max.
     *
     * @param msg
     *            the prompt to show
     * @param min
     *            the smallest value accepted
     * @param max
     *            the largest value returned
     * @return the number entered, capped at max
     */
    private int readInt(String msg, int min, int max)
    {
        while (true)
        {
            String input = readNonEmpty(msg);
            int value;
            try
            {
                value = Integer.parseInt(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a whole number.");
                continue;
            }
            if (value < min)
            {
                System.out.println("Value must be at least " + min + ".");
                continue;
            }
            if (value > max)
            {
                System.out.println("Value above limit; capped at " + max + ".");
                return max;
            }
            return value;
        }
    }


    /**
     * Reads a number (decimals allowed). Non-numbers and values below min
     * re-prompt; values above max are capped to max.
     *
     * @param msg
     *            the prompt to show
     * @param min
     *            the smallest value accepted
     * @param max
     *            the largest value returned
     * @return the number entered, capped at max
     */
    private double readDouble(String msg, double min, double max)
    {
        while (true)
        {
            String input = readNonEmpty(msg);
            double value;
            try
            {
                value = Double.parseDouble(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a number.");
                continue;
            }
            if (Double.isNaN(value))
            {
                System.out.println("Please enter a number.");
                continue;
            }
            if (value < min)
            {
                System.out.println("Value must be at least " + min + ".");
                continue;
            }
            if (value > max)
            {
                System.out.println("Value above limit; capped at " + max + ".");
                return max;
            }
            return value;
        }
    }


    /**
     * Reads until the input matches one of options, ignoring case.
     *
     * @param msg
     *            the prompt to show; the options are appended
     * @param options
     *            the allowed values
     * @return the matching option, spelled as stored in options
     */
    private String readChoice(String msg, String[] options)
    {
        while (true)
        {
            String input =
                    readNonEmpty(msg + " (" + String.join("/", options) + "): ");
            for (String option : options)
            {
                if (option.equalsIgnoreCase(input))
                {
                    return option;
                }
            }
            System.out.println(
                    "Invalid choice. Options are: " + String.join(", ", options));
        }
    }


    /**
     * Reads until the input is y, yes, n or no, ignoring case.
     *
     * @param msg
     *            the question to ask; " (y/n): " is appended
     * @return true for y/yes, false for n/no
     */
    private boolean readYesNo(String msg)
    {
        while (true)
        {
            String input = readNonEmpty(msg + " (y/n): ");
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
            {
                return true;
            }
            if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
            {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }
}
