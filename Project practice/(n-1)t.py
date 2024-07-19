import sqlite3
import random


def main():
    number_of_elements = [3, 5, 7, 9, 11]
    iterations = 50000

    for elements in number_of_elements:
        results, correct_results = get_experiment_data(elements, iterations)
        curr_index = 0
        correct_results_count = 0
        cannot_find = 0
        for correct_result in correct_results:
            versions_results = []
            for _ in range(0, elements):
                version_result = results[curr_index][0]
                versions_results.append(version_result)
                curr_index += 1
            try:
                result = vote(versions_results)
                if result == correct_result[0]:
                    correct_results_count += 1
            except ValueError:
                cannot_find += 1
        print("Elements:", elements, "Iterations:", iterations,
              "Correct results:", correct_results_count,
              "Cannot find:", cannot_find)


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
        # если нашли группу с количеством элементов больше t, то возвращаем
        # результат ее выходного элемента.
        if max_same > t:
            correct_result = outputs[output_index]
            found = True
        # если групп 0, то возвращаем результат несравниваемого элемента,
        # т.к. такое возможно, только если количество неверных элементов
        # среди сравниваемых не меньше t.
        elif groups_count == 0:
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
        # else:
        #     correct_result = outputs[output_index]
        #     found = True
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


def vote1(results):
    n = len(results)
    t = ((n - 1) / 2).__trunc__()
    output_count = n - t
    output_indexes_step = ((n - 1) / (output_count - 1)).__trunc__()
    output_indexes = {i: results[i] for i in range(0, n - 1,
                                                   output_indexes_step)}
    output_indexes[n - 1] = results[n - 1]

    comparators = dict()
    max_count_indexes = dict()
    cur_start_index = -1
    max_group_count = 0
    cur_max_group_count = 0  
    for i in range(n - 2):
        if results[i] == results[i + 1]:
            comparators[(i, i + 1)] = 0
            if cur_max_group_count == 0:
                cur_start_index = i
                cur_max_group_count = 2
            else:
                cur_max_group_count += 1
        else:
            comparators[(i, i + 1)] = 1

            if cur_start_index != -1:
                if cur_max_group_count not in max_count_indexes:
                    max_count_indexes[cur_max_group_count] = \
                        [(cur_start_index, i)]
                else:
                    max_count_indexes[cur_max_group_count] \
                        .append((cur_start_index, i))

            cur_start_index = -1
            cur_max_group_count = 0

        if cur_max_group_count > max_group_count:
            max_group_count = cur_max_group_count

    if cur_start_index != -1:
        if cur_max_group_count not in max_count_indexes:
            max_count_indexes[cur_max_group_count] = [(cur_start_index, n - 2)]
        else:
            max_count_indexes[cur_max_group_count] \
                .append((cur_start_index, n - 2))

    correct_result: float
    if max_group_count >= t:
        if len(max_count_indexes[max_group_count]) == 1:
            for key, val in output_indexes.items():
                if max_count_indexes[max_group_count][0][0] <= key \
                        <= max_count_indexes[max_group_count][0][1]:
                    correct_result = val
                    break
            else:
                correct_result = \
                    results[max_count_indexes[max_group_count][0][0]] \
                    .version_answer
        else:
            correct_result = output_indexes[n - 1]
    else:
        correct_result = output_indexes[n - 1]

    return correct_result


if __name__ == '__main__':
    main()
