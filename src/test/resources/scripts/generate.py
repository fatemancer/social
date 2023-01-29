import concurrent.futures
import random

import requests
from concurrent.futures import ThreadPoolExecutor

cities = []
names = []
h = {"Content-Type": "application/json"}
default_bio = ""
default_pass = "password"


def read_data():
    with open("cities_u.txt", 'r', encoding="utf8") as cities:
        cities_arr = cities.read().splitlines()
        with open("names_u.txt", 'r', encoding="utf8") as names:
            names_arr = names.read().splitlines()
    return cities_arr, names_arr


def form_data(session):
    n = random.choice(names)
    c = random.choice(cities)
    first, second = n.split(" ")
    person = {
        "first_name": first,
        "second_name": second,
        "age": random.randint(18, 80),
        "city": c,
        "biography": default_bio,
        "password": default_pass
    }
    session.post("http://localhost:6868/user/register", json=person, headers=h)


if __name__ == '__main__':
    cities, names = read_data()

    with ThreadPoolExecutor(64) as executor:
        with requests.Session() as session:
            iterator = [executor.submit(form_data, session) for it in range(1, 1_000_000)]
            print("Yaaaay!...not yet")
            concurrent.futures.wait(iterator)
            print("Yaaaay! 1 million users inserted")
