package com.kafka.products.service;

import com.kafka.products.rest.CreateProductRestModel;

import java.util.concurrent.ExecutionException;

public interface ProductService {

    String createProduct(CreateProductRestModel productRestModel) throws ExecutionException, InterruptedException;
}
