package com.meufty.ws.products;

import com.meufty.ws.products.rest.CreateProductrestModel;
import com.meufty.ws.products.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@DirtiesContext // Reset the context before each test
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test") // Uses application-test.yml or application-test.properties
@EmbeddedKafka(partitions = 3, count = 3, controlledShutdown = true)
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class ProductsServiceIntegrationTest {

    @Autowired
    ProductService productService;

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
}
