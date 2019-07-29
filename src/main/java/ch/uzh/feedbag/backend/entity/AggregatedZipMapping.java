package ch.uzh.feedbag.backend.entity;

import java.util.Date;

public class AggregatedZipMapping {

    private Date day;
    private long count;
    private boolean markedForDelete;

    public AggregatedZipMapping(Date day, long count, boolean markedForDelete) {
        this.day = day;
        this.count = count;
        this.markedForDelete = markedForDelete;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public boolean isMarkedForDelete() {
        return markedForDelete;
    }

    public void setMarkedForDelete(boolean markedForDelete) {
        this.markedForDelete = markedForDelete;
    }
}
