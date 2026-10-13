package rentals;

import java.util.Scanner;


public class RidePortal
{
 // ~ Fields ................................................................
    private static Scanner scanner = new Scanner(System.in);
    static LinkedChain<Vehicle> inventory = new LinkedChain<Vehicle>();
  
    // ~ Constructors ..........................................................

    // ~Public Methods ........................................................
    public static void main(String args[])
    {
        System.out.println(
            "Welcome to Ride Portal! Enter available options of rental vehicles.");
        System.out.println("We will help you find a right option for you");
        
        addItem(); //user will add an item first. 
    }


    public static void run()
    {
        boolean running = true;
        
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
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
                    System.out.println("Thank you for using Ride Portal. Goodbye!");
                    running = false;
                    break;
                
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void displayMenu()
    {
        System.out.println("===== Ride Portal Menu =====");
        System.out.println("1. Add a vehicle");
        System.out.println("2. Remove a vehicle");
        System.out.println("3. Display all vehicles");
        System.out.println("4. Search vehicles");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }


    public static void addItem()
    {
        System.out.println("Are you entering a motorcycle or car?");
        System.out.println("Enter 0 for motorcycle and 1 for car");
        String input = scanner.nextLine();
        //this decides if a user will input a motorcycle or car
        int vehicleType = Integer.parseInt(input);
        
        //input common parameters first
        
        String year = promptYear("Enter the manufactured year of the vehicle:");
        int year1 = Integer.parseInt(year);

        String name = promptName("Enter the model name of the vehicle.");

        String maker = promptMaker("Enter the manufacturer.");

        String cylinders =
            promptCylinder("Enter the number of cylinders. 0 for electic.");
        int cylinders1 = Integer.parseInt(cylinders);

        String pax = promptPax("Enter the number of passengers in vehicle. ");
        int pax1 = Integer.parseInt(pax);

        String mileage = promptMileage("Enter the mileage of vehicle.");
        double mileage1 = Double.parseDouble(mileage);

        String price = promptPrice("Enter the price of the vehicle.");
        double price1 = Double.parseDouble(price);

        String tankSize = promptTank("Enter the size of the tank.");
        double tankSize1 = Double.parseDouble(tankSize);

        String fuelType = promptFuel("Enter the fuel type.");
        
        if (vehicleType == 0) {
            String motorcycleType = promptMotorcycleType("Enter motorcycle type:");
            
            inventory.add(new Motorcycle(year1, name, maker, cylinders1, pax1, mileage1, price1, tankSize1, fuelType, motorcycleType));
        }
        else {
            String carType = promptCarType("Enter car type.");
            
            String transmission = promptTrans("Enter 0 for auto, 1 for manual");
            int transmission1 = Integer.parseInt(transmission);
            
            String autopilot = promptAuto("Enter autopilot level from 0-5");
            int autopilot1 = Integer.parseInt(autopilot);
            
            inventory.add(new Car(year1, name, maker, cylinders1, pax1, mileage1, price1, tankSize1, fuelType, carType, transmission1, autopilot1));
        }
        
        System.out.println("Enter 0 to add more items and 1 to start searching");
        String addSearch = scanner.nextLine();
        int addOrSearch = Integer.parseInt(addSearch);
        
        if (addOrSearch == 0) {
            addItem(); //recursion this method until user will run search
        }
        else if(addOrSearch == 1) {
            run(); //end recursion and start searching
        }
    }
 
    public static void removeItem()
    {
        if (inventory.isEmpty()) {
            System.out.println("No vehicles listed.");
            return;
        }
        System.out.print("Enter the id of the vehicle to remove: ");
        String input = scanner.nextLine().trim();
        
        int id;
        
        try
        {
            id = Integer.parseInt(input);
        }
        
        catch (NumberFormatException e)
        {
            System.out.println("Invalid ID. Please try again.");
            return;
        }
        
        Vehicle[] vehicles = inventory.toArray(new Vehicle[inventory.size()]);
        
        for (Vehicle vehicle : vehicles)
        {
            if (vehicle.getId() == id)
            {
                inventory.remove(vehicle);
                System.out.println("Removed: " + vehicle);
                return;
            }
        }

        System.out.println("Vehicle not found"); 
    }


    public static void displayAll()
    {
        if (inventory.isEmpty())
        {
            System.out.println("No vehicles listed");
            return;
        }

        Vehicle[] vehicles = inventory.toArray(new Vehicle[inventory.size()]);
        
        for (Vehicle vehicle : vehicles)
        {
            System.out.println(vehicle);
        }
    }
    
    public static LinkedChain<Vehicle> searchBy(LinkedChain<Vehicle> source)
    {
        while (true) {
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
            
            switch (choice) {
                case "0":
                    return null;
                case "1":
                    return searchName(source, promptName("Enter model name: "));
                case "2":
                    return searchYear(source, promptYear("Enter year: "));
                case "3":
                    return searchMaker(source, promptMaker("Enter manufacturer: "));
                case "4":
                    return searchCylinder(source,
                        Integer.parseInt(promptCylinder("Enter number of cylinders: ")));
                case "5":
                    return searchPax(source,
                        Integer.parseInt(promptPax("Enter number of passengers: ")));
                case "6":
                    return searchMileage(source,
                        Double.parseDouble(promptMileage("Enter maximum mileage: ")));
                case "7":
                    return searchPrice(source,
                        Double.parseDouble(promptPrice("Enter maximum price: ")));
                case "8":
                    return searchTank(source,
                        Double.parseDouble(promptTank("Enter minimum tank size: ")));
                case "9":
                    return searchFuel(source, promptFuel("Enter fuel type: "));
                case "10":
                    return searchCarType(source, promptCarType("Enter car type: "));
                case "11":
                    return searchTrans(source,
                        Integer.parseInt(promptTrans("Enter 0 for auto, 1 for manual: ").trim()))
                case "12":
                    return searchAuto(source,
                        Integer.parseInt(promptAuto("Enter autopilot level (0-5): ")));
                case "13":
                    return searchMotorcycleType(source,
                        promptMotorcycleType("Enter motorcycle type: "));
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static LinkedChain<Vehicle> searchResults()
    {
        LinkedChain<Vehicle> results = inventory;
        
        while (true)
        {
            LinkedChain<Vehicle> filtered = searchBy(results);
            if (filtered == null)
            {
                break; // user chose "Done"
            }
            
            results = filtered;         
            System.out.println(results.size() + " vehicle(s) match so far.");
            
            System.out.println();
            System.out.println("===== Search Results =====");
            
            if (results.isEmpty())
            {
                System.out.println("No matching vehicles found");
            }
            
            else
            {
                Vehicle[] vehicles = results.toArray(new Vehicle[results.size()]);
                for (Vehicle vehicle : vehicles)
                {
                    System.out.println(vehicle);
                }
            }
        }
        return results;
    }
    
    /**
     * general search methods 
     */
    
    public static LinkedChain<Vehicle> searchName(LinkedChain<Vehicle> source, String name)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || name == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (name.equalsIgnoreCase(vehicles[i].getName()))
            {
                result.add(vehicles[i]);
            }
        }
        return result;
        
    }


    public static LinkedChain<Vehicle> searchYear(LinkedChain<Vehicle> source, int year)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (vehicles[i].getYear() == year)
            {
                result.add(vehicles[i]);
            }
        }
    }

    public static LinkedChain<Vehicle> searchMaker(LinkedChain<Vehicle> source, String maker)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || maker == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (maker.equalsIgnoreCase(vehicles[i].getMaker()))
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }


    public static LinkedChain<Vehicle> searchCylinder(
        LinkedChain<Vehicle> source, int cylinders)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (vehicles[i].getCylinders() == cylinders)
            {
                result.add(vehicles[i])
            }
        }
        return result;
    }


    public static LinkedChain<Vehicle> searchPax(LinkedChain<Vehicle> source, int pax)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (vehicles[i].getPax() == pax)
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }


    public static
        LinkedChain<Vehicle>
        searchMileage(LinkedChain<Vehicle> source, double maxMileage)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (vehicles[i].getMileage() <= maxMileage)
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }


    public static
        LinkedChain<Vehicle>
        searchPrice(LinkedChain<Vehicle> source, double maxPrice)
    {
        if (source == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (vehicles[i].getPrice() <= maxPrice)
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }


    public static
        LinkedChain<Vehicle>
        searchTank(LinkedChain<Vehicle> source, double minTank)
    {
        if (source == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (vehicles[i].getTankSize() >= minTank)
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }


    public static
        LinkedChain<Vehicle>
        searchFuel(LinkedChain<Vehicle> source, String fuelType)
    {

        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || maker == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (fuelType.equalsIgnoreCase(vehicles[i].getFuelType()))
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }

    
    /*
     * Car/Motorcycle specific search methods
     */

    public static
        LinkedChain<Vehicle>
        searchCarType(LinkedChain<Vehicle> source, String carType)
    {

        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || maker == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (maker.equalsIgnoreCase(vehicles[i].getMaker()))
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }


    public static
        LinkedChain<Vehicle>
        searchTrans(LinkedChain<Vehicle> source, boolean transmission)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (vehicles[i] instanceof Car
                && ((Car) vehicles[i]).getTransmission() == transmission)
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }


    public static
        LinkedChain<Vehicle>
        searchAuto(LinkedChain<Vehicle> source, int auto)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (vehicles[i] instanceof Car && ((Car) vehicles[i]).isAutoPilot() == autoPilot)
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }


    public static
        LinkedChain<Vehicle>
        searchMotorcycleType(LinkedChain<Vehicle> source, String type)
    {
        LinkedChain<Vehicle> result = new LinkedChain<Vehicle>();
        if (source == null || type == null)
        {
            return result;
        }
        Vehicle[] vehicles = source.toArray(new Vehicle[source.size()]);
        for (int i = vehicles.length - 1; i >= 0; i--)
        {
            if (vehicles[i] instanceof Motorcycle
                && type.equalsIgnoreCase(((Motorcycle) vehicles[i]).getMotorcycleType()))
            {
                result.add(vehicles[i]);
            }
        }
        return result;
    }

    
    /*
     * Prompt methods
     */
    
    public static String promptYear(String year)
    {
        System.out.print(year);
        String input = scanner.nextLine();
        while (input == null || input.trim().isEmpty())
        {
            System.out.println(
                "Input cannot be null or blank. Please enter valid data.");
            System.out.print(year);
            input = scanner.nextLine();
        }

        /*
         * at the moment it will not work as intended if user does not enter a valid year. 
         * In the final product there has to be a code to prevent this
         */

        return input;
    }


    public static String promptName(String name)
    {
        System.out.print(name);
        String input = scanner.nextLine();
        while (input == null || input.trim().isEmpty())
        {
            System.out.println(
                "Input cannot be null or blank. Please enter valid data.");
            System.out.print(name);
            input = scanner.nextLine();
        }

        return input;
    }

    public static String promptMaker(String maker)
    {
        System.out.print(maker);
        String input = scanner.nextLine();
        while (input == null || input.trim().isEmpty())
        {
            System.out.println(
                "Input cannot be null or blank. Please enter valid data.");
            System.out.print(maker);
            input = scanner.nextLine();
        }

        return input;
    }


    public static String promptCylinder()
    {
        return readInt("Enter the number of cylinders (0 for electric): ",
            0, Vehicle.MAX_CYLINDERS);
    }


    public static String promptPax()
    {
        return readInt("Enter the number of passengers: ", 1, Vehicle.MAX_PAX);
    }


    public static String promptMileage()
    {
        return readInt("Enter the mileage: ", 0, (int) Vehicle.MAX_MILEAGE);
    }


    public static String promptPrice()
    {
        return readDouble("Enter the price: ", 0, Vehicle.MAX_PRICE);
    }


    public static String promptTank()
    {
        return readDouble("Enter the tank size in gallons (e.g. 15.5): ",
            0, Vehicle.MAX_TANK);
    }


    public static String promptFuel()
    {
        return readChoice("Enter the fuel type", Vehicle.FUEL_TYPES);
    }


    public static String promptCarType()
    {
        return readChoice("Enter the car type", Car.CAR_TYPES);
    }


    public static String promptMotorcycleType()
    {
        return readChoice("Enter the motorcycle type", Motorcycle.MOTORCYCLE_TYPES);
    }


   public static String promptAuto() {
       return readInt("Enter autopilot level (0-5): ", 0, 5);
   }
   
   public static String promptTrans(String message) {
       while (true)
       {
           String input = readNonEmpty("Enter 0 for automatic, 1 for manual: ");
           if (input.equals("0") || input.equals("1"))
           {
               return Integer.parseInt(input);
           }
           System.out.println("Please enter 0 or 1.");
       }
   }
   
   

/*
 * Private input helpers (shared by all prompt methods)
 */
   
   private static String readNonEmpty(String msg)
   {
       System.out.print(msg);
       String input = scanner.nextLine().trim();
       
       while (input.isEmpty())
       {
           System.out.println("Input cannot be blank. Please enter valid data.");
           System.out.print(msg);
           input = scanner.nextLine().trim();
       }
       
       return input;
   }
   
   private static int readInt(String msg, int min, int max)
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
   
   private static double readDouble(String msg, double min, double max)
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
   
   private static String readChoice(String msg, String[] options)
   {
       while (true)
       {
           String input = readNonEmpty(msg + " (" + String.join("/", options) + "): ");
           
           for (String option : options)
           {
               if (option.equalsIgnoreCase(input))
               {
                   return option;
               }
           }
           
           System.out.println("Invalid choice. Options are: "
               + String.join(", ", options));
       }
   }
   
   private static boolean readYesNo(String msg)
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