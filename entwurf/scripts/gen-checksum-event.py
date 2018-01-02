#!/bin/python

import hashlib
import json
from normalize import normalize

testEvent = {
    "type" : "event",
    "id": 2,
    "title": "Beispielverabredung 1010",
    "creator": 200,
    "time-start": 1514761200000,
    "time-end": 1515000600000,
    "latitude": 49.011978,
    "longitude": 8.416377,
    "participants": [
        {
            "type": "account",
            "id": 3,
            "user": "w4rum",
            "icon-checksum": "123abc"
        },
        {
            "type": "account",
            "id": 200,
            "user": "notw4rum",
            "icon-checksum": "blaa"
        },
        {
            "type": "account",
            "id": 43378,
            "user": "absolutetlynotw4rum",
            "icon-checksum": "foobar"
        },
    ]
}

testEvent2 = {
    "id": 2,
    "type" : "event",
    "title": "Beispielverabredung 1010",
    "creator": 200,
    "time-start": 1514761200000,
    "time-end": 1515000600000,
    "latitude": 49.011978,
    "longitude": 8.416377,
    "participants": [
        {
            "type": "account",
            "id": 3,
            "user": "w4rum",
            "icon-checksum": "123abc"
        },
        {
            "type": "account",
            "id": 200,
            "user": "notw4rum",
            "icon-checksum": "blaa"
        },
        {
            "type": "account",
            "id": 43378,
            "user": "absolutetlynotw4rum",
            "icon-checksum": "foobar"
        },
    ]
}

n = normalize(testEvent)
n2 = normalize(testEvent2)

for i in range(7):
    n = n.replace(chr(i).encode(), b"(*\circled{%i}*)" % i)
print(n.decode())
