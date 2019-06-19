package ch.uzh.feedbag.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name="activityinterval")
public class ActivityInterval extends BaseInterval {
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private ActivityType type;

	public ActivityInterval() {
	}
	
	public ActivityInterval(Instant begin, Instant end, ActivityType type, User user) {
		super(begin, end, user);
		this.type = type;
	}

	@JsonIgnore
	public boolean isVisible() {
		return end.minus(type.minDisplayedDuration()).compareTo(begin) >= 0;
	}
	
	public boolean covers(ActivityInterval i) {
		return type.equals(i.getType()) && contains(i);
	}
	
	public boolean canMerge(BaseInterval i) {
		if(!(i instanceof ActivityInterval)) return false;
		ActivityInterval interval = (ActivityInterval) i;
		return interval.getType().equals(type) && (canMerge(interval.getBegin()) || canMerge(interval.getEnd()));
	}

	public boolean canMerge(Instant i, ActivityType type2) {
		return type.equals(type2) && canMerge(i);
	}
	
	public boolean canMerge(Instant i) {
		return contains(i) || extendsBoundaries(i);
	}
	
	private boolean extendsBoundaries(Instant i) {
		return extendsBeginBoundary(i) || extendsEndBoundary(i);
	}

	private boolean extendsEndBoundary(Instant i) {
		Instant timeoutEnd = end.plus(type.timeoutDuration());
		return i.isAfter(end) && i.compareTo(timeoutEnd) <= 0;
	}

	private boolean extendsBeginBoundary(Instant i) {
		Instant timeoutBegin = begin.minus(type.timeoutDuration());
		return i.isBefore(begin) && i.compareTo(timeoutBegin) >= 0;
	}

	public ActivityType getType() {
		return type;
	}
	
	public String toString() {
		return "ActivityInterval["+begin.toString()+","+end.toString()+","+(type==null ? "NOTYPE" : type.toString())+"]";
	}
	
	public String toJSON() {
		return "{\"getBegin\":\""+ getBegin().toString()+"\",\"getEnd\":\""+ getEnd().toString()+"\",\"user\"=\""+user.getName()+"\",\"type\"=\""+getType().toString()+"\"}";
	}
}
