package hu.farcsal.utils.format;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author zoli
 */
public class UtcDateFormat extends SimpleDateFormat {
    
    private static final long serialVersionUID = -8275126788734707527L;
    
    public enum UseCase {
        FORMAT, PARSE_DATE, PARSE_DATE_TIME
    }
    
    // not static because date formatters have to use the same time zone
    private final DateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    
    private final int millisLength;
    private final boolean strictMode;
    
    public UtcDateFormat() {
    	this(false);
    }
    
    public UtcDateFormat(boolean keepTimeZone) {
    	this(keepTimeZone, true);
    }
    
    public UtcDateFormat(boolean keepTimeZone, boolean withMillis) {
    	this(keepTimeZone, withMillis ? 3 : 0);
    }
    
    public UtcDateFormat(boolean keepTimeZone, int millisLength) {
    	this(false, keepTimeZone, millisLength);
    }
    
    public UtcDateFormat(boolean strictMode, boolean keepTimeZone, int millisLength) {
    	super("yyyy-MM-dd'T'HH:mm:ss" + millis(millisLength, false) + "Z", Locale.ENGLISH);
        if (!keepTimeZone) setTimeZone(TimeZone.getTimeZone("UTC"));
    	this.millisLength = millisLength;
        this.strictMode = strictMode;
    }

    @Override
    public void setTimeZone(TimeZone zone) {
        super.setTimeZone(zone);
        localDateFormat.setTimeZone(zone);
    }
    
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, java.text.FieldPosition pos)
    {
        final StringBuffer buf = super.format(checkDate(UseCase.FORMAT, date), toAppendTo, pos);
        return new StringBuffer(buf.toString().replaceFirst("\\+0000", "Z")); // replace '+0000' to 'Z' (optional)
    }

    @Override
    public Date parse(String source) throws java.text.ParseException {
        if (!strictMode && !source.contains("T")) return checkDate(UseCase.PARSE_DATE, localDateFormat.parse(source)); // parse as short local date if the source is just a date without time
        source = source.replaceFirst("(.*)(Z| UTC)$", "$1+0000"); // replace 'Z' and ' UTC' to '+0000'
        source = source.replaceFirst("(.*[\\+-])([0-9]{2}):([0-9]{2})$", "$1$2$3"); // replace '+00:00' to '+0000'
        source = source.replaceFirst("(.*[\\+-])([0-9]{2})$", "$1$200"); // replace '+00' to '+0000'
        if (!strictMode && !source.matches(".*[\\+-][0-9]{4}$")) source += getTimeZoneOffset(source); // append the formatter's timezone localtime offset if the timezone is not specified
        if (millisLength == 0) source = source.replaceFirst("\\.[0-9]{3,10}", ""); // remove milliseconds
        else source = source.replaceFirst("(.*:[0-9]{2})([\\+-][0-9]{4})", "$1" + millis(millisLength, true) + "$2"); // append milliseconds
        return checkDate(UseCase.PARSE_DATE_TIME, super.parse(source));
    }
    
    protected Date checkDate(UseCase useCase, Date d) {
    	return d;
    }
    
    private String getTimeZoneOffset(String source) throws ParseException {
        return getTimeZoneOffset(localDateFormat.getTimeZone(), localDateFormat.parse(source));
    }
    
    private static String getTimeZoneOffset(TimeZone tz, Date d) {
        int offset = tz.getOffset((d == null ? new Date() : d).getTime());
        int hours = Math.abs(offset) / 1000 / 60 / 60;
        int minutes = (Math.abs(offset) - (hours * 1000 * 60 * 60)) / 1000 / 60;
        return String.format((offset >= 0 ? '+' : '-') + "%02d%02d", hours, minutes);
    }
    
    private static String millis(int length, boolean zero) {
    	if (length != 0 && (length < 3 || length > 10)) throw new IllegalArgumentException("millis length have to be 0 or between 3 and 10");
    	String s = length > 0 ? "." : "";
    	for (int i = 1; i <= length; i++) {
    		s += zero ? '0' : 'S';
    	}
    	return s;
    }
    
}
