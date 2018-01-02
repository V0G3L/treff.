order = {
    "event": [
        "type",
        "id",
        "title",
        "creator",
        "time-start",
        "time-end",
        "latitude",
        "longitude",
        "participants"
    ],
    "account": [
        "type",
        "id",
        "user",
        "icon-checksum"
    ]
}

def normalize(o):
    if type(o) == dict:
        output = b"\x01"
        sortedO = [0] * len(o)
        t = o["type"]
        for k, v in o.items():
            sortedO[order[t].index(k)] = v

        for v in sortedO:
            output += normalize(v) + b"\x02"

        # remove trailing seperator
        output = output[:-1]
        output += b"\x03"
        return output
    elif type(o) == list:
        output = b"\x04"
        o.sort(key=lambda x: x["id"])

        for v in o:
            output += normalize(v) + b"\x05"

        # remove trailing seperator
        output = output[:-1]
        output += b"\x06"
        return output
    elif type(o) == str:
        return b'"' + o.encode() + b'"'
    else:
        return str(o).encode()

