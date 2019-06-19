package ch.uzh.feedbag.backend.entity;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;

@MappedSuperclass
public abstract class BaseInterval {
    public static final Comparator<BaseInterval> BEGIN_COMPARATOR = new Comparator<BaseInterval>() {
        @Override
        public int compare(BaseInterval o1, BaseInterval o2) {
            return o1.begin.compareTo(o2.begin);
        }
    };

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    protected Instant begin;

    @Column(nullable = false)
    protected Instant end;

    @ManyToOne(optional = false)
    protected User user;

    public BaseInterval() {
    }

    public BaseInterval(Instant begin, Instant end, User user) {
        this.begin = begin;
        this.end = end;
        this.user = user;
    }

    public boolean contains(BaseInterval i) {
        return contains(i.getBegin()) && contains(i.getEnd());
    }

    public boolean contains(Instant i) {
        boolean beginsBefore = begin.equals(i) || begin.isBefore(i);
        boolean endsAfter = end.equals(i) || end.isAfter(i);
        return beginsBefore && endsAfter;
    }

    public void merge(BaseInterval i) {
        merge(i.getBegin());
        merge(i.getEnd());
    }

    public void merge(Instant i) {
        this.begin = min(begin, i);
        this.end = max(end, i);
    }

    public abstract boolean canMerge(BaseInterval i);

    public abstract boolean canMerge(Instant i);

    public Instant getBegin() {
        return begin;
    }

    public void setBegin(Instant begin) {
        this.begin = begin;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public String toString() {
        return "<" + begin + ", " + end + ", " + user + ">";
    }

    public static Instant min(Instant a, Instant b) {
        return a.isBefore(b) ? a : b;
    }

    public static Instant max(Instant a, Instant b) {
        return a.isAfter(b) ? a : b;
    }

    public void addDuration(Duration duration) {
        end = end.plus(duration);
    }
}
