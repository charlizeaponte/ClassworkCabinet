from queue import PriorityQueue, Queue
from dataclasses import dataclass, field
from typing import Any
import json

# Enter the content to encode to "string.txt", an example is preloaded
# Encoded content can then be decoded (run program again)

mode = input("Choose: 1 - encode, 2 - decode\n")    # Encode or decode?

if mode == "1":

    # Doing type setting for priority queue
    @dataclass(order=True)
    class PrioritizedItem:
        priority: int
        item: Any=field(compare=False)

    # Class for Huffman Tree
    class SubTree:
        def __init__(self, freq, left=None, right=None):
            self.freq = freq
            self.left = left
            self.right = right

    text = open("string.txt").read()                    # Open the string file
    freq = { }                                          # Create frequency map
    data = PriorityQueue()                              # Initialize priority queue

    # Get frequencies
    for char in text:
        if char in freq.keys():
            freq[char] += 1
        else:
            freq[char] = 1

    # Populate priority queue
    for key, value in freq.items():
        data.put(PrioritizedItem(value, key))

    # Create tree
    while(data._qsize() != 1):

        # Get two smallest values (subtree or not)
        left = data.get()
        right = data.get()

        join = SubTree(left.priority + right.priority, left.item, right.item)
        data.put(PrioritizedItem(join.freq, join))

    # Access root of the tree
    final = data.get().item
    bfs = Queue()
    bfs.put((final, ""))
    decoded = {}

    # Find all binary representations of each letter
    while bfs.qsize() != 0:
        node = bfs.get()
        if type(node[0].left) == str:
            cur = f'{node[1]}0'
            decoded[node[0].left] = cur
        else:
            bfs.put((node[0].left, f'{node[1]}0'))
        if type(node[0].right) == str:
            cur = f'{node[1]}1'
            decoded[node[0].right] = cur
        else:
            bfs.put((node[0].right, f'{node[1]}1'))

    # Encode and write to new file
    encrpt = open("encode.txt", 'w')
    decoded_string = ""
    for char in text:
        decoded_string = f'{decoded_string}{decoded[char]}'
    encrpt.write(f'{json.dumps(decoded)}\n{decoded_string}')
elif mode == "2":

    codes = open("encode.txt").readlines()                          # Open encoded file
    encoded = codes[1]                                              # Store encoded string
    codes = dict(json.loads(codes[0]))                              # Store frequency table
    codes = dict([(value, key) for key, value in codes.items()])    # Swap keys with values for faster lookup
    
    # Two pointer approach to decoding
    leftP = 0
    rightP = 1
    decoded = ""
    while rightP <= len(encoded):
        if encoded[leftP:rightP] in codes:
            decoded = f'{decoded}{codes[encoded[leftP:rightP]]}'
            leftP = rightP
            rightP += 1
        else:
            rightP += 1

    final = open("decoded.txt", 'w')
    final.write(decoded)
else:
    print("Must input either 1 or 2")