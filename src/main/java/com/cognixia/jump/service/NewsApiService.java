package com.cognixia.jump.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.cognixia.jump.model.News;
import com.cognixia.jump.model.News.Category;
import com.cognixia.jump.repository.NewsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NewsApiService {

	private final String apiKey = "?apikey=pub_31803218a8be6346cc7de2d13f2b86da2a206";
	@Autowired
	NewsRepository repo;

	public void parseApi(Category category) {
		int maxContentLength = 500; // Set your desired maximum content length

		String country = "&country=us";
		String language = "&language=en";
		String size = "&size=10";
		String categoryString = "&category=" + category.toString().toLowerCase();
		String params = country + language + size + categoryString;

		try {
			String apiUrl = "https://newsdata.io/api/1/news" + apiKey + params;
			System.out.println("url: " + apiUrl);
			URL url = new URL(apiUrl);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("accept", "application/json");

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream responseStream = connection.getInputStream();
				ObjectMapper mapper = new ObjectMapper();
				JsonNode root = mapper.readTree(responseStream);
				JsonNode resultsNode = root.get("results");

				if (resultsNode.isArray()) {
					for (JsonNode newsNode : resultsNode) {
						String content = newsNode.get("content").asText();
						String truncatedContent = newsNode.get("content").asText().substring(0,
								Math.min(newsNode.get("content").asText().length(), maxContentLength));
						String truncatedDescription = newsNode.get("description").asText().substring(0,
								Math.min(newsNode.get("description").asText().length(), maxContentLength));

						StringBuilder keywordsBuilder = new StringBuilder();
						JsonNode keywordsNode = newsNode.get("keywords");
						System.out.println(keywordsNode);
						if (keywordsNode != null && keywordsNode.isArray()) {
							for (JsonNode keywordNode : keywordsNode) {
								keywordsBuilder.append(keywordNode.asText()).append(",");
							}
							// Remove the trailing comma
							if (keywordsBuilder.length() > 0) {
								keywordsBuilder.setLength(keywordsBuilder.length() - 1);
							}
							if (keywordsBuilder.length() > 500) {
								keywordsBuilder.setLength(500);
							}
						}

						String title = newsNode.get("title").asText();

						// Check if a news article with the same title already exists
						if (repo.findByTitle(title) == null) {
							// If not, create a new News object and save it
							News news = new News();
							news.setArticleId(newsNode.get("article_id").asText());
							news.setTitle(title);
							news.setLink(newsNode.get("link").asText());
							news.setDescription(truncatedDescription);
							news.setContent(truncatedContent);
							news.setPubDate(newsNode.get("pubDate").asText());
							news.setSource_id(newsNode.get("source_id").asText());
							news.setKeyword(keywordsBuilder.toString());
							news.setCategory(category);

							System.out.println(news.toString());
				            // Check conditions before saving to the database
				            if (title != null && content != null && !hasDuplicatePrefix(content, 20)) {
				                repo.save(news);
				            }
						}
					}
				}
			} else {
				System.err.println("HTTP Response Code: " + connection.getResponseCode());
				System.err.println("HTTP Response Message: " + connection.getResponseMessage());
			}

			connection.disconnect();
		} catch (IOException e) {
			// Handle exceptions that may occur during the HTTP request or JSON parsing
			e.printStackTrace(); // You should handle or log the exception properly
		}
	}
    private boolean hasDuplicatePrefix(String content, int prefixLength) {
        // Extract the prefix of the specified length from the content
        String prefix = content.substring(0, Math.min(prefixLength, content.length()));

        // Check if the prefix is already in the set
        return duplicatePrefixes.contains(prefix);
    }

    private Set<String> duplicatePrefixes = new HashSet<>();
    // Method to get the total number of entries in the database
    public long getTotalEntries() {
        return repo.count();
    }

    // Method to delete the oldest entries to reduce the total number of entries to the specified limit
    public void deleteOldestEntries(int entriesToDelete) {
        List<News> oldestEntries = repo.findAllByOrderByPubDateAsc(PageRequest.of(0, entriesToDelete));
        repo.deleteAll(oldestEntries);
    }
}