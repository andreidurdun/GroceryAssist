# 🛒 GroceryAssist

**GroceryAssist** is a Java Maven desktop application that helps users create and manage their shopping lists efficiently.

## Features

- Add ingredients manually or select from previously saved recipes.
- Automatically adds ingredients and quantities when a recipe is selected.
- Aggregates ingredient quantities to avoid duplicates.
- Categorizes products using a custom-trained machine learning model.
- The model was trained by me on product data I collected via web scraping from a supermarket website, using **Selenium**.
- Communicates with a **Python** server via HTTP requests to perform ingredient classification.
- Built following solid **Object-Oriented Programming (OOP)** principles for clean, modular, and maintainable code.
- Includes a minimalist **Java Swing** graphical user interface.

## Machine Learning

The machine learning model used for product categorization was **trained on a dataset I personally collected** via web scraping. The dataset includes real-world product information extracted from a supermarket’s website using Selenium. This model allows the application to classify each ingredient into intuitive shopping categories (e.g., *dairy*, *produce*, *meat*), helping users better organize their shopping lists.

## Technologies Used

- **Java** (Maven) — Core application logic and GUI
- **Java Swing** — GUI components
- **Python** — Server hosting and training the ML model
- **Selenium** — Web scraping for training data
- **HTTP** — Communication between Java client and Python server
- **PostgreSQL** — Database for storing shopping lists history, recipes and ingredients

