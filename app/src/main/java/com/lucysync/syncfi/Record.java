package com.lucysync.syncfi;

public class Record {
    private String date;
    private String position;
    private String number;

    public Record() {
        date = "";
        position = "";
        number = "";
    }

    public Record(String d, String p, String n) {
        date = d;
        position = p;
        number = n;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Record{" +
                "date='" + date + '\'' +
                ", position='" + position + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
