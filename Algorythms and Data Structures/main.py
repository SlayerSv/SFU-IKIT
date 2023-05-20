from pathlib import Path


def main():
    file = open('data.txt')
    split(file)
    merge_and_sort()
    file = open('file_c.txt')
    split(file)


def split(file_data):
    file_a = open('file_a.txt', 'w')
    file_b = open('file_b.txt', 'w')
    current_file = file_a
    while True:
        next_number = file_data.readline()
        if next_number == '':
            file_data.close()
            file_a.close()
            file_b.close()
            return
        next_number = int(next_number)
        if 'prev_number' in locals():
            if prev_number > next_number:
                if current_file == file_a:
                    current_file = file_b
                else:
                    current_file = file_a
                current_file.write('\n')
        current_file.write(str(next_number) + '\n')
        prev_number = next_number


def merge_and_sort():
    file_a = open('file_a.txt', 'r')
    file_b = open('file_b.txt', 'r')
    file_c = open('file_c.txt', 'w')
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
            print(number_a)
            print(number_b)
            if int(number_a) < int(number_b):
                file_c.write(number_a)
                number_a = file_a.readline()
            else:
                file_c.write(number_b)
                number_b = file_b.readline()


def split():
    if next_number == '\n':
        if current_file == file_a:
            current_file = file_b
        else:
            current_file = file_a
        continue


# def split():
#     file_a = open('file_a.txt', 'w')
#     file_b = open('file_b.txt', 'w')
#     file_c = open('file_c.txt', 'r')
#     current_file = file_a
#     while True:
#         next_number = read_next_number(file_c)
#         if next_number == '':
#             return
#         if next_number == ';':
#             current_file.write(';')
#             if current_file == file_a:
#                 current_file = file_b
#             else:
#                 current_file = file_a
#         else:
#             current_file.write(str(next_number) + ' ')


if __name__ == '__main__':
    main()
