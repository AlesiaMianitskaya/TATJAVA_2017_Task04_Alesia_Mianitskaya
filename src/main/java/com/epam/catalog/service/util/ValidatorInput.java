package com.epam.catalog.service.util;

import com.epam.catalog.bean.News;
import com.epam.catalog.service.exception.ServiceException;

/**
 * Validator input data for DAO
 */
public class ValidatorInput {
  /**
   * validate news of null and emptiness
   *
   * @param news validated news
   * @throws ServiceException if news aren't valid
   */
  public static void validateNews(News news) throws ServiceException {
    boolean notCategory = news.getCategory().isEmpty() || (news.getCategory() == null);
    if (notCategory) {
      throw new ServiceException("News' category is not valid.");
    }
    boolean notTitle = news.getTitle().isEmpty() || (news.getTitle() == null);
    if (notTitle) {
      throw new ServiceException("News' title is not valid.");
    }
    boolean notAuthor = news.getAuthor().isEmpty() || (news.getAuthor() == null);
    if (notAuthor) {
      throw new ServiceException("News' author is not valid.");
    }
    boolean notDate = news.getDate().isEmpty() || (news.getDate() == null);
    if (notDate) {
      throw new ServiceException("News' date is not valid.");
    }
  }

  /**
   * validate news' parameter of null and emptiness
   *
   * @param parameter - news' parameter
   * @throws ServiceException if news' parameter isn't valid
   */
  public static void validateParameter(String parameter) throws ServiceException {
    if (parameter == null || parameter.isEmpty()) {
      throw new ServiceException("Comparison condition is not valid.");
    }
  }
}
