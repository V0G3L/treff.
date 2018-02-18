#!/bin/python

import hashlib
import binascii
import json
from normalize import normalize

testEvent = {
    "type" : "event",
    "id": 2,
    "title": "200k+ DPS or kick",
    "creator": 200,
    "time-start": 1514761200000,
    "time-end": 1515000600000,
    "latitude": 49.011978,
    "longitude": 8.416377,
    "participants": [ 3, 200, 43378 ]
}

testEvent2 = {
    "id": 2,
    "type" : "event",
    "title": "200k+ DPS or kick",
    "creator": 200,
    "time-start": 1514761200000,
    "time-end": 1515000600000,
    "latitude": 49.011978,
    "longitude": 8.416377,
    "participants": [ 3, 200, 43378 ]
}

def formatForLatex(s):
    for i in range(7):
        s = s.replace(chr(i).encode(), b"(*\circled{%i}*)" % i)
    return s

s = str(testEvent)
s2 = str(testEvent2)

n = normalize(testEvent)
n2 = normalize(testEvent2)

print(formatForLatex(n).decode())

m = hashlib.md5()
m.update(n)
print("md5sum: %s" % binascii.hexlify(m.digest()))

print("String equality: %s\nNormalized string equality: %s" %
      (s == s2, n == n2))
