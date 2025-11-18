import pandas as pd
import yaml
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from xgboost import XGBClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import accuracy_score
import pickle
from sklearn.preprocessing import LabelEncoder

# 1. Загрузка параметров эксперимента
params = yaml.safe_load(open("params.yaml"))["train"]
model_name = params["model"]

# 2. Загрузка данных
df = pd.read_csv('anecdotes.csv')

# Кодируем текстовые метки в числа ---
print("Кодируем текстовые метки в числа...")
le = LabelEncoder()
df['category_encoded'] = le.fit_transform(df['category'])
print("Кодирование завершено.")

# 3. Разделение данных
X = df['text']
y = df['category_encoded']
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 4. Векторизация
vectorizer = TfidfVectorizer(max_features=params["max_features"])
X_train_vec = vectorizer.fit_transform(X_train)
X_test_vec = vectorizer.transform(X_test)

# 5. Выбор и обучение модели
print(f"Обучаем модель: {model_name}")
# XGBoost теперь будет счастлив, потому что y_train - это числа
if model_name == 'xgb':
    # use_label_encoder=False здесь важно, т.к. мы кодируем сами!
    model = XGBClassifier(use_label_encoder=False, eval_metric='mlogloss', n_estimators=params["n_estimators"])
elif model_name == 'rf':
    model = RandomForestClassifier(n_estimators=params["n_estimators"])
elif model_name == 'nb':
    model = MultinomialNB()

model.fit(X_train_vec, y_train)

# 6. Оценка качества
y_pred = model.predict(X_test_vec)
accuracy = accuracy_score(y_test, y_pred)
print(f"Точность: {accuracy}")

# 7. Сохранение результатов и модели
with open("metrics.txt", "w") as f:
    f.write(f"Accuracy: {accuracy}")

pickle.dump(model, open("model.pkl", "wb"))