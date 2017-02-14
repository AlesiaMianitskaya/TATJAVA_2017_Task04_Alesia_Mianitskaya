package com.epam.catalog.dao.connection;

import java.util.ResourceBundle;

/**
 * Provides functionality of database resource management
 * creating one object of class and getting this object
 * getting value of database parameters
 */
public class DBResourceManager {
  private final static DBResourceManager instance = new DBResourceManager();

  private ResourceBundle bundle = ResourceBundle.getBundle("db");

  public static DBResourceManager getInstance() {
    return instance;
  }

  public String getValue(String key) {
    return bundle.getString(key);
  }
}

