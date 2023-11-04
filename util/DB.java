package com.hikmatullo.uz.tasks.task10.util;

import com.hikmatullo.uz.tasks.task10.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DB implements Serializable {

    private static final String FILE_NAME = "src/users.ser";

    public static void save(User user) {
        userList.add(user);
        saveUsersToFile();
    }

    public static List<User> loadUsers() {
        File file = new File(FILE_NAME);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {
                return (List<User>) is.readObject();
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<User>();
    }

    private static void saveUsersToFile() {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            os.writeObject(userList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> userList = loadUsers(); // Initialize userList by loading from file
}
