// package com.example.app;

// import com.example.app.controller.UserController;
// import com.example.app.repository.UserRepository;
// import com.example.app.service.UserService;
// import java.nio.file.Path;

// public class Application {
//     public static void main(String[] args) {
    
//         Path filePath = Path.of("data", "users.txt");
//         System.out.println(filePath.toAbsolutePath().toString());
//         UserRepository repo = new UserRepository(filePath);
//         UserService service = new UserService(repo);
//         UserController controller = new UserController(service);

//         var u1 = controller.registerUser("Alice", "alice@example.com");
//         var u2 = controller.registerUser("Bob", "bob@example.com");
//         var u3 = controller.registerUser("Charlie", "charlie@example.com");

//         System.out.println("Created: " + u1.getId());
//         System.out.println("Created: " + u2.getId());
//         System.out.println("Created: " + u3.getId());

//         System.out.println("\nAll users:");
//         for (var u : controller.listUsers()) {
//             System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail());
//         }

//         System.out.println("\nDelete Bob:");
//         controller.removeUser(u2.getId());
//         for (var u : controller.listUsers()) {
//             System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail());
//         }

//         System.out.println("App started. Loaded users: " + controller.listUsers().size());

//         var id = controller.registerUser("Alice", "alice@example.com");
//         System.out.println("Created: " + id);

//         System.out.println("All users:");
//         for (var u : controller.listUsers()) {
//             System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail());
//         }
//     }
// }


package com.example.app;

import com.example.app.controller.UserController;
import com.example.app.repository.UserRepository;
import com.example.app.service.UserService;
import java.nio.file.Path;

public class Application {

    public static void main(String[] args) {
        // wiring (composition root)
        Path filePath = Path.of("data", "users.txt");
        UserRepository repo = new UserRepository(filePath);
        UserService service = new UserService(repo);
        UserController controller = new UserController(service);

        if (args.length == 0) {
            printHelp();
            return;
        }

        String cmd = args[0].toLowerCase();

        try {
            switch (cmd) {
                case "create" -> {
                    requireArgs(args, 3, "create <name> <email>");
                    String name = args[1];
                    String email = args[2];
                    String id = controller.registerUser(name, email).getId();
                    System.out.println("Created user id: " + id);
                }
                case "list" -> {
                    var users = controller.listUsers();
                    if (users.isEmpty()) {
                        System.out.println("(no users)");
                        return;
                    }
                    for (var u : users) {
                        System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail());
                    }
                }
                case "get" -> {
                    requireArgs(args, 2, "get <id>");
                    String id = args[1];
                    var u = controller.getUserById(id);
                    System.out.println(u.getId() + " | " + u.getName() + " | " + u.getEmail());
                }
                case "update-email" -> {
                    requireArgs(args, 3, "update-email <id> <newEmail>");
                    String id = args[1];
                    String newEmail = args[2];
                    controller.updateUserEmail(id, newEmail);
                    System.out.println("Updated email for id: " + id);
                }
                case "delete" -> {
                    requireArgs(args, 2, "delete <id>");
                    String id = args[1];
                    controller.removeUser(id);
                    System.out.println("Deleted: " + id);
                }
                case "help" -> printHelp();
                default -> {
                    System.out.println("Unknown command: " + cmd);
                    printHelp();
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void requireArgs(String[] args, int expected, String usage) {
        if (args.length != expected) {
            throw new IllegalArgumentException("Usage: " + usage);
        }
    }

    private static void printHelp() {
        System.out.println("""
                Commands:
                  create <name> <email>
                  list
                  get <id>
                  update-email <id> <newEmail>
                  delete <id>
                  help

                Examples:
                  create "Alice" "alice@example.com"
                  list
                  get <id>
                  update-email <id> "alice@new.com"
                  delete <id>
                """);
    }
}
