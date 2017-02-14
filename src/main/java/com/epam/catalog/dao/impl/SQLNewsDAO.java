package com.epam.catalog.dao.impl;

import com.epam.catalog.bean.News;
import com.epam.catalog.dao.AbstractConnector;
import com.epam.catalog.dao.NewsDAO;
import com.epam.catalog.dao.exception.DAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementation of requests to the catalog
 * catalog is database contains information about news
 * extends AbstractConnector to access the database
 */
public class SQLNewsDAO extends AbstractConnector implements NewsDAO {
  private static final String SQL_INSERT_NEWS = "INSERT INTO catalog.news (idcategory, title, author, date) VALUES (?,?,?,?)";
  private static final String SQL_SELECT_BY_TITLE = "SELECT * FROM catalog.news WHERE title = ? ";
  private static final String SQL_SELECT_BY_AUTHOR = "SELECT * FROM catalog.news WHERE author = ?";
  private static final String SQL_SELECT_BY_CATEGORY = "SELECT * FROM catalog.news WHERE idcategory = ?";
  private static final String SQL_SELECT_BY_NEWS = "SELECT * FROM catalog.news WHERE title = ? AND author = ? AND date = ?";

  /**
   * add news to the catalog
   *
   * @param news added news
   * @return 1 if news added otherwise
   * 0 for SQL statements that return nothing
   * @throws DAOException if a database access error occurs
   */
  @Override
  public int addNews(News news) throws DAOException {
    int amountUp;
    PreparedStatement pst = null;
    try {
      pst = getPrepareStatement(SQL_INSERT_NEWS);
      pst.setString(1, news.getCategory());
      pst.setString(2, news.getTitle());
      pst.setString(3, news.getAuthor());
      pst.setString(4, news.getDate());
      amountUp = pst.executeUpdate();
    } catch (SQLException e) {
      throw new DAOException("Problem with catalog.", e);
    } finally {
      closePreparedStatement(pst);
      returnConnection();
    }
    return amountUp;
  }

  /**
   * find news in catalog by title
   *
   * @param titleBy title by which looking for
   * @return foundList contains found news if news not found
   * throws DAOException
   * @throws DAOException if a database access error occurs
   */
  @Override
  public ArrayList<News> findByTitle(String titleBy) throws DAOException {
    ArrayList<News> foundList = new ArrayList<>();
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = getPrepareStatement(SQL_SELECT_BY_TITLE);
      st.setString(1, titleBy);
      rs = st.executeQuery();
      while (rs.next()) {
        String category = rs.getString(2);
        String title = rs.getString(3);
        String author = rs.getString(4);
        String date = rs.getString(5);
        foundList.add(new News(category, title, author, date));
      }
      isEmpty(foundList);
    } catch (SQLException e) {
      throw new DAOException("Problem with catalog.", e);
    } finally {
      closePreparedStatement(st);
      closeResultSet(rs);
      returnConnection();
    }
    return foundList;
  }

  /**
   * find news in catalog by author
   *
   * @param authorBy author's name by which looking for
   * @return foundList contains found news
   * if news not found throws DAOException
   * @throws DAOException if a database access error occurs
   */
  @Override
  public ArrayList<News> findByAuthor(String authorBy) throws DAOException {
    ArrayList<News> foundList = new ArrayList<>();
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = getPrepareStatement(SQL_SELECT_BY_AUTHOR);
      st.setString(1, authorBy);
      rs = st.executeQuery();
      while (rs.next()) {
        String category = rs.getString(2);
        String title = rs.getString(3);
        String author = rs.getString(4);
        String date = rs.getString(5);
        foundList.add(new News(category, title, author, date));
      }
      isEmpty(foundList);
    } catch (SQLException e) {
      throw new DAOException("Problem with catalog.", e);
    } finally {
      closePreparedStatement(st);
      closeResultSet(rs);
      returnConnection();
    }
    return foundList;
  }

  /**
   * find specific news in catalog
   *
   * @param news news by which looking for
   * @return foundList contains found news if news not found
   * return empty arrayList
   * @throws DAOException if a database access error occurs
   */
  @Override
  public ArrayList<News> findByNews(News news) throws DAOException {
    ArrayList<News> foundList = new ArrayList<>();
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = getPrepareStatement(SQL_SELECT_BY_NEWS);
      st.setString(1, news.getTitle());
      st.setString(2, news.getAuthor());
      st.setString(3, news.getDate());
      rs = st.executeQuery();
      while (rs.next()) {
        String category = rs.getString(2);
        String title = rs.getString(3);
        String author = rs.getString(4);
        String date = rs.getString(5);
        foundList.add(new News(category, title, author, date));
      }
      isEmpty(foundList);
    } catch (SQLException e) {
      throw new DAOException("Problem with catalog.", e);
    } finally {
      closePreparedStatement(st);
      closeResultSet(rs);
      returnConnection();
    }
    return foundList;
  }

  /**
   * find news in catalog by category
   *
   * @param categoryBy category by which looking for
   * @return foundList contains found news
   * if news not found throws DAOException
   * @throws DAOException if a database access error occurs
   */
  @Override
  public ArrayList<News> findByCategory(String categoryBy) throws DAOException {
    ArrayList<News> foundList = new ArrayList<>();
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = getPrepareStatement(SQL_SELECT_BY_CATEGORY);
      st.setString(1, categoryBy);
      rs = st.executeQuery();
      while (rs.next()) {
        String category = rs.getString(2);
        String title = rs.getString(3);
        String author = rs.getString(4);
        String date = rs.getString(5);
        foundList.add(new News(category, title, author, date));
      }
      isEmpty(foundList);
    } catch (SQLException e) {
      throw new DAOException("Problem with catalog.", e);
    } finally {
      closePreparedStatement(st);
      closeResultSet(rs);
      returnConnection();
    }
    return foundList;
  }

  /**
   * check output found news, throw DAOException if news not found
   *
   * @param foundList - list contains found list
   * @throws DAOException if list empty
   */
  private void isEmpty(ArrayList<News> foundList) throws DAOException {
    if (foundList.isEmpty()) {
      throw new DAOException("List contains news is empty.");
    }
  }
}

