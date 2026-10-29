import banking.BankAccount;
import banking.SavingsAccount;
import banking.CurrentAccount;
import banking.LoanRejectedException;
import banking.InvalidTransactionException;
import banking.InsufficientFundsException;
import banking.Loanable;

import java.util.*;
import java.io.Console;


public class BankingSystemMain{

    private static final Scanner input = new Scanner(System.in);

    private static final ArrayList<BankAccount> accounts =
            new ArrayList<>();


    private static final Map<String, Integer> UnsuccessfulAttempts =
            new TreeMap<>();


    private static final Set<String> blockedAccounts =
            new TreeSet<>();

    public static void main(String[] args) {
        createSampleAccounts();

        boolean working = true;

        while (working) {
            showMenu();
            int choice = readInt("Choose 1-7: ");

            try {
                switch (choice) {
                    case 1:
                        details();
                        break;

                    case 2:
                        deposit();
                        break;

                    case 3:
                        withdraw();
                        break;

                    case 4:
                        transfer();
                        break;

                    case 5:
                        interest();
                        break;

                    case 6:
                        loan();
                        break;

                    case 7:
                        working = false;
                        System.out.println("Thank you for using our ATM");
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Select 1 to 7");
                }

            } catch (InvalidTransactionException | InsufficientFundsException | LoanRejectedException e){

                System.out.println(
                        "Transaction failed: " + e.getMessage());
            }
        }

        input.close();
    }
    private static double readAmount(Scanner input){

        return input.nextDouble();
    }

    private static void createSampleAccounts() {
        try {
            accounts.add(new SavingsAccount("1111", 500, "Monica Hlopheho", "901020605"));

            accounts.add(new CurrentAccount("2222", 500,"Itumeleng Lechesa", "901020583"));

            accounts.add(new SavingsAccount("3333", 1000, "Malefu Ts'oeu", "901020582"));

            accounts.add(new CurrentAccount("4444", 3000, "Bokhabane Ntshema", "901020604"));

            accounts.add(new CurrentAccount("5555", 30, "Tshepo Joele", "901020567"));

        } catch (InvalidTransactionException e) {
            System.out.println(
                    "Account setup failed: " + e.getMessage());
        }
    }

    private static void showMenu() {
        System.out.println("MENU");
        System.out.println("1. View account details");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Transfer money");
        System.out.println("5. Calculate interest");
        System.out.println("6. Request a loan");
        System.out.println("7. Exit");
    }


    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a legit whole number");
            }
        }
    }


    private static double readAmount(String prompt)
            throws InvalidTransactionException {

        System.out.print(prompt);

        try {
            double amount =
                    Double.parseDouble(input.nextLine().trim());

            if (!Double.isFinite(amount)) {
                throw new NumberFormatException();
            }

            return amount;

        } catch (NumberFormatException e) {
            throw new InvalidTransactionException(
                    "Enter a legitimate numeric amount");
        }
    }

    private static BankAccount findAccount(String number) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber()
                    .equalsIgnoreCase(number)) {
                return account;
            }
        }

        return null;
    }


    private static BankAccount varifyAccount() {
        System.out.print("Enter your account number: ");
        String number = input.nextLine().trim();

        BankAccount account = findAccount(number);

        if (account == null) {
            System.out.println("Invalid account number or pin");
            return null;
        }

        String accountNumber = account.getAccountNumber();

        if (blockedAccounts.contains(accountNumber)) {
            System.out.println(
                    "This account is locked. Contact the administrator");
            return null;
        }

        while (true) {
            String enteredPin = readPin("Enter your 4-digit pin: ");

            if (account.checkPin(enteredPin)) {
                UnsuccessfulAttempts.remove(accountNumber);
                System.out.println("Authentication successful");
                return account;
            }

            int attempts =
                    UnsuccessfulAttempts.getOrDefault(accountNumber, 0) + 1;

            UnsuccessfulAttempts.put(accountNumber, attempts);

            System.out.println("Invalid account number or pin");

            if (attempts >= 3) {
                blockedAccounts.add(accountNumber);
                System.out.println(
                        "Account locked after three failed attempts");
                return null;
            }

            System.out.println(
                    "Attempts remaining: " + (3 - attempts));
        }
    }


    private static String readPin(String prompt) {
        Console console = System.console();

        if (console != null) {
            char[] pin = console.readPassword("string", prompt);

            if (pin == null) {
                return "";
            }

            try {
                return new String(pin);
            } finally {
                java.util.Arrays.fill(pin, '\0');
            }
        }


        System.out.print(prompt);
        return input.nextLine().trim();
    }

    private static void details() {
        BankAccount account = varifyAccount();

        if (account != null) {
            account.displayAccountDetails();
        }
    }

    private static void deposit()
            throws InvalidTransactionException {

        BankAccount account = varifyAccount();

        if (account == null) {
            return;
        }

        account.deposit(readAmount("Amount to deposit: M"));

        System.out.printf("New balance: M%.2f%n",
                account.getBalance());
    }

    private static void withdraw()
            throws InvalidTransactionException,
            InsufficientFundsException {

        BankAccount account = varifyAccount();

        if (account == null) {
            return;
        }

        account.withdraw(readAmount("Amount to withdraw: M"));

        System.out.printf("New balance: M%.2f%n",
                account.getBalance());
    }

    private static void transfer()
            throws InvalidTransactionException,
            InsufficientFundsException {

        BankAccount from = varifyAccount();

        if (from == null) {
            return;
        }

        System.out.print("Receiving account number: ");
        BankAccount to = findAccount(input.nextLine().trim());

        if (to == null) {
            System.out.println("Receiving account not found");
            return;
        }

        from.transfer(to, readAmount("Amount to be migrated: M"));

        System.out.println("pass on successful.");
        System.out.printf("Your remaining amount: M%.2f%n",
                from.getBalance());
    }

    private static void interest() {
        BankAccount account = varifyAccount();

        if (account == null) {
            return;
        }

        double amount = account.calculateInterest();

        System.out.printf(
                "Interest added: M%.2f%nNew balance: M%.2f%n",
                amount, account.getBalance());
    }
    private static void loan()
            throws InvalidTransactionException,
            LoanRejectedException {

        BankAccount account = varifyAccount();

        if (account == null) {
            return;
        }

        if (!(account instanceof Loanable)) {
            System.out.println(
                    "Loans are unavailable for this account");
            return;
        }

        Loanable loanable = (Loanable) account;

        System.out.printf("Loan limit: M%.2f%n",
                loanable.getLoanLimit());

        loanable.requestLoan(readAmount("Loan amount: M"));

        System.out.printf("Loan approved. New balance: M%.2f%n",
                account.getBalance());
    }
}

