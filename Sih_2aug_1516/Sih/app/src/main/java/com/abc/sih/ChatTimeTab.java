package com.abc.sih;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatTimeTab {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60*SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60*MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24*HOUR_MILLIS;

    public static String getTimeAgo(long time){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));

        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar calcurr = Calendar.getInstance();
        calcurr.setTime(new Date());
        int daycurr = calcurr.get(Calendar.DAY_OF_MONTH);


        final long diff = daycurr-day;
        if(diff == 0 ){
            return "Today";
        }
        else if(diff == 1){
            return "yesterday";
        }
        else{
            return DateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.UK).format(new Date(time));
        }

    }
}
