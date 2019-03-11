package com.learning.cyberkom.cyberkomlearningfix.model;

public class AkunUser {

    int id;
    String username;
    String email;
    String password;
    String level;
    String foto;


    public AkunUser(){

    }

    public AkunUser(int id, String username, String email, String password, String level, String foto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.level = level;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
