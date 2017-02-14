package com.epam.catalog.bean;

import java.io.Serializable;

/**
 * Contains information about news in catalog
 */
public class News implements Serializable {
  private static final long serialVersionUID = 1L;
  private String category;
  private String title;
  private String author;
  private String date;

  public News() {
  }

  public News(String category, String title, String author, String date) {
    this.category = category;
    this.title = title;
    this.author = author;
    this.date = date;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    News news = (News) obj;

    if (category != null ? !category.equals(news.category) : news.category != null) return false;
    if (title != null ? !title.equals(news.title) : news.title != null) return false;
    if (author != null ? !author.equals(news.author) : news.author != null) return false;
    return date != null ? date.equals(news.date) : news.date == null;
  }

  @Override
  public int hashCode() {
    int result = category != null ? category.hashCode() : 0;
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (author != null ? author.hashCode() : 0);
    result = 31 * result + (date != null ? date.hashCode() : 0);
    return result;
  }
}
