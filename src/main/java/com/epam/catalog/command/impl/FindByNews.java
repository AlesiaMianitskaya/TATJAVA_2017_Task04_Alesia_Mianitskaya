package com.epam.catalog.command.impl;

import com.epam.catalog.bean.News;
import com.epam.catalog.command.Command;
import com.epam.catalog.command.util.PrinterResult;
import com.epam.catalog.service.NewsService;
import com.epam.catalog.service.exception.ServiceException;
import com.epam.catalog.service.factory.ServiceFactory;

import java.util.ArrayList;

/**
 * Execution command - find by specific news
 */
public class FindByNews implements Command {
  private final String SEPARATOR = " ";
  private final String SEPARATOR_TITLE = "\'";

  /**
   * get arguments from request and execute command
   *
   * @param request line contains specific news
   * @return response to the request
   */
  @Override
  public String execute(String request) {
    String category = request.split(SEPARATOR)[1];
    String title = request.substring(request.indexOf(SEPARATOR_TITLE) + 1, request.lastIndexOf(SEPARATOR_TITLE));
    String author = request.substring(request.lastIndexOf(SEPARATOR_TITLE) + 2, request.lastIndexOf(SEPARATOR));
    String date = request.substring(request.lastIndexOf(SEPARATOR) + 1);

    String response;
    ServiceFactory service = ServiceFactory.getInstance();
    NewsService newsService = service.getNewsService();
    try {
      ArrayList<News> foundList = newsService.findByNews(new News(category, title, author, date));
      PrinterResult.printFound(foundList);
      response = "It is search results.";
    } catch (ServiceException e) {
      response = "\nNews not found.";
    }
    return response;
  }
}
