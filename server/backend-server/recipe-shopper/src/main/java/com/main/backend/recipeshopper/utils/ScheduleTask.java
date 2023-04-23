package com.main.backend.recipeshopper.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.main.backend.recipeshopper.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduleTask {
    @Autowired
    ProductService svc;

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 5)
    public void scheduleScraping() {
        log.info(">>> Running scheduled product scrapping...");
        for (String category : Constants.PRODUCT_CATEGORIES) {
            svc.scrapeFromUrl(category);
        }
    }

}
