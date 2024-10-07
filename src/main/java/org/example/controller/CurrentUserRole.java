package org.example.controller;

public class CurrentUserRole {
    private static boolean isAdmin = false;
    public static void setAdminCurrentUser(){
        isAdmin = true;
    }

    public static void setNotAdminCurrentUser(){
        isAdmin = false;
    }

    public static boolean getCurrentUserRole(){
        return isAdmin;
    }
}
