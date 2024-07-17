import sqlite3


def main():
    results, correct_answers = get_experiment_data(3, 5)
    #print(results)
    #print(correct_answers)
    print(len(correct_answers[0]), len(results[0]))


def vote(results, t=-1):
    n = len(results)
    if t == -1:
        t = (n-1) // 2
    if t > (n - 1) // 2:
        raise ValueError("Number of versions with potentially wrong results cannot exceed (n-1)/2")
    mrc = t * 2 + 1 # начальное количество сравниваемых версий

    outputs = [] # результаты версий, которые пошли на выход.
    for i in range(0, mrc-1, 2):
        outputs.append(results[i])
    outputs.append(results[mrc-1])

    comparators = []
    for i in range(0, mrc-2):
        if results[i] != results[i+1]:
            comparators.append(1)
        else:
            comparators.append(0)

    curr_same = 1
    max_same = 1
    output_index = 0 # индекс версии на выход с корректным результатом
    max_same_count = 1
    length = len(comparators)
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

    correct_result = 0.0
    # если количество групп с максимальным количеством
    #  версий больше одной, то возвращаем результат версии
    #  не участвовавшей в сравнении.
    if max_same_count > 1:
        correct_result = outputs[-1]
    else:
        # иначе возвращаем результат версии подгруппы
        correct_result = outputs[output_index]
    return correct_result


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
    
    connection = sqlite3.connect("C:\Programs\programming\SQLite\Databases\experiment_edu.db")
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
        raise ValueError("Returned number of results from database does not match expected number of results")

    query = """
    select distinct correct_answer from experiment_data
    where experiment_name = ? and module_iteration_num < ?
    order by module_iteration_num
    """
    cursor.execute(query, (experiment_name, iterations))
    correct_answers = cursor.fetchall()
    if len(correct_answers) != iterations:
        print(len(correct_answers), iterations)
        raise ValueError("Returned number of correct answers from database does not match expected number of correct_answers")
    
    return results, correct_answers

main()
