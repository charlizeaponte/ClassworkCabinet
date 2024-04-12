"""
Anton & Charlize
4/11/24
Decompression Challenge
"""

# Format the input
ask = input("Massive Text Challenge? (Y/N): ")
massiveText: bool = True if ask == "Y" else False
numLines: int = int(input("Lines to decompress: "))
lines: list[list[str]] = []
i = 1
while numLines != 0:
    lines.append(input(f"Line {i}: ").split(" "))
    i += 1
    numLines -= 1

# Decompress (massive)
if massiveText:
    for line in lines:
        info = [[0, line[0], None, None]]
        """An array that for each instruction stores [length of current decompressed string at this instruction, suffix for this instruction, prefix substring start index, prefix substring end index]"""
        freq = {}
        """A dictionary to store frequencies of each letter"""

        # Initialize freqency dict
        for letter in line[0]:
            freq[letter] = freq.get(letter, 0) + 1

        # Go through each instruction
        index = 1
        curLength = len(line[0])
        stringLookup = ""
        while index != len(line):
            s = int(line[index])
            l = s + int(line[index+1])
            t = line[index+2]
            index += 3

            # Find the instruction index to work back from to reconstruct the substring
            instructToUse: int
            for i, instruction in enumerate(info):
                if instruction[0] < l:
                    instructToUse = i
                elif instruction[0] == l:
                    instructToUse = i
                    break
                else:
                    break
            
            # Reconstruct the substring, efficiently
            substring: str = info[0][1]
            replaced = False
            for index2 in range(1, instructToUse+1, 1):
                if info[index2][2] <= len(stringLookup):
                    if replaced == False:
                        substring = stringLookup
                    replaced = True
                    continue
                substring += substring[info[index2][2]:info[index2][3]] + info[index2][1]
            
            for letter in substring[s:l] + t:
                freq[letter] = freq.get(letter, 0) + 1
            
            newString = substring + substring[s:l] + t
            if len(newString) > len(stringLookup):
                stringLookup = newString

            info.append([curLength, t, s, l])
            curLength += l + len(t)
            print(f"{int((index / len(line))*100)}% decoded...")
        
        # Debug
        for a, b in freq.items():
            print(a, b)


# Decompress (non-massive)
else:
    for line in lines:
        string = line[0]
        index = 1
        while index != len(line):
            s = int(line[index])
            l = s + int(line[index+1])
            t = line[index+2]
            index += 3
            string += string[s:l] + t
        print(string)