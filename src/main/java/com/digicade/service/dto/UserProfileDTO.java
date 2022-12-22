package com.digicade.service.dto;

import com.digicade.domain.GameBadge;
import java.util.HashSet;
import java.util.Set;

public class UserProfileDTO {

    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String email;
    private String gender;
    private String imageUrl;
    private String xp;
    private int tix;
    private int comp;
    private int credit;
    private Set<GameBadge> gameBadges = new HashSet<>();

    public UserProfileDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getXp() {
        return xp;
    }

    public void setXp(String xp) {
        this.xp = xp;
    }

    public int getTix() {
        return tix;
    }

    public void setTix(int tix) {
        this.tix = tix;
    }

    public int getComp() {
        return comp;
    }

    public void setComp(int comp) {
        this.comp = comp;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Set<GameBadge> getGameBadges() {
        return gameBadges;
    }

    public void setGameBadges(Set<GameBadge> gameBadges) {
        this.gameBadges = gameBadges;
    }
}
