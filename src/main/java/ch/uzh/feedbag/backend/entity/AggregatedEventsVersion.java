package ch.uzh.feedbag.backend.entity;

import java.io.Serializable;
import java.util.Date;

public class AggregatedEventsVersion implements Serializable {

    private Date date;
    private long count;
    private String version;

    public AggregatedEventsVersion(Date date, long count, String version) {
        this.date = date;
        this.count = count;
        this.version = version;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
