package com.jordancuker.thevoid;

import java.util.Date;

class Void {
    private String text;
    private long timeCreated;
    private long timeCreatedPlus24;
    private int databaseID;
    private int mood; //0 = smile 1 = neutral 2 = sad 3 = mad

    public final static long secondsIn24Hours = 86400000;
    Void(){
        text = null;
        timeCreated = 0;
    }

    Void(String text, Date timeCreated){
        this.text = text;
        this.timeCreated = timeCreated.getTime();
        timeCreatedPlus24 = this.timeCreated + secondsIn24Hours;
    }
    Void(String text, long timeCreated){
        this.text = text;
        this.timeCreated = timeCreated;
    }

    String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated.getTime();
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
        this.timeCreatedPlus24 = timeCreated + secondsIn24Hours;
    }

    public int getMinutes(){
        return (int) ((timeCreated / (1000*60)) % 60);
    }
    long getRemainingTime(int format){
        long millisLeft = timeCreatedPlus24 - new Date().getTime();
        if (format == 0) return millisLeft; // millis
        else if (format == 1) return (long)millisLeft / (((1000*60))); // hours
        else if (format == 2) return (long)millisLeft / (((1000*60*60))); // minutes
        else if (format == 3) return (long)millisLeft / 1000; // seconds
        else return 0;
    }

    public boolean isLocked(){
        return (timeCreatedPlus24 > new Date().getTime());
    }

    public void setDatabaseID(int databaseID){this.databaseID = databaseID;}
    public int getDatabaseID(){return databaseID;}

    public void setMood(int mood){this.mood = mood;}
    public int getMood(){return mood;}
}
