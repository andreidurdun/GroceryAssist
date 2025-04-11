package com.grocery.assist.service;

import com.grocery.assist.model.ShoppingList;
import com.grocery.assist.repository.ShoppingListRepository;


import java.sql.SQLException;
import java.util.*;

public class ShoppingListService {
    private final ShoppingListRepository repository;

    public ShoppingListService(ShoppingListRepository repository) {
        this.repository = repository;
    }

    //Afisarea istoricului de liste dupa data
    public List<ShoppingList> getShoppingHistory(Date fromDate, Date toDate) throws SQLException {
        List<ShoppingList> shoppingLists;
        shoppingLists = repository.findByDate(fromDate, toDate);
        Collections.sort(shoppingLists);

        return shoppingLists;
    }

    //creaza o lista de cumparaturi
    public void createShoppingList(ShoppingList shoppingList) throws SQLException {
        repository.save(shoppingList);
    }



}
