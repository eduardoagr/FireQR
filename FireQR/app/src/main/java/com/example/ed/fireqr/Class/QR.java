package com.example.ed.fireqr.Class;

import android.widget.TextView;


public class QR {

    private String nameOfCard, name, org, email, url, cell, tell, address;

    public QR() {
    }

    public QR(String nameOfCard, String name, String org, String email, String url, String cell, String tell, String address) {
        this.nameOfCard = nameOfCard;
        this.name = name;
        this.org = org;
        this.email = email;
        this.url = url;
        this.cell = cell;
        this.tell = tell;
        this.address = address;
    }

    public String getNameOfCard() {
        return nameOfCard;
    }

    public void setNameOfCard(String nameOfCard) {
        this.nameOfCard = nameOfCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}