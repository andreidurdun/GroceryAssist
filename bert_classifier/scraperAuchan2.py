from selenium import webdriver
from selenium.webdriver.chrome.service import Service
import time
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.options import Options

import pandas as pd

# Derulează pagina pentru a încărca toate produsele
def load_products():
    loaded_products = 0
    while True:
        # Localizează produsele vizibile în acest moment
        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, ".vtex-product-summary-2-x-container"))
        )
        product_elements = driver.find_elements(By.CSS_SELECTOR, ".vtex-product-summary-2-x-container")
        
        # Verifică dacă numărul de produse a crescut
        if len(product_elements) > loaded_products:
            loaded_products = len(product_elements)
            print(f"Produse încărcate: {loaded_products}")
            
            # Derulează puțin mai jos pentru a încărca mai multe produse
            driver.execute_script("window.scrollBy(0, 1000);")
            time.sleep(2)  # Așteaptă ca produsele să se încarce
        else:
            # Dacă numărul de produse nu mai crește, ieși din buclă
            break
    return product_elements

# Apasă pe butonul "Arată mai mult" și așteaptă încărcarea produselor noi
def click_show_more():
    try:
        # Așteaptă ca butonul să fie vizibil și interactiv
        show_more_button = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.XPATH, "//button[contains(@class, 'vtex-button') and contains(., 'Arată mai mult')]"))
        )
        show_more_button.click()
        
        # Așteaptă ca numărul de produse să crească
        previous_count = len(driver.find_elements(By.CSS_SELECTOR, ".vtex-product-summary-2-x-container"))
        WebDriverWait(driver, 10).until(
            lambda d: len(d.find_elements(By.CSS_SELECTOR, ".vtex-product-summary-2-x-container")) > previous_count
        )

    except Exception as e:
        print(f"Eroare la apăsarea butonului sau încărcarea produselor: {e}")


# Configurează opțiunile Chrome
chrome_options = Options()
chrome_options.add_argument("--disable-notifications")  # Dezactivează notificările

service = Service(executable_path="C:\chromedriver-win64\chromedriver.exe")
driver = webdriver.Chrome(service=service, options=chrome_options)
# driver.get("https://www.auchan.ro/")

def scrapeCategory(category, subcategory):
    print(f"\n\nCategory: {category}, Subcategory: {subcategory}")
    data = {"name": [], "price": [], "category": []}
    
    # Navighează la categoria specificată
    category_url = f"https://www.auchan.ro/{category}/{subcategory}/c"
    driver.get(category_url)
    
    # Închide pop-up-ul pentru cookie-uri dacă este prezent
    try:
        cookie_popup = WebDriverWait(driver, 1).until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, "button.onetrust-close-btn-handler"))
        )
        cookie_popup.click()
        print("Pop-up-ul pentru cookie-uri a fost închis.")
    except Exception as e:
        pass

    for _ in range(5):
        try:
            products = load_products()
            for product in products:
                # Extrage informațiile despre produs (exemplu: nume, preț, etc.)
                product_name = product.find_element(By.CSS_SELECTOR, ".vtex-product-summary-2-x-productBrand").text
                product_price = product.find_element(By.CSS_SELECTOR, ".vtex-product-price-1-x-sellingPrice").text
                data["name"].append(product_name)
                data["price"].append(product_price)
                data["category"].append(subcategory)
            click_show_more()
        except Exception as e:
            print(f"\nEroare la încărcarea produselor: {e}")
            break
    
    return data


d = {
    "lactate-carne-mezeluri---peste":["lactate", "carne", "mezeluri", "pescarie"],
    "bauturi-si-tutun":["apa", "bauturi-racoritoare", "vin-si-sampanie", "bauturi-spirtoase", "aperitive-si-digestive", "bere-si-cidru"],
    "bacanie":["alimente-de-baza", "ceai-si-cafea", "dulciuri", "conserve", "alimente-sarate", "condimente-si-sosuri", "cereale"],
    "fructe-si-legume":["fructe", "legume", "salate-si-verdeturi"],
    "brutarie-cofetarie-gastro":["paine", "gastro", "cofetarie", "patiserie", "semipreparate"]
    }
data = {
        "name": [], 
        "price": [], 
        "category": []
        }
df = pd.DataFrame(data)
df.to_csv('produse_supermarket.csv', mode='w', index=False, header=True)#scrie numele coloanelor doar la prima interatie
i=2
for category, subcategory in d.items():
    for sub in subcategory:
        try:
            newData = scrapeCategory(category, sub)
        except Exception as e:
            print(f"\nEroare la scraping pentru categoria {category}, subcategoria {sub}: {e}")
            continue

        data["name"] += newData["name"]
        data["price"] += (newData["price"])
        data["category"] += (newData["category"])

        df = pd.DataFrame(data)
        df.to_csv('produse_supermarket.csv', mode='a', index=False, header=False)#scrie numele coloanelor doar la prima interatie
        #mode='a' = append, index=False = nu scrie indexul, header=not bool(i) = scrie header doar la prima iteratie

    #     i-=1
    #     if i == 0:
    #         break
    # if i == 0:
    #     break

driver.quit()