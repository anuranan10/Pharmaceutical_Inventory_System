public class Liquid extends Medicine {

	private int costLiquid;
	private int sellPriceLiquid;

	public Liquid(String type, String name, int quantity) {
		super(type, name, quantity);
	}

	@Override
	public void calcExpense() {
		costLiquid = (int) (16 * getQuantity() + 0.13);
		setCalcExpense(costLiquid);
	}

	@Override
	public void calcIncome() {
		sellPriceLiquid = (int) (19 * getQuantity() + 0.13);
		setCalcIncome(sellPriceLiquid);
	}
}