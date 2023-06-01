"""Основной модуль для внешней сортировки"""
import external_sort


def menu():
    """Основное меню программы, показывает опции сортировки и запрашивает
    данные у пользователя.
    """

    print("Welcome to external sorting program!")
    print("Here you can sort txt and csv files.")
    print("Choose what kind of sorting do you want to perform:\n")
    user_input = get_sorting_option()
    print("Enter a path to the file for sorting.\n"
          + "You can enter multiple file paths separated by spaces.\n"
          + "File names must not contain spaces:")
    src = input("File path(s): ")
    src = src.split(" ")
    if user_input == "2" or user_input == "4":
        output = input("Enter output file path: ")
    else:
        output = ""
    reverse = get_sorting_order()
    if user_input == "3" or user_input == "4":
        key = input("Enter the name of the field to sort: ")
    else:
        key = ""
    external_sort.external_natural_merge_sort(src, output, reverse, key)


def get_sorting_option():
    """Запрашивает выбор сортировки у пользователя."""

    print("Choose what kind of sorting do you want to perform:\n")
    print("1. Sort text files in-place")
    print("2. Sort text files using output file")
    print("3. Sort csv files in-place")
    print("4. Sort csv files using output file")
    while True:
        user_input = input("Enter number of the option: ")
        if user_input == "1" or user_input == "2" \
                or user_input == "3" or user_input == "4":
            return user_input
        else:
            print("Wrong input! Try again.")


def get_sorting_order():
    """Запрашивает тип сортировки: по неубыванию или по невозрастанию."""

    print("In what order do you want to sort?")
    print("1. Non-decreasing")
    print("2. Non-increasing")
    while True:
        order = input("Enter number of the option: ")
        if order == "1":
            return False
        elif order == "2":
            return True
        else:
            print("Wrong input! Try again.")


if __name__ == "__main__":
    menu()
