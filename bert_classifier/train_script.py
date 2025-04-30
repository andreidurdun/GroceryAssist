import pandas as pd
from sklearn.model_selection import train_test_split
from datasets import Dataset, ClassLabel
from transformers import AutoTokenizer, AutoModelForSequenceClassification, TrainingArguments, Trainer
import numpy as np
import evaluate
import json

data = pd.read_csv("produse_supermarket_prelucrat.csv")

# Convert price from string ("5,79 lei") to float (5.79)
data['price'] = data['price'].str.replace(" lei", "", regex=False)
data['price'] = data['price'].str.replace(",", ".", regex=False).astype(float)

# Verificăm dacă conversia a mers
# print(data['price'].head())


# Împărțim datele în train/test pentru ambele taskuri (folosim aceeași împărțire)
# test_size=0.2 înseamnă că 20% din date vor fi folosite pentru testare
# stratify=data['category'] asigură că distribuția categoriilor este similară în train și test
# random_state=42 asigura ca impartirea va fi aceeasi de fiecare dată când rulăm codul
train_data, test_data = train_test_split(data, test_size=0.2, random_state=42, stratify=data['category'])


# Verificăm distribuția categoriilor în train și test
train_cat_dist = train_data['category'].value_counts(normalize=True) # normalize=True returnează proporția fiecărei categorii
test_cat_dist = test_data['category'].value_counts(normalize=True)

model_name = "distilbert-base-multilingual-cased"
tokenizer = AutoTokenizer.from_pretrained(model_name)

# Exemplu de tokenizare
encoded = tokenizer("Lapte de consum integral Auchan, 3.5% grasime, 1L",
                    truncation=True,
                    padding='max_length',
                    max_length=128,
                    return_tensors="pt")

# print(encoded)

# === 3. Pregătim label encoder ===
labels = sorted(data['category'].unique())
label2id = {label: idx for idx, label in enumerate(labels)}
id2label = {idx: label for label, idx in label2id.items()}


# Salvează id2label și label2id într-un fișier JSON ca sa le utilizeam pentru predictii
with open("label_mappings.json", "w") as f:
    json.dump({
        "id2label": id2label,
        "label2id": label2id
    }, f)



train_data['label'] = train_data['category'].map(label2id)
test_data['label'] = test_data['category'].map(label2id)

# === 4. HuggingFace Dataset ===
train_dataset = Dataset.from_pandas(train_data[['name', 'label']])
test_dataset = Dataset.from_pandas(test_data[['name', 'label']])

# === 5. Tokenizer ===
model_name = "distilbert-base-multilingual-cased"
tokenizer = AutoTokenizer.from_pretrained(model_name)

def tokenize_function(example):
    return tokenizer(example["name"], truncation=True, padding="max_length", max_length=128)

train_dataset = train_dataset.map(tokenize_function)
test_dataset = test_dataset.map(tokenize_function)

# === 6. Model BERT ===
model = AutoModelForSequenceClassification.from_pretrained(
    model_name,
    num_labels=len(labels),
    id2label=id2label,
    label2id=label2id
)

# === 7. Metrică ===
accuracy = evaluate.load("accuracy")

def compute_metrics(eval_pred):
    logits, labels = eval_pred
    predictions = np.argmax(logits, axis=-1) #Selectează clasa cu cel mai mare scor pentru fiecare exemplu (predicția finală).
    return accuracy.compute(predictions=predictions, references=labels) #Calculează acuratețea comparând predicțiile cu etichetele reale.

# === 8. Antrenare ===
training_args = TrainingArguments(
    output_dir="./bert_clasificator",
    eval_strategy="epoch",
    save_strategy="epoch",
    per_device_train_batch_size=4,  # Reduce dimensiunea batch-ului
    per_device_eval_batch_size=4,
    num_train_epochs=1,  # Reduce numărul de epoci
    weight_decay=0.01,
    logging_dir="./logs",
    logging_steps=500,  # Reduce frecvența jurnalizării
    load_best_model_at_end=True,
    metric_for_best_model="accuracy",
    fp16=False  
)

trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=train_dataset,
    eval_dataset=test_dataset,
    tokenizer=tokenizer,
    compute_metrics=compute_metrics
)

trainer.train()
