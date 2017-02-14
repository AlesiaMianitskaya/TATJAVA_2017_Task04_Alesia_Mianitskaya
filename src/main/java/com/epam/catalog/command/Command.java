package com.epam.catalog.command;

/**
 * Interface for execution commands
 */
public interface Command {
  String execute(String request);
}
