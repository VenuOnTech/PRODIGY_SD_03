import java.io.*;
import java.util.*;

class Contact {
    private String name;
    private String phone;
    private String email;
    private String address;

    public Contact(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nPhone: " + phone + "\nEmail: " + email + "\nAddress: " + address;
    }
}

public class ContactManagementSystem {
    private static List<Contact> contacts = new ArrayList<>();
    private static final String FILE_NAME = "contacts.txt";
    private static final List<String> PUBLIC_EMAIL_PROVIDERS = Arrays.asList("@gmail.com", "@yahoo.com", "@outlook.com", "@hotmail.com");

    public static void main(String[] args) {
        loadContactsFromFile();

        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n=== Contact Management System ===");
            System.out.println("1. Add Contact");
            System.out.println("2. Edit Contact");
            System.out.println("3. Delete Contact");
            System.out.println("4. Search Contact");
            System.out.println("5. List All Contacts");
            System.out.println("6. Export Contacts");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addContact(sc);
                case 2 -> editContact(sc);
                case 3 -> deleteContact(sc);
                case 4 -> searchContact(sc);
                case 5 -> listAllContacts();
                case 6 -> saveContactsToFile();
                case 7 -> System.out.println("Exiting... Goodbye!");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
        sc.close();
    }

    private static void addContact(Scanner sc) {
        try {
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            validateName(name);

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();
            validatePhone(phone);

            System.out.print("Enter Email: ");
            String email = sc.nextLine();
            validateEmail(email);

            System.out.print("Enter Address: ");
            String address = sc.nextLine();

            contacts.add(new Contact(name, phone, email, address));
            System.out.println("Contact added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void editContact(Scanner sc) {
        System.out.print("Enter the name of the contact to edit: ");
        String name = sc.nextLine();
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                try {
                    System.out.print("Enter New Phone: ");
                    String phone = sc.nextLine();
                    validatePhone(phone);
                    contact.setPhone(phone);

                    System.out.print("Enter New Email: ");
                    String email = sc.nextLine();
                    validateEmail(email);
                    contact.setEmail(email);

                    System.out.print("Enter New Address: ");
                    contact.setAddress(sc.nextLine());

                    System.out.println("Contact updated successfully.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                return;
            }
        }
        System.out.println("Contact not found.");
    }

    private static void deleteContact(Scanner sc) {
        System.out.print("Enter the name of the contact to delete: ");
        String name = sc.nextLine();
        Iterator<Contact> iterator = contacts.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(name)) {
                iterator.remove();
                System.out.println("Contact deleted successfully.");
                return;
            }
        }
        System.out.println("Contact not found.");
    }

    private static void searchContact(Scanner sc) {
        System.out.print("Enter name, phone, or email to search: ");
        String query = sc.nextLine().toLowerCase();
        for (Contact contact : contacts) {
            if (contact.getName().toLowerCase().contains(query) ||
                contact.getPhone().toLowerCase().contains(query) ||
                contact.getEmail().toLowerCase().contains(query)) {
                System.out.println("\n" + contact);
                return;
            }
        }
        System.out.println("No matching contact found.");
    }

    private static void listAllContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts available.");
        } else {
            for (Contact contact : contacts) {
                System.out.println("\n" + contact);
            }
        }
    }

    private static void saveContactsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Contact contact : contacts) {
                writer.write(contact.getName() + "," +
                             contact.getPhone() + "," +
                             contact.getEmail() + "," +
                             contact.getAddress());
                writer.newLine();
            }
            System.out.println("Contacts saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    private static void loadContactsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    contacts.add(new Contact(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }

    private static void validateName(String name) {
        if (!name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Name must contain only letters and spaces.");
        }
    }

    private static void validatePhone(String phone) {
        if (!phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone must be a 10-digit number.");
        }
    }

    private static void validateEmail(String email) {
        boolean isValid = PUBLIC_EMAIL_PROVIDERS.stream().anyMatch(email::endsWith);
        if (!isValid) {
            throw new IllegalArgumentException("Email must end with a valid public email provider (e.g., @gmail.com).");
        }
    }
}
