package com.dapr.order.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionData<T> {
    private T data;
}
