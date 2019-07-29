package ch.uzh.feedbag.backend.entity;

import java.io.Serializable;
import java.util.Date;

public class ActivityHeatmapEntry implements Serializable {

    private long count;
    private Date date;

    public ActivityHeatmapEntry(long count, Date date) {
        this.count = count;
        this.date = date;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
