package timemeter;

import java.util.Date;

class Interval {
    
    private Date begin, end;
    private volatile Long time;

    public Interval() {
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public long getTime() {
        if (time != null) return time;
        if (end == null || begin == null) return 0;
        return end.getTime() - begin.getTime();
    }
    
    public Interval setBegin(Date begin) {
        this.begin = begin;
        return this;
    }

    public Interval setEnd(Date end) {
        this.end = end;
        return this;
    }

    public Interval setTime(long time) {
        this.time = time;
        return this;
    }
    
}
