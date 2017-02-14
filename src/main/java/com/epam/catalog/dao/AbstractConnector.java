package com.epam.catalog.dao;

import com.epam.catalog.dao.connection.ConnectionPool;
import com.epam.catalog.dao.exception.DAOException;

import java.sql.*;

/**
 * Abstract class provides functionality of access to a connection pool
 */
public abstract class AbstractConnector implements NewsDAO {
  private Connection connection;
  private ConnectionPool connectionPool = ConnectionPool.getInstance();

  public AbstractConnector() {
    try {
      connectionPool.initPoolData();
      connection = connectionPool.takeConnection();
    } catch (DAOException e) {
      System.out.println("Problem with database!");
    }
  }

  /**
   * get PreparedStatement
   *
   * @param query string contains SQL query
   * @return PreparedStatement
   * @throws SQLException if connection or statement is null
   */
  public PreparedStatement getPrepareStatement(String query) throws SQLException {
    if (connection != null) {
      PreparedStatement pst = connection.prepareStatement(query);
      if (pst != null) {
        return pst;
      }
    }
    throw new SQLException("Connection or statement is null.");
  }

  /**
   * close PreparedStatement
   *
   * @param pst PreparedStatement
   */
  public void closePreparedStatement(PreparedStatement pst) throws DAOException {
    if (pst != null) {
      try {
        pst.close();
      } catch (SQLException e) {
        throw new DAOException("Statement is null");
      }
    }
  }

  /**
   * close ResultSet
   *
   * @param rs ResultSet
   */
  public void closeResultSet(ResultSet rs) throws DAOException {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) {
        throw new DAOException("Result set is null.");
      }
    }
  }

  /**
   * return connection in pool
   *
   * @throws DAOException if problem closing connection
   */
  public void returnConnection() throws DAOException {
    try {
      connection.close();
    } catch (SQLException e) {

    }
  }

  /**
   * close all connections from pool
   *
   * @throws DAOException if problem closing all connection
   */
  public void destroy() throws DAOException {
    connectionPool.dispose();
  }
}
