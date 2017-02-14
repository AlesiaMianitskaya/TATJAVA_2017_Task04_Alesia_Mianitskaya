package com.epam.catalog.controller;

import com.epam.catalog.command.Command;
import com.epam.catalog.service.NewsService;
import com.epam.catalog.service.exception.ServiceException;
import com.epam.catalog.service.factory.ServiceFactory;

/**
 * Execute command from request
 */
public class Controller {
  private final CommandProvider commandProvider = new CommandProvider();
  private final String SEPARATOR = " ";

  /**
   * execute command
   *
   * @param request line contains written command
   * @return response to the request
   */
  public String executeGoal(String request) {
    String commandName;
    Command executionCommand;

    commandName = request.split(SEPARATOR, 2)[0];
    executionCommand = commandProvider.getCommand(commandName);

    String response;
    response = executionCommand.execute(request);
    return response;
  }

  /**
   * complete of execution of all commands
   */
  public void destroy() {
    try {
      ServiceFactory service = ServiceFactory.getInstance();
      NewsService newsService = service.getNewsService();
      newsService.destroy();
    } catch (ServiceException e) {
      System.out.println("Problem with the completion of execution commands.");
    }
  }
}
