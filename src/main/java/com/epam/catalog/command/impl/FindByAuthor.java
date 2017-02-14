package com.epam.catalog.command.impl;

import com.epam.catalog.bean.News;
import com.epam.catalog.command.Command;
import com.epam.catalog.command.util.PrinterResult;
import com.epam.catalog.service.NewsService;
import com.epam.catalog.service.exception.ServiceException;
import com.epam.catalog.service.factory.ServiceFactory;

import java.util.ArrayList;

/**
 * Execution command - find by author
 */
public class FindByAuthor implements Command{
  private final char SEPARATOR = ' ';

  /**
   * get arguments from request and execute command
   *
   * @param request line contains author's name
   * @return response to the request
   */
  @Override
  public String execute(String request) {
    String author = request.substring(request.indexOf(SEPARATOR) + 1);

    ServiceFactory service = ServiceFactory.getInstance();
    NewsService newsService = service.getNewsService();

    String response;
    try {
      ArrayList<News> foundList = newsService.findByAuthor(author);
      PrinterResult.printFound(foundList);
      response = "It is search results.";
    } catch (ServiceException e) {
      response = "\nNews not found.";
    }
    return response;
  }
}
