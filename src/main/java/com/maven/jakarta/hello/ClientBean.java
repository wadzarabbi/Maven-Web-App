package com.maven.jakarta.hello;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "clientBean")
@SessionScoped
public class ClientBean {

    private List<Client> clients = new ArrayList<>();
    private Client newClient = new Client();  

    // Getter and Setter for clients and newClient
    public List<Client> getClients() {
        if (clients.isEmpty()) {
            loadClients();
        }
        return clients;
    }

    public Client getNewClient() {
        return newClient;
    }

    public void setNewClient(Client newClient) {
        this.newClient = newClient;
    }

    // Load clients from database Function
    private void loadClients() {
        String url = "jdbc:postgresql://localhost:5432/newdb";
        String user = "mydb";  // PostgreSQL user
        String password = "mypassword";  // PostgreSQL password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM \"client\"";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    Client client = new Client();
                    client.setClientId(rs.getInt("client_id"));
                    client.setFirstName(rs.getString("first_name"));
                    client.setLastName(rs.getString("last_name"));
                    client.setAge(rs.getInt("age"));
                    client.setEmail(rs.getString("email"));
                    client.setAddress(rs.getString("address"));
                    client.setSalary(rs.getInt("salary"));
                    clients.add(client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update client in database Function
    public void updateClient() {
        String url = "jdbc:postgresql://localhost:5432/newdb";
        String user = "mydb";
        String password = "mypassword";

        String sql = "UPDATE \"client\" SET first_name = ?, last_name = ?, age = ?, email = ?, address = ?, salary = ? WHERE client_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newClient.getFirstName());
            stmt.setString(2, newClient.getLastName());
            stmt.setInt(3, newClient.getAge());
            stmt.setString(4, newClient.getEmail());
            stmt.setString(5, newClient.getAddress());
            stmt.setInt(6, newClient.getSalary());
            stmt.setInt(7, newClient.getClientId());

            stmt.executeUpdate();

            // Reload clients list after update
            clients.clear();
            loadClients();
            newClient = new Client();  

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete client from database
    public void deleteClient(Client client) {
        String url = "jdbc:postgresql://localhost:5432/newdb";
        String user = "mydb";
        String password = "mypassword";

        String sql = "DELETE FROM \"client\" WHERE client_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, client.getClientId());

            stmt.executeUpdate();

            // Reload clients list after delete
            clients.clear();
            loadClients();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    //Add New Function
    public void addClient() {
        String url = "jdbc:postgresql://localhost:5432/newdb";
        String user = "mydb"; 
        String password = "mypassword";  

        // SQL query to insert a new client
        String sql = "INSERT INTO \"client\" (first_name, last_name, email, address, age, salary) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newClient.getFirstName());
            stmt.setString(2, newClient.getLastName());
            stmt.setString(3, newClient.getEmail());
            stmt.setString(4, newClient.getAddress());
            stmt.setInt(5, newClient.getAge());
            stmt.setInt(6, newClient.getSalary());

            // Execute the insert
            stmt.executeUpdate();

            // Clear clients list and reload
            clients.clear();
            loadClients();

            // Optionally, clear the form after submission
            newClient = new Client(); 

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


