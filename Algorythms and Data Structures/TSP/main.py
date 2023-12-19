"""Модуль для работы с csv файлами."""
import csv
import math
import time
import queue


def main():
    """Программа для нахождения примерного кратчайшего пути.

    Программа считывает координаты вершин, затем высчитывает расстояния
    между ними, строит полный граф из этих вершин, затем строит
    минимальное остовное дерево, затем строит примерный путь, после чего
    использует оптимизацию пути используя 2-opt алгоритм.

    Time complexity: O(V^2 * log(V^2))
    Space complexity: O(V^2)
    """
    start = time.time()
    coordinates = read_data()
    graph = build_graph(coordinates)
    print("Done building graph. Current execution time:",
          round(time.time() - start, 2), "seconds")
    start_city = 3753
    mst = build_mst(graph, start_city)
    path = build_path(mst, start_city)
    print("Done building path. Current execution time:",
          round(time.time() - start, 2), "seconds")
    optimized_path = two_opt(path, graph)
    print("Done optimizing path. Current execution time:",
          round(time.time() - start, 2), "seconds")
    distance_path = build_distance_path(optimized_path, graph, 0)
    write_data(distance_path[0])
    print("All done. Total execution time:",
          round(time.time() - start, 2), "seconds.\nTotal distance:",
          distance_path[1])


def read_data(file_path="data.csv"):
    """Считывает координаты вершин из csv файла.

    Time complexity: O(V)
    Space complexity: O(V)
    """
    file = open(file_path, "r", newline="", encoding='utf-8')
    reader = csv.DictReader(file, delimiter=",")
    data = []
    for row in reader:
        data.append([int(row["latitude_dd"]), int(row["longitude_dd"])])
    file.close()
    return data


def build_graph(coords):
    """Высчитывает расстояния между вершинами и строит матрицу смежности.

    Расстояния высчитываются как Евклидово расстояние. Возвращает построенную
    матрицу смежности.

    Time complexity: O(V^2)
    Space complexity: O(V^2)
    """
    n = len(coords)
    adjacent = [[0] * n for i in range(n)]
    for i in range(0, n, 1):
        for j in range(i + 1, n, 1):
            x = coords[i][0] - coords[j][0]
            y = coords[i][1] - coords[j][1]
            distance = math.sqrt(x * x + y * y)
            adjacent[i][j] = distance
            adjacent[j][i] = distance
    return adjacent


def two_opt(path, graph):
    """Алгоритм 2-opt для оптимизации пути.

    Алгоритм берет 2 ребра и определяет можно ли уменьшить расстояние пути
    поменяв соединения между вершинами этих 2 ребер. Если да, то строит новые
    ребра.
    В качестве оптимизации для путей с большим количеством вершин используется
    ограничение для интервала сравнивания ребер, т.к. основное количество
    улучшений происходит среди вершин, находящихся рядом.

    Time complexity: O(V^2 * min(V, limit)) (дополнительный множитель V из-за
    создания новых путей)
    Space complexity: O(V)
    """
    n = len(path) - 2
    limit = min(400, n)
    improved = True
    while improved is True:
        improved = False
        for i in range(1, n - 1):
            end = min(i + limit, n)
            for j in range(i + 2, end):
                old_distance = graph[path[i]][path[i + 1]] \
                    + graph[path[j]][path[j + 1]]
                new_distance = graph[path[i]][path[j]] \
                    + graph[path[i + 1]][path[j + 1]]
                if new_distance < old_distance:
                    start = path[:i + 1]
                    mid = path[i + 1:j + 1]
                    mid.reverse()
                    end = path[j + 1:]
                    path = start + mid + end
                    improved = True
    return path


def build_mst(graph, start=0):
    """Строит минимальное остовное дерево используя алгоритм Прима.

    Использует очередь с приоритетом для оптимизации поиска ближайшего
    ребра. В качестве хранения информации о вершинах ребра использует формулу
    (индекс вершины начала ребра * общее количество вершин +
     + индекс вершины конца ребра).

    Time complexity: O((V + E) * log(E))
    Space complexity: O(V + E)
    """
    n = len(graph)
    is_connected = [0] * n
    mst = [[] for i in range(n)]
    q = queue.PriorityQueue()
    for i in range(n):
        q.put((graph[start][i], start * n + i))
    connected_count = 1
    is_connected[start] = 1
    tenpercent = int(n / 10)
    while connected_count < n:
        nodes = q.get()[1]
        node1 = int(nodes % n)
        node2 = int(nodes / n)
        if is_connected[node1] == 1:
            continue
        mst[node2].append(node1)
        mst[node1].append(node2)
        is_connected[node1] = 1
        connected_count = connected_count + 1
        if connected_count % tenpercent == 0:
            print("Building MST...",
                  str(int(connected_count / tenpercent * 10)) + "% done.")
        for i in range(n):
            dist = graph[node1][i]
            if dist > 0 and is_connected[i] == 0:
                q.put((dist, node1 * n + i))
    return mst


def build_path(mst, start=0):
    """Строит путь делая обход минимального остовного дерева в глубину.

    Извлекает из стека последнюю положенную туда вершину, добавляет её в
    итоговый путь, затем кладет в стек соседей этой вершины. Отслеживает
    уже присоединенные вершины путем листа connected.

    Time complexity: O(V + E)
    Space complexity: O(V + E)
    """
    stack = [start]
    path = []
    is_connected = [0] * len(mst)
    while len(stack) > 0:
        node = stack.pop()
        path.append(node)
        is_connected[node] = 1
        for neigh in mst[node]:
            if is_connected[neigh] == 0:
                stack.append(neigh)
    path.append(start)
    return path


def build_distance_path(path, graph, start):
    """Строит путь расстояний из пути вершин и рассчитывает итоговый путь.

    Time complexity: O(V)
    Space complexity: O(V)
    """
    prev = start
    distance_path = []
    total_distance = 0.0
    for node in path:
        distance_path.append(graph[prev][node])
        total_distance += graph[prev][node]
        prev = node
    return [distance_path, total_distance]


def write_data(data, file_path="solution.csv"):
    """Записывает в csv файл итоговый путь в виде дистанций между вершинами.

    Time complexity: O(V)
    Space complexity: O(1)
    """
    file = open(file_path, "w", newline="", encoding='utf-8')
    writer = csv.writer(file, delimiter=";")
    writer.writerow(data)
    file.close()


if __name__ == "__main__":
    main()
