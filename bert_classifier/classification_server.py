from fastapi import FastAPI, Request
from pydantic import BaseModel
from typing import List

from transformers import AutoTokenizer, AutoModelForSequenceClassification
import torch
import numpy as np


model_path = "./bert_model3"
tokenizer = AutoTokenizer.from_pretrained(model_path)
model = AutoModelForSequenceClassification.from_pretrained(model_path)

id2label = model.config.id2label # Aici luăm id2label din modelul salvat (nu mai e nevoie să-l citim din fișier JSON, pentru că modelul îl conține deja)

# FastAPI app
app = FastAPI()

# Request model: aici definim cum arată request-ul pe care îl primim de la client (un JSON "text" : string)
class ProductRequest(BaseModel):
    text: str

class BulkProductRequest(BaseModel):
    texts: List[str]

def classify_product(text):
    inputs = tokenizer(text, return_tensors="pt", truncation=True, padding="max_length", max_length=30)
    # return_tensors="pt" returnează tensori PyTorch, deci nu mai e nevoie de torch.tensor(inputs)

    with torch.no_grad(): # Intrăm într-un context fără calculul gradientului (nu facem backpropagation aici, doar inferență ⇒ e mai rapid și folosește mai puțină memorie).
        outputs = model(**inputs) # Aici se face inferența, modelul returnează logits pentru fiecare clasă
        logits = outputs.logits # logits sunt scorurile brute pentru fiecare clasă (nu sunt normalizate, deci nu sunt probabilități)
        predicted_class_id = int(torch.argmax(logits, dim=-1)) # Aici selectăm clasa cu cel mai mare scor (predicția finală).
        predicted_label = id2label[predicted_class_id] # Aici transformăm id-ul clasei în eticheta corespunzătoare folosind id2label

    return predicted_label


@app.post("/predict")
def predict(req: ProductRequest):
    predicted_label = classify_product(req.text)
    return {"label": predicted_label}

@app.post("/predict_bulk")
def predict_bulk(req: BulkProductRequest):
    predictions = [classify_product(text) for text in req.texts]
    return {"labels": predictions}

# uvicorn clasibicator_bert_predictii:app --reload   (reload = pentru a reîncărca aplicația la fiecare modificare a codului)