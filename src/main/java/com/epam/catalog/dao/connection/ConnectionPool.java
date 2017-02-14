package com.epam.catalog.dao.connection;

import com.epam.catalog.dao.exception.DAOException;

import java.sql.*;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

/**
 * Pool of connections to the database
 */
public final class ConnectionPool {

  private final static ConnectionPool instance = new ConnectionPool();

  private BlockingQueue<Connection> connectionQueue;
  private BlockingQueue<Connection> givenAwayConQueue;

  private String driverName;
  private String url;
  private String user;
  private String password;
  private int poolSize;

  public static ConnectionPool getInstance() {
    return instance;
  }

  /**
   * fill parameters of database from file bd.properties
   */
  private ConnectionPool() {
    DBResourceManager dbResourceManager = DBResourceManager.getInstance();
    this.driverName = dbResourceManager.getValue(DBParameter.DB_DRIVER);
    this.url = dbResourceManager.getValue(DBParameter.DB_URL);
    this.user = dbResourceManager.getValue(DBParameter.DB_USER);
    this.password = dbResourceManager.getValue(DBParameter.DB_PASSWORD);
    try {
      this.poolSize = Integer.parseInt(dbResourceManager.getValue(DBParameter.DB_POOL_SIZE));
    } catch (NumberFormatException e) {
      this.poolSize = 5;
    }
  }

  /**
   * poll initialization of connections and add connections to the queue
   *
   * @throws DAOException if a database driver class can't find
   *                      or if a database access error occurs
   */
  public void initPoolData() throws DAOException {
    Locale.setDefault(Locale.ENGLISH);
    try {
      Class.forName(driverName);
      givenAwayConQueue = new ArrayBlockingQueue<Connection>(poolSize);
      connectionQueue = new ArrayBlockingQueue<Connection>(poolSize);
      for (int i = 0; i < poolSize; i++) {
        Connection connection = DriverManager.getConnection(url, user, password);
        PooledConnection pooledConnection = new PooledConnection(connection);
        connectionQueue.add(pooledConnection);
      }
    } catch (SQLException e) {
      throw new DAOException("SQLException in connection pool.", e);
    } catch (ClassNotFoundException e) {
      throw new DAOException("Can't find database driver class", e);
    }
  }

  /**
   * clear connection pool and close all connection
   *
   * @throws DAOException if error closing the connection
   */
  public void dispose() throws DAOException {
    clearConnectionQueue();
  }

  /**
   * clear queues contains available connection
   * and given away connection
   *
   * @throws DAOException if error closing the connection
   */
  private void clearConnectionQueue() throws DAOException {
    try {
      closeConnectionsQueue(givenAwayConQueue);
      closeConnectionsQueue(connectionQueue);
    } catch (SQLException e) {
      throw new DAOException("Error closing the connection.", e);
    }
  }

  /**
   * close all connection from queue
   *
   * @param queue contains connections
   * @throws SQLException if error closing the connection
   */
  private void closeConnectionsQueue(BlockingQueue<Connection> queue) throws SQLException {
    Connection connection;
    while ((connection = queue.poll()) != null) {
      if (!connection.getAutoCommit()) {
        connection.commit();
      }
      ((PooledConnection) connection).reallyClose();
    }
  }

  /**
   * take connection from the queue contained all connections
   * add taken connection to the givenAwayConQueue
   *
   * @return taken connection
   * @throws DAOException if error connecting to the data source
   */
  public Connection takeConnection() throws DAOException {
    Connection connection = null;
    try {
      connection = connectionQueue.take();
      givenAwayConQueue.add(connection);
    } catch (InterruptedException e) {
      throw new DAOException("Error connecting to the data source.", e);
    }
    return connection;
  }

  /**
   * close connection, statement and result set
   *
   * @param con - connection
   * @param st  - statement
   * @param rs  - result set
   */
  public void closeConnection(Connection con, Statement st, ResultSet rs) {
    try {
      rs.close();
    } catch (SQLException e) {
      //logger.log Level.ERROR, "Result Set isn't closed."
    }
    try {
      st.close();
    } catch (SQLException e) {
      //logger.log Level.ERROR, "Statement isn't closed."
    }
    try {
      con.close();
    } catch (SQLException e) {
      //logger.log Level.ERROR, "Connection isn't return to the pool."
    }
  }

  /**
   * close connection and statement
   *
   * @param con - connection
   * @param st  - statement
   */
  public void closeConnection(Connection con, Statement st) {
    try {
      con.close();
    } catch (SQLException e) {
      //logger.log Level.ERROR, "Connection isn't return to the pool."
    }
    try {
      st.close();
    } catch (SQLException e) {
      //logger.log Level.ERROR, "Statement isn't closed."
    }
  }

  public void getInfo() {
    System.out.println("!!!!" + connectionQueue.size() + " " + givenAwayConQueue.size());
  }

  /**
   * Inner class for connections from connection pool
   */
  private class PooledConnection implements Connection {
    private Connection connection;

    public PooledConnection(Connection c) throws SQLException {
      this.connection = c;
      this.connection.setAutoCommit(true);
    }

    public void reallyClose() throws SQLException {
      connection.close();
    }

    @Override
    public Statement createStatement() throws SQLException {
      return connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
      return connection.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
      return connection.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
      return connection.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
      connection.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
      return connection.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
      connection.commit();
    }

    @Override
    public void rollback() throws SQLException {
      connection.rollback();
    }

    @Override
    public void close() throws SQLException {
      if (connection.isClosed()) {
        throw new SQLException("Attempting to close closed connection");
      }
      if (connection.isReadOnly()) {
        connection.setReadOnly(false);
      }
      if (!givenAwayConQueue.remove(this)) {
        throw new SQLException("Error deleting connection from the given away connections pool.");
      }
      if (!connectionQueue.offer(this)) {
        throw new SQLException("Error allocating connection in the pool.");
      }
    }

    @Override
    public boolean isClosed() throws SQLException {
      return connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
      return connection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
      connection.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
      return connection.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
      connection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
      return connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
      connection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
      return connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
      return connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
      connection.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
      return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
      return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
      return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
      return connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
      connection.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
      connection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
      return connection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
      return connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
      return connection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
      connection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
      connection.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
      return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
      return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
      return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
      return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
      return connection.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
      return connection.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
      return connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
      return connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
      return connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
      return connection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
      return connection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
      connection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
      connection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
      return connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
      return connection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
      return connection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
      return connection.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
      connection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
      return connection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
      connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
      connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
      return connection.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
      return connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
      return connection.isWrapperFor(iface);
    }
  }
}
