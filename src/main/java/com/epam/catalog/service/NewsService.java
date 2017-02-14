package com.epam.catalog.service;

import com.epam.catalog.bean.News;
import com.epam.catalog.service.exception.ServiceException;

import java.util.ArrayList;

/**
 * Interface for requests to the catalog
 */
public interface NewsService {

  void destroy() throws ServiceException;

  void addNews(News news) throws ServiceException;

  ArrayList<News> findByCategory(String category) throws ServiceException;

  ArrayList<News> findByTitle(String title) throws ServiceException;

  ArrayList<News> findByAuthor(String author) throws ServiceException;

  ArrayList<News>findByNews(News news) throws ServiceException;
}
