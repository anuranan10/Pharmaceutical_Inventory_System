# Pharmaceutical Inventory Management System

## Description
The Pharmaceutical Inventory Management System is a Java-based application that simulates the interactions between a wholesaler and a retailer in the pharmaceutical industry. The system allows the user to manage the inventory of various types of medicines, including liquids, injections, and tablets. It enables wholesalers to purchase medicines from manufacturers and sell them to retailers while keeping track of expenses and income.

## Features
- **Inventory Management**: Track stock levels of different medicines.
- **Sales Processing**: Handle sales transactions to retailers.
- **Expense Tracking**: Calculate expenses incurred when purchasing medicines.
- **Income Calculation**: Calculate income from sales.
- **Final Reporting**: Generate reports summarizing total income, expenses, and profit margins.

## Technologies Used
- Java (JDK 22.0.1)
- Basic File I/O for loading and saving data
- Collections Framework for managing data

## Files Included
- **Java Files**:
  - `WholesalerApp.java`: Main application class for running the program.
  - `Medicine.java`: Parent class for different types of medicines.
  - `Liquid.java`: Class for liquid type medicines.
  - `Injection.java`: Class for injection type medicines.
  - `Tablet.java`: Class for tablet type medicines.
  
- **Text Files**:
  - `MedicineInventory.txt`: Initial inventory data for the wholesaler.
  - `RandomMedicine.txt`: Randomly generated medicines required by the retailer.
