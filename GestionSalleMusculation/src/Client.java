import java.sql.Connection;
import java.util.Scanner;

public class Client
{
    Connection connection;
    Scanner scanner;

    public Client(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

}
