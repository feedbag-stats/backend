package ch.uzh.feedbag.backend.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="AllEvents")
public class AllEvents extends TaggedInstantBase {

	@Column(nullable=false)
	private String event;

	@Column(nullable=false)
	private String version;

	@Column(nullable=false)
	@Type(type="text")
	private String payload;

	public AllEvents() {}

	public AllEvents(Instant i, User u, String event, String version, String payload) {
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

	private String getPayload() {
		return payload;
	}
}
