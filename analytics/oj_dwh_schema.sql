--- 1. Измерение: Время (самый универсальный справочник)
CREATE TABLE dim_date (
    date_key INT PRIMARY KEY, -- Ключ в формате YYYYMMDD, например: 20230925
    full_date DATE NOT NULL,
    day_name NVARCHAR(20),
    day_of_week INT,
    day_of_month INT,
    month_name NVARCHAR(20),
    month_number INT,
    quarter INT,
    year INT
);

--- 2. Измерение: Пользователи (денормализовано со странами)
CREATE TABLE dim_user (
    user_key INT PRIMARY KEY IDENTITY(1,1), -- Суррогатный ключ хранилища
    user_id INT NOT NULL,              -- Ключ из исходной OLTP-базы для связи при наполнении
    user_name NVARCHAR(100),
    country_name NVARCHAR(50)
);

--- 3. Измерение: Языки программирования
CREATE TABLE dim_language (
    language_key INT PRIMARY KEY IDENTITY(1,1),
    language_id INT NOT NULL,
    language_name NVARCHAR(50)
);

--- 4. Измерение: Статусы посылок
CREATE TABLE dim_status (
    status_key INT PRIMARY KEY IDENTITY(1,1),
    status_id INT NOT NULL,
    status_name NVARCHAR(30)
);

--- 5. Измерение: Категории Задач
CREATE TABLE dim_category (
    category_key INT PRIMARY KEY IDENTITY(1,1),
    category_id INT NOT NULL,
    category_name NVARCHAR(50)
);

--- 6. Измерение: Задачи (без категорий, они вынесены в мост)
CREATE TABLE dim_problem (
    problem_key INT PRIMARY KEY IDENTITY(1,1),
    problem_id INT NOT NULL,
    problem_name NVARCHAR(100),
    difficulty INT
);

--- 7. Таблица ФАКТОВ: Посылки (центр нашей звезды)
CREATE TABLE fact_submissions (
    -- Ключи к измерениям
    language_key INT FOREIGN KEY REFERENCES dim_language(language_key),
    problem_key INT FOREIGN KEY REFERENCES dim_problem(problem_key),
    user_key INT FOREIGN KEY REFERENCES dim_user(user_key),
    status_key INT FOREIGN KEY REFERENCES dim_status(status_key),
    date_key INT FOREIGN KEY REFERENCES dim_date(date_key),
    -- Мера (то, что мы считаем)
    submission_count INT NOT NULL DEFAULT 1 -- Каждая строка = 1 посылка
);

--- 8. МОСТ "Многие-ко-многим": связь Задач и Категорий
-- Эта таблица позволяет нам анализировать факты из fact_submissions
-- в разрезе категорий, с которыми связаны задачи.
CREATE TABLE fact_problem_category_bridge (
    problem_key INT FOREIGN KEY REFERENCES dim_problem(problem_key),
    category_key INT FOREIGN KEY REFERENCES dim_category(category_key),
    -- Составной первичный ключ, чтобы не было дублей пар "задача-категория"
    CONSTRAINT pk_fact_problem_category_bridge PRIMARY KEY (problem_key, category_key)
);