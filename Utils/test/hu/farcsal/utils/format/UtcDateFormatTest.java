package hu.farcsal.utils.format;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author zoli
 */
@RunWith(Parameterized.class)
public class UtcDateFormatTest {
    
    private static final boolean FORMAT_DATE = false;
    private static final String FORMAT_TIMEZONE = "Europe/Budapest";
    
    private static DateFormat fmtHuman;
    
    private DateFormat fmtUtc, fmtLocal;

    private final String usedTimeZone;
    
    public UtcDateFormatTest(String usedTimeZone) {
        this.usedTimeZone = usedTimeZone;
    }
    
    @Parameterized.Parameters
    public static Collection timeZones() {
        return Arrays.asList(new Object[][] {
            { "Europe/Budapest" },  // +01 & +02
            { "Europe/Bucharest" }, // +02 & +03
            { "America/New_York" }, // -05 & -04
            { "Europe/Moscow" },    // +04 & +04
            { "Africa/Bamako" },    //  00 &  00
        });
    }
    
    @Before
    public void setUp() {
        System.out.println("Using time zone: " + usedTimeZone);
        TimeZone.setDefault(TimeZone.getTimeZone(usedTimeZone));
        
        if (FORMAT_DATE) {
            fmtHuman = DateFormat.getDateTimeInstance();
            fmtHuman.setTimeZone(TimeZone.getTimeZone(FORMAT_TIMEZONE));
        }
        
        fmtLocal = new UtcDateFormat(true);
        fmtUtc = new UtcDateFormat(false);
        
        System.out.println();
    }

    @Test
    public void testFormat1() throws ParseException {
        System.out.println("[Format1]");
        testFormat("2014-01-01T01:01:01+0100", false);
        testFormat("2014-01-01T01:01:01.001+0100", true);
    }
    
    @Test
    public void testFormat2() throws ParseException {
        System.out.println("[Format2]");
        testFormat("2014-01-01T01:01:01+01", false);
        testFormat("2014-01-01T01:01:01.001+01", true);
    }
    
    @Test
    public void testFormat3a() throws ParseException {
        System.out.println("[Format3a]");
        testFormat("2014-01-01T01:01:01", false);
        testFormat("2014-01-01T01:01:01.001", true);
    }
    
    @Test
    public void testFormat3b() throws ParseException {
        System.out.println("[Format3b]");
        testFormat("2014-06-01T01:01:01", false);
        testFormat("2014-06-01T01:01:01.001", true);
    }
    
    @Test
    public void testFormat4() throws ParseException {
        System.out.println("[Format4]");
        testFormat("2014-01-01", true);
    }
    
    @Test
    public void testFormat5() throws ParseException {
        System.out.println("[Format5]");
        testFormat("2014-07-01", true);
    }
    
    @Test
    public void testNow() throws ParseException {
        System.out.println("[now]");
        test(new Date());
    }
    
    @Test
    public void testWinter() throws ParseException {
        System.out.println("[winter]");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        test(c.getTime());
    }
    
    @Test
    public void testSummer() throws ParseException {
        System.out.println("[summer]");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.JULY);
        test(c.getTime());
    }
    
    @Test
    public void testMs() throws ParseException {
        System.out.println("[ms]");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.MILLISECOND, 1);
        String s1 = fmtUtc.format(c1.getTime());
        String s2 = fmtUtc.format(c2.getTime());
        System.out.println(s1 + " != " + s2);
        assertFalse(s1.equals(s2));
        assertFalse(fmtUtc.parse(s1).equals(fmtUtc.parse(s2)));
        System.out.println();
    }
    
    private void testFormat(String source, boolean print) throws ParseException {
        Date localDate = fmtLocal.parse(source);
        Date utcDate = fmtLocal.parse(source);
        if (print) System.out.println(format(localDate));
        assertEquals(localDate, utcDate);
        if (print) System.out.println();
    }
    
    private void test(Date d) throws ParseException {
        String localStr = test(fmtLocal, d);
        String utcStr = test(fmtUtc, d);
        
        Date[] dates = new Date[8];
        dates[0] = fmtLocal.parse(localStr);
        dates[1] = fmtUtc.parse(utcStr);
        dates[2] = fmtUtc.parse(localStr);
        dates[3] = fmtLocal.parse(utcStr);
        dates[4] = fmtLocal.parse(localStr);
        dates[5] = fmtLocal.parse(utcStr);
        dates[6] = fmtUtc.parse(localStr);
        dates[7] = fmtUtc.parse(utcStr);
        
        for (int i = 0; i <= 7; i += 2) { // i = 0, 2, 4, 6
            assertEquals(dates[i], dates[i + 1]);
        }
        
        System.out.println(utcStr + " = " + localStr);
        for (int i = 2; i <= 7; i += 2) { // i = 2, 4, 6
            assertEquals(dates[0], dates[i]);
        }
        
        System.out.println();
    }
    
    private static String test(DateFormat fmt, Date d) throws ParseException {
        String str = fmt.format(d);
        Date d2 = fmt.parse(str);
        System.out.println(str + " = " + format(d));
        assertEquals(d, d2);
        return str;
    }
    
    private static String format(Date d) {
        return FORMAT_DATE ? fmtHuman.format(d) : d.toString();
    }
    
}
