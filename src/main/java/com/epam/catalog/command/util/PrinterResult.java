package com.epam.catalog.command.util;

import com.epam.catalog.bean.News;

import java.util.ArrayList;

/**
 * Class for printing search result on the console
 */
public class PrinterResult {

  /**
   * print found news
   *
   * @param foundList arrayList contains found news
   */
  public static void printFound(ArrayList<News> foundList) {
    System.out.println("\nFound " + foundList.size() + " news:");
    for (int i = 0; i < foundList.size(); i++) {
      System.out.println(foundList.get(i).getCategory() + " " + foundList.get(i).getTitle() +
          " " + foundList.get(i).getAuthor() + " " + foundList.get(i).getDate());
    }
  }
}
