from pathlib import Path
import csv


def main():
    # my_sort('datas.txt', 'file_e.txt', False)
    # csv_run()
    src = ['data_floats.txt']
    output = 'file_output.txt'
    external_natural_merge_sort(src, output, False)


def external_natural_merge_sort(src: list, output: str = "",
                                reverse: bool = False, key="",
                                type_data='i'):
    if output != "" and len(src) > 1:
        merge_and_sort(output, reverse, src)
        is_sorting_finished = initial_split(output, reverse)
        while is_sorting_finished is not True:
            merge_and_sort(output, reverse)
            is_sorting_finished = split(output)
        print('done sorting')
    else:
        for file in src:
            is_sorting_finished = initial_split(file, reverse)
            if output == "":
                output_sort = file
            else:
                output_sort = output
            while is_sorting_finished is not True:
                merge_and_sort(output_sort, reverse)
                is_sorting_finished = split(output_sort)
            print('done sorting' + file)


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


def merge_and_sort(output: str = 'file_output.txt',
                   reverse: bool = False,
                   src: list = ['file_a.txt', 'file_b.txt']):
    print(src)
    file_a = open(src[0], 'r')
    file_b = open(src[1], 'r')
    file_output = open(output, 'w')
    number_a = file_a.readline()
    number_b = file_b.readline()
    while True:
        if number_a == '':
            while True:
                if number_b == '':
                    file_output.close()
                    file_a.close()
                    file_b.close()
                    return
                file_output.write(number_b)
                number_b = file_b.readline()
        elif number_a == '\n':
            while True:
                if number_b == '\n':
                    file_output.write('\n')
                    number_b = file_b.readline()
                    number_a = file_a.readline()
                    break
                if number_b == '':
                    number_a = file_a.readline()
                    file_output.write('\n')
                    break
                file_output.write(number_b)
                number_b = file_b.readline()
        if number_b == '':
            while True:
                if number_a == '':
                    file_output.close()
                    file_a.close()
                    file_b.close()
                    return
                file_output.write(number_a)
                number_a = file_a.readline()
        elif number_b == '\n':
            while True:
                if number_a == '\n':
                    file_output.write('\n')
                    number_b = file_b.readline()
                    number_a = file_a.readline()
                    break
                if number_a == '':
                    number_b = file_b.readline()
                    file_output.write('\n')
                    break
                file_output.write(number_a)
                number_a = file_a.readline()
        try:
            a_is_less = float(number_a) < float(number_b)
        except ValueError:
            a_is_less = number_a < number_b
        if a_is_less is not reverse:
            file_output.write(number_a)
            number_a = file_a.readline()
        else:
            file_output.write(number_b)
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