package com.cognixia.jump;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cognixia.jump.model.News.Category;
import com.cognixia.jump.service.NewsApiService;

@Component
public class Scheduler {

    @Autowired
    NewsApiService newsService;

    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    public void runScheduledTaskforTopNews() {
        // Code to be executed periodically
        newsService.parseApi(Category.TOP);

        long totalEntries = newsService.getTotalEntries();

        // If the number of entries is greater than 500, reduce it to 500 by deleting
        // oldest entries
        if (totalEntries > 12000) {
            int entriesToDelete = (int) (totalEntries - 12000);
            newsService.deleteOldestEntries(entriesToDelete);
        }
    }

    @Scheduled(initialDelay = 28800000, fixedRate = 5400000) // 1 hour in milliseconds
    public void runScheduledTask() {
        // Code to be executed periodically
        Category[] categories = Category.values();
        for (Category category : categories) {
            // Skip the loop if the category is TOP
            if (category == Category.TOP) {
                continue;
            }

            newsService.parseApi(category);
        }
        long totalEntries = newsService.getTotalEntries();

        // If the number of entries is greater than 500, reduce it to 500 by deleting
        // oldest entries
        if (totalEntries > 12000) {
            int entriesToDelete = (int) (totalEntries - 12000);
            newsService.deleteOldestEntries(entriesToDelete);
        }
    }

}
