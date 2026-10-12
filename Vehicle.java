package rentals;
public class Vehicle
{
    //~ Fields ................................................................
    private int year;
    private String name;
    private String maker;
    private int cylinders;
    private int pax;
    private double mileage;
    private double price;
    private double tankSize;
    private String fuelType;
    //~ Constructors ..........................................................
    
    public Vehicle(int year, String name, String maker, int cylinders, int pax, double mileage, double price, double tankSize, String fuelType) {
        this.year = year;
        this.name = name;
        this.maker = maker;
        this.cylinders = cylinders;
        this.pax = pax;
        this.mileage = mileage;
        this.price = price;
        this.tankSize = tankSize;
        this.fuelType = fuelType;
    }
    //~Public  Methods ........................................................

    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getMaker() {
        return maker;
    }
    
    public void setMaker(String maker) {
        this.maker = maker;
    }
    
    public int getCylinders() {
        return cylinders;
    }
    
    public void setCylinders(int cylinders) {
        this.cylinders = cylinders;
    }
    
    public int getPax() {
        return pax;
    }
    
    public void setPax(int pax) {
        this.pax = pax;
    }
    
    public double getMileage() {
        return mileage;
    }
    
    public void setMileage(double mileage) {
        this.mileage = mileage;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getTankSize() {
        return tankSize;
    }
    
    public void setTankSize(double tankSize) {
        this.tankSize = tankSize;
    }
    
    public String getFuelType() {
        return fuelType;
    }
    
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    
    public String getVehicleType() {
        return null;
    }
    
    public String toString() {
        return null;
    }
    
    public boolean equals(Object object) {
        return false;
    }
    
    
}
