package com.yeahdev.yeahsleeptimerfree.model;


public class RvItem {
    private String headLine;
    private String timeLine;
    private String cycleLine;
    private int cvBackground;
    private int cvForeground;

    public RvItem() {}

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getTimeLine() {
        return timeLine;
    }

    public void setTimeLine(String timeLine) {
        this.timeLine = timeLine;
    }

    public String getCycleLine() {
        return cycleLine;
    }

    public void setCycleLine(String cycleLine) {
        this.cycleLine = cycleLine;
    }

    public int getCvBackground() {
        return cvBackground;
    }

    public void setCvBackground(int cvBackground) {
        this.cvBackground = cvBackground;
    }

    public int getCvForeground() {
        return cvForeground;
    }

    public void setCvForeground(int cvForeground) {
        this.cvForeground = cvForeground;
    }
}
