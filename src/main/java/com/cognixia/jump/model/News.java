package com.cognixia.jump.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class News {
	
	public enum Category {
	    BUSINESS,
	    ENTERTAINMENT,
	    ENVIRONMENT,
	    FOOD,
	    HEALTH,
	    POLITICS,
	    SCIENCE,
	    SPORTS,
	    TECHNOLOGY,
	    TOP,
	    TOURISM,
	    WORLD
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String articleId;
	private String title;
	private String link;
	@Column(length = 500)
	private String keyword = "";

	@Column(length = 500)
	private String description;

	@Column(length = 500)
	private String content;

	private String pubDate;
	private String source_id;
	private String country = "us";
	
	@Enumerated(EnumType.STRING) 
	private Category category;

	public News() {
	}

	public Integer getId() {
		return id;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", articleId=" + articleId + ", title=" + title + ", link=" + link + ", keyword="
				+ keyword + ", description=" + description + ", content=" + content + ", pubDate=" + pubDate
				+ ", source_id=" + source_id + ", country=" + country + ", category=" + category + "]";
	}

}