package com.hikmatullo.uz.tasks.task10.run;

import com.hikmatullo.uz.tasks.task10.model.User;
import com.hikmatullo.uz.tasks.task10.service.UserService;
import com.hikmatullo.uz.tasks.task10.util.DB;
import com.hikmatullo.uz.tasks.task10.util.SC;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Logger;

import static com.hikmatullo.uz.tasks.task10.util.DB.userList;

public class App {
    private static User currentUser = null;

    public static void run() {
        while(true) {
            UserService service = new UserService();
            System.out.println("""
                    welcome to chat app
                    1) login
                    2) sign up
                    3 exit""");
            String choice = SC.SC_STR.nextLine();
            while (!choice.equals("2") && !choice.equals("1") && !choice.equals("3")) {
                System.out.println("Please choose right option");
                choice = SC.SC_STR.nextLine();
            }
            switch (choice) {
                case "1" -> {
                    int i = 0;
                    while (currentUser == null && ++i <= 3) {
                        System.out.println("please enter your email");
                        String email = SC.SC_STR.nextLine();

                        System.out.println("please enter your password");
                        String password = SC.SC_STR.nextLine();
                        currentUser = service.login(email, password);
                        if (currentUser == null) {
                            System.out.println("we could not find such user, please try again");
                        }
                    }
                }
                case "2" -> {
                    while (currentUser == null) {
                        currentUser = service.signup();
                    }
                    DB.save(currentUser);
                    Logger logger = Logger.getLogger("myLogger");
                    logger.info("signed up successfully");
                }
                case "3" -> {
                    return;
                }
            }
            menu(currentUser);

        }

    }
    public static void menu(User user) {
        loop:while (user != null) {
            System.out.println("""
                        1) send message
                        2) see messages
                        3) log out
                        4) exit""");
            int option = SC.SC_INT.nextInt();
            switch (option) {
                case 1 -> {
                    System.out.println("enter receiver email address");
                    String email = SC.SC_STR.nextLine();
                    sendMessage(email);
                }
                case 2 -> {
                    printMessages(currentUser);
                }
                case 3 -> {
                }
                case 4 -> {
                    currentUser = null;
                    break loop;
                }
            }
        }
    }
    private static void printMessages(User user) {
        Map<String, String> messages = user.getMessages();
        for(Map.Entry<String, String> entry : messages.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(value.equals(user.getEmail())) {
                System.out.printf("%50s", key);
            }
        }
    }
    private static void sendMessage(String email) {
        UserService service = new UserService();
        boolean authenticated = service.isAuthenticated(email);
        User receiver = null;
        for(User user : userList) {
            if(user.getEmail().equals(email)) {
                receiver = user;
                break;
            }
        }
        if (receiver == null) {
            System.out.println("we could not find the user");
        } else {
            System.out.println("enter message");
            String message = SC.SC_STR.nextLine();
            receiver.getMessages().put("%s [%s %tF %n]".formatted(message, receiver.getUsername(), LocalDateTime.now()),
                    receiver.getEmail());
            Logger logger = Logger.getLogger("myLogger");
            logger.info("message sent successfully");
        }
    }
}
