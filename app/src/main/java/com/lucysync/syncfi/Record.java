package com.lucysync.syncfi;

/**
 * Class that helps organizing data in {@link RecordsActivity}. It helps creating objects "record",
 * which are those generated on every number asked for in {@link MainActivity}. It is made up of
 * attributes: date, position and number.
 * */
public class Record {
    private String date;
    private String position;
    private String number;

    public Record() {
        date = "";
        position = "";
        number = "";
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
