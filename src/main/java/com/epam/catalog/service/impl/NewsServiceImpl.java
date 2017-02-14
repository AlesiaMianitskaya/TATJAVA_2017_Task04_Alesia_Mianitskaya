package com.epam.catalog.service.impl;

import com.epam.catalog.bean.News;
import com.epam.catalog.dao.NewsDAO;
import com.epam.catalog.dao.exception.DAOException;
import com.epam.catalog.dao.factory.DAOFactory;
import com.epam.catalog.service.NewsService;
import com.epam.catalog.service.exception.ServiceException;
import com.epam.catalog.service.util.ValidatorInput;

import java.util.ArrayList;

/**
 * Implementation requests. Validate input data for DAO
 */
public class NewsServiceImpl implements NewsService {
  private ArrayList<News> foundList;

  /**
   * validate input data and add news to the catalog
   *
   * @param news added news to the catalog
   * @throws ServiceException for incorrect input data
   */
  @Override
  public void addNews(News news) throws ServiceException {
   ValidatorInput.validateNews(news);
    try {
      DAOFactory daoFactoryObj = DAOFactory.getInstance();
      NewsDAO newsDAO = daoFactoryObj.getNewsDAO();
      newsDAO.addNews(news);
    } catch (DAOException e) {
      throw new ServiceException(e);
    }
  }

  /**
   * validate input data and find news by category
   *
   * @param category found news' title
   * @throws ServiceException for incorrect input data
   */
  @Override
  public ArrayList<News> findByCategory(String category) throws ServiceException {
    ValidatorInput.validateParameter(category);
    try {
      DAOFactory daoFactoryObj = DAOFactory.getInstance();
      NewsDAO newsDAO = daoFactoryObj.getNewsDAO();
      foundList = newsDAO.findByCategory(category);
    } catch (DAOException e) {
      throw new ServiceException(e);
    }
    return foundList;
  }

  /**
   * validate input data and find news by title
   *
   * @param title found news' title
   * @throws ServiceException for incorrect input data
   */
  @Override
  public ArrayList<News> findByTitle(String title) throws ServiceException {
    ValidatorInput.validateParameter(title);
    try {
      DAOFactory daoFactoryObj = DAOFactory.getInstance();
      NewsDAO newsDAO = daoFactoryObj.getNewsDAO();
      foundList = newsDAO.findByTitle(title);
    } catch (DAOException e) {
      throw new ServiceException(e);
    }
    return foundList;
  }

  /**
   * validate input data and find news by author
   *
   * @param author found news' author
   * @throws ServiceException for incorrect input data
   */
  @Override
  public ArrayList<News> findByAuthor(String author) throws ServiceException {
   ValidatorInput.validateParameter(author);
    try {
      DAOFactory daoFactoryObj = DAOFactory.getInstance();
      NewsDAO newsDAO = daoFactoryObj.getNewsDAO();
      foundList = newsDAO.findByAuthor(author);
    } catch (DAOException e) {
      throw new ServiceException(e);
    }
    return foundList;
  }

  /**
   * validate input data and find specific news
   *
   * @param news found news
   * @throws ServiceException for incorrect input data
   */
  @Override
  public ArrayList<News> findByNews(News news) throws ServiceException {
    ValidatorInput.validateNews(news);
    try {
      DAOFactory daoFactoryObj = DAOFactory.getInstance();
      NewsDAO newsDAO = daoFactoryObj.getNewsDAO();
      foundList = newsDAO.findByNews(news);
    } catch (DAOException e) {
      throw new ServiceException(e);
    }
    return foundList;
  }

  /**
   * complete of all requests
   *
   * @throws ServiceException if problem with the completion of requests
   */
  public void destroy() throws ServiceException {
    try {
      DAOFactory daoFactoryObj = DAOFactory.getInstance();
      NewsDAO newsDAO = daoFactoryObj.getNewsDAO();
      newsDAO.destroy();
    } catch (DAOException e) {
      throw new ServiceException(e);
    }
  }
}
