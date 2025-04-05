package com.grocery.assist.service;

import com.grocery.assist.model.Subcategory;
import com.grocery.assist.repository.SubcategoryRepository;

public class SubcategoryService {
    private final SubcategoryRepository repository;
    private final Scraper scraper;

    public SubcategoryService(SubcategoryRepository repository, Scraper scraper) {
        this.repository = repository;
        this.scraper = scraper;
    }

    public void scrapeById(Long id) {
        Subcategory subcategory = repository.findById(id);
        if (subcategory != null) {
            scraper.scrapeJsoup(subcategory.getLink());
        } else {
            System.out.println("Subcategory not found for id: " + id);
        }
    }
}
