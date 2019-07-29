package ch.uzh.feedbag.backend.entity;

import org.junit.Ignore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="User")
public class User implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false) 
	private String name;
	
	@Column(nullable = false, unique = true) 
	private String username;
	
	@Column(nullable = false, unique = true) 
	private String token;

	@Transient
	private LocalDate lastUpload = null;

	@Transient
	private boolean isRecalculatingStats = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDate getLastUpload() {
		return lastUpload;
	}

	public void setLastUpload(LocalDate lastUpload) {
		this.lastUpload = lastUpload;
	}

	public boolean isRecalculatingStats() {
		return isRecalculatingStats;
	}

	public void setRecalculatingStats(boolean recalculatingStats) {
		isRecalculatingStats = recalculatingStats;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}
	
	public String toString() {
		return "User("+id+", "+name+", "+username+", "+token+")";
	}
}
