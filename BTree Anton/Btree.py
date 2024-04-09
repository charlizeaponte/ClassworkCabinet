"""
A B-Tree Implementation in Python
4/8/24
Anton & Charlize
"""

import sys


class Node:
    def __init__(self, leaf: bool) -> None:
        self.isLeaf: bool = leaf
        self.keys: list[int] = []
        self.children: list[Node] = []

class BTree:
    def __init__(self, order: int) -> None:
        self.root: Node = Node(True)
        self.order: int = order

    def insert(self, num: int):
        root = self.root
        self.insertNum(root, num)
        # If the node is full
        if len(root.keys) == self.order:
            newParent = Node(False) # Create new parent node
            self.root = newParent # The parent node is the new root
            newParent.children.insert(0, root) # The parent node inherits the full node as a left child
            self.splitChild(newParent, 0, False) # We have to split the new parent node

    def insertNum(self, node: Node, num: int):
        i = len(node.keys) - 1 # Get index of last element in current node
        # If node is leaf, find where to place number
        if node.isLeaf:
            node.keys.append(None)
            while i >= 0 and num < node.keys[i]:
                node.keys[i + 1] = node.keys[i]
                i -= 1
            node.keys[i + 1] = num
        # Else
        else:
            # Find which child node to place the number in
            while i >= 0 and num < node.keys[i]:
                i -= 1
            i += 1
            # if num > node.keys[i]:
            #     i += 1
            self.insertNum(node.children[i], num) # Insert into the child node
            # Check if child node is full
            if (len(node.children[i].keys) == self.order):
                self.splitChild(node, i) # Split if so
    
    def splitChild(self, newParent: Node, i: int, test=False):
        childToSplit = newParent.children[i] # Get the node to split
        newNode = Node(childToSplit.isLeaf) # Make a right child
        newParent.children.insert(i + 1, newNode) # Insert the right child node to the parent
        newParent.keys.insert(i, childToSplit.keys[self.order//2]) # the key to insert is the middle value of the full node
        newNode.keys = childToSplit.keys[self.order//2 + 1:] # The right child gets all values to the right of the middle values
        # print(childToSplit.keys)
        if test:
            childToSplit.keys = childToSplit.keys[:self.order//2-1] # The left child inherits all other keys
        else:
            childToSplit.keys = childToSplit.keys[:self.order//2]
        # print(childToSplit.keys)
        # If the child is not a leaf, then the new node inherits children, and the original node inherits all other children.
        if not childToSplit.isLeaf:
            newNode.children = childToSplit.children[self.order//2 + 1:]
            childToSplit.children = childToSplit.children[:self.order//2]
            # print(newNode.children)
            # print(childToSplit.children)
    
    def print(self, x, l=0):
        print("Level ", l, " ", len(x.keys), end=":")
        for i in x.keys:
            print(i, end=" ")
        print()
        l += 1
        if len(x.children) > 0:
            for i in x.children:
                self.print(i, l)
    
    def search(self, num: int, node: Node=None) -> int:
        node = self.root if node == None else node
        i = 0
        nodesChecked = 1
        while i < len(node.keys) and num > node.keys[i]:
            i += 1
        if i < len(node.keys) and num == node.keys[i]:
            return nodesChecked
        elif node.isLeaf:
            return nodesChecked
        else:
            return nodesChecked + self.search(num, node.children[i])



if __name__ == '__main__':
    order = int(sys.argv[1])
    numNums = int(sys.argv[2])
    nums = []
    for i in range(numNums):
        nums.append(int(sys.argv[3 + i]))
    numSearches = int(sys.argv[3 + numNums])
    search = []
    for i in range(numSearches):
        search.append(int(sys.argv[3 + numNums + i]))
    
    tree = BTree(order)
    for num in nums:
        tree.insert(num)
        # tree.print(tree.root)
        # input()

    # tree.print(tree.root)
    
    for num in search:
        print(tree.search(num))




