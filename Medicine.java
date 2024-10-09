public class Medicine {

	private String type;
	private String name;
	private int quantity;
	private int calcIncome;
	private int calcExpense;

	public Medicine(String type, String name, int quantity) {
		// Added null and range checks for input
		if (type == null || name == null || quantity < 0) {
			throw new IllegalArgumentException(
					"Invalid input: Type, name must be non-null, and quantity must be non-negative.");
		}

		this.type = type;
		this.name = name;
		this.quantity = quantity;
	}

	public void calcExpense() {

	}

	public void calcIncome() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCalcIncome() {
		return calcIncome;
	}

	public void setCalcIncome(int calcIncome) {
		this.calcIncome = calcIncome;
	}

	public int getCalcExpense() {
		return calcExpense;
	}

	public void setCalcExpense(int calcExpense) {
		this.calcExpense = calcExpense;
	}

	public String toString() {
		return "Type: " + type + "\n" + "Name: " + name + "\n" + "Quantity: " + quantity;
	}

}