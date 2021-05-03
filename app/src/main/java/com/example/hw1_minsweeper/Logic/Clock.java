package com.example.hw1_minsweeper.Logic;

import java.io.Serializable;

public class Clock implements Serializable {

    private int minutes, hours, seconds;

    public Clock() {
        this.reset();
    }


    public void tick() {
        seconds++;
        minutes += seconds / 60;
        hours += minutes / 60;
        seconds %= 60;
        minutes %= 60;
        hours %= 24;
    }

    public void reset() {
        seconds = 0;
        minutes = 0;
        hours = 0;
    }


    public String toString() {
        StringBuffer outPut = new StringBuffer();
        if (hours < 10) {
            outPut.append("0");
        }
        outPut.append(hours + ":");
        if (minutes < 10) {
            outPut.append("0");
        }
        outPut.append(minutes + ":");
        if (seconds < 10) {
            outPut.append("0");
        }
        outPut.append(seconds);

        return outPut.toString();
    }

    public void run() {

        this.tick();


    }
}
