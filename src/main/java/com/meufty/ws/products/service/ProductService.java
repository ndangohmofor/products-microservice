package com.meufty.ws.products.service;

import com.meufty.ws.products.rest.CreateProductrestModel;

public interface ProductService {
    String createProduct(CreateProductrestModel product) throws Exception;
}
