package com.epam.catalog.dao.impl;

import com.epam.catalog.bean.News;
import com.epam.catalog.dao.NewsDAO;
import com.epam.catalog.dao.exception.DAOException;
import com.epam.catalog.dao.factory.DAOFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static org.testng.AssertJUnit.assertEquals;


/**
 * Unit tests for class SQlNewsDAO
 */
public class SQLNewsDAOTest {
  private DAOFactory daoFactoryObj;
  private NewsDAO newsDAO;
  private News news;
  private ArrayList<News> foundList;

  // JDBC Driver, URL, username and password of MySQL server
  private static final String DRIVER = "com.mysql.jdbc.Driver";
  private static final String URL = "jdbc:mysql://127.0.0.1/catalog";
  private static final String USER = "root";
  private static final String PASSWORD = "root";

  private Connection connection;
  private Statement statement;
  private ResultSet resultSet;

  private final static String SQL_SELECT_ADDED_NEWS = "SELECT * FROM catalog.news WHERE title ='Atlas Shrug' AND author = 'Ayn Rand' AND date = '2017-01-18'";
  private final static String SQL_SELECT_NEWS_FOR_SEARCH = "SELECT * FROM catalog.news WHERE idnesw = '1'";

  @BeforeTest
  public void setUp() throws Exception {
    daoFactoryObj = DAOFactory.getInstance();
    newsDAO = daoFactoryObj.getNewsDAO();
    connection = null;
    statement = null;
    resultSet = null;
    Class.forName(DRIVER);
    connection = DriverManager.getConnection(URL, USER, PASSWORD);
    statement = connection.createStatement();

  }

  @AfterTest
  public void tearDown() throws Exception {
    daoFactoryObj = null;
    newsDAO = null;

    if (resultSet != null) {
      resultSet.close();
    }
    if (statement != null) {
      statement.close();
    }
    if (connection != null) {
      connection.close();
    }
  }

  private News getNewsFromBD() throws Exception{
    News newsFromDB = new News();
    resultSet = statement.executeQuery(SQL_SELECT_NEWS_FOR_SEARCH);
    while (resultSet.next()) {
      String categoryFromBD = resultSet.getObject(2).toString();
      String titleFromBD = resultSet.getObject(3).toString();
      String authorFromBD = resultSet.getObject(4).toString();
      String dateFromBD = resultSet.getObject(5).toString();
      newsFromDB = new News(categoryFromBD, titleFromBD, authorFromBD, dateFromBD);
    }
    return newsFromDB;
  }

  @Test
  public void testAddToDataBase() throws Exception {
    String category = "Book";
    String title = "Atlas Shrug";
    String author = "Ayn Rand";
    String date = "2017-01-18";
    news = new News(category, title, author, date);
    newsDAO.addNews(news);

    News newsFromDB = new News();
    resultSet = statement.executeQuery(SQL_SELECT_ADDED_NEWS);
    while (resultSet.next()) {
      String categoryFromBD = resultSet.getObject(2).toString();
      String titleFromBD = resultSet.getObject(3).toString();
      String authorFromBD = resultSet.getObject(4).toString();
      String dateFromBD = resultSet.getObject(5).toString();
      newsFromDB = new News(categoryFromBD, titleFromBD, authorFromBD, dateFromBD);
    }
    assertEquals(news, newsFromDB);
  }


  @Test
  public void testFindByTitlePositive() throws Exception {
    foundList = newsDAO.findByTitle(getNewsFromBD().getTitle());
    assertEquals(getNewsFromBD(), foundList.get(0));
  }

  @Test(expectedExceptions = DAOException.class)
  public void testFindByTitleEmpty() throws Exception {
    newsDAO.findByTitle("");
  }

  @Test(expectedExceptions = DAOException.class)
  public void findByTitleNull() throws Exception {
    newsDAO.findByTitle(null);
  }

  @Test
  public void testFindByAuthorPositive() throws Exception {
    foundList = newsDAO.findByAuthor(getNewsFromBD().getAuthor());
    assertEquals(getNewsFromBD(), foundList.get(0));
  }

  @Test(expectedExceptions = DAOException.class)
  public void testFindByAuthorEmpty() throws Exception {
    newsDAO.findByAuthor("");
  }

  @Test(expectedExceptions = DAOException.class)
  public void testFindByAuthorNull() throws Exception {
    newsDAO.findByAuthor(null);
  }

  @Test
  public void testFindByCategoryPositive() throws Exception {
    foundList = newsDAO.findByCategory(getNewsFromBD().getCategory());
    assertEquals(getNewsFromBD().getCategory(), foundList.get(0).getCategory());
  }

  @Test(expectedExceptions = DAOException.class)
  public void testFindByCategoryEmpty() throws Exception {
    newsDAO.findByCategory("");
  }

  @Test(expectedExceptions = DAOException.class)
  public void testFindByCategoryNull() throws Exception {
    newsDAO.findByAuthor(null);
  }

  @Test
  public void testFindByNewsPositive() throws Exception {
    foundList = newsDAO.findByNews(getNewsFromBD());
    assertEquals(getNewsFromBD(), foundList.get(0));
  }

  @DataProvider(name = "incorrect data")
  public Object[][] getWrongNews() {
    return new Object[][]{
        {new News(null, null, null, null)},
        {new News(null, "The Fountainhead", "Ayn Rand", "01.18.2017")},
        {new News("Book", null, "Ayn Rand", "01.18.2017")},
        {new News("Book", "The Fountainhead", null, "01.18.2017")},
        {new News("Book", "The Fountainhead", "Ayn Rand", null)},
        {new News("", "", "", "")},
        {new News("", "The Fountainhead", "Ayn Rand", "01.18.2017")},
        {new News("Book", "", "Ayn Rand", "01.18.2017")},
        {new News("Book", "The Fountainhead", "", "01.18.2017")},
        {new News("Book", "The Fountainhead", "Ayn Rand", "")}
    };
  }

  @Test(expectedExceptions = DAOException.class, dataProvider = "incorrect data")
  public void testFindByNewsException(News wrongNews) throws Exception {
    foundList = newsDAO.findByNews(wrongNews);
  }

  @Test(expectedExceptions = DAOException.class, dataProvider = "incorrect data")
  public void testAddNewsException(News wrongNews) throws Exception {
    newsDAO.addNews(wrongNews);
  }
}