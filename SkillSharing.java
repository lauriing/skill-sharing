package SkillSharing;

import java.util.ArrayList;
import java.util.Scanner;

class Skill {
    String name;
    String description;

    public Skill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String toString() {
        return name + (description.isEmpty() ? "" : " (" + description + ")");
    }
}

class User {
    String name;
    String discordUsername;
    ArrayList<Skill> skillsOffered = new ArrayList<>();
    ArrayList<String> skillsWanted = new ArrayList<>();

    public User(String name, String discordUsername) {
        this.name = name;
        this.discordUsername = discordUsername;
    }

    public void addSkillOffered(Skill skill) {
        skillsOffered.add(skill);
    }

    public void addSkillWanted(String skill) {
        skillsWanted.add(skill);
    }

    public String toString() {
        return "Name: " + name + " | Discord: " + discordUsername +
                "\n  Offers: " + skillsOffered +
                "\n  Wants: " + skillsWanted;
    }
}

public class SkillSharing {
    private static ArrayList<User> users = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nIndividualis");
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
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your Discord Username: ");
        String discord = scanner.nextLine();

        User user = new User(name, discord);

        System.out.print("How many skills do you offer? ");
        int offerCount = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < offerCount; i++) {
            System.out.print("Enter skill offered #" + (i + 1) + ": ");
            String skillName = scanner.nextLine();
            System.out.print("Enter description for this skill: ");
            String desc = scanner.nextLine();
            user.addSkillOffered(new Skill(skillName, desc));
        }

        System.out.print("How many skills do you want? ");
        int wantCount = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < wantCount; i++) {
            System.out.print("Enter skill wanted #" + (i + 1) + ": ");
            String skillWanted = scanner.nextLine();
            user.addSkillWanted(skillWanted);
        }

        users.add(user);
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
            String skillName = scanner.nextLine();
            System.out.print("Enter description for this skill: ");
            String desc = scanner.nextLine();
            user.addSkillOffered(new Skill(skillName, desc));
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
                    for (Skill skill : provider.skillsOffered) {
                        if (provider != seeker && skill.name.equalsIgnoreCase(wanted)) {
                            System.out.println(seeker.name + " wants " + wanted + " - Match: " + provider.name + " (" + skill + ")");
                            found = true;
                        }
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