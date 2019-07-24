package ch.uzh.feedbag.backend.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

public class AggregatedActivity implements Serializable {

    private Integer duration;
    private ActivityType type;
    private Date date;

    public AggregatedActivity(ActivityType type, Integer duration, Date date) {
        this.type = type;
        this.duration = duration;
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
