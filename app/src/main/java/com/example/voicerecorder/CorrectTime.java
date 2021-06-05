package com.example.voicerecorder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CorrectTime {
    public String getCorrectTime(long duration){
        Date time = new Date();

        long sec = TimeUnit.MILLISECONDS.toSeconds(time.getTime() - duration);
        long min = TimeUnit.MILLISECONDS.toMinutes(time.getTime() - duration);
        long hours = TimeUnit.MILLISECONDS.toHours(time.getTime() - duration);
        long days = TimeUnit.MILLISECONDS.toDays(time.getTime() - duration);

        if(sec < 60) return "just now";
        else if(min == 1) return "a minute ago";
        else if(min > 1 & min < 60) return min + " minutes ago";
        else if(hours == 1) return "an hour ago";
        else if(hours > 1 & hours < 24) return hours + " hours ago";
        else if(days == 1) return "a day ago";
        else return days + " days ago";
    }
}
