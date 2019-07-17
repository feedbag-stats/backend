package ch.uzh.feedbag.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name="AllEvents")
public class AllEvents extends TaggedInstantBase {

	@Column(nullable=false)
	private String event;
	
	public AllEvents() {}
	
	public AllEvents(Instant i, User u, String text) {
		super(i, u);
		event = text;
	}
	
	public String getEvent() {
		return event;
	}

}
