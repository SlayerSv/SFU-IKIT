from pathlib import Path
import csv


def main():
    src = []
    output = ''
    reverse = False
    key = ""
    # external_natural_merge_sort(src, output, reverse, key)


def external_natural_merge_sort(src: list, output: str = "",
                                reverse: bool = False, key="",
                                type_data='i'):
    if key != "":
        csv_sort(src, output, reverse, key)
    elif output != "" and len(src) > 1:
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


def csv_sort(src, output_path, reverse, key):
    for file_source_path in src:
        if output_path == "":
            output_path = file_source_path
        file_source = open(file_source_path, "r", newline="")
        reader = csv.DictReader(file_source, delimiter=";")
        headers = reader.fieldnames
        file_numbers = open("file_numbers.txt", "w")
        file_source_copy = open("file_source_copy.csv", "w", newline="")
        writer = csv.DictWriter(file_source_copy, fieldnames=headers,
                                delimiter=";")
        writer.writeheader()
        for row in reader:
            file_numbers.write(row[key] + "\n")
            writer.writerow(row)
        file_numbers.close()
        file_source.close()
        file_source_copy.close()
        external_natural_merge_sort(["file_numbers.txt"], "", reverse)
        file_output = open(output_path, "w", newline="")
        file_numbers = open("file_numbers.txt", "r")
        file_source_copy = open("file_source_copy.csv", "r", newline="")
        reader = csv.DictReader(file_source_copy, delimiter=";")
        print(headers)
        writer = csv.DictWriter(file_output, fieldnames=headers, delimiter=";")
        writer.writeheader()
        for row in reader:
            next_number = file_numbers.readline().strip()
            row[key] = next_number
            writer.writerow(row)
        file_source.close()
        file_source_copy.close
        file_numbers.close()
        file_output.close()


if __name__ == '__main__':
    main()
