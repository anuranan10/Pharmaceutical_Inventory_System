import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class WholesalerApp {

    ArrayList<Medicine> retailerInventory;
    ArrayList<Medicine> wholesalerInventory;
    ArrayList<Medicine> incomeInventory;
    ArrayList<Medicine> expenseInventory;

    public WholesalerApp() {
        retailerInventory = new ArrayList<>();
        wholesalerInventory = new ArrayList<>();
        incomeInventory = new ArrayList<>();
        expenseInventory = new ArrayList<>();
    }

    public static void main(String[] args) {
        WholesalerApp app = new WholesalerApp();
        Scanner sc = new Scanner(System.in);

        app.loadDataWholesaler("MedicineInventory.txt", sc);

        System.out.println("Welcome to the Pharmaceutical Inventory Management System. Do you want to continue? (Yes/No)");
        String choice = sc.next();


        while (choice.equalsIgnoreCase("Yes")) {
            app.loadRandomMedicine("RandomMedicine.txt", sc);
            System.out.println("Medicine required by the retailer: ");
            Medicine randomMedicine = app.getRandomMedicine();
            System.out.println(randomMedicine);

            app.sellToRetailer(randomMedicine, sc);

            System.out.println("Welcome to the Pharmaceutical Inventory Management System. Do you want to continue? (Yes/No)");
            choice = sc.next();
        }
        
        System.out.println("No transactions made during this session.");

        sc.close();
    }

    public void loadRandomMedicine(String fileName, Scanner sc) { // File Input
        try {
            File file = new File(fileName);
            sc = new Scanner(file);

            while (sc.hasNext()) {
                Medicine randomMed = null;

                String line = sc.nextLine();
                String[] lineArray = line.split(",");

                String type = lineArray[0];
                String name = lineArray[1];
                int quantity = Integer.parseInt(lineArray[2]);
                randomMed = new Medicine(type, name, quantity);
                retailerInventory.add(randomMed);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error while reading file! " + e.getMessage());
        }
    }

    public Medicine getRandomMedicine() {
        Random random = new Random();
        int index = random.nextInt(retailerInventory.size());
        return retailerInventory.get(index);
    }

    public void loadDataWholesaler(String fileName, Scanner sc) { // File Input
        try {
            File file = new File(fileName);
            sc = new Scanner(file);

            while (sc.hasNext()) {
                Medicine med = null;

                String line = sc.nextLine();
                String[] lineArray = line.split(",");

                if (lineArray[0].equalsIgnoreCase("Liquid")) {
                    String type = lineArray[0];
                    String name = lineArray[1];
                    int quantity = Integer.parseInt(lineArray[2]);
                    med = new Liquid(type, name, quantity);
                } else if (lineArray[0].equalsIgnoreCase("Injection")) {
                    String type = lineArray[0];
                    String name = lineArray[1];
                    int quantity = Integer.parseInt(lineArray[2]);
                    med = new Injection(type, name, quantity);
                } else if (lineArray[0].equalsIgnoreCase("Tablet")) {
                    String type = lineArray[0];
                    String name = lineArray[1];
                    int quantity = Integer.parseInt(lineArray[2]);
                    med = new Tablet(type, name, quantity);
                } else {
                    continue;
                }
                med.calcExpense();
                med.calcIncome();
                wholesalerInventory.add(med);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error while reading file! " + e.getMessage());
        }
    }

    public boolean searchInventory(Medicine randomMedicine) {
        for (int i = 0; i < wholesalerInventory.size(); i++) {
            Medicine med = wholesalerInventory.get(i);
            if (med.getType().equalsIgnoreCase(randomMedicine.getType())
                    && med.getName().equalsIgnoreCase(randomMedicine.getName())
                    && med.getQuantity() == randomMedicine.getQuantity()) {
                return true;
            }
        }
        return false;
    }

    public void sellToRetailer(Medicine randomMedicine, Scanner sc) {
        boolean found = searchInventory(randomMedicine);

        if (found) {
            System.out.println("Medicine required by the retailer already present in the inventory!");
            System.out.println("Do you want to sell the item to the retailer? (Yes/No)");
            String sell = sc.next();
            if (sell.equalsIgnoreCase("Yes")) {
                foundSelling(randomMedicine, sc);
            } else {
                notSelling(sc);
            }
        }

        else {
            System.out.println("Medicine required by the retailer is not in the inventory!");
            placeOrder(randomMedicine, sc);

            System.out.println("Do you want to sell the item to the retailer? (Yes/No)");
            String sell = sc.next();
            if (sell.equalsIgnoreCase("Yes")) {
                notFoundSelling(randomMedicine, sc);

                // Handling surplus after selling
                updateInventoryAfterSale(randomMedicine); // Added here to update inventory correctly after sale

            } else {
                notSelling(sc);
            }

        }
    }

    // Helper method to calculate surplus
    public int findSurplus(Medicine randomMedicine) {
        for (Medicine med : expenseInventory) {
            if (med.getType().equalsIgnoreCase(randomMedicine.getType()) &&
                    med.getName().equalsIgnoreCase(randomMedicine.getName())) {
                return med.getQuantity() - randomMedicine.getQuantity();
            }
        }
        return 0; // No surplus
    }

    // Helper method to add surplus back to inventory
    public void addSurplusToInventory(Medicine randomMedicine, int surplus) {
        wholesalerInventory.add(new Medicine(randomMedicine.getType(), randomMedicine.getName(), surplus));
    }

    public void addToIncomeInventory(Medicine randomMedicine) {
        try {
            for (int i = 0; i < wholesalerInventory.size(); i++) {
                Medicine med = wholesalerInventory.get(i);
                if (med.getType().equalsIgnoreCase(randomMedicine.getType())
                        && med.getName().equalsIgnoreCase(randomMedicine.getName())
                        && med.getQuantity() == randomMedicine.getQuantity()) {
                    incomeInventory.add(med);
                }
            }
        } catch (NullPointerException e) { // Exception Handling - if wholesalerInventory is null, the program still continues
            System.out.println("The inventory is empty! " + e.getMessage());
        }
    }

    public void removeFromWholesalerInventory(Medicine randomMedicine) {
        for (int i = 0; i < wholesalerInventory.size(); i++) {
            Medicine med = wholesalerInventory.get(i);
            if (med.getType().equalsIgnoreCase(randomMedicine.getType()) &&
                    med.getName().equalsIgnoreCase(randomMedicine.getName())) {
                int remainingQuantity = med.getQuantity() - randomMedicine.getQuantity();
                if (remainingQuantity > 0) {
                    med.setQuantity(remainingQuantity);
                } else {
                    wholesalerInventory.remove(i);
                }
                break;
            }
        }
    }

    public void placeOrder(Medicine randomMedicine, Scanner sc) {
        System.out.println("Place order: ");

        System.out.print("Enter the type of the medicine: ");
        String type = sc.next();
        while (!type.equalsIgnoreCase(randomMedicine.getType())) {
            System.out.println("Invalid medicine type! Try Again.");
            System.out.print("Enter the type of the medicine: ");
            type = sc.next();
        }

        sc.nextLine();

        System.out.print("Enter the name of the medicine: ");
        String name = sc.nextLine().trim();
        while (!name.trim().equalsIgnoreCase(randomMedicine.getName().trim())) {
            System.out.println("Invalid medicine name! Try Again.");
            System.out.print("Enter the name of the medicine: ");
            name = sc.nextLine().trim();
        }

        int quantity = 0;
        boolean validInput = false;
        while (!validInput) { // loop to repeatedly prompt the user for valid input
            try {
                System.out.print("Enter the quantity: ");
                quantity = sc.nextInt();
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than zero.");
                    continue; // Ask again
                }
                validInput = true;
            } catch (InputMismatchException e) { // Exception Handling - if the input (quantity) is not "int", exception is thrown preventing any crash
                System.out.println("Invalid input. Enter a valid integer for quantity.");
                sc.nextLine();
            }
        }

        Medicine med = null;
        if (type.equalsIgnoreCase("Liquid")) {
            med = new Liquid(type, name, quantity);
        } else if (type.equalsIgnoreCase("Injection")) {
            med = new Injection(type, name, quantity);
        } else if (type.equalsIgnoreCase("Tablet")) {
            med = new Tablet(type, name, quantity);
        }
        if (med != null) {
            expenseInventory.add(med);
            med.calcExpense();
            med.calcIncome();
            System.out.println("Added to the inventory!");

            // Handling surplus inventory if ordered more than needed
            int surplus = quantity - randomMedicine.getQuantity();
            if (surplus > 0) {
                Medicine surplusMed = new Medicine(type, name, surplus);
                wholesalerInventory.add(surplusMed);
                System.out.println("Ordered quantity exceeds the required amount. Surplus of " + surplus
                        + " units has been added to the inventory.");
            }

        } else {
            System.out.println("Error!Order not placed.");
        }
    }

    public int buyFromManufacturer() {
        System.out.println("Buying from the manufacturer...");
        int totalExpense = 0;
        for (int i = 0; i < expenseInventory.size(); i++) {
            Medicine med = expenseInventory.get(i);
            totalExpense += med.getCalcExpense();
        }
        return totalExpense;
    }

    public void saveUpdatedInventory(String outputFile) { // File Output
        try {
            File f = new File(outputFile);
            PrintWriter pw = new PrintWriter(f);

            for (int i = 0; i < wholesalerInventory.size(); i++) {
                Medicine med = wholesalerInventory.get(i);
                pw.println(med);
            }
            System.out.println("Wholesaler inventory saved to file: " + outputFile);

            pw.close();
        } catch (IOException e) {
            System.out.println("Error while writing into the file " + e.getMessage());
        }
    }

    // Method to update inventory after a sale
    public void updateInventoryAfterSale(Medicine randomMedicine) {
        for (int i = 0; i < wholesalerInventory.size(); i++) {
            Medicine med = wholesalerInventory.get(i);
            if (med.getType().equalsIgnoreCase(randomMedicine.getType()) &&
                    med.getName().equalsIgnoreCase(randomMedicine.getName())) {
                int remainingQuantity = med.getQuantity() - randomMedicine.getQuantity();
                if (remainingQuantity > 0) {
                    med.setQuantity(remainingQuantity);
                } else {
                    wholesalerInventory.remove(i);
                }
                break;
            }
        }
    }

    public void foundSelling(Medicine randomMedicine, Scanner sc) {
        addToIncomeInventory(randomMedicine);
        System.out.println("Selling the item to the retailer...");

        int totalIncome = 0;
        for (int i = 0; i < incomeInventory.size(); i++) {
            Medicine med = incomeInventory.get(i);
            totalIncome += med.getCalcIncome();
        }
        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expense: $0");
        System.out.println("Margin: $" + totalIncome);

        removeFromWholesalerInventory(randomMedicine);
        dispUpdatedInventory();
        repeatProgram(sc);
    }

    public void notFoundSelling(Medicine randomMedicine, Scanner sc) {
        addToIncomeInventory(randomMedicine);
        System.out.println("Selling the item to the retailer...");

        int totalIncome = 0;
        for (int i = 0; i < expenseInventory.size(); i++) {
            Medicine med = expenseInventory.get(i);
            totalIncome += med.getCalcIncome();
        }
        int totalExpense = buyFromManufacturer();

        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expense: $" + totalExpense);
        int margin = totalIncome - totalExpense;
        System.out.println("Margin: $" + margin);

        removeFromWholesalerInventory(randomMedicine);
        dispUpdatedInventory();
        repeatProgram(sc);
    }

    public void notSelling(Scanner sc) {
        System.out.println("Do you want to continue with the program? (Yes/No)");
        String choice = sc.next();
        if (choice.equalsIgnoreCase("Yes")) {

        } else {
            System.out.println(
                    "Exiting the program. Thank you for using the Pharmaceutical Inventory Management System.");
            System.exit(0);
        }
    }

    public void dispUpdatedInventory() {
        saveUpdatedInventory("UpdatedWholesalerInventory.txt");

        System.out.println("Updated Inventory: ");
        for (int i = 0; i < wholesalerInventory.size(); i++) {
            System.out.println(wholesalerInventory.get(i));
        }
    }

    public void repeatProgram(Scanner sc) {
        System.out.println("Do you want to repeat the program ? (Yes/No)");
        String repeat = sc.next();
        if (repeat.equalsIgnoreCase("Yes")) {

        } else {
            System.out.println(
                    "Exiting the program. Thank you for using the Pharmaceutical Inventory Management System.");
            System.exit(0);
        }
    }

}