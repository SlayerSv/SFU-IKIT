from pathlib import Path
import csv


def main():
    # my_sort('datas.txt', 'file_e.txt', False)
    # csv_run()
    file = open('csv.csv')
    next = ' '
    while next != '':
        next = file.readline()
        print(next)


def my_sort(src: str, output: str = None, reverse: bool = False, key=None,
            data_type='i'):
    is_sorting_finished = initial_split(src, reverse)
    if output is None:
        output = 'file_c.txt'
    while is_sorting_finished is not True:
        merge_and_sort(output, reverse)
        is_sorting_finished = split(output)
    print('done sorting')


def initial_split(file_path, reverse: bool):
    file_data = open(file_path)
    file_a = open('file_a.txt', 'w')
    file_b = open('file_b.txt', 'w')
    current_file = file_a
    has_switched = False
    is_string = False
    while True:
        next_number = file_data.readline()
        if next_number == '':
            file_data.close()
            file_a.close()
            file_b.close()
            return has_switched is False
        try:
            next_number = int(next_number)
        except ValueError:
            try:
                next_number = float(next_number)
            except ValueError:
                is_string = True
        if 'prev_number' in locals():
            if (prev_number > next_number) is not reverse:
                if current_file == file_a:
                    current_file = file_b
                else:
                    current_file = file_a
                if has_switched is True:
                    current_file.write('\n')
                has_switched = True
        if is_string is False:
            current_file.write(str(next_number) + '\n')
        else:
            current_file.write(next_number)
        prev_number = next_number


def merge_and_sort(output, reverse: bool):
    file_a = open('file_a.txt', 'r')
    file_b = open('file_b.txt', 'r')
    file_c = open(output, 'w')
    number_a = file_a.readline()
    number_b = file_b.readline()
    print(number_a)
    print(number_b)
    while True:
        if number_a == '' or number_a == '\n':
            while True:
                if number_a == '' and number_b == '':
                    file_c.close()
                    file_a.close()
                    file_b.close()
                    return
                if number_b == '\n':
                    file_c.write('\n')
                    number_b = file_b.readline()
                    number_a = file_a.readline()
                    break
                file_c.write(number_b)
                number_b = file_b.readline()
        elif number_b == '' or number_b == '\n':
            while True:
                if number_a == '' and number_b == '':
                    return
                if number_a == '\n':
                    file_c.write('\n')
                    number_b = file_b.readline()
                    number_a = file_a.readline()
                    break
                file_c.write(number_a)
                number_a = file_a.readline()
        else:
            try:
                a_is_less = float(number_a) < float(number_b)
            except ValueError:
                a_is_less = number_a < number_b
            if a_is_less is not reverse:
                file_c.write(number_a)
                number_a = file_a.readline()
            else:
                file_c.write(number_b)
                number_b = file_b.readline()


def split(file_path):
    file_data = open(file_path)
    file_a = open('file_a.txt', 'w')
    file_b = open('file_b.txt', 'w')
    current_file = file_a
    switch_files_count = 0
    has_switched = False
    while True:
        next_number = file_data.readline()
        if next_number == '':
            file_data.close()
            file_a.close()
            file_b.close()
            return has_switched is False
        elif next_number == '\n':
            if current_file == file_a:
                current_file = file_b
            else:
                current_file = file_a
            if has_switched is True:
                current_file.write('\n')
            has_switched = True
            switch_files_count += 1
        else:
            current_file.write(next_number)


def csv_run():
    TEST_NUMBER = [
        [],
        [1],
        [1, 2, 3, 4, 5],
        [0, 0, 0, 55, 55, 60],
        [9, 8, 7, 6, 5, 4, 3, 2, 1, 0],
        [8, 0, 42, 3, 4, 8, 0, 45, 50, 9999, 7],
        [-5, 0, 9, -999, 874, 35, -4, -5, 0],
        # [1, 1, 1],
    ]
    key = "sort"
    for data in TEST_NUMBER:
        file = open("csv.csv", "w", encoding="utf-8")
        file.write("")
        file.close()
        ptr = open("csv.csv", "a", newline="", encoding="utf-8")
        writer = csv.DictWriter(ptr, fieldnames=[key])
        writer.writeheader()
        for i in data:
            writer.writerow({key: int(i)})
        ptr.close()


if __name__ == '__main__':
    main()
