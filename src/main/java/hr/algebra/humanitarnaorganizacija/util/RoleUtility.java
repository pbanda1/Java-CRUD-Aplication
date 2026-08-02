package hr.algebra.humanitarnaorganizacija.util;

import hr.algebra.humanitarnaorganizacija.model.AppUser;

public class RoleUtility {
    private static AppUser currentUser;

    private RoleUtility() {
    }

    public static AppUser getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(AppUser currentUser) {
        RoleUtility.currentUser = currentUser;
    }
    public static void clearCurrentUser() {
        currentUser = null;
    }
    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == AppUser.Role.Admin;
    }
}
