package SkillSharing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Skill {
    String name;
    String description;
    String category;

    public Skill(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category != null && !category.isEmpty() ? category : "Other";
    }

    public Skill(String name, String description) {
        this(name, description, "Other");
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
    int ratingCount = 0;
    double ratingAverage = 0.0;

    public User(String name, String discordUsername) {
        this.name = name;
        this.discordUsername = discordUsername;
    }

    public void addSkillOffered(Skill skill) {
        // Check if skill already exists
        for (Skill existingSkill : skillsOffered) {
            if (existingSkill.name.equalsIgnoreCase(skill.name)) {
                return; // Skip if skill already exists
            }
        }
        skillsOffered.add(skill);
    }

    public void addSkillWanted(String skill) {
        // Check if skill already exists
        for (String existingSkill : skillsWanted) {
            if (existingSkill.equalsIgnoreCase(skill)) {
                return; // Skip if skill already exists
            }
        }
        skillsWanted.add(skill);
    }

    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5");
            return;
        }
        
        ratingCount++;
        double totalScore = ratingAverage * (ratingCount - 1) + rating;
        ratingAverage = totalScore / ratingCount;
    }

    public String getRatingInfo() {
        if (ratingCount == 0) {
            return "No ratings yet";
        }
        return String.format("Rating: %.1f/5 (%d reviews)", ratingAverage, ratingCount);
    }

    public String toString() {
        return "Name: " + name + " | Discord: " + discordUsername +
                "\n  " + getRatingInfo() +
                "\n  Offers: " + skillsOffered +
                "\n  Wants: " + skillsWanted;
    }
}

public class SkillSharing {
    private static ArrayList<User> users = new ArrayList<>();
    private static Map<String, ArrayList<Skill>> skillsDatabase = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    static {
        // Initialize with some default users
        User ian = new User("Ian Byczek", "ian");
        ian.addSkillOffered(new Skill("Nursing", ""));
        users.add(ian);

        User marlon = new User("Marlon Castanon", "marlon");
        marlon.addSkillOffered(new Skill("Blacksmith", "", "Crafts"));
        users.add(marlon);

        User wesley = new User("Wesley Zhang", "wesley");
        wesley.addSkillOffered(new Skill("Cooking", "", "Cooking"));
        users.add(wesley);

        User chester = new User("Chester Duong", "chester");
        chester.addSkillOffered(new Skill("Those who know", ""));
        users.add(chester);

        User juan = new User("Juan Zombrano", "juan");
        juan.addSkillOffered(new Skill("Serving", ""));
        users.add(juan);
        
        // Update skills database
        updateSkillsDatabase();
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nIndividualis");
            System.out.println("1. Register User");
            System.out.println("2. Add Skill Offered");
            System.out.println("3. Add Skill Wanted");
            System.out.println("4. View All Users");
            System.out.println("5. Find Matches");
            System.out.println("6. Search Skill");
            System.out.println("7. Rate User");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1: registerUser(); break;
                case 2: addSkillOffered(); break;
                case 3: addSkillWanted(); break;
                case 4: viewUsers(); break;
                case 5: findMatches(); break;
                case 6: searchSkill(); break;
                case 7: rateUser(); break;
                case 8: System.exit(0);
                default: System.out.println("Invalid option.");
            }
        }
    }

    private static void registerUser() {
        try {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();
            
            if (name.length() < 2) {
                System.out.println("Name must be at least 2 characters.");
                return;
            }
            
            // Check if user already exists
            if (findUserByName(name) != null) {
                System.out.println("A user with that name already exists.");
                return;
            }
            
            System.out.print("Enter your Discord Username: ");
            String discord = scanner.nextLine().trim();
            
            if (discord.isEmpty()) {
                System.out.println("Discord username is required.");
                return;
            }

            User user = new User(name, discord);

            System.out.print("How many skills do you offer? ");
            int offerCount;
            try {
                offerCount = Integer.parseInt(scanner.nextLine());
                if (offerCount < 0) {
                    System.out.println("Number of skills can't be negative.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
                return;
            }
            
            for (int i = 0; i < offerCount; i++) {
                System.out.print("Enter skill offered #" + (i + 1) + ": ");
                String skillName = scanner.nextLine().trim();
                
                if (skillName.isEmpty()) {
                    System.out.println("Skill name cannot be empty. Skipping.");
                    continue;
                }
                
                System.out.print("Enter description for this skill: ");
                String desc = scanner.nextLine();
                
                System.out.print("Enter category for this skill (or leave blank for 'Other'): ");
                String category = scanner.nextLine();
                
                user.addSkillOffered(new Skill(skillName, desc, category));
            }

            System.out.print("How many skills do you want? ");
            int wantCount;
            try {
                wantCount = Integer.parseInt(scanner.nextLine());
                if (wantCount < 0) {
                    System.out.println("Number of skills can't be negative.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
                return;
            }
            
            for (int i = 0; i < wantCount; i++) {
                System.out.print("Enter skill wanted #" + (i + 1) + ": ");
                String skillWanted = scanner.nextLine().trim();
                
                if (skillWanted.isEmpty()) {
                    System.out.println("Skill name cannot be empty. Skipping.");
                    continue;
                }
                
                user.addSkillWanted(skillWanted);
            }

            users.add(user);
            updateSkillsDatabase();
            System.out.println("User registered successfully.");
        } catch (Exception e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    private static User findUserByName(String name) {
        for (User user : users) {
            if (user.name.equalsIgnoreCase(name)) return user;
        }
        return null;
    }

    private static void addSkillOffered() {
        try {
            System.out.print("Enter user name: ");
            String name = scanner.nextLine().trim();
            User user = findUserByName(name);
            
            if (user == null) {
                System.out.println("User not found.");
                return;
            }
            
            System.out.print("Enter skill to offer: ");
            String skillName = scanner.nextLine().trim();
            
            if (skillName.isEmpty()) {
                System.out.println("Skill name cannot be empty.");
                return;
            }
            
            // Check if user already has this skill
            for (Skill existingSkill : user.skillsOffered) {
                if (existingSkill.name.equalsIgnoreCase(skillName)) {
                    System.out.println("User already has this skill.");
                    return;
                }
            }
            
            System.out.print("Enter description for this skill: ");
            String desc = scanner.nextLine();
            
            System.out.print("Enter category for this skill (or leave blank for 'Other'): ");
            String category = scanner.nextLine();
            
            user.addSkillOffered(new Skill(skillName, desc, category));
            updateSkillsDatabase();
            System.out.println("Skill added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding skill: " + e.getMessage());
        }
    }

    private static void addSkillWanted() {
        try {
            System.out.print("Enter user name: ");
            String name = scanner.nextLine().trim();
            User user = findUserByName(name);
            
            if (user == null) {
                System.out.println("User not found.");
                return;
            }
            
            System.out.print("Enter skill wanted: ");
            String skill = scanner.nextLine().trim();
            
            if (skill.isEmpty()) {
                System.out.println("Skill name cannot be empty.");
                return;
            }
            
            // Check if user already wants this skill
            for (String existingSkill : user.skillsWanted) {
                if (existingSkill.equalsIgnoreCase(skill)) {
                    System.out.println("User already wants this skill.");
                    return;
                }
            }
            
            user.addSkillWanted(skill);
            System.out.println("Skill wanted added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding skill wanted: " + e.getMessage());
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
        try {
            boolean found = false;
            
            // Create a map for faster matching
            Map<String, ArrayList<User>> skillProviderMap = new HashMap<>();
            
            // Build map of skills to providers
            for (User provider : users) {
                for (Skill skill : provider.skillsOffered) {
                    String skillKey = skill.name.toLowerCase();
                    if (!skillProviderMap.containsKey(skillKey)) {
                        skillProviderMap.put(skillKey, new ArrayList<>());
                    }
                    skillProviderMap.get(skillKey).add(provider);
                }
            }
            
            // Find matches by looking up wanted skills in the map
            for (User seeker : users) {
                for (String wanted : seeker.skillsWanted) {
                    String wantedKey = wanted.toLowerCase();
                    
                    if (skillProviderMap.containsKey(wantedKey)) {
                        for (User provider : skillProviderMap.get(wantedKey)) {
                            // Skip if seeker and provider are the same person
                            if (seeker.name.equals(provider.name)) continue;
                            
                            // Find the matching skill with details
                            Skill matchingSkill = null;
                            for (Skill skill : provider.skillsOffered) {
                                if (skill.name.equalsIgnoreCase(wanted)) {
                                    matchingSkill = skill;
                                    break;
                                }
                            }
                            
                            if (matchingSkill != null) {
                                System.out.println(seeker.name + " wants " + wanted + " - Match: " + 
                                                   provider.name + " (" + matchingSkill + ")");
                                found = true;
                            }
                        }
                    }
                }
            }
            
            if (!found) {
                System.out.println("No matches found.");
            }
        } catch (Exception e) {
            System.out.println("Error finding matches: " + e.getMessage());
        }
    }

    private static void searchSkill() {
        try {
            System.out.print("Enter skill to search for: ");
            String skillSearch = scanner.nextLine().trim();
            
            if (skillSearch.isEmpty()) {
                System.out.println("Skill name cannot be empty.");
                return;
            }
            
            boolean found = false;
            for (User user : users) {
                for (Skill skill : user.skillsOffered) {
                    if (skill.name.equalsIgnoreCase(skillSearch)) {
                        System.out.println(user);
                        System.out.println("----------------------");
                        found = true;
                        break;
                    }
                }
            }
            
            if (!found) {
                System.out.println("No users found offering that skill.");
            }
        } catch (Exception e) {
            System.out.println("Error searching for skill: " + e.getMessage());
        }
    }
    
    private static void rateUser() {
        try {
            System.out.print("Enter user name to rate: ");
            String name = scanner.nextLine().trim();
            User user = findUserByName(name);
            
            if (user == null) {
                System.out.println("User not found.");
                return;
            }
            
            System.out.print("Enter rating (1-5): ");
            int rating;
            try {
                rating = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating. Please enter a number between 1 and 5.");
                return;
            }
            
            if (rating < 1 || rating > 5) {
                System.out.println("Rating must be between 1 and 5.");
                return;
            }
            
            user.addRating(rating);
            System.out.println("Rating added successfully.");
            System.out.println("User's current rating: " + user.getRatingInfo());
        } catch (Exception e) {
            System.out.println("Error rating user: " + e.getMessage());
        }
    }
    
    private static void updateSkillsDatabase() {
        // Reset database
        skillsDatabase.clear();
        
        // Build database from all users' skills
        for (User user : users) {
            for (Skill skill : user.skillsOffered) {
                String category = skill.category != null ? skill.category : "Other";
                if (!skillsDatabase.containsKey(category)) {
                    skillsDatabase.put(category, new ArrayList<>());
                }
                
                boolean exists = false;
                for (Skill existingSkill : skillsDatabase.get(category)) {
                    if (existingSkill.name.equalsIgnoreCase(skill.name)) {
                        exists = true;
                        break;
                    }
                }
                
                if (!exists) {
                    skillsDatabase.get(category).add(skill);
                }
            }
        }
    }
}
