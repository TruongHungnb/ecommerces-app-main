package com.example.demo.exceptions;

public class AppUserException extends Exception {
    private String username;
    private String msg;
    public AppUserException(String user,String msg) {
        this.msg = msg;
        this.username = user;
    }

    @Override
    public String toString() {
        return "exception happened for user"+ '\''+ username + '\'' + ": msg='" + '\'' + msg + '\'' + '}';
    }
}
