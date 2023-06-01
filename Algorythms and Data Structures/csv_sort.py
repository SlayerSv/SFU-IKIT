"""Модуль для работы с .csv файлами."""
import csv
import os


def sort_csv(src: list, output, reverse: bool, key):
    """Основная функция для сортировки csv файлов.

    Может сортировать как на месте, так и с выбором выходного файла.
    Может сортировать несколько файлов.
    """
    if len(src) > 1 and output != "":
        merge_and_sort(src, output, reverse, key)
        files_for_sorting = initial_split(output, reverse, key)
        merge_and_sort(files_for_sorting, output, reverse, key)
    else:
        for path in src:
            if output == "":
                output_path = path
            else:
                output_path = output
            files_for_sorting = initial_split(path, reverse, key)
            merge_and_sort(files_for_sorting, output_path, reverse, key)
    for file in files_for_sorting:
        os.remove(file)


def initial_split(file_path_source, reverse: bool, key=""):
    """Функция для изначального разделения файла на блоки для последующей
    сортировки.

    Использует алгоритм естественного слияния для разбиения файла на множество
    файлов с блоками по неубыванию или невозрастанию. Сохраняет список новых
    файлов для группировки в список и возвращает его для дальнейшего слияния
    и сортировки.
    """

    file_source = open(file_path_source, "r", newline="", encoding='utf-8')
    reader = csv.DictReader(file_source, delimiter=";")
    fieldnames = reader.fieldnames
    next_file_index = 0
    paths_for_sorting = []
    file_path_writing = 'file_' + str(next_file_index) + '.csv'
    paths_for_sorting.append(file_path_writing)
    file_writing = open(file_path_writing, 'w', newline="", encoding='utf-8')
    writer = csv.DictWriter(file_writing, fieldnames=fieldnames, delimiter=";")
    writer.writeheader()
    while True:
        try:
            row = next(reader)
        except StopIteration:
            paths_for_sorting.append(file_path_writing)
            file_writing.close()
            file_source.close()
            break
        next_value = get_value(row, key)
        if 'prev_value' in locals():
            if reverse is True:
                is_next_value_less = next_value > prev_value  # pylint: disable=E0601
            else:
                is_next_value_less = next_value < prev_value
            if is_next_value_less:
                file_writing.close()
                next_file_index += 1
                file_path_writing = 'file_' + str(next_file_index) + '.csv'
                paths_for_sorting.append(file_path_writing)
                file_writing = open(file_path_writing, 'w', newline="",
                                    encoding='utf-8')
                writer = csv.DictWriter(file_writing, fieldnames=fieldnames,
                                        delimiter=";")
                writer.writeheader()
        writer.writerow(row)
        prev_value = next_value
    return paths_for_sorting


def merge_and_sort(paths: list, output, reverse, key):
    """Объединяет и сортирует файлы.

    Считывает последовательно все файлы, сливая их в один. Сначала
    копирует последний файл из списка, удаляет его из списка, затем
    проходится по списку с начала и каждый раз объединяет следующий файл
    из списка с итоговым файлом.
    """
    if len(paths) == 0:
        print("Empty list")
        return
    if len(paths) == 1:
        copy_file(paths[0], output)
        return
    copy_file(paths[len(paths) - 1], output)
    paths.pop()
    for path in paths:
        file_current_reading = open(path, "r", newline="", encoding='utf-8')
        reader_a = csv.DictReader(file_current_reading, delimiter=";")
        fieldnames = reader_a.fieldnames
        copy_file(output, "file_output_copy.csv")
        file_output_copy = open("file_output_copy.csv", "r", newline="",
                                encoding='utf-8')
        reader_merged = csv.DictReader(file_output_copy, delimiter=";")
        file_output = open(output, "w", newline="", encoding='utf-8')
        writer = csv.DictWriter(file_output, fieldnames=fieldnames,
                                delimiter=";")
        writer.writeheader()
        row_a = next(reader_a)
        row_merged = next(reader_merged)
        while True:
            is_done = False
            number_a = get_value(row_a, key)
            number_merged = get_value(row_merged, key)
            is_number_a_less = number_a < number_merged
            if reverse is True:
                is_number_a_less = not is_number_a_less
            if is_number_a_less:
                writer.writerow(row_a)
                try:
                    row_a = next(reader_a)
                except StopIteration:
                    while True:
                        try:
                            writer.writerow(row_merged)
                            row_merged = next(reader_merged)
                        except StopIteration:
                            is_done = True
                            break
            else:
                writer.writerow(row_merged)
                try:
                    row_merged = next(reader_merged)
                except StopIteration:
                    while True:
                        try:
                            writer.writerow(row_a)
                            row_a = next(reader_a)
                        except StopIteration:
                            is_done = True
                            break
            if is_done is True:
                break
        file_current_reading.close()
        file_output.close()
        file_output_copy.close()


def copy_file(path_source, path_copy):
    """Создает копию файла."""
    file_source = open(path_source, "r", newline="", encoding='utf-8')
    reader = csv.DictReader(file_source, delimiter=";")
    fieldnames = reader.fieldnames
    file_copy = open(path_copy, "w", newline="", encoding='utf-8')
    writer = csv.DictWriter(file_copy, fieldnames=fieldnames, delimiter=";")
    writer.writeheader()
    for row in reader:
        writer.writerow(row)
    file_source.close()
    file_copy.close()


def get_value(row: dict, key: str | int):
    """Извлекает значение из строки по ключу."""
    try:
        value = int(row[key])
    except ValueError:
        try:
            value = float(row[key])
        except ValueError:
            value = row[key]
    return value
