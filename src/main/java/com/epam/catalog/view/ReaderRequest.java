package com.epam.catalog.view;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * reading request from txt-file.
 * request is line contains command's name and input arguments
 */
public class ReaderRequest {
  private final String PATH = ".\\requests.txt";
  private ArrayList<String> requestList = new ArrayList<>();

  /**
   * read requests
   *
   * @return arrayList contains requests
   */
  public ArrayList<String> readRequest() {
    try {
      FileReader fileReader = new FileReader(PATH);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String request;
      while ((request = bufferedReader.readLine()) != null) {
        requestList.add(request);
      }
      bufferedReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("File with requests not found.");
    } catch (IOException e) {
      System.out.println("Error during reading the file.");
    }
    return requestList;
  }
}
