package com.dapr.order.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {

	private String orderId;
	private List<Item> items;
	private Date orderDate;
	
}

