package timemeter;

import java.util.Date;

class Interval {
    
    private Date begin, end;

    public Interval() {
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public long getTime() {
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
    
}
