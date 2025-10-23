-- Наполнение dim_user
INSERT INTO dim_user (user_id, user_name, country_name)
SELECT
    u.user_id,
    u.user_name,
    c.country_name
FROM users u
JOIN countries c ON u.country_id = c.country_id;

-- Наполнение dim_category
INSERT INTO dim_category (category_id, category_name)
SELECT category_id, category_name FROM categories;

-- Наполнение dim_problem
INSERT INTO dim_problem (problem_id, problem_name, difficulty)
SELECT problem_id, problem_name, difficulty FROM problems;

INSERT INTO dim_language (language_id, language_name)
SELECT l.language_id, l.language_name FROM languages l;

INSERT INTO dim_status (status_id, status_name)
SELECT s.status_id, s.status_name FROM statuses s;

-- Dim_date
-- Устанавливаем понедельник как первый день недели
SET DATEFIRST 1;

-- 1. Задаем период, на который хотим сгенерировать календарь
DECLARE @StartDate DATE = '2023-01-01';
DECLARE @EndDate DATE = '2025-12-31';

-- 3. Начинаем цикл от начальной до конечной даты
DECLARE @CurrentDate DATE = @StartDate;

WHILE @CurrentDate <= @EndDate
BEGIN
    -- Вставляем в таблицу рассчитанные атрибуты для текущей даты
    INSERT INTO dim_date (
        date_key,
        full_date,
        day_name,
        day_of_week,
        day_of_month,
        month_name,
        month_number,
        quarter,
        year
    )
    VALUES (
        CONVERT(INT, CONVERT(VARCHAR(8), @CurrentDate, 112)), -- Ключ в формате YYYYMMDD (например, 20230926)
        @CurrentDate,                                        -- Полная дата (2023-09-26)
        DATENAME(weekday, @CurrentDate),                      -- Название дня недели (Вторник)
        DATEPART(weekday, @CurrentDate),                      -- Номер дня недели (2)
        DATEPART(day, @CurrentDate),                          -- День месяца (26)
        DATENAME(month, @CurrentDate),                        -- Название месяца (Сентябрь)
        DATEPART(month, @CurrentDate),                        -- Номер месяца (9)
        DATEPART(quarter, @CurrentDate),                      -- Квартал (3)
        DATEPART(year, @CurrentDate)                          -- Год (2023)
    );

    -- Переходим к следующему дню
    SET @CurrentDate = DATEADD(day, 1, @CurrentDate);
END;

-- Наполнение МОСТА
INSERT INTO fact_problem_category_bridge (problem_key, category_key)
SELECT
    dp.problem_key,
    dc.category_key
FROM problems_categories AS pc -- Исходная таблица-связка
JOIN dim_problem AS dp ON pc.problem_id = dp.problem_id
JOIN dim_category AS dc ON pc.category_id = dc.category_id;

-- Наполнение главной таблицы фактов fact_submissions
INSERT INTO fact_submissions (date_key, user_key, problem_key, language_key, status_key, submission_count)
SELECT
    CONVERT(INT, CONVERT(VARCHAR(8), s.submission_date, 112)), -- Превращаем дату в ключ YYYYMMDD
    du.user_key,
    dp.problem_key,
    dl.language_key,
    dst.status_key,
    1 -- Каждая строка в submissions - это один факт, поэтому мера = 1
FROM submissions AS s
JOIN dim_user AS du ON s.user_id = du.user_id
JOIN dim_problem AS dp ON s.problem_id = dp.problem_id
JOIN dim_language AS dl ON s.language_id = dl.language_id
JOIN dim_status AS dst ON s.status_id = dst.status_id;