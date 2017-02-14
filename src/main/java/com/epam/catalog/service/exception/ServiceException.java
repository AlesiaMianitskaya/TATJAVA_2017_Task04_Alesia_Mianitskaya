package com.epam.catalog.service.exception;


/**
 * exception for service layer
 */
public class ServiceException extends Exception{
  public ServiceException() {
    super();
  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(Exception e) {
    super(e);
  }

  public ServiceException(String message, Exception e){
    super(message, e);
  }

}
