package com.luvsoft.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utilities {
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static Date StringToDate(String str){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT); // it's not depend on the current language
        try {
            return formatter.parse(str);
        } catch (ParseException e) {
            System.out.println("Fail to parse date form str: " + str);
        }
        return null;
    }
    
    public static String DateToString(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT); // it's not depend on the current language
        return formatter.format(date);
    }
    
    public static String DateToTimeStampString(Date date){
        return date.getTime() + "";
    }
    
    public static Date TimeStampStringToDate(String timestamp){
        return new Date(Long.parseLong(timestamp) );
    }

    public static Date reachMonthBegin(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date reachDayBegin(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static Date reachDayEnd(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * Increase/Decrease inputted date by nbrOfDays
     * @param date
     * @param nbrOfDays
     * @return
     */
    public static Date addDate(Date date, int nbrOfDays){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, nbrOfDays);
        return cal.getTime();
    }

    public static NumberFormat getNumberFormat(){
        NumberFormat nf = DecimalFormat.getInstance((Locale.ITALY));
        DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setDecimalSeparator('.');
        customSymbol.setGroupingSeparator(',');
        ((DecimalFormat)nf).setDecimalFormatSymbols(customSymbol);
        nf.setGroupingUsed(true);
        return nf;
    }
    
    public static Double getDoubleValueFromFormattedStr(String str){
        try{
            return getNumberFormat().parse(str).doubleValue();
        }
        catch(Exception e){
            throw new NumberFormatException();
        }
    }

    public static NumberFormat getPercentageFormat() {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(1);
        nf.setGroupingUsed(true);
        return nf;
    }

    public static Float convertPercentageStringToFloat(String percentage) {
        System.out.println("Percentage " + percentage);
        if(percentage.endsWith("%")) {
            percentage = percentage.replace("%", "");
        }
        System.out.println("Percentage " + percentage);
        return Float.parseFloat(percentage);
    }
}
