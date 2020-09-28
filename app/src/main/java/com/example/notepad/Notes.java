package com.example.notepad;

import java.io.Serializable;

public class Notes implements Serializable {

    private String title;
    private String noteText;
    private long timestamp;

    public Notes(String title, String noteText, long timestamp) {
        this.title = title;
        this.noteText = noteText;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getNoteText() {
        return noteText;
    }

    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Notes{" +
                "title='" + title + '\'' +
                ", noteText='" + noteText + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
