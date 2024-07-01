import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
    private String id;
    private String carModel;
    private String carBrand;
    private double basePrice;
    private boolean isAvailable;

    public Car(String id, String carModel, String carBrand, double basePrice) {
        this.id = id;
        this.carModel = carModel;
        this.carBrand = carBrand;
        this.basePrice = basePrice;
        this.isAvailable = true;  // Initialize isAvailable as true when a car is created
    }

    public String getCarModel() {
        return carModel;
    }
    public String getCarBrand() {
        return carBrand;
    }
    public double getBasePrice() {
        return basePrice;
    }
    public String getId() {
        return id;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void rent() {
        isAvailable = false;
    }
    public void returnCar() {
        isAvailable = true;
    }
    public double calculatePrice(int rentDays) {
        return basePrice * rentDays;
    }
}

class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private long phoneNo;

    public Customer(String customerId, String firstName, String lastName, long phoneNo) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
    }

    public String getCustomerId() {
        return customerId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public long getPhoneNo() {
        return phoneNo;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int day;

    public Rental(Car car, Customer customer, int day) {
        this.car = car;
        this.customer = customer;
        this.day = day;
    }

    public Car getCar() {
        return car;
    }
    public Customer getCustomer() {
        return customer;
    }
    public int getDay() {
        return day;
    }
}

class CarRentalSystem {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int day) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, day));
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        car.returnCar();

        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not rented.");
        }
    }

    void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                System.out.println("\n== Rent a Car ==\n");
                System.out.print("Enter your first name: ");
                String firstName = scanner.nextLine();
                System.out.print("Enter your last name: ");
                String lastName = scanner.nextLine();
                System.out.print("Enter your phone number: ");
                long phoneNo = scanner.nextLong();
                scanner.nextLine();

                System.out.println("\nAvailable Cars:");
                for (Car car : cars) {
                    if (car.isAvailable()) {
                        System.out.println(car.getId() + " - " + car.getCarBrand() + "( " + car.getCarModel() +")");
                    }
                }

                System.out.print("\nEnter the car ID you want to rent: ");
                String carId = scanner.nextLine();

                System.out.print("Enter the number of days for rental: ");
                int rentalDays = scanner.nextInt();
                scanner.nextLine();

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), firstName, lastName, phoneNo);
                addCustomer(newCustomer);

                Car selectedCar = null;
                for (Car car : cars) {
                    if (car.getId().equals(carId) && car.isAvailable()) {
                        selectedCar = car;
                        break;
                    }
                }

                if (selectedCar != null) {
                    double totalPrice = selectedCar.calculatePrice(rentalDays);
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getFirstName() + " " + newCustomer.getLastName());
                    System.out.println("Car: " + selectedCar.getCarBrand() + " " + selectedCar.getCarModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);

                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();

                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, rentalDays);
                        System.out.println("\nCar rented successfully.");
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n== Return a Car ==\n");
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine();

                Car carToReturn = null;
                for (Car car : cars) {
                    if (car.getId().equals(carId) && !car.isAvailable()) {
                        carToReturn = car;
                        break;
                    }
                }

                if (carToReturn != null) {
                    Customer customer = null;
                    for (Rental rental : rentals) {
                        if (rental.getCar() == carToReturn) {
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null) {
                        returnCar(carToReturn);
                        System.out.println("Car returned successfully by " + customer.getFirstName());
                    } else {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Camry", "Toyota", 60.0);
        Car car2 = new Car("C002", "Accord", "Honda", 70.0);
        Car car3 = new Car("C003", "Thar", "Mahindra", 150.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
}
