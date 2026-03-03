import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

class BankAccount {
    private BigDecimal balance;

    public BankAccount() {
        this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public BankAccount(BigDecimal openingBalance) {
        if (openingBalance == null || openingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }
        this.balance = openingBalance.setScale(2, RoundingMode.HALF_UP);
    }

    // Function: checkBalance
    public BigDecimal checkBalance() {
        return balance;
    }

    // Function: deposit
    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        balance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        System.out.println("✅ Deposited: ₹" + amount + ". New Balance: ₹" + balance);
    }

    // Function: withdraw
    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (amount.compareTo(balance) > 0) {
            throw new IllegalArgumentException("Insufficient funds. Available balance: ₹" + balance);
        }
        balance = balance.subtract(amount).setScale(2, RoundingMode.HALF_UP);
        System.out.println("✅ Withdrawn: ₹" + amount + ". New Balance: ₹" + balance);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }
}

public class BankApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankAccount account = new BankAccount(); // Start with zero balance
        System.out.println("🏦 Welcome to Simple Bank");
        System.out.println("--------------------------");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Select an option (1-4): ");

            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        System.out.println("💰 Current Balance: ₹" + account.checkBalance());
                        break;
                    case "2":
                        BigDecimal depositAmount = promptForAmount(scanner, "Enter amount to deposit: ₹");
                        account.deposit(depositAmount);
                        break;
                    case "3":
                        BigDecimal withdrawAmount = promptForAmount(scanner, "Enter amount to withdraw: ₹");
                        account.withdraw(withdrawAmount);
                        break;
                    case "4":
                        running = false;
                        System.out.println("👋 Thank you for banking with us!");
                        break;
                    default:
                        System.out.println("⚠️ Invalid option. Please choose 1-4.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("❌ Error: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("❌ Unexpected error: " + ex.getMessage());
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("1) Check Balance");
        System.out.println("2) Deposit");
        System.out.println("3) Withdraw");
        System.out.println("4) Exit");
    }

    private static BigDecimal promptForAmount(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().replace(",", "");
            try {
                BigDecimal amount = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("⚠️ Amount must be greater than zero. Try again.");
                    continue;
                }
                return amount;
            } catch (NumberFormatException ex) {
                System.out.println("⚠️ Invalid number format. Example valid input: 100, 250.50");
            }
        }
    }
}
