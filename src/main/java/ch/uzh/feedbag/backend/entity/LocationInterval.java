package ch.uzh.feedbag.backend.entity;

import java.time.Instant;
import javax.persistence.*;

@Entity
@Table(name="LocationInterval")
public class LocationInterval extends BaseInterval {

	@Column
	private String location;

	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private LocationLevel level;

	public LocationInterval() {}

	@Override
	public boolean canMerge(BaseInterval i) {
		return false;
	}

	@Override
	public boolean canMerge(Instant i) {
		return false;
	}

	public LocationInterval(Instant begin, Instant end, String location, User user, LocationLevel level) {
		super(begin, end, user);
		this.location = location;
		this.level = level;
	}

	public String getLocation() {
		return location;
	}

	public LocationLevel getLevel() {
		return level;
	}

	public String toString() {
		return "LocationInterval<"+begin+"-"+end+" level: "+level+" location: "+location+">";
	}

}
