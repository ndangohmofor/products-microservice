package com.meufty.ws.products.service;

import com.meufty.ws.core.ProductCreatedEvent;
import com.meufty.ws.products.rest.CreateProductrestModel;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceImpl implements ProductService {

    KafkaTemplate<String, ProductCreatedEvent> template;
    private final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> template) {
        this.template = template;
    }
    @Override
    public String createProduct(CreateProductrestModel product) throws Exception {
        String productId = UUID.randomUUID().toString();

        //TODO: Save product in database

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, product.getTitle(), product.getPrice(), product.getQuantity());

        LOGGER.info("Before publishing a ProductCreatedEvent");

        ProducerRecord<String, ProductCreatedEvent> record = new ProducerRecord<>("product-created-event-topic", productId, productCreatedEvent);

        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        SendResult<String, ProductCreatedEvent> result = template.send(record).get();

        LOGGER.info("Topic: " + result.getRecordMetadata().topic());
        LOGGER.info("Partition: " + result.getRecordMetadata().partition());
        LOGGER.info("Offset: " + result.getRecordMetadata().offset());

//        future.whenComplete((result, exception) -> {
//            if (exception != null) {
//                LOGGER.error("Error while sending product created event: " + exception.getMessage());
//                throw new RuntimeException(exception);
//            } else {
//                LOGGER.info("Product created event sent successfully: Topic - " + result.getRecordMetadata().topic() + ", Partion - " + result.getRecordMetadata().partition() + ". Offset - " + result.getRecordMetadata().offset());
//            }
//        });
//
//        //future.join(); // Wait for the future to complete before return the product id to caller. => Synchronous operation.

        LOGGER.info("*************** Returning product id: " + productId + " ***************");
        return productId;
    }
}
