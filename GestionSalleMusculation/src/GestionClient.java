import javax.sql.StatementEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class GestionClient
    {
        protected Connection connection;
        protected Scanner scanner;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        public GestionClient(Connection connection, Scanner scanner)
                {
                    this.connection = connection;
                    this.scanner = scanner;
                }
                Boolean ClientExist(String cin) throws SQLException
                        {
                            String query = "SELECT * FROM Tclients WHERE CIN = ?";
                            try
                                    {
                                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                                        preparedStatement.setString(1, cin);
                                        ResultSet resultSet = preparedStatement.executeQuery();
                                        if (resultSet.next())
                                            return true;
                                        else
                                            return false;
                                    }
                            catch (SQLException e)
                                    {
                                        System.out.println(e);
                                    }
                             return false;
                        }
                        public void AddClient() throws SQLException
                                {
                                    System.out.println("Enter client full full_name: ");
                                    String full_name = scanner.nextLine();
                                    System.out.println("Entrer CIN");
                                    String CIN = scanner.nextLine();
                                    System.out.print("Enter client email: ");
                                    String email = scanner.nextLine();
                                    System.out.print("Enter telephone number: ");
                                    String Ntelephone = scanner.nextLine();
                                    LocalDate date_inscription = LocalDate.now()  ;
                                    java.sql.Date sqlDate = java.sql.Date.valueOf(date_inscription);
                                    Boolean statut = true;
                                    if (!ClientExist(CIN))
                                    {
                                        try
                                                {

                                                    String InsertClient = "INSERT INTO Tclients(full_name, CIN, email, statut, Ntelephone, date_inscription) VALUES (?, ?, ?, ?, ?, ?)";
                                                    PreparedStatement preparedStatement = connection.prepareStatement(InsertClient);
                                                    preparedStatement.setString(1, full_name);
                                                    preparedStatement.setString(2, CIN);
                                                    preparedStatement.setString(3, email);
                                                    preparedStatement.setBoolean(4, statut);
                                                    preparedStatement.setString(5, Ntelephone);
                                                    preparedStatement.setDate(6, sqlDate);
                                                    int rowsAffected = preparedStatement.executeUpdate();
                                                    if (rowsAffected > 0)
                                                        System.out.println("Client added successfully");
                                                     else
                                                        throw new SQLException("Client could not be added");
                                                }
                                        catch (SQLException e)
                                        {
                                            System.out.println(e);
                                        }
                                    }
                                    else
                                        System.out.println("Client Already Exist");
                                        return;
                                }

                                public Void DeleteClient(String cin) throws SQLException
                                            {
                                                String delete = "DELETE FROM Tclients WHERE CIN = ?";
                                                try
                                                        {

                                                            PreparedStatement preparedStatement = connection.prepareStatement(delete);
                                                            preparedStatement.setString(1, cin);
                                                            int rowsAffected = preparedStatement.executeUpdate();
                                                            if (rowsAffected > 0)
                                                                System.out.println("Client deleted successfully");
                                                            else
                                                                System.out.println("on a pas un client avec un cin: " + cin);
                                                        }
                                                catch (SQLException e)
                                                        {
                                                            throw new SQLException(e.getMessage());
                                                        }
                                                return null;
                                            }

                                            public void Affiche_Les_Pas_Encore_Payee() throws SQLException
                                                        {
                                                            updateStatut();
                                                            String query = "SELECT * FROM Tclients WHERE statut=0";
                                                            Statement statement = connection.prepareStatement(query) ;
                                                            ResultSet resultSet = statement.executeQuery(query);
                                                            System.out.println("les clients qui n'est pas encore payee est:");
                                                            System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{");
                                                            while (resultSet.next())
                                                            {
                                                                String full_name = resultSet.getString(1); // Adaptez le nom de la colonne
                                                                // ... récupérer et afficher les autres attributs
                                                                System.out.println("full_name:" + full_name);
                                                            }
                                                                System.out.println("}}}}}}}}}}}}}}}}}}}}}}}}}}");
                                                        }

                                                        void updateStatut() throws SQLException
                                                                {
                                                                    String select = "SELECT CIN, date_inscription FROM Tclients";
                                                                    try (PreparedStatement selectStatement = connection.prepareStatement(select);
                                                                         ResultSet resultSet = selectStatement.executeQuery())
                                                                            {
                                                                                while (resultSet.next())
                                                                                {
                                                                                    String cin = resultSet.getString("CIN");
                                                                                    Date date_inscription = resultSet.getDate("date_inscription");
                                                                                    if (date_inscription != null)
                                                                                    { // Vérifier si la date d'inscription est nulle
                                                                                        LocalDate currentDate = LocalDate.now();
                                                                                        Period period = Period.between(resultSet.getDate("date_inscription").toLocalDate(), currentDate);
                                                                                        int monthsSinceInscription = period.getMonths() + period.getYears() * 12;
                                                                                        if (monthsSinceInscription >= 1)
                                                                                        {
                                                                                            String update = "UPDATE Tclients SET statut = false WHERE CIN = ?";
                                                                                            try (PreparedStatement updateStatement = connection.prepareStatement(update))
                                                                                                    {
                                                                                                        updateStatement.setString(1, cin);
                                                                                                        updateStatement.executeUpdate();
                                                                                                    }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                    catch (SQLException e)
                                                                    {
                                                                        System.err.println("Erreur lors de la mise à jour des statuts : " + e.getMessage());
                                                                        throw e; // Rejeter l'exception après l'avoir enregistrée
                                                                    }
                                                                }

                                                                public void Payement(String cin) throws SQLException
                                                                            {
                                                                               try
                                                                                        {
                                                                                           if(ClientExist(cin))
                                                                                           {
                                                                                               String payee ="Select * from Tclients where CIN = ?";
                                                                                               PreparedStatement preparedStatement1 = connection.prepareStatement(payee);
                                                                                               preparedStatement1.setString(1, cin);
                                                                                               ResultSet resultSet = preparedStatement1.executeQuery();
                                                                                               if(resultSet.next())
                                                                                               {
                                                                                                   Boolean statut = resultSet.getBoolean(4);
                                                                                                    if(statut)
                                                                                                   {
                                                                                                       java.sql.Date date = resultSet.getDate(6);
                                                                                                       LocalDate localDate = date.toLocalDate();
                                                                                                       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");
                                                                                                       String dateFormatee = localDate.format(formatter);
                                                                                                       System.out.println("C'est pas role de " + resultSet.getString(1) + " pour le payement jusqu'a" +dateFormatee);
                                                                                                       return;
                                                                                                   }
                                                                                               }
                                                                                               LocalDate New_date_dinscription = LocalDate.now()  ;
                                                                                               java.sql.Date sqlDate = java.sql.Date.valueOf(New_date_dinscription);
                                                                                               String query = "UPDATE Tclients SET statut = true , date_inscription = ? WHERE CIN = ?";
                                                                                               PreparedStatement preparedStatement = connection.prepareStatement(query);
                                                                                               preparedStatement.setString(2, cin);
                                                                                               preparedStatement.setDate(1, sqlDate);
                                                                                               preparedStatement.executeUpdate();
                                                                                           }
                                                                                           else
                                                                                               System.out.println("Client not exist");
                                                                                       }
                                                                               catch (Exception e)
                                                                               {
                                                                                   throw new RuntimeException("payement failed");
                                                                               }

                                                                            }

                                                                            void AffichClient(String cin) throws SQLException
                                                                                   {
                                                                                        String query = "SELECT * FROM Tclients WHERE CIN = ?";
                                                                                        try
                                                                                                {
                                                                                                    if(ClientExist(cin))
                                                                                                    {
                                                                                                        System.out.println("Client Exist");
                                                                                                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                                                                                                        preparedStatement.setString(1, cin);
                                                                                                        ResultSet resultSet = preparedStatement.executeQuery();
                                                                                                        if (resultSet.next())
                                                                                                        {
                                                                                                            String full_name = resultSet.getString(1); // Adaptez le nom de la colonne
                                                                                                            String CIN = resultSet.getString(2);// Adaptez le nom de la colonne
                                                                                                            String email = resultSet.getString(3);
                                                                                                            Boolean statut = resultSet.getBoolean(4);
                                                                                                            String telephone = resultSet.getString(5);
                                                                                                            Date date = resultSet.getDate(6);

                                                                                                            // ... récupérer et afficher les autres attributs
                                                                                                            System.out.println("full_name:" + full_name);
                                                                                                            System.out.println("CIN:" + CIN);
                                                                                                            System.out.println("email:" + email);
                                                                                                            System.out.println("status:" + statut);
                                                                                                            System.out.println("telephone:" + telephone);
                                                                                                            System.out.println("date:" + date);
                                                                                                        }
                                                                                                    }
                                                                                                    else
                                                                                                            System.out.println("Client not exist");
                                                                                                    }
                                                                                        catch (SQLException e)
                                                                                        {
                                                                                            System.out.println(e);
                                                                                        }

                                                                                   }
    }



