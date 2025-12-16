package com.kafka.products.service;

import com.kafka.core.ProductCreatedEvent;
import com.kafka.products.rest.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ProductServiceImpl implements ProductService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws ExecutionException, InterruptedException {
        String productId = UUID.randomUUID().toString();
//        TODO: Persist product details into db before publishing event

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId, productRestModel.getTitle(),
                productRestModel.getPrice(), productRestModel.getQuantity());
        LOGGER.info("Before publishing events");
//        CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
//                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
//        future.whenComplete((result, exception) -> {
//            if (exception != null) {
//                LOGGER.error("********* Failed to send message: {}", exception.getMessage());
//            } else {
//                LOGGER.info("********* Message sent successfully: {}", result.getRecordMetadata());
//            }
//        });
//        below line will make synchronous or block the current thread
//        option 1
//        future.join();

//        option 2

        SendResult<String, ProductCreatedEvent> result =
                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();
        LOGGER.info("Partitions: {}", result.getRecordMetadata().partition());
        LOGGER.info("Topics: {}", result.getRecordMetadata().topic());
        LOGGER.info("********* Returning product id");
        return productId;
    }
}
