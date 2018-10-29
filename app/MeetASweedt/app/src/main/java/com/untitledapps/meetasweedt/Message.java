package com.untitledapps.meetasweedt;

import java.util.Calendar;

/**
 * Created by fredr on 2016-09-27.
 */

public class Message {
    String message;
    Person sender;
    Calendar calendar;

    public Message(String message, Person sender, Calendar calendar) {
        this.message = message;
        this.sender = sender;
        this.calendar = calendar;
    }

    public Person getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
