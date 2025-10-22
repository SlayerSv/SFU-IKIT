-- 1. Справочник стран
CREATE TABLE countries (
    country_id INT PRIMARY KEY,
    country_name NVARCHAR(50) NOT NULL UNIQUE
);

-- 2. Справочник языков программирования
CREATE TABLE languages (
    language_id INT PRIMARY KEY,
    language_name NVARCHAR(50) NOT NULL UNIQUE
);

-- 3. Справочник категорий задач
CREATE TABLE categories (
    category_id INT PRIMARY KEY,
    category_name NVARCHAR(50) NOT NULL UNIQUE
);

-- 4. Справочник статусов посылок
CREATE TABLE statuses (
    status_id INT PRIMARY KEY,
    status_name NVARCHAR(30) NOT NULL UNIQUE
);

-- 5. Таблица пользователей
CREATE TABLE users (
    user_id INT PRIMARY KEY,
    user_name NVARCHAR(100) NOT NULL UNIQUE,
    country_id INT FOREIGN KEY REFERENCES countries(country_id)
);

-- 6. Справочник задач
CREATE TABLE problems (
    problem_id INT PRIMARY KEY,
    problem_name NVARCHAR(100),
    difficulty INT CHECK (difficulty <= 100 and difficulty > 0)
);

-- 7. Связующая таблица для задач и категорий (многие-ко-многим)
CREATE TABLE problems_categories (
    problem_id INT FOREIGN KEY REFERENCES problems(problem_id),
    category_id INT FOREIGN KEY REFERENCES categories(category_id),
    PRIMARY KEY (problem_id, category_id)
);

-- 8. Главная таблица с посылками
CREATE TABLE submissions (
    submission_id BIGINT PRIMARY KEY,
    language_id INT FOREIGN KEY REFERENCES languages(language_id),
    problem_id INT FOREIGN KEY REFERENCES problems(problem_id),
    user_id INT FOREIGN KEY REFERENCES users(user_id),
    status_id INT FOREIGN KEY REFERENCES statuses(status_id),
    submission_date DATETIME NOT NULL
);