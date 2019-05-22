package com.yomic.drive.helper;

import com.yomic.drive.domain.Role;
import com.yomic.drive.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class ContextHelper {
    public static User getCurrentUser() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(p instanceof User) return (User) p;
        return null;
    }

    public static String getCurrentUsername() {
        UserDetails user = getCurrentUser();
        return user != null ? user.getUsername() : "anonymous";
    }

    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }
}
