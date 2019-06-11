package ch.uzh.feedbag.backend.entity;

import java.io.Serializable;

public class AggregatedActivity implements Serializable {

    private long duration;
    private String type;

    public AggregatedActivity(String type, long duration ) {
        this.type = type;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
