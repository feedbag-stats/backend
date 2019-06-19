package ch.uzh.feedbag.backend.entity;

import java.io.Serializable;
import java.time.Duration;

public class AggregatedActivity implements Serializable {

    private Duration duration;
    private ActivityType type;

    public AggregatedActivity(ActivityType type, Duration duration ) {
        this.type = type;
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }
}
