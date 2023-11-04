package com.hikmatullo.uz.tasks.task10.service;

import com.hikmatullo.uz.tasks.task10.model.User;
import com.hikmatullo.uz.tasks.task10.util.SC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hikmatullo.uz.tasks.task10.util.DB.userList;

public class UserService {

    public User login(String email, String password) {
        for (User user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public User signup() {
        System.out.println("please enter username : ");
        String username = SC.SC_STR.nextLine();
        System.out.println("please enter email : ");
        String email = SC.SC_STR.nextLine();
        while(isAuthenticated(email)) {
            System.out.println("this email has been registered please enter other email or press 0 to exit : ");
            email = SC.SC_STR.nextLine();
            if(email.equals("0")) {
                return null;
            }
        }
        while (!isEmailValid(email)) {
            System.out.print("please enter a valid email : ");
            email = SC.SC_STR.nextLine();
        }

        System.out.println("please enter password : ");
        String password = SC.SC_STR.nextLine();
        String res = isPasswordValid(password);
        while(res != null) {
            System.out.println(res);
            System.out.println("please enter password : ");
            password = SC.SC_STR.nextLine();
            res = isPasswordValid(password);
        }
        return new User(username, email, password);
    }

    private boolean isEmailValid(String email) {
        Pattern compile = Pattern.compile("^(\\w+)@([\\w-]+)\\.(\\w{2,4})");
        Matcher matcher = compile.matcher(email);
        return matcher.matches();
    }
    private String isPasswordValid(String password) {
        Pattern compile = Pattern.compile("(?=.*[a-zA-Z]+)(?=.*[\\d]+).{8,30}");
        Matcher matcher = compile.matcher(password);
        if(!matcher.matches()) {
            return "password length must be at least 8 characters, and contain numbers and letters";
        }

        return null;

    }
    public boolean isAuthenticated(String email) {
        for(User user : userList) {
            if(user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
