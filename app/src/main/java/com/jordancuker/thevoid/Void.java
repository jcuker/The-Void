package com.jordancuker.thevoid;

class Void {
    private String text;
    private long timeCreated;

    Void(){
        text = null;
        timeCreated = 0;
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

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }
}
