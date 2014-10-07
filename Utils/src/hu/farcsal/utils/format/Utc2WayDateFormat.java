package hu.farcsal.utils.format;

import java.text.SimpleDateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author zoli
 */
public class Utc2WayDateFormat extends SimpleDateFormat {

    private final SimpleDateFormat dfFormat, dfParse;
    
    public Utc2WayDateFormat() {
    	this(true);
    }
    
    public Utc2WayDateFormat(boolean withMillis) {
    	this(withMillis ? 3 : 0);
    }
    
    public Utc2WayDateFormat(int millisLength) {
    	this(false, millisLength);
    }
    
    public Utc2WayDateFormat(boolean strictMode, int millisLength) {
    	dfFormat = createDateFormat(strictMode, millisLength);
        dfParse = createDateFormat(strictMode, millisLength);
    }

    private static SimpleDateFormat createDateFormat(boolean strictMode, int millisLength) {
        return new UtcDateFormat(strictMode, true, millisLength);
    }
    
    @Override
    public void setTimeZone(TimeZone zone) {
        setFormatTimeZone(zone);
        setParseTimeZone(zone);
    }
    
    public void setFormatTimeZone(TimeZone zone) {
        dfFormat.setTimeZone(zone);
    }
    
    public void setParseTimeZone(TimeZone zone) {
        dfParse.setTimeZone(zone);
    }
    
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return dfFormat.format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(String source) throws ParseException {
        return dfParse.parse(source);
    }
    
    public static void main(String[] args) throws ParseException {
        Date date = new Date();
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Utc2WayDateFormat df = new Utc2WayDateFormat();
        
        System.out.println("Format current date:");
        String daystr, tzless, utcstr = df.format(date);
        System.out.println("Local TZ: " + utcstr);
        df.setFormatTimeZone(utc);
        utcstr = df.format(date);
        tzless = utcstr.substring(0, utcstr.length() - 1);
        daystr = utcstr.substring(0, 10);
        System.out.println("UTC   TZ: " + utcstr);
        System.out.println("Day only: " + daystr);
        System.out.println("\nParse with local TZ:");
        System.out.println(utcstr + " = " + df.parse(utcstr));
        System.out.println(tzless + "  = " + df.parse(tzless));
        System.out.println(daystr + "               = " + df.parse(daystr));
        df.setParseTimeZone(utc);
        System.out.println("\nParse with UTC TZ:");
        System.out.println(utcstr + " = " + df.parse(utcstr));
        System.out.println(tzless + "  = " + df.parse(tzless));
        System.out.println(daystr + "               = " + df.parse(daystr));
    }
    
}
