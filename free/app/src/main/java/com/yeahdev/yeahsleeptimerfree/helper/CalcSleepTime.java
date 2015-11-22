package com.yeahdev.yeahsleeptimerfree.helper;


public class CalcSleepTime {
    private static final int FULLHOUR = 1;
    private static final int HALFHOURMIN = 30;
    private static final int FALLASLEEPMINDEFAULT = 14;
    private static final int FULLHOURMIN = 60;
    private static final int FULLDAYHOUR = 24;
    private static final int NULLHOURMINUTE = 0;
    private boolean useFallAsleepTime;
    private int userFallAsleepTime;

    public CalcSleepTime(boolean useFallAsleepTime) {
        this.useFallAsleepTime = useFallAsleepTime;
    }

    public CalcSleepTime(boolean useFallAsleepTime, int userFallAsleepTime) {
        this.useFallAsleepTime = useFallAsleepTime;
        this.userFallAsleepTime = userFallAsleepTime;
    }

    public String[] goToSleepTime(int hour, int minutes) {
        String[] goToSleepTimes = new String[6];
        int restHour;

        if ((useFallAsleepTime) && (userFallAsleepTime == 0)) {
            minutes = minutes + FALLASLEEPMINDEFAULT;
        }

        if (userFallAsleepTime != 0) {
            minutes = minutes + userFallAsleepTime;
        }

        for (int i = 0; i < goToSleepTimes.length; i++) {
            hour = hour + FULLHOUR;
            minutes = minutes + HALFHOURMIN;

            if (minutes >= FULLHOURMIN) {
                hour = hour + FULLHOUR;
                minutes = minutes - FULLHOURMIN;
            }

            if (hour >= FULLDAYHOUR) {
                restHour = hour - FULLDAYHOUR;
                hour = hour + restHour - FULLDAYHOUR;
            }
            goToSleepTimes[i] = Util.convertValueToDoubleZeroString(hour) + ":" + Util.convertValueToDoubleZeroString(minutes);
        }
        return goToSleepTimes;
    }

    public String[] wakeUpTime(int hour, int minutes) {
        String[] wakeUpTimes = new String[6];

        for (int index = 0; index < wakeUpTimes.length; index++) {
            hour = hour - FULLHOUR;
            minutes = minutes - HALFHOURMIN;

            if (minutes < NULLHOURMINUTE) {
                hour = hour - FULLHOUR;
                minutes = FULLHOURMIN + minutes;
            }

            if (hour < NULLHOURMINUTE) {
                hour = FULLDAYHOUR + hour;
            }

            if (index == 5) {
                if ((useFallAsleepTime) && (userFallAsleepTime == 0)) {
                    minutes = minutes - FALLASLEEPMINDEFAULT;
                }

                if (userFallAsleepTime != 0) {
                    minutes = minutes - userFallAsleepTime;
                }

                if (minutes < NULLHOURMINUTE) {
                    minutes = FULLHOURMIN + minutes;
                    hour = hour - FULLHOUR;
                }
            }
            wakeUpTimes[index] = Util.convertValueToDoubleZeroString(hour) + ":" + Util.convertValueToDoubleZeroString(minutes);
        }
        return wakeUpTimes;
    }
}
