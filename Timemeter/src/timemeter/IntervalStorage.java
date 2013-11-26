package timemeter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class StorageBean {
    
    double price = 850;
    boolean running = false;
    boolean frameVisible = true;
    final Point frameLocation = new Point(0, 0);
    final List<Interval> intervals = new ArrayList<Interval>();
    
}

class IntervalStorage {
    
    private static final File STORE_FILE = createFile("timemeter.json"),
                              LOCK_FILE  = createFile("timemeter.lock");
    
    private static Boolean locked;
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy.MM.dd. HH:mm:ss.SSS").create();
    
    private final StorageBean BEAN = load();

    private Interval runningInterval;

    public Interval getRunningInterval() {
        return runningInterval;
    }
    
    public long getRunningTime() {
        if (!isRunning()) return 0;
        return new Interval().setBegin(runningInterval.getBegin()).setEnd(new Date()).getTime();
    }
    
    public long getRunningTimeSum() {
        return getTimeSum() + getRunningTime();
    }
    
    public static long getTimeSum(List<Interval> l) {
        long sum = 0;
        for (Interval i : l) {
            sum += i.getTime();
        }
        return sum;
    }
    
    public long getTimeSum() {
        return getTimeSum(getIntervals());
    }
    
    public double getPrice() {
        return BEAN.price;
    }
    
    public void setPrice(double price) {
        BEAN.price = price;
    }
    
    public boolean isFrameVisible() {
        return BEAN.frameVisible;
    }
    
    public boolean isRunning() {
        return BEAN.running;
    }
    
    public List<Interval> getIntervals() {
        return BEAN.intervals;
    }

    public Point getFrameLocation(DetailsFrame frame) {
        if (!frame.isLocationCorrect(BEAN.frameLocation)) return DetailsFrame.MAX_RECT.getLocation();
        return BEAN.frameLocation;
    }
    
    public IntervalStorage setFrameLocation(Point p, boolean save) {
        BEAN.frameLocation.setLocation(p);
        if (save) save();
        return this;
    }
    
    public IntervalStorage setFrameVisible(boolean b, boolean save) {
        BEAN.frameVisible = b;
        if (save) save();
        return this;
    }
    
    public IntervalStorage setTimer(boolean start, boolean save) {
        return setTimer(start, true, save);
    }
    
    public IntervalStorage setTimer(boolean start, boolean saveTimerState, boolean save) {
        Date now = new Date();
        if (start) {
            if (runningInterval != null) runningInterval.setEnd(now);
            runningInterval = new Interval();
            runningInterval.setBegin(now);
            getIntervals().add(runningInterval);
            if (saveTimerState) BEAN.running = true;
            if (save) save();
        }
        else if (runningInterval != null) {
            runningInterval.setEnd(now);
            runningInterval = null;
            if (saveTimerState) BEAN.running = false;
            if (save) save();
        }
        return this;
    }
    
    public boolean save() {
        try {
            FileWriter out = new FileWriter(STORE_FILE, false);
            GSON.toJson(BEAN, out);
            out.close();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    private StorageBean load() {
        StorageBean o = null;
        try {
            o = GSON.fromJson(new FileReader(STORE_FILE), StorageBean.class);
        }
        catch (Exception ex) {
            ;
        }
        if (o == null) o = new StorageBean();
        return o;
    }
    
    public static boolean isLocked() {
        if (locked == null) locked = LOCK_FILE.exists();
        return locked;
    }
    
    public static void setLocked(boolean b) {
        try {
            if (b) LOCK_FILE.createNewFile();
            else LOCK_FILE.delete();
        }
        catch (Exception ex) {
            ;
        }
    }
    
    private static File createFile(String name) {
        return new File(System.getProperty("user.home") + File.separator + name);
    }
    
}
