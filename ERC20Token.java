import java.util.HashMap;
import java.util.Map;

public class ERC20Token {
    private String name;
    private String symbol;
    private int decimals;
    private Map<String, Integer> balances;

    public ERC20Token(String name, String symbol, int decimals) {
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.balances = new HashMap<>();
    }

    public void transfer(String from, String to, int amount) {
        int balance = balances.getOrDefault(from, 0);
        if (balance < amount) {
            System.out.println("Insufficient balance");
            return;
        }

        balances.put(from, balance - amount);
        balances.put(to, balances.getOrDefault(to, 0) + amount);
        System.out.println("Transfer successful");
    }

    public int balanceOf(String address) {
        return balances.getOrDefault(address, 0);
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getDecimals() {
        return decimals;
    }

    public static void main(String[] args) {
        ERC20Token token = new ERC20Token("MyToken", "MTK", 18);
        // Set initial balances
        token.balances.put("Alice", 1000);
        token.balances.put("Bob", 500);
        token.balances.put("Charlie", 200);
        // Perform some transfers
        token.transfer("Alice", "Bob", 200);
        token.transfer("Charlie", "Alice", 100);
        token.transfer("Bob", "Charlie", 50);
        // Print final balances
        System.out.println("Alice balance: " + token.balanceOf("Alice"));
        System.out.println("Bob balance: " + token.balanceOf("Bob"));
        System.out.println("Charlie balance: " + token.balanceOf("Charlie"));
    }
}
