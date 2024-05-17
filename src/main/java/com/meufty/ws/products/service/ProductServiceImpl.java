package com.meufty.ws.products.service;

import com.meufty.ws.products.rest.CreateProductrestModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    KafkaTemplate<String, ProductCreatedEvent> template;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> template) {
        this.template = template;
    }
    @Override
    public String createProduct(CreateProductrestModel product) {
        String productId = UUID.randomUUID().toString();

        //TODO: Save product in database

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, product.getTitle(), product.getPrice(), product.getQuantity());

        template.send("product-created-event-topic", productId, productCreatedEvent);

        return productId;
    }
}
