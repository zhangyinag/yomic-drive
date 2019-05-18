package com.yomic.drive.helper;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class ContextHelper {
    public static UserDetails getCurrentUser() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(p instanceof UserDetails) return (UserDetails)p;
        return null;
    }

    public static String getCurrentUsername() {
        UserDetails user = getCurrentUser();
        return user != null ? user.getUsername() : "anonymous";
    }
}
