import pandas as pd
from faker import Faker
from unidecode import unidecode
import random
import datetime

fake = Faker('ru_RU')
NUM_OF_USERS = 100
NUM_OF_PROBLEMS = 1000
NUM_OF_SUBMISSIONS = 10000

countries = (
    [1, 2, 3, 4, 5, 6],
    ['Russia', 'China', 'USA', 'India', 'Japan', 'South Korea'],
    [0.15, 0.3, 0.25, 0.2, 0.04, 0.06],
    ['ru_RU', 'zh_CN', 'en_US', 'en_IN', 'ja_JP', 'ko_KR']
)
statuses = (
    [1, 2, 3, 4, 5, 6],
    ['Accepted', 'Wrong Answer', 'Time Limit Exceeded', 'Memory Limit Exceeded', 'Compilation Error', 'Runtime Error'],
    [0.4, 0.3, 0.1, 0.05, 0.05, 0.1]
)
categories = (
    [1, 2, 3, 4, 5, 6, 7, 8],
    ['Dynamic Programming', 'Graph Theory', 'Greedy', 'Number Theory', 'Combinatorics', 'Sorting', 'Math', 'Strings'],
    [0.2, 0.3, 0.1, 0.025, 0.03, 0.1, 0.045, 0.2]
)
languages = (
    [1, 2, 3, 4, 5, 6],
    ['C++', 'Python', 'Java', 'Go', 'Rust', 'Node.js'],
    [0.6, 0.15, 0.1, 0.07, 0.03, 0.05]
)
users = []
for x in range(1, NUM_OF_USERS+1):
    country_id = random.choices(countries[0], countries[2], k=1)[0]
    f = Faker(countries[3][country_id-1])
    name = unidecode(f.name())
    users.append((x, name, country_id))

problems: list[tuple[int, str, int]] = []
problems_categories: list[tuple[int, int]] = []
for i in range(1, NUM_OF_PROBLEMS+1):
    category_count = random.randint(1, 4)
    cats = random.choices(categories[0], categories[2], k=random.randint(1, min(len(categories[0]), 4)))
    difficulty = random.randint(1, 100)
    name = fake.bs()
    problems.append((i, name, difficulty))
    for j in set(cats):  
        problems_categories.append((i, j))

submissions: list[tuple[int, int, int, int, int, datetime.datetime]] = []
for i in range(1, NUM_OF_SUBMISSIONS+1):
    language_id = random.choices(languages[0], weights=languages[2], k=1)[0]
    problem_id = random.randint(1, len(problems))
    user_id = random.randint(1, len(users))
    status_id = random.choices(statuses[0], weights=statuses[2], k=1)[0]
    date = fake.date_time_between(start_date='-2y', end_date='now')
    submissions.append((i, language_id, problem_id, user_id, status_id, date))

pd.DataFrame({'language_id': languages[0], 'language_name': languages[1]}).to_csv('languages.csv', index=False)
pd.DataFrame({'status_id': statuses[0], 'status_name': statuses[1]}).to_csv('statuses.csv', index=False)
pd.DataFrame({'country_id': countries[0], 'country_name': countries[1]}).to_csv('countries.csv', index=False)
pd.DataFrame({'category_id': categories[0], 'category_name': categories[1]}).to_csv('categories.csv', index=False)
pd.DataFrame(users, columns=['user_id', 'user_name', 'country_id']).to_csv('users.csv', index=False)
pd.DataFrame(problems, columns=['problem_id', 'problem_name', 'difficulty']).to_csv('problems.csv', index=False)
pd.DataFrame(submissions, columns=['submission_id', 'language_id', 'problem_id', 'user_id', 'status_id', 'submission_date']).to_csv('submissions.csv', index=False)
pd.DataFrame(problems_categories, columns=['problem_id', 'category_id']).to_csv('problems_categories.csv', index=False)
