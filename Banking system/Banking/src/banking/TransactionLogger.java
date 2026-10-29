
package banking;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public final class TransactionLogger {
    private static final String FileName = "transactions.txt";
    private static final DateTimeFormatter Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TransactionLogger() { }


    public static void log(String accountNumber, String type,
                           double amount, double balance, String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FileName, true))) {
            out.printf("%s | %s | %s | %.2f | %.2f | %s%n",
                    LocalDateTime.now().format(Formatter), accountNumber, type, amount, balance, message);
        } catch (IOException e) {

            System.out.println("Failed " + FileName + " (" + e.getMessage() + ")");
        }
    }
}
