package br.com.pavanati;

import java.util.Objects;

public final class Pageable {
    public static final int DEFAULT_LIMIT = 25;
    public static final int MAX_LIMIT = 1000;
    private final int offset;
    private final int limit;
    private final Sort sort;

    private Pageable(int offset, int limit, Sort sort) {
        if (0 > offset) {
            throw new IllegalArgumentException("Offset must not be less than zero!");
        } else if (0 > limit) {
            throw new IllegalArgumentException("Limit must not be less than zero!");
        } else if (limit > 1000) {
            throw new IllegalArgumentException("Limit must not be greater than 1000!");
        } else {
            if (0 == limit) {
                limit = 25;
            }

            this.offset = offset;
            this.limit = limit;
            this.sort = sort;
        }
    }

    public static Pageable of(int offset, int limit) {
        return of(offset, limit, (Sort)null);
    }

    public static Pageable of(int offset, int limit, Sort sort) {
        return new Pageable(offset, limit, sort);
    }

    public int getLimit() {
        return this.limit;
    }

    public int getOffset() {
        return this.offset;
    }

    public Sort getSort() {
        return this.sort;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Pageable)) {
            return false;
        } else {
            Pageable that = (Pageable)obj;
            boolean offsetEqual = this.offset == that.offset;
            boolean limitEqual = this.limit == that.limit;
            boolean sortEqual = this.sort == null ? that.sort == null : this.sort.equals(that.sort);
            return offsetEqual && limitEqual && sortEqual;
        }
    }

    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.limit;
        hash = 53 * hash + Objects.hashCode(this.sort);
        return hash;
    }
}

