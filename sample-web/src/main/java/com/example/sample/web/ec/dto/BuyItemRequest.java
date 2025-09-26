package com.example.sample.web.ec.dto;

public class BuyItemRequest {
    private String itemId;
    private Integer quantity;

    public BuyItemRequest() {}

    public BuyItemRequest(String itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
