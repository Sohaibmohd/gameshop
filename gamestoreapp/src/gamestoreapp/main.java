package gamestoreapp;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        try {
            dbcode dbc = new dbcode();
            int ch = 0;
            int cho = 0;
            boolean isAdmin = false;
            String Platform = "";
            String user = "";
            float price = 0;

            
            if (!dbc.login("admin", "admin123")) {
                boolean reg = dbc.register("admin", "admin123", "-", "-");
                System.out.println(reg ? "Admin registered!" : "Failed to register admin.");
            } else {
                System.out.println("Admin already exists.");
            }


            System.out.println("\n!--------Welcome to the Game Store--------!");
            while (true) {
                System.out.println("1. Register\n2. Login\n3. Exit");
                int loginChoice = sc.nextInt();
                sc.nextLine();
                
                if (loginChoice == 1) {
                    System.out.println("Register using:\n1. Phone Number\n2. Email");
                    int regChoice = sc.nextInt();
                    sc.nextLine();

                    String contactInfo = "";
                    String email = "";
                    String mobile = "";

                    if (regChoice == 1) {
                        System.out.println("Enter Phone Number (10 digits):");
                        contactInfo = sc.nextLine();
                        if (!contactInfo.matches("\\d{10}")) {
                            System.out.println("Invalid phone number. Registration failed.");
                            continue;
                        }
                        mobile = contactInfo;
                        email = "-";
                    } else if (regChoice == 2) {
                        System.out.println("Enter Email Address:");
                        contactInfo = sc.nextLine();
                        if (!contactInfo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                            System.out.println("Invalid email format. Registration failed.");
                            continue;
                        }
                        email = contactInfo;
                        mobile = "-";
                    }

                    System.out.println("Enter Username:");
                    String newUser = sc.nextLine();
                    System.out.println("Enter Password (must be at least 8 characters):");
                    String newPassword = sc.nextLine();

                    if (newPassword.length() < 8) {
                        System.out.println("Password too short. Registration failed.");
                        continue;
                    }

                    if (dbc.register(newUser, newPassword, email, mobile)) {
                        System.out.println("Registration Successful! Please login.");
                    } else {
                        System.out.println("Registration failed.");
                    }
                }
                else if (loginChoice == 2) {
                    System.out.println("Enter Username:");
                    String username = sc.nextLine();
                    System.out.println("Enter Password:");
                    String password = sc.nextLine();

                    if (dbc.login(username, password)) {
                        System.out.println("Login Successful");
                        user = username;
                        isAdmin = username.equals("admin");
                        break;
                    } else {
                        System.out.println("Invalid Login Credentials");
                    }
                }
                	else if (loginChoice == 3) {
                    System.out.println("Exiting...");
                    return;
                } else {
                    System.out.println("Invalid Choice. Try Again.");
                }
            }
            
            System.out.println("Select gaming platform ");
            System.out.println("1. PS5\n2. PC\n3. Xbox");
            cho = sc.nextInt();
            sc.nextLine();

            if (cho == 1) {
                Platform = "PS5";
            } else if (cho == 2) {
                Platform = "PC";
            } else if (cho == 3) {
                Platform = "Xbox";
            } else {
                System.out.println("Invalid choice");
            }

            dbc.create(Platform);
            while (true) {
                System.out.println("-----Games catalogue for " + Platform + " ----");
                
                if (isAdmin) {
                    System.out.println("1. Add Games\n2. View All Games\n3. Edit Game Price\n4. Remove Games\n0. Exit");
                } else {
                    System.out.println("1. View Games\n2. Buy\n3. Purchase History\n4. Generate Bill\n5. Exit");
                }

                ch = sc.nextInt();
                sc.nextLine();

                if (isAdmin) {
                    if (ch == 1) {
                        System.out.println("Enter game name:");
                        String gameName = sc.nextLine();
                        System.out.println("Enter game price (must be positive):");
                        price = sc.nextFloat();
                        if (price < 0) {
                            System.out.println("Invalid price. Cannot add game.");
                        } else {
                            int r = dbc.add(Platform, gameName, price);
                            System.out.println(r != 0 ? "Game saved successfully" : "Failed to save game");
                        }
                    } else if (ch == 2) {
                        dbc.view(Platform);
                    } else if (ch == 3) {
                        System.out.println("Enter game ID:");
                        int id = sc.nextInt();
                        System.out.println("Enter new price (must be positive):");
                        price = sc.nextFloat();
                        if (price < 0) {
                            System.out.println("Invalid price. Cannot update.");
                        } else {
                            System.out.println(dbc.edit(Platform, id, price) != 0 ? "Price modified successfully" : "Failed to modify price");
                        }
                    } else if (ch == 4) {
                        System.out.println("Enter game ID:");
                        int id = sc.nextInt();
                        System.out.println(dbc.remove(Platform, id) != 0 ? "Game removed successfully" : "Failed to remove game");
                    } else if (ch == 0) {
                        System.out.println("Thank you for using the Game Store. Goodbye!");
                        break;
                  
                    }
                } else {
                    
                	if (ch == 1) {
                        dbc.view(Platform);
                   
                    } else if (ch == 2) {
                        dbc.view(Platform);
                        ArrayList<Integer> selectedGames = new ArrayList<>();
                        while (true) {
                            System.out.println("Enter Game Id to add to cart (or 0 to finish):");
                            int id = sc.nextInt();
                            if (id == 0) break;
                            selectedGames.add(id);
                        }
                        for (int gameId : selectedGames) {
                            System.out.println(dbc.buyGame(user, Platform, gameId) != 0 ? "Game Purchased Successfully" : "Failed Transaction");
                        } } else if (ch == 3) {
                        dbc.viewPurchase(user);
                    } else if (ch == 4) {
                        dbc.bill(user, Platform);
                    } else if (ch == 5) {
                        System.out.println("Logging out! Goodbye!");
                        return;
                    }
                }
                
            }
        } catch (SQLException e) {
            System.out.println("Database Error: " + e);
        } catch (InputMismatchException e) {
            System.out.println("Input Error: Please enter valid values");
        } finally {
            sc.close();
        }
    }
}
