import sqlite3
import random


def main():
    number_of_versions = [3, 5, 7, 9, 11]
    iterations = 50000

    for versions in number_of_versions:
        results, correct_results = get_experiment_data(versions, iterations)
        curr_index = 0
        correct_results_count = 0
        cannot_find = 0
        incorrect_entries = 0
        t = (versions - 1) // 2

        for correct_result in correct_results:
            versions_results = []
            incorrect_versions = 0

            for _ in range(0, versions):
                version_result = results[curr_index][0]
                versions_results.append(version_result)
                curr_index += 1
                if version_result != correct_result[0]:
                    incorrect_versions += 1

            if incorrect_versions > t:
                incorrect_entries += 1
                continue

            try:
                result = dpvote(versions_results, t)
                if result == correct_result[0]:
                    correct_results_count += 1
            except ValueError:
                cannot_find += 1

        print("Versions:", versions,
              "Iterations:", iterations - incorrect_entries,
              "Correct:", correct_results_count,
              "Incorrect:", iterations - correct_results_count
              - cannot_find - incorrect_entries,
              "Cannot find:", cannot_find,
              "Incorrect entries:", incorrect_entries)


def vote(results, t=-1):
    n = len(results)
    if t == -1:
        t = (n-1) // 2
    if t > (n - 1) // 2:
        raise ValueError("Number of versions with potentially wrong results "
                         "cannot exceed (n-1)/2")
    mrc = t * 2 + 1  # начальное количество сравниваемых версий.
    while mrc <= n:
        outputs = []  # результаты версий, которые пошли на выход.
        for i in range(0, mrc-1, 2):
            outputs.append(results[i])
        outputs.append(results[mrc-1])

        # заполняем массив компараторов сравнивая соседние элементы
        # если результат одинаков, то компаратор = 0, иначе - 1.
        comparators = []
        for i in range(0, mrc-2):
            if results[i] != results[i+1]:
                comparators.append(1)
            else:
                comparators.append(0)
        length = len(comparators)

        # считаем количество групп с элементами больше 1.
        groups_count = 0
        for i in range(0, length):
            if comparators[i] == 0:
                groups_count += 1
                while i < length and comparators[i] == 0:
                    i += 1

        # проходимся по компараторам и проверяем максимальное количество
        # одинаковых элементов, число групп с таким максимальным количество
        # элементов и индекс выходного элемента, который содержится
        # в максимальной группе.
        curr_same = 1
        max_same = 1
        output_index = 0  # индекс версии на выход с корректным результатом.
        max_same_count = 1
        for i in range(0, length + 1):
            if i == length or comparators[i] == 1:
                if curr_same == max_same:
                    max_same_count += 1
                if curr_same > max_same:
                    output_index = i // 2
                    max_same = curr_same
                    max_same_count = 1
                curr_same = 1
            else:
                curr_same += 1

        # ищем верный результат.
        correct_result = 0.0
        found = False

        # если групп 0, то возвращаем результат несравниваемого элемента,
        # т.к. такое возможно, только если количество неверных элементов
        # среди сравниваемых не меньше t.
        if groups_count == 0:
            correct_result = outputs[-1]
            found = True
        # если группа 1, и количество элементов в ней равно 2,
        # то в зависимости от положения группы возвращаем либо,
        # входной элемент, либо несравниваемый элемент.
        elif groups_count == 1:
            if max_same == 2:
                position = comparators.index(0)
                if position == 1 or position == len(comparators) - 2:
                    correct_result = outputs[-1]
                else:
                    correct_result = outputs[output_index]
                found = True
            else:
                # если количество элементов в этой группе больше 2,
                # то возвращаем результат выходного элемента в этой группе,
                # т.к. можно доказать, что эта группа не может содержать
                # неверные результаты, иначе количество неверных эелементов
                # превысит t, т.к. помимо этой группы количество дополнительных
                # неверных элементов будет не меньше оставшихся разделить на 2,
                # что в сумме всегда даст значение больше t.
                correct_result = outputs[output_index]
                found = True
        elif max_same == t and max_same_count > 1:
            correct_result = outputs[-1]
            found = True
        else:
            correct_result = outputs[output_index]
            found = True
        if found is True:
            return correct_result
        # если ни один случай не совпал, то определить верный результат
        # невозможно. Потому проверяем, есть ли еще элементы, которые не
        # участвовали в сравнении. Если да, то добавляем один из них к
        # сравниваемым, перемешиваем массив с результатами и начинаем
        # процесса с начала.
        # Иначе завершаем работу аварийно.
        if mrc == n:
            raise ValueError("Cannot find correct result")
        mrc += 1
        random.shuffle(results)


def get_experiment_data(number_of_versions, iterations):
    """
    Получение результатов версий из базы данных.
    number_of_versions - количество версий.
    iterations - количество проведенных экспериментов
    этими версиями.
    """
    experiment_name = ""
    match number_of_versions:
        case 3:
            experiment_name = "M3_I50000"
        case 5:
            experiment_name = "M5_I50000"
        case 7:
            experiment_name = "M7_I50000"
        case 9:
            experiment_name = "M9_I50000"
        case 11:
            experiment_name = "M11_I50000"
        case _:
            raise ValueError("No experiments with such number of versions.")

    if 0 > iterations > 50000:
        raise ValueError("Unsupported number of iterations")

    connection = sqlite3.connect(
        "C:\Programs\programming\SQLite\Databases\experiment_edu.db")
    cursor = connection.cursor()

    query = """
    Select version_answer from experiment_data
    where experiment_name = ? and module_iteration_num < ?
    order by module_iteration_num, version_id
    """
    cursor.execute(query, (experiment_name, iterations))
    results = cursor.fetchall()
    if len(results) != iterations * number_of_versions:
        print(len(results))
        print(iterations * number_of_versions)
        raise ValueError("Returned number of results from database does"
                         " not match expected number of results")

    query = """
    select correct_answer from experiment_data
    where experiment_name = ? and module_iteration_num < ?
    group by experiment_name, module_iteration_num
    order by module_iteration_num
    """
    cursor.execute(query, (experiment_name, iterations))
    correct_answers = cursor.fetchall()
    if len(correct_answers) != iterations:
        print(len(correct_answers), iterations)
        raise ValueError("Returned number of correct answers from database"
                         " does not match expected number of correct_answers")

    return results, correct_answers


def dpvote(results, t=-1):
    n = len(results)
    t = (n-1) // 2
    if t > (n - 1) // 2:
        raise ValueError("Number of versions with potentially wrong results"
                         " cannot exceed (n-1)/2")
    mrc = t * 2 + 1  # начальное количество сравниваемых версий
    while mrc <= n:
        outputs = []  # результаты версий, которые пошли на выход.
        for i in range(0, mrc-1, 2):
            outputs.append(results[i])
        outputs.append(results[mrc-1])

        outputs = []  # результаты версий, которые пошли на выход.
        for i in range(0, mrc-1, 2):
            outputs.append(results[i])
        outputs.append(results[mrc-1])
        # заполняем массив компараторов сравнивая соседние элементы
        # если результат одинаков, то компаратор = 0, иначе - 1.
        comparators = []
        for i in range(0, mrc-2):
            if results[i] != results[i+1]:
                comparators.append(1)
            else:
                comparators.append(0)
        length = len(comparators)

        # проходимся по компараторам и проверяем максимальное количество
        # одинаковых элементов, число групп с таким максимальным количество
        # элементов и индекс выходного элемента,
        # который содержится в максимальной группе.
        curr_same = 1
        max_same = 1
        output_index = 0  # индекс версии на выход с корректным результатом.
        groups = []
        for i in range(0, length + 1):
            if i == length or comparators[i] == 1:
                if curr_same > max_same:
                    output_index = i // 2
                    max_same = curr_same
                groups.append(curr_same)
                curr_same = 1
            else:
                curr_same += 1

        correct_result = 0.0
        found = False

        dp = []
        for _ in groups:
            dp.append([0, 0])
        start = groups.index(max_same)
        i = start
        total = max_same
        if i - 1 >= 0:
            dp[i - 1][1] = groups[i - 1]
        i -= 2
        while (i >= 0):
            dp[i][1] = min(dp[i + 1][0], dp[i + 1][1]) + groups[i]
            dp[i][0] = dp[i + 1][1]
            i -= 1
        if start != 0:
            total += min(dp[0][1], dp[0][0])
        i = start + 1
        if i < len(groups):
            dp[i][1] = groups[i]
        i += 1
        while (i < len(groups)):
            dp[i][1] = min(dp[i - 1][0], dp[i - 1][1]) + groups[i]
            dp[i][0] = dp[i - 1][1]
            i += 1
        if start != len(groups) - 1:
            total += min(dp[-1][0], dp[-1][1])
        if total > t:
            correct_result = outputs[output_index]
            found = True
        elif total == t:
            i = 0
            dp[i][1] = groups[i]
            dp[i][0] = 0
            i += 1
            while (i < len(groups)):
                dp[i][1] = min(dp[i - 1][0], dp[i - 1][1]) + groups[i]
                dp[i][0] = dp[i - 1][1]
                i += 1
            if min(dp[-1][0], dp[-1][1]) == t:
                correct_result = outputs[-1]
                found = True
            # else:
            #     correct_result = outputs[output_index]
            #     found = True
        # else:
        #     correct_result = outputs[output_index]
        #     found = True

        if found is True:
            return correct_result
        # если ни один случай не совпал, то определить верный результат
        # невозможно. Потому проверяем, есть ли еще элементы, которые не
        # участвовали в сравнении. Если да, то добавляем один из них к
        # сравниваемым, перемешиваем массив с результатами
        # и начинаем процесса с начала.
        # Иначе завершаем работу аварийно.
        if mrc == n:
            raise ValueError("Cannot find correct result")
        mrc += 1
        random.shuffle(results)


if __name__ == '__main__':
    main()
