package ch.uzh.feedbag.backend.entity;

import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Type;

@Entity
@Table(name="ActivityEntry")
public class ActivityEntry extends TaggedInstantBase {

	@Column(nullable=false)
	private String event;

	@Column(nullable=false)
	private String version;

	@Column(nullable=false)
	@Type(type="text")
	private String payload;

	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private ActivityType type;

	public ActivityEntry() {}

	public ActivityEntry(Instant i, User u, String event, String version, String payload) {
		super(i, u);
		this.event = event;
		this.version = version;
		this.payload = payload;
	}

	public Long getId() {
		return id;
	}

	public String getEvent() {
		return event;
	}

	public String getVersion() {
		return version;
	}

	public ActivityType getType() {
		return type;
	}

	private String getPayload() {
		return payload;
	}
}
