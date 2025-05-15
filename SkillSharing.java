package SkillSharing;

import java.util.ArrayList;
import java.util.Scanner;

// User class to represent a platform user
class User {
    String name;
    String email;
    String phone;
    ArrayList<String> skillsOffered = new ArrayList<>();
    ArrayList<String> skillsWanted = new ArrayList<>();

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void addSkillOffered(String skill) {
        skillsOffered.add(skill);
    }

    public void addSkillWanted(String skill) {
        skillsWanted.add(skill);
    }

    public String toString() {
        return "Name: " + name + " | Email: " + email + " | Phone: " + phone +
                "\n  Offers: " + skillsOffered + "\n  Wants: " + skillsWanted;
    }
}

public class SkillSharing {
    private static ArrayList<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nSkill Sharing Platform");
            System.out.println("1. Register User");
            System.out.println("2. Add Skill Offered");
            System.out.println("3. Add Skill Wanted");
            System.out.println("4. View All Users");
            System.out.println("5. Find Matches");
            System.out.println("6. Search User");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1: registerUser(); break;
                case 2: addSkillOffered(); break;
                case 3: addSkillWanted(); break;
                case 4: viewUsers(); break;
                case 5: findMatches(); break;
                case 6: searchUser(); break;
                case 7: System.exit(0);
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void registerUser() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        users.add(new User(name, email, phone));
        System.out.println("User registered.");
    }

    private static User findUserByName(String name) {
        for (User user : users) {
            if (user.name.equalsIgnoreCase(name)) return user;
        }
        return null;
    }

    private static void addSkillOffered() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        User user = findUserByName(name);
        if (user != null) {
            System.out.print("Enter skill to offer: ");
            String skill = scanner.nextLine();
            user.addSkillOffered(skill);
            System.out.println("Skill added.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void addSkillWanted() {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        User user = findUserByName(name);
        if (user != null) {
            System.out.print("Enter skill wanted: ");
            String skill = scanner.nextLine();
            user.addSkillWanted(skill);
            System.out.println("Skill added.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void viewUsers() {
        if (users.isEmpty()) {
            System.out.println("No users registered.");
        } else {
            for (User user : users) {
                System.out.println(user);
                System.out.println("----------------------");
            }
        }
    }

    private static void findMatches() {
        boolean found = false;
        for (User seeker : users) {
            for (String wanted : seeker.skillsWanted) {
                for (User provider : users) {
                    if (provider != seeker && provider.skillsOffered.contains(wanted)) {
                        System.out.println(seeker.name + " wants " + wanted + " - Match: " + provider.name);
                        found = true;
                    }
                }
            }
        }
        if (!found) {
            System.out.println("No matches found.");
        }
    }

    private static void searchUser() {
        System.out.print("Enter user name to search: ");
        String name = scanner.nextLine();
        User user = findUserByName(name);
        if (user != null) {
            System.out.println(user);
        } else {
            System.out.println("User not found.");
        }
    }
}