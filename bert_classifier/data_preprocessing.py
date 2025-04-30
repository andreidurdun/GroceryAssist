import re
import pandas as pd

def sterge_al_doilea_cuvant_mare(text):

    text =  re.sub(r'\b\d+\s?(g|ml|l|kg|buc|bucăți)\b', '', text, flags=re.IGNORECASE)
    text = text.split(",")[0] 
    text = text.strip(',')
     # Găsește toate cuvintele care încep cu literă mare
    cuvinte_majuscule = [m.start() for m in re.finditer(r'\b[A-Z]+[a-z]*\b', text)]
    
    if len(cuvinte_majuscule) < 2:
        return text  # nu există al doilea cuvânt mare, nu tăiem
    
    # Oprire după al doilea cuvânt cu majusculă
    stop_index = cuvinte_majuscule[1]
    return text[:stop_index].strip()

# Citește fișierul CSV
input_path = "./produse_supermarket.csv"
output_path = "./produse_supermarket_modificat.csv"

# Încarcă datele într-un DataFrame
df = pd.read_csv(input_path)
df = df.drop_duplicates(subset=['name'])  # Elimină duplicatele pe baza coloanei 'name'

inlocuire_categorii = {
    "alimente-sarate" : "dulciuri",
    "aperitive-si-digestive" : "bauturi alcoolice",
    "bauturi-spirtoase" : "bauturi alcoolice",
    "bere-si-cidru" : "bauturi alcoolice",
    "paine" : "panificatie",
    "patiserie" : "panificatie",
    "vin-si-sampanie" : "bauturi alcoolice",
}

df['category'] = df['category'].replace(inlocuire_categorii)

# Aplică funcția pe coloana "name"
df['name'] = df['name'].apply(sterge_al_doilea_cuvant_mare)
df = df.map(lambda x: x.lower() if isinstance(x, str) else x)
df['name'] = df['name'].str.replace(r'[^a-zA-Z ]', '', regex=True)
df['name'] = df['name'].map(lambda x: x.strip() if isinstance(x, str) else x)
df = df.drop_duplicates(subset=['name'])

# Salvează rezultatul într-un nou fișier CSV
df.to_csv(output_path, index=False)




