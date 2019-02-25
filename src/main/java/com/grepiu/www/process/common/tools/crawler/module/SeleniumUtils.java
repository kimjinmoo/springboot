package com.grepiu.www.process.common.tools.crawler.module;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * Selenium 크롤링 유틸
 *
 */
public class SeleniumUtils {

  /**
   *
   * Root 엘리먼트에서 Loop 엘리먼트를 가져온다.
   *
   * @param webElement
   * @param by
   * @return
   */
  public static List<WebElement> findElements(WebElement webElement, By by) {
    try {
      return webElement.findElements(by);
    } catch (Exception e) {
      return null;
    }

  }

  /**
   *
   * Root엘리먼트에서 특정 엘리먼트를 가져온다.
   *
   * @param webElement
   * @param by
   * @return
   * @throws Exception
   */
  public static WebElement findElement(WebElement webElement, By by) throws Exception {
    try {
      return webElement.findElement(by);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 엘리먼트를 클릭한다.
   */
  public static void elementClick(WebDriver webDriver, WebElement webElement) {
    Actions actions = new Actions(webDriver);
    actions.moveToElement(webElement).click().perform();
    sleep();
  }

  /**
   * 시간 지연 처리한다.
   * 네트워크 사항등을 고려하여 변경해야될 필요도 있다.
   */
  public static void sleep() {
    try {
      Thread.sleep(CrawlerConstants.DEFAULT_SLEEP_SECOND);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
