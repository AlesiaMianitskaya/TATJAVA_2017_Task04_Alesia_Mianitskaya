package com.epam.catalog.command.impl;

import com.epam.catalog.bean.News;
import com.epam.catalog.command.Command;
import com.epam.catalog.service.NewsService;
import com.epam.catalog.service.exception.ServiceException;
import com.epam.catalog.service.factory.ServiceFactory;

/**
 * Execution command - add news
 */
public class AddNews implements Command {
  private final String SEPARATOR = " ";
  private final String SEPARATOR_TITLE = "\'";

  /**
   * get arguments from request and execute command
   *
   * @param request line contains arguments
   * @return response to the request
   */
  @Override
  public String execute(String request) {
    String category = request.split(SEPARATOR)[1];
    String title = request.substring(request.indexOf(SEPARATOR_TITLE) + 1, request.lastIndexOf(SEPARATOR_TITLE));
    String author = request.substring(request.lastIndexOf(SEPARATOR_TITLE) + 2, request.lastIndexOf(SEPARATOR));
    String date = request.substring(request.lastIndexOf(SEPARATOR) + 1);

    ServiceFactory service = ServiceFactory.getInstance();
    NewsService newsService = service.getNewsService();

    String response;
    try {
      newsService.addNews(new News(category, title, author, date));
      response = "\nNews added to the catalog.";
    } catch (ServiceException e) {
      response = "\nProblem with adding news.";
    }
    return response;
  }
}
