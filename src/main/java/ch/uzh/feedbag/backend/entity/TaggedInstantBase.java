package ch.uzh.feedbag.backend.entity;

import javax.persistence.*;
import java.time.Instant;
import java.util.Comparator;

@MappedSuperclass
public abstract class TaggedInstantBase {
	
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id;
	
	@Column(nullable=false)
	protected final Instant instant;

	@ManyToOne(optional = false)
	protected User user;
	
	public static final Comparator<TaggedInstantBase> INSTANT_COMPARATOR = new Comparator<TaggedInstantBase>() {
		@Override
		public int compare(TaggedInstantBase o1, TaggedInstantBase o2) {
			return o1.instant.compareTo(o2.instant);
		}
	};
	
	public TaggedInstantBase(Instant instant, User user) {
		this.instant = instant;
		this.user = user;
	}


	public Instant instant() {
		return instant;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String toString() {
		return "<"+instant.toString()+user.toString()+">";
	}
}
