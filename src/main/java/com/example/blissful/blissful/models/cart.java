package com.example.blissful.blissful.models;

import java.util.List;
import java.util.Objects;

public class cart {

    private Long userId;
    private List<Long> productIds;

    public cart() {
    }

    public cart(Long userId, List<Long> productIds) {
        this.userId = userId;
        this.productIds = productIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        cart cart = (cart) o;
        return Objects.equals(userId, cart.userId) && Objects.equals(productIds, cart.productIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productIds);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userId=" + userId +
                ", productIds=" + productIds +
                '}';
    }
}
