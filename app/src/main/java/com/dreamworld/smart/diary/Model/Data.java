package com.dreamworld.smart.diary.Model;

public class Data {

    private String note;
    private String title;
    private String date;
    private String id;
    public Data(){

    }

    public Data(String note, String title, String date, String id) {
        this.note = note;
        this.title = title;
        this.date = date;
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
