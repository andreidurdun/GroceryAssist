package com.grocery.assist.service;

import com.grocery.assist.model.Category;
import com.grocery.assist.model.Product;
import com.grocery.assist.repository.CategoryRepository;
import com.grocery.assist.repository.ProductRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    public final BertClassifier clasifier;
    public final ProductRepository productRepository;
    public final CategoryRepository categoryRepository;

    public ProductService() throws SQLException {
        this.clasifier = new BertClassifier();
        this.productRepository = ProductRepository.getInstance();
        this.categoryRepository = CategoryRepository.getInstance();
    }

    public List<Product> assignCategories(List<Product> products) throws IOException, SQLException {
        List<String> productsNames = new ArrayList<>();
        for (Product product : products) {
            productsNames.add(product.getProductName());
        }
        List<String> predictedCategories = clasifier.predictBulk(productsNames);

        for (int i = 0; i < products.size(); i++) {
            String categoryName = predictedCategories.get(i).replace("-", " ");
            Long categoryId = categoryRepository.findByName(categoryName);
            if(categoryId == null) { //daca categoria nu exista o inseram
                categoryId = categoryRepository.save(new Category(categoryName));
            }
            products.get(i).setCategory(categoryId);
        }
        return products;
    }

    public static void main(String[] args) throws SQLException, IOException {
        ProductService productService = new ProductService();
        ProductRepository productRepository = ProductRepository.getInstance();
        Product product = productRepository.find(17L);
        productRepository.updateCategory(product.getId(), 2L);
    }
}

