public class Tablet extends Medicine {

    private int costTablet;
    private int sellPriceTablet;

    public Tablet(String type, String name, int quantity) {
        super(type, name, quantity);
    }

    @Override
    public void calcExpense() {
        costTablet = (int) (14 * getQuantity() + 0.1);
        setCalcExpense(costTablet);
    }

    @Override
    public void calcIncome() {
        sellPriceTablet = (int) (16 * getQuantity() + 0.1);
        setCalcIncome(sellPriceTablet);
    }
}