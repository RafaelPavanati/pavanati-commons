package br.com.pavanati;

import com.querydsl.core.types.OrderSpecifier;

import java.io.Serializable;
import java.util.*;

public final class Sort implements Iterable<OrderSpecifier<?>>, Serializable {
    private final List<OrderSpecifier<?>> orders;

    private Sort(List<OrderSpecifier<?>> orders) {
        this.orders = Objects.nonNull(orders) ? orders : Collections.emptyList();
    }

    public static Sort of(OrderSpecifier<?>... orders) {
        return of(Arrays.asList(orders));
    }

    public static Sort of(List<OrderSpecifier<?>> orders) {
        return new Sort(orders);
    }

    public static Sort of(Iterable<OrderSpecifier<?>> orders) {
        List<OrderSpecifier<?>> orderList = new ArrayList();
        Iterator var2 = orders.iterator();

        while(var2.hasNext()) {
            OrderSpecifier<?> order = (OrderSpecifier)var2.next();
            orderList.add(order);
        }

        return of((List)orderList);
    }

    public Sort and(Sort sort) {
        if (sort == null) {
            return this;
        } else {
            List<OrderSpecifier<?>> these = new ArrayList(this.orders);
            Iterator var3 = sort.orders.iterator();

            while(var3.hasNext()) {
                OrderSpecifier<?> order = (OrderSpecifier)var3.next();
                these.add(order);
            }

            return of((List)these);
        }
    }

    public boolean isEmpty() {
        return this.orders.isEmpty();
    }

    public Iterator<OrderSpecifier<?>> iterator() {
        return this.orders.iterator();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Sort)) {
            return false;
        } else {
            Sort that = (Sort)obj;
            return this.orders.equals(that.orders);
        }
    }


}
