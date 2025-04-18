package com.grocery.assist.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Scraper {

    public static void scrapeSelenium() {
        // Configurare automata cu WebDriverManager
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        String url = "https://www.auchan.ro/lactate-carne-mezeluri---peste/lactate/lapte/c";

        try {
            driver.get(url);
            Thread.sleep(3000); // Așteaptă încărcarea JavaScript

            List<WebElement> products = driver.findElements(By.cssSelector(".vtex-product-summary-2-x-container"));
            for (WebElement product : products) {
                String name = product.findElement(By.cssSelector(".vtex-product-summary-2-x-productBrand")).getText();
                String price = product.findElement(By.cssSelector(".vtex-product-price-1-x-sellingPrice")).getText();

                System.out.println("Denumire: " + name);
                System.out.println("Preț: " + price);
                System.out.println("------------------");
            }

        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    public static void scrapeSelenium2() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String url = "https://www.auchan.ro/lactate-carne-mezeluri---peste/lactate/lapte/c";

        try {
            driver.get(url);

            // Așteaptă să se încarce primele produse
            Thread.sleep(3000);

            // Apasă pe "Arată mai mult" până când nu mai există
            while (true) {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(1000); // dă timp să apară butonul

                try {
                    WebElement showMoreBtn = wait.until(ExpectedConditions.elementToBeClickable(
                            By.cssSelector("button.vtex-search-result-3-x-showMoreButton")));

                    showMoreBtn.click();
                    Thread.sleep(2000); // așteaptă să se încarce produsele noi
                } catch (TimeoutException e) {
                    // Butonul nu mai apare => toate produsele sunt încărcate
                    break;
                }
            }

            // După ce toate produsele sunt încărcate
            List<WebElement> products = driver.findElements(By.cssSelector(".vtex-product-summary-2-x-container"));
            for (WebElement product : products) {
                try {
                    String name = product.findElement(By.cssSelector(".vtex-product-summary-2-x-productBrand")).getText();
                    String price = product.findElement(By.cssSelector(".vtex-product-price-1-x-sellingPrice")).getText();

                    System.out.println("Denumire: " + name);
                    System.out.println("Preț: " + price);
                    System.out.println("------------------");
                } catch (NoSuchElementException ignored) {
                    // Unele produse pot lipsi temporar sau pot avea structura diferită
                }
            }

        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }


    public static void scrapeJsoup() {
        String url = "https://www.auchan.ro/lactate-carne-mezeluri---peste/lactate/lapte/c";

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .get();

            // Selectori corecți (adaptează după inspectare)
            Elements products = doc.select(".vtex-product-summary-2-x-container"); // Container produs
            for (Element product : products) {
                String name = product.select(".vtex-product-summary-2-x-productBrand").text();
                String price = product.select(".vtex-product-price-1-x-sellingPrice").text();

                System.out.println("Denumire: " + name);
                System.out.println("Preț: " + price);
                System.out.println("------------------");
            }

        } catch (IOException e) {
            System.err.println("Eroare: " + e.getMessage());
        }
    }






    public static void main(String[] args) {
        //scrapeSelenium();
        //scrapeJsoup();
        scrapeSelenium2();

    }


}