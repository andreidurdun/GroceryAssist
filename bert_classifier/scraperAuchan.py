from selenium import webdriver
from selenium.webdriver.chrome.service import Service
import time
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.options import Options

# Configurează opțiunile Chrome
chrome_options = Options()
chrome_options.add_argument("--disable-notifications")  # Dezactivează notificările

service = Service(executable_path="C:\chromedriver-win64\chromedriver.exe")
driver = webdriver.Chrome(service=service, options=chrome_options)
driver.get("https://www.auchan.ro/")

try:
    close_popup = driver.find_element(By.CSS_SELECTOR, ".popup-close-button")
    close_popup.click()
except:
    pass  # Dacă nu există pop-up, continuă

driver.implicitly_wait(10)

# Derulează pagina pentru a încărca toate produsele
def load_products():
    loaded_products = 0
    while True:
        # Localizează produsele vizibile în acest moment
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

# Apasă pe butonul "Arată mai mult" și așteaptă încărcarea produselor noi
def click_show_more():
    try:
        # Așteaptă ca butonul să fie vizibil și interactiv
        show_more_button = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.XPATH, "//button[contains(., 'Arată mai mult')]"))
        )
        show_more_button.click()
        print("Butonul 'Arată mai mult' a fost apăsat.")
        
        # Așteaptă ca numărul de produse să crească
        previous_count = len(driver.find_elements(By.CSS_SELECTOR, ".vtex-product-summary-2-x-container"))
        WebDriverWait(driver, 10).until(
            lambda d: len(d.find_elements(By.CSS_SELECTOR, ".vtex-product-summary-2-x-container")) > previous_count
        )
        print("Produsele noi au fost încărcate.")
    except Exception as e:
        print(f"Eroare la apăsarea butonului sau încărcarea produselor: {e}")


try:
    # Localizează și apasă pe butonul de acceptare sau închidere a cookie-urilor
    cookie_popup = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, "button.onetrust-close-btn-handler"))
    )
    cookie_popup.click()
    print("Pop-up-ul pentru cookie-uri a fost închis.")
except Exception as e:
    print(f"Nu a fost găsit pop-up-ul pentru cookie-uri: {e}")


# Apasă pe butonul de meniu pentru a deschide categoriile
try:
    menu_button = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, "button[data-id='mega-menu-trigger-button']"))
    )
    menu_button.click()
    print("Butonul de meniu a fost apăsat.")
except Exception as e:
    print(f"Eroare la apăsarea butonului de meniu: {e}")


# Apasă pe butoanele care extind subcategoriile
try:
    expand_buttons = WebDriverWait(driver, 10).until(
        EC.presence_of_all_elements_located((By.XPATH, "//li[@class='auchan-mega-menu-0-x-menuItem']//a[@class='auchan-mega-menu-0-x-styledLink no-underline c-on-base w-100 pa0 t-body pointer']"))
    )
    print(f"Am găsit {len(expand_buttons)} butoane de extindere.")
    i = 0
    for button in expand_buttons:
        if i == 0:
            i += 1
            continue

        try:
            driver.execute_script("arguments[0].scrollIntoView(true);", button)  # Derulează până la buton
            button.click()
            print(f"Am apăsat pe butonul: {button.text}")
            time.sleep(2)  # Așteaptă puțin pentru a permite încărcarea subcategoriilor
        except Exception as e:
            print(f"Eroare la apăsarea butonului: {e}")

        try:
            subcategories = WebDriverWait(driver, 10).until(
                EC.presence_of_all_elements_located((By.XPATH, "//a[@class='vtex-list-context-0-x-infoCardCallActionContainer vtex-list-context-0-x-infoCardCallActionContainer--categoryList mt6 mb6']"))
            )
            print("Elementele au fost găsite.")
            for subcategory in subcategories:
                driver.execute_script("arguments[0].click();", subcategory)  # Forțează click pe subcategorie

                for _ in range(1):
                    try:
                        load_products()
                        click_show_more()
                    except Exception as e:
                        print(f"Eroare la încărcarea produselor: {e}")

                    
                    try:
                        lactate_link = WebDriverWait(driver, 10).until(
                            EC.presence_of_element_located((By.XPATH, "//a[contains(@class, 'vtex-breadcrumb-1-x-link')]"))
                        )
                        driver.execute_script("arguments[0].click();", lactate_link)  # Forțează click-ul folosind JavaScript
                        print("Link-ul 'Lactate, Carne, Mezeluri & Peste' a fost apăsat.")
                    except Exception as e:
                        print(f"Eroare la apăsarea link-ului: {e}")

        except Exception as e:
            print(f"Eroare la subcategorie: {e}")

        break
        
except Exception as e:
    print(f"Eroare la localizarea butoanelor de extindere: {e}")



time.sleep(3)



# Închide browserul
driver.quit()

