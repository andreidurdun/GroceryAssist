package com.grocery.assist.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.List;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
    }


}