package com.epam.catalog.dao.factory;
import com.epam.catalog.dao.NewsDAO;
import com.epam.catalog.dao.impl.SQLNewsDAO;

/**
 * creating one object of class FileNewsDAO and
 * getting this object
 */
public class DAOFactory {
  private static final DAOFactory instance = new DAOFactory();

  private final NewsDAO newsDAO = new SQLNewsDAO();

  public static DAOFactory getInstance() {
    return instance;
  }

  public NewsDAO getNewsDAO() {
    return newsDAO;
  }
}
