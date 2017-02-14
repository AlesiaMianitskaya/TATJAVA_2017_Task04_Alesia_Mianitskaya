package com.epam.catalog.controller;

import com.epam.catalog.command.Command;
import com.epam.catalog.command.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * define executable command
 */
class CommandProvider {
  private final Map<CommandName, Command> box = new HashMap<>();

  CommandProvider() {
    box.put(CommandName.ADD_NEWS, new AddNews());
    box.put(CommandName.FIND_BY_TITLE, new FindByTitle());
    box.put(CommandName.FIND_BY_AUTHOR, new FindByAuthor());
    box.put(CommandName.FIND_BY_NEWS, new FindByNews());
    box.put(CommandName.FIND_BY_CATEGORY, new FindByCategory());
    box.put(CommandName.WRONG_REQUEST, new WrongRequest());
  }

  /**
   * get needed command by command name
   *
   * @param name - name of the command
   * @return execution command
   */
  Command getCommand(String name) {
    CommandName commandName;
    Command command;
    try {
      commandName = CommandName.valueOf(name.toUpperCase());
      command = box.get(commandName);
    } catch (IllegalArgumentException | NullPointerException e) {
      command = box.get(CommandName.WRONG_REQUEST);
    }
    return command;
  }
}
