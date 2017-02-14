package com.epam.catalog.service.factory;

import com.epam.catalog.service.NewsService;
import com.epam.catalog.service.impl.NewsServiceImpl;

/**
 * creating one object of class NewsServiceImpl and
 * getting this object
 */
public class ServiceFactory {
  private static final ServiceFactory instance = new ServiceFactory();

  private final NewsService newsService = new NewsServiceImpl();

  public static ServiceFactory getInstance() {
    return instance;
  }

  public NewsService getNewsService() {
    return newsService;
  }
}
