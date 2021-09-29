package ru.javaprojects.rewardcalculator.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    BOOK_KEEPER,
    ECONOMIST,
    REWARD_MAKER,
    ADMIN;

    //https://stackoverflow.com/a/19542316/548473
    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}