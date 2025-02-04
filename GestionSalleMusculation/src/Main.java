import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
    {
        public static void main(String[] args)
                {
            // Informations de connexion à la base de données
                    String url = "jdbc:mysql://localhost:3306/SalleMusculation";
                    String username = "root";
                    String password = "2004";
                    try
                            {
                                Class.forName("com.mysql.cj.jdbc.Driver");
                            }
                    catch (ClassNotFoundException e)
                            {
                                System.out.println(e.getMessage());
                            }
                    try
                            {
                                Connection connection = DriverManager.getConnection(url, username, password);
                                Scanner scanner =  new Scanner(System.in);
                                Client client = new Client(connection, scanner);
                                GestionClient gestionClient = new GestionClient(connection, scanner);

                                Date date = new Date();
                                LocalDate date1 = LocalDate.now();
                                System.out.println(""+date);
                                System.out.println(""+date1);

                                while(true)
                                {
                                    System.out.println("***{{{WELCOME TO SALLE DE  MUSCULATION}}}***");
                                    System.out.println();
                                    System.out.println("1. add client");
                                    System.out.println("2. Search Client");
                                    System.out.println("3. supprimer un client");
                                    System.out.println("4.afficher les clients pas encore payee");
                                    System.out.println("5.un client veut payee");
                                    System.out.println("Enter your choice: ");
                                    int choice1 = scanner.nextInt();
                                    scanner.nextLine();
                                    switch (choice1)
                                    {
                                        case 1:
                                            gestionClient.AddClient();
                                            break;
                                        case 2:
                                            System.out.println();
                                            System.out.println("donner CIN du Client");
                                            String cin = scanner.nextLine();
                                            gestionClient.AffichClient(cin);
                                            break;
                                        case 3:
                                            System.out.println("entrer le CIN du Client");
                                            String cin2 = scanner.nextLine();
                                            gestionClient.DeleteClient(cin2);
                                            break;
                                        case 4:
                                            gestionClient.Affiche_Les_Pas_Encore_Payee();
                                            break;
                                        case 5:
                                            System.out.println("entrer Cin du client");
                                            String cin3 = scanner.nextLine();
                                            gestionClient.Payement(cin3);
                                            break;
                                        default:
                                            System.out.println("Enter Valid Choice!");
                                            break;
                                    }
                                }
                            }
                    catch (SQLException e)
                            {
                                throw new RuntimeException(e);
                            }
                }
    }
