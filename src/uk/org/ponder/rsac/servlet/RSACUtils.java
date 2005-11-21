/*
 * Created on Sep 21, 2005
 */
package uk.org.ponder.rsac.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.rsac.RSACBeanLocator;

/**
 * @author andrew, Antranig
 * 
 */
public class RSACUtils {
  public static final String REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE = "requestScopeApplicationContext";
  /** The default name for the bean representing the HttpServletFactory **/
  public static final String HTTP_SERVLET_FACTORY = "httpservletfactory";

  /**
   * This method is to be used in the awkward situation where a request is
   * subject to a RequestDispatch partway through its lifecycle, and the request
   * visible to it is not the one for which the RSAC context is required to
   * execute, but client nonetheless wishes to populate or amend some beans into
   * the request scope. Clearly none of these beans should be RequestAware. <br>
   * This method has the effect of marking the bean container both onto the
   * current thread and onto a request attributes, which it is assumed the
   * request dispatcher has the sense not to trash.
   * @param rsacbl 
   */
  public static void protoStartServletRequest(HttpServletRequest request, RSACBeanLocator rsacbl) {
    rsacbl.startRequest();
  }
  
  public static void startServletRequest(HttpServletRequest request, 
      HttpServletResponse response, RSACBeanLocator rsacbl, String httpfactorybean) {
    if (!rsacbl.isStarted()) {
      rsacbl.startRequest();
    }
    BeanLocator locator = rsacbl.getBeanLocator();
    HttpServletFactory factory = (HttpServletFactory) locator.locateBean(httpfactorybean);
    if (factory != null) {
      factory.setHttpServletRequest(request);
      factory.setHttpServletResponse(response);
    }
    setRequestApplicationContext(request, locator);
  }
  
  public static void setRequestApplicationContext(
      ServletRequest request, BeanLocator context) {
    request.setAttribute(REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE, context);
  }

  public static BeanLocator getRequestApplicationContext(
      ServletRequest request) {
    return (BeanLocator) request
        .getAttribute(REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE);
  }

  public static void removeRequestApplicationContext(ServletRequest request) {
    request.removeAttribute(REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE);
  }
}