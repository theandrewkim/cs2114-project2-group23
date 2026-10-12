package rentals;
public class Motorcycle extends Vehicle
{
    //~ Fields ................................................................
    private String motorcycleType;
    //~ Constructors ..........................................................

    public Motorcycle(int year, String name, String maker, int cylinders, int pax, double mileage, double price, double tankSize, String fuelType, String motorcycleType) {
        super(year, name, maker, cylinders, pax, mileage, price, tankSize, fuelType);
        this.motorcycleType = motorcycleType;
    }
    //~Public  Methods ........................................................
    public String getMotorcycleType() {
        return motorcycleType;
    }
    
    public void setMotorcycleType(String motorcycleType) {
        this.motorcycleType = motorcycleType;
    }
    
    public String getVehicleType() {
        return "Motorcycle";    
    }
    
    public String toString() {
        return super.toString()
            + + ", motorcycleType=" + motorcycleType;
    }
    
}   
