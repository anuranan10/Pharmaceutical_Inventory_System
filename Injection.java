public class Injection extends Medicine {

    private int costInjection;
    private int sellPriceInjection;

    public Injection(String type, String name, int quantity) {
        super(type, name, quantity);
    }

    @Override
    public void calcExpense() {
        costInjection = (int) (28 * getQuantity() + 0.15);
        setCalcExpense(costInjection);
    }

    @Override
    public void calcIncome() {
        sellPriceInjection = (int) (32 * getQuantity() + 0.15);
        setCalcIncome(sellPriceInjection);
    }
}