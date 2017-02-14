package com.epam.catalog.command.impl;

import com.epam.catalog.command.Command;

/**
 * Execution command if request is wrong
 */
public class WrongRequest implements Command {
  /**
   * return message about wrong request
   *
   * @param request line with wrong argument
   * @return response to the request
   */
  @Override
  public String execute(String request) {
    String response = "\nRequest can not be executed.";
    return response;
  }
}
