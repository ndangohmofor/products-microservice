package com.meufty.ws.products;

import com.meufty.ws.products.rest.CreateProductrestModel;
import com.meufty.ws.products.service.ProductService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Map;

@DirtiesContext // Reset the context before each test
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test") // Uses application-test.yml or application-test.properties
@EmbeddedKafka(partitions = 3, count = 3, controlledShutdown = true)
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class ProductsServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private Environment environment;

    @Test
    void testCreateProduct_whenGivenValidProductDetails_successfulSendsKafkaMessage() throws Exception {


        //Arrange: Initialize objects, config mocks/stubs, prepare any input data
        String title = "title";
        BigDecimal price = new BigDecimal(100);
        Integer quantity = 1;
        CreateProductrestModel createProductrestModel = new CreateProductrestModel();
        createProductrestModel.setTitle(title);
        createProductrestModel.setPrice(price);
        createProductrestModel.setQuantity(quantity);

        //Act: Invoke methods under test using data from arrange section
        productService.createProduct(createProductrestModel);


        //Assert: Verify that methods under test executed successfully and returned expected results
    }

    private Map<String, Object> getConsumerProperties(){
        return Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group-id"),
                ConsumerConfig.);
    }
}