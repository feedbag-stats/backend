package ch.uzh.feedbag.backend.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name="locationinterval")
public class LocationInterval extends BaseInterval {
	
	@ManyToOne(optional=false)
	private final EditLocation location;

	public LocationInterval(Instant begin, Instant end, EditLocation location, User user) {
		super(begin, end, user);
		this.location = location;
	}
 
	@Override
	public boolean canMerge(BaseInterval i) {
		if(!(i instanceof LocationInterval)) return false;
		LocationInterval interval = (LocationInterval) i;
		boolean locationMatch = location.equals(interval.location());
		boolean intervalOverlap = canMerge(i.getBegin()) || canMerge(i.getEnd()) || interval.contains(this);
		return locationMatch && intervalOverlap;
	}

	@Override
	public boolean canMerge(Instant i) {
		return begin.compareTo(i) >= 0 && end.compareTo(i) <= 0;
	}
	
	public EditLocation location() {
		return location;
	}

}
