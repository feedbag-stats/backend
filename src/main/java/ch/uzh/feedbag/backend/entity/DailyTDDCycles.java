package ch.uzh.feedbag.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="DailyTDDCycles")
public class DailyTDDCycles {
	@ManyToOne(optional=false)
	@JsonIgnore
	private User user;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private long id;
	
	@Column(nullable=false)
	private LocalDate date;
	
	@Column(nullable=false)
	private int cycleCount;
	
	public DailyTDDCycles() {}
	public DailyTDDCycles(User u, LocalDate d, int count) {
		user = u;
		date = d;
		cycleCount = count;
	}
	
	public void setCount(int n) {
		cycleCount = n;
	}
	
	public User getUser() {
		return user;
	}
	public long getId() {
		return id;
	}
	public LocalDate getDate() {
		return date;
	}
	public int getCycleCount() {
		return cycleCount;
	}
	
	public String toString() {
		return user.getUsername()+" - "+date+" - "+cycleCount;
	}
}
