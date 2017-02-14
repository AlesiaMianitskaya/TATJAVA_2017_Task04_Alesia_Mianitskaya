package com.epam.catalog.command.impl;

import com.epam.catalog.bean.News;
import com.epam.catalog.command.Command;
import com.epam.catalog.command.util.PrinterResult;
import com.epam.catalog.service.NewsService;
import com.epam.catalog.service.exception.ServiceException;
import com.epam.catalog.service.factory.ServiceFactory;

import java.util.ArrayList;

/**
 * Execution command - find by title
 */
public class FindByTitle implements Command {
  private final char SEPARATOR = ' ';

  /**
   * get arguments from request and execute command
   *
   * @param request line contains news' title
   * @return response to the request
   */
  @Override
  public String execute(String request) {
    String title = request.substring(request.indexOf(SEPARATOR) + 1);

    ServiceFactory service = ServiceFactory.getInstance();
    NewsService newsService = service.getNewsService();

    String response;
    try {
      ArrayList<News> foundList = newsService.findByTitle(title);
      PrinterResult.printFound(foundList);
      response = "It is search results.";  ///see here
    } catch (ServiceException e) {
      response = "\nNews not found.";
    }
    return response;
  }
}
