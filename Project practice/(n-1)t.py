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
    left = n - mrc # количество оставшихся версий, которые не участвуют в сравнении
    found_correct_answer = False
    while(found_correct_answer is not True and left > 0):
        output_versions = [] # версии, чьи результаты пойдут на выход.
        for i in range(0, mrc-1, t + 1):
            output_versions.append(i)
        output_versions.append(mrc-1)
        curr_same = 1
        max_same = 1
        start_index = -1
        groups_count = {}
        for i in range(0, mrc, 1):
            if i == mrc - 1 or results[i][0] != results[i+1][0]:
                if curr_same > max_same:
                    start_index = i - (max(0, curr_same - 2))
                groups_count[curr_same] += 1
                curr_same = 1
            else:
                curr_same += 1
                max_same = max(curr_same, max_same)
                
        if groups_count[max_same] > 1:
            return results[mrc-1][0]
        elif same_t_groups == 1:
            for index in output_versions:
                if index >= start_index:
                    return results[index][0]
        mrc += 1
        left -= 1
    raise ValueError("Cannot find correct result")


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
