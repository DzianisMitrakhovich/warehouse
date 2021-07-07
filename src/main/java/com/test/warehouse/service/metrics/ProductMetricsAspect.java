package com.test.warehouse.service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Aspect
@Component
public class ProductMetricsAspect {

    private static final String PRODUCTS_SOLD_METRIC = "products.sold";

    @Autowired
    private MeterRegistry meterRegistry;
    private Counter counter;

    @PostConstruct
    void initCounterMetric() {
        counter = Counter.builder(PRODUCTS_SOLD_METRIC)
                .description("Total number of products sold")
                .register(meterRegistry);
    }

    @AfterReturning("execution(* com.test.warehouse.service.ProductServiceImpl.sellProduct(..)) ")
    public void logAfterMethodCall(JoinPoint joinPoint) {
        int productAmount = (int) joinPoint.getArgs()[1];
        counter.increment(productAmount);
        log.debug("Updated metric '{}' to {} items", PRODUCTS_SOLD_METRIC, productAmount);
    }


}
