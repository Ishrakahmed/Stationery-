import java.io.*;
import java.util.*;

// Product Class
class Book implements Serializable {
    private String name;
    private float quantity;
    private float price;

    public Book (String name, float quantity, float price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Quantity: %.2f, Price: %.2f", name, quantity, price);
    }
}

// User Class
class User implements Serializable {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
}

// Main System Class
public class StationaryShop {
    private static final String Books_FILE = "Book.dat";
    private static final String USERS_FILE = "User.dat";

    private static List<Book> books = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();
        boolean loggedIn = false;

        while (true) {
            if (!loggedIn) {
                displayLoginMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        loggedIn = loginUser();
                        break;
                    case 0:
                        saveData();
                        System.out.println("Exiting...");
			System.out.println("Exited");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                displayBookMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        updateBook();
                        break;
                    case 3:
                        sellBook();
                        break;
                    case 4:
                        searchBook();
                        break;
                    case 5:
                        displayProducts();
                        break;
                    case 0:
                        saveData();
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private static void displayLoginMenu() {
        System.out.println("------- Stationary Shop --------");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.println("--------------------------------");
    }

    private static void displayBookMenu() {
        System.out.println("------- Stationary Shop --------");
        System.out.println("1. Add Book");
        System.out.println("2. Update Book");
        System.out.println("3. Sell Book");
        System.out.println("4. Search Book");
        System.out.println("5. Display Book");
        System.out.println("0. Exit");
        System.out.println("---------------------------------");
    }

    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        users.add(new User(username, password));
        System.out.println("Registration successful.");
    }

    private static boolean loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.validatePassword(password)) {
                System.out.println("Login successful. Welcome, " + username + "!");
                return true;
            }
        }
        System.out.println("Invalid credentials.");
        return false;
    }

    private static void addBook() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product quantity: ");
        float quantity = scanner.nextFloat();
        System.out.print("Enter product price: ");
        float price = scanner.nextFloat();
        scanner.nextLine(); // Consume newline

        books.add(new Book(name, quantity, price));
        System.out.println("Book added successfully.");
    }

    private static void updateBook() {
        System.out.print("Enter the name of the book to update: ");
        String name = scanner.nextLine();

        for (Book book : books) {
            if (book.getName().equalsIgnoreCase(name)) {
                System.out.print("Enter new quantity: ");
                book.setQuantity(scanner.nextFloat());
                System.out.print("Enter new price: ");
                book.setPrice(scanner.nextFloat());
                scanner.nextLine(); // Consume newline
                System.out.println("Book updated successfully.");
                return;
            }
        }

        System.out.println("Book not found.");
    }

    private static void sellBook() {
        System.out.print("Enter the name of the book to delete: ");
        String name = scanner.nextLine();

        books.removeIf(book -> book.getName().equalsIgnoreCase(name));
        System.out.println("Book sold successfully.");
    }

    private static void searchBook() {
        System.out.print("Enter the name of the product to search: ");
        String name = scanner.nextLine();

        for (Book book : books) {
            if (book.getName().equalsIgnoreCase(name)) {
                System.out.println(book);
                return;
            }
        }

        System.out.println("Product not found.");
    }

    private static void displayProducts() {
        System.out.println("============== Product List ==============");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void saveData() {
        saveToFile(Books_FILE, books);
        saveToFile(USERS_FILE, users);
    }

    private static void loadData() {
        books = loadFromFile(Books_FILE, Book.class);
        users = loadFromFile(USERS_FILE, User.class);
    }

    private static <T> void saveToFile(String fileName, List<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> loadFromFile(String fileName, Class<T> type) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}