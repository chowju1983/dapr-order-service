package com.dapr.order.controllers;

import java.util.Arrays;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dapr.order.model.DaprSubscription;
import com.dapr.order.model.Item;
import com.dapr.order.model.Order;
import com.dapr.order.model.SubscriptionData;

@RestController
public class OrderController {

	@Autowired
	Environment environment;

	private Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	private static final String PUBSUB_NAME = "orderpubsub";
	private static final String TOPIC = "orders";
	

	@GetMapping(value = "/order/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Order getOrderDetails(@PathVariable("orderId") String orderId) {
		String SERVER_PORT = environment.getProperty("server.port") != null ? environment.getProperty("server.port")
				: "8080";
		LOGGER.info("Current Server Port:{}", SERVER_PORT);

		return new Order(orderId, Arrays.asList(new Item("Puma", "Shoe", 1)), Calendar.getInstance().getTime());
	}

	/**
	 * Register Dapr pub/sub subscriptions.
	 *
	 * @return DaprSubscription Object containing pubsub name, topic and route for
	 *         subscription.
	 */
	@GetMapping(path = "/dapr/subscribe", produces = MediaType.APPLICATION_JSON_VALUE)
	public DaprSubscription[] getSubscription() {
		var daprSubscription = DaprSubscription.builder().pubSubName(PUBSUB_NAME).topic(TOPIC).route(TOPIC).build();
		LOGGER.info("Subscribed to Pubsubname {} and topic {}", "orderpubsub", "orders");
		return new DaprSubscription[] { daprSubscription };
	}

	/**
	 * Dapr subscription in /dapr/subscribe sets up this route.
	 * Subscribes to a Redis Topic
	 *
	 * @param body Request body
	 * @return ResponseEntity Returns ResponseEntity.ok()
	 */
	@PostMapping(path = "/orders", consumes = MediaType.ALL_VALUE)
	public ResponseEntity<?> processOrders(@RequestBody SubscriptionData<Order> body) {
		LOGGER.info("Subscriber received: " + body.getData());
		return ResponseEntity.ok().build();
	}
	

	/**This method binds to a Kafka topic named checkout
	 * @param body
	 * @return ResponseEntity
	 */
	@PostMapping(path = "/checkout")
	public ResponseEntity<?> getCheckout(@RequestBody(required = false) byte[] body) {
		LOGGER.info("Binding Event received: " + new String(body));
		return ResponseEntity.ok().build();
	}

	

}
