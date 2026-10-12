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
        
    }


    public static void displayMenu()
    {
        
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

    }


    public static void displayAll()
    {

    }


    public static LinkedChain<Vehicle> searchResults()
    {
        return null;
    }


    public static LinkedChain<Vehicle> searchBy(LinkedChain<Vehicle> source)
    {
        return null;
    }


    public static LinkedChain<Vehicle> searchName(LinkedChain<Vehicle> source)
    {
        return null;
    }


    public static LinkedChain<Vehicle> searchYear(LinkedChain<Vehicle> source)
    {
        return null;
    }


    public static LinkedChain<Vehicle> searchMaker(LinkedChain<Vehicle> source)
    {
        return null;
    }


    public static LinkedChain<Vehicle> searchCylinder(
        LinkedChain<Vehicle> source)
    {
        return null;
    }


    public static LinkedChain<Vehicle> searchPax(LinkedChain<Vehicle> source)
    {
        return null;
    }


    public static
        LinkedChain<Vehicle>
        searchMileage(LinkedChain<Vehicle> source, double maxMileage)
    {
        return null;
    }


    public static
        LinkedChain<Vehicle>
        searchPrice(LinkedChain<Vehicle> source, double maxPrice)
    {
        return null;
    }


    public static
        LinkedChain<Vehicle>
        searchTank(LinkedChain<Vehicle> source, double minTank)
    {
        return null;
    }


    public static
        LinkedChain<Vehicle>
        searchFuel(LinkedChain<Vehicle> source, String fuelType)
    {
        return null;
    }

    /*
     * Car/Motorcycle specific search methods
     */


    public static
        LinkedChain<Vehicle>
        searchCarType(LinkedChain<Vehicle> source, String carType)
    {
        return null;
    }


    public static
        LinkedChain<Vehicle>
        searchTrans(LinkedChain<Vehicle> source, boolean transmission)
    {
        return null;
    }


    public static
        LinkedChain<Vehicle>
        searchAuto(LinkedChain<Vehicle> source, int auto)
    {
        return null;
    }


    public static
        LinkedChain<Vehicle>
        searchMotorcycleType(LinkedChain<Vehicle> source, String type)
    {
        return null;
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
        return null;
    }


    public static String promptPax()
    {
        return null;
    }


    public static String promptMileage()
    {
        return null;
    }


    public static String promptPrice()
    {
        return null;
    }


    public static String promptTank()
    {
        return null;
    }


    public static String promptFuel()
    {
        return null;
    }


    public static String promptCarType()
    {
        return null;
    }


    public static String promptMotorcycleType()
    {
        return null;
    }


   public static String promptAuto() {
       return null;
   }
   
   public static String promptTrans() {
       return null;
   }
   
   

/*
 * Private input helpers (shared by all prompt methods)
 */
}