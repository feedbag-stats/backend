package ch.uzh.feedbag.backend.entity;

import java.io.Serializable;

public class AggregatedActivity implements Serializable {

    private Integer duration;
    private ActivityType type;

    public AggregatedActivity(ActivityType type, Integer duration ) {
        this.type = type;
        this.duration = duration;
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
}
