package timemeter;

import java.util.Date;

class Interval {
    
    private Date begin, end;
    private volatile Long time;
    private String details;
    private volatile Date genuineBegin;

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

    public Date getGenuineBegin() {
        if (genuineBegin == null) return getBegin();
        return genuineBegin;
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

    public String getDetails() {
        return details;
    }

    public Interval setDetails(String details) {
        this.details = details;
        return this;
    }

    public Interval setGenuineBegin(Date genuineBegin) {
        this.genuineBegin = genuineBegin;
        return this;
    }
    
}
