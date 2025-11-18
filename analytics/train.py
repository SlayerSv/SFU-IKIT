import pandas as pd
import yaml
import json
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from xgboost import XGBClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import accuracy_score
import pickle
from sklearn.preprocessing import LabelEncoder

# 1. Загрузка параметров
params = yaml.safe_load(open("params.yaml"))["train"]
model_name = params["model"]

# 2. Загрузка данных
df = pd.read_csv('anecdotes.csv')

# 3. Кодирование меток
le = LabelEncoder()
df['category_encoded'] = le.fit_transform(df['category'])

# 4. Разделение данных
X = df['text']
y = df['category_encoded']
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 5. Векторизация
vectorizer = TfidfVectorizer(max_features=params["max_features"])
X_train_vec = vectorizer.fit_transform(X_train)
X_test_vec = vectorizer.transform(X_test)

# 6. Обучение модели
print(f"Обучаем модель: {model_name}")
if model_name == 'xgb':
    model = XGBClassifier(use_label_encoder=False, eval_metric='mlogloss', n_estimators=params["n_estimators"])
elif model_name == 'rf':
    model = RandomForestClassifier(n_estimators=params["n_estimators"])
elif model_name == 'nb':
    model = MultinomialNB()

model.fit(X_train_vec, y_train)

# 7. Оценка качества
y_pred = model.predict(X_test_vec)
accuracy = accuracy_score(y_test, y_pred)
print(f"Точность: {accuracy}")

# --- ИЗМЕНЕНИЯ ЗДЕСЬ ---
# 8. Сохранение результатов в JSON, а не в TXT
metrics = {'accuracy': accuracy}
with open("results.json", "w") as f:
    json.dump(metrics, f)

# Сохраняем модель как и раньше
pickle.dump(model, open("model.pkl", "wb"))