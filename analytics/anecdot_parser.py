import re
import csv

# --- НАСТРОЙКИ ---
input_folder = 'anecdotes/'
output_file_path = 'anecdotes.csv'
# -----------------

categories = ['Армия', 'Винни Пух', 'Вовочка', 'Врачи', 'ГАИ', 'Математика', 'Муж и жена', 'Программисты']
id = 1

def parse_anecdotes(file_path):
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
    except FileNotFoundError:
        print(f"Ошибка: Файл '{file_path}' не найден.")
        return None
    except Exception as e:
        print(f"Произошла ошибка при чтении файла: {e}")
        return None

    anecdotes_text = re.split(r' [+-]\d+ ', content)
    cleaned_anecdotes = [anec.strip() for anec in anecdotes_text if anec.strip()]

    return cleaned_anecdotes

def save_to_csv(anecdotes_list, output_path, category):
    """
    Сохраняет список анекдотов в CSV-файл.
    """
    if not anecdotes_list:
        print("Список анекдотов пуст. CSV-файл не будет создан.")
        return

    with open(output_path, 'a', newline='', encoding='utf-8') as csvfile:
        # Создаем writer для записи в CSV.
        # quoting=csv.QUOTE_ALL гарантирует, что текст анекдота будет целиком в кавычках,
        # даже если внутри него есть запятые. Это важно.
        writer = csv.writer(csvfile, quoting=csv.QUOTE_ALL)

        global id
        for anecdote in anecdotes_list:
            writer.writerow([id, anecdote, category])
            id += 1

    print(f"Успешно! Записано {len(anecdotes_list)} анекдотов категории {category}.")


# --- ОСНОВНОЙ КОД ---
if __name__ == "__main__":
    with open(output_file_path, 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.writer(csvfile, quoting=csv.QUOTE_ALL)
        writer.writerow(['id', 'text', 'category'])
    
    for category in categories:
        path = input_folder + category + '.txt'
        anecdotes = parse_anecdotes(path)
        save_to_csv(anecdotes, output_file_path, category)