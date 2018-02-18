from json import dumps

order = {
    "event" : [
        "type",
        "id",
        "title",
        "creator",
        "time-start",
        "time-end",
        "latitude",
        "longitude",
        "participants"
    ]
}

for k in order:
    order[k].sort()

def normalize(o):
    if type(o) == dict:
        output = b"\x01"

        for key in order[o["type"]]:
            output += normalize(o[key]) + b"\x02"

        # remove trailing seperator
        output = output[:-1]
        output += b"\x03"
        return output
    elif type(o) == list:
        output = b"\x04"

        if len(o) > 0:
            if type(o[0]) == dict:
                o.sort(key=lambda x: x["id"])
            elif type(o[0]) == int:
                o.sort()

            for v in o:
                output += normalize(v) + b"\x05"

            # remove trailing seperator
            output = output[:-1]

        output += b"\x06"
        return output
    elif type(o) == str:
        # cut off quotation marks after
        # json serialization
        return dumps(o)[1:-1].encode()
    else:
        return str(o).encode()

