package com.main.backend.recipeshopper.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.main.backend.recipeshopper.exceptions.DjangoBadResponseException;
import com.main.backend.recipeshopper.exceptions.ProductUpsertException;
import com.main.backend.recipeshopper.service.ProductService;

import jakarta.json.stream.JsonParsingException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduleTask {
    @Autowired
    ProductService svc;

    /**
     * 
     */
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 4320, initialDelay = 2)
    public void scheduleScraping() {
        log.info(">>> Running scheduled product scrapping...");
        for (String category : Constants.PRODUCT_CATEGORIES) {
            try {
                svc.scrapeFromUrl(category);

            } catch (JsonParsingException e) {
                log.error("--- Error parsing: {}", category);
            } catch (DjangoBadResponseException e) {
                log.error("--- Django server error for {}: {}", category, e.getMessage());
            } catch (ProductUpsertException e) {
                log.error("--- Product upsert error for {}: {}", category, e.getMessage());

            }
        }
        log.info(">>> Scrapping has completed.");
    }

}
