package rentals;
public class Car extends Vehicle
{
    //~ Fields ................................................................
    private String carType;
    private int transmission; //0 for auto, 1 for manual
    private int autoPilot;
    
    //~ Constructors ..........................................................
    public Car(int year, String name, String maker, int cylinders, int pax, double mileage, double price, double tankSize, String fuelType, String carType, int transmission, int autoPilot) {
        super(year, name, maker, cylinders, pax, mileage, price, tankSize, fuelType);
        this.carType = carType;
        this.transmission = transmission;
        this.autoPilot = autoPilot;
    }
    //~Public  Methods ........................................................
    public String getCarType() {
        return carType;
    }
    
    public void setCarType(String carType) {
        this.carType = carType;
    }
    
    public int getTransmission() {
        return transmission;
    }
    
    public void setTransmission(int transmission) {
        this.transmission = transmission;
    }
    
    public int getAutoPilot() {
        return autoPilot;
    }
    
    public void setAutoPilot(int autoPilot) {
        this.autoPilot = autoPilot;
    }
    
    public String getVehicleType() {
        return "Car";
    }
    
    public String toString() {
        return super.toString()
            + ", carType=" + carType
            + ", eco=" + eco
            + ", transmission=" + transmission
            + ", autoPilot=" + autoPilot;
        
    }
}
