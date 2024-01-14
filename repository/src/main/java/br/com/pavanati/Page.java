package br.com.pavanati;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

public class Page<T> implements Iterable<T> {
    public static final Page EMPTY = new Page(Collections.emptyList(), Pageable.of(0, 0), 0L);
    private final Collection<T> content;
    private final Pageable pageable;
    private final long total;

    public Page(Collection<T> content, Pageable pageable, long total) {
        Objects.requireNonNull(content);
        Objects.requireNonNull(pageable);
        this.content = content;
        this.pageable = pageable;
        this.total = total;
    }

    public static <X> Page<X> empty() {
        return EMPTY;
    }

    public Pageable getPageable() {
        return this.pageable;
    }

    public Collection<T> getContent() {
        return Collections.unmodifiableCollection(this.content);
    }

    public long getTotal() {
        return this.total;
    }

    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    public String toString() {
        String contentType = "UNKNOWN";
        if (!this.content.isEmpty()) {
            contentType = this.content.iterator().next().getClass().getName();
        }

        return String.format("Offset %d containing %d %s instances", this.pageable.getOffset(), this.pageable.getLimit(), contentType);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Page)) {
            return false;
        } else {
            Page<?> page = (Page)o;
            return (new EqualsBuilder()).append(this.total, page.total).append(this.content, page.content).append(this.pageable, page.pageable).isEquals();
        }
    }

    public int hashCode() {
        return (new HashCodeBuilder(17, 37)).append(this.content).append(this.pageable).append(this.total).toHashCode();
    }
}
