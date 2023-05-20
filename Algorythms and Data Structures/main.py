from pathlib import Path


def main():
    initial_split()
    merge_and_sort()
    split()
    merge_and_sort()


def read_next_number(file_data):
    next_number = ''
    while True:
        next_character = file_data.read(1)
        if next_character == '':
            if next_number == '':
                return next_number
            else:
                return int(next_number)
        if next_character == ' ':
            if next_number == '':
                return next_number
            return int(next_number)
        if next_character == ';':
            return ';'
        next_number += next_character


def initial_split():
    file_data = open('data.txt')
    file_a = open('file_a.txt', 'w')
    file_b = open('file_b.txt', 'w')
    current_file = file_a
    while True:
        next_number = read_next_number(file_data)
        if next_number == '':
            current_file.write(';')
            file_data.close()
            file_a.close()
            file_b.close()
            return
        if 'prev_number' in locals():
            if prev_number > next_number:
                current_file.write(';')
                if current_file == file_a:
                    current_file = file_b
                else:
                    current_file = file_a
        current_file.write(str(next_number) + ' ')
        prev_number = next_number


def merge_and_sort():
    file_a = open('file_a.txt', 'r')
    file_b = open('file_b.txt', 'r')
    file_c = open('file_c.txt', 'w')
    number_a = read_next_number(file_a)
    number_b = read_next_number(file_b)
    while True:
        print(number_a)
        print(number_b)
        if number_a == '' or number_a == ';':
            while True:
                if number_b == '' and number_a == '':
                    return
                if number_b == ';':
                    file_c.write(';')
                    number_b = read_next_number(file_b)
                    number_a = read_next_number(file_a)
                    break
                file_c.write(str(number_b) + ' ')
                number_b = read_next_number(file_b)
        elif number_b == '' or number_b == ';':
            while True:
                if number_a == '' and number_b == '':
                    return
                if number_a == ';':
                    file_c.write(';')
                    number_a = read_next_number(file_a)
                    number_b = read_next_number(file_b)
                    print(number_a)
                    print(number_b)
                    break
                file_c.write(str(number_a) + ' ')
                number_a = read_next_number(file_a)
        else:
            if number_a < number_b:
                file_c.write(str(number_a) + ' ')
                number_a = read_next_number(file_a)
            else:
                file_c.write(str(number_b) + ' ')
                number_b = read_next_number(file_b)


def split():
    file_a = open('file_a.txt', 'w')
    file_b = open('file_b.txt', 'w')
    file_c = open('file_c.txt', 'r')
    current_file = file_a
    while True:
        next_number = read_next_number(file_c)
        if next_number == '':
            return
        if next_number == ';':
            current_file.write(';')
            if current_file == file_a:
                current_file = file_b
            else:
                current_file = file_a
        else:
            current_file.write(str(next_number) + ' ')


if __name__ == '__main__':
    main()
