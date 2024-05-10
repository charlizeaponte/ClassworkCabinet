"""
Charlize & Anton
New BTree code (restarted) w/ edits from comments
05/03/2024
"""

import bisect
 
class BTreeNode:
   
    def __init__(self, order, leaf):
        self.keys = []
        self.order = order
        self.children: list[BTreeNode] = []
        self.leaf = leaf
 
    def split(self):
        """
        Splits the node it is called on.
        """
       
        newEntry = BTreeNode(self.order, self.leaf)         # Our "new entry" is our new right child
        val = self.keys[self.order // 2]                    # The value to split this node at is the midpoint CHANGE
        newEntry.keys = self.keys[(self.order // 2) + 1:]   # Our new node is our new right child, inheriting the larger keys CHANGE
        self.keys = self.keys[:(self.order // 2)]           # The splitting node becomes the left child, inheriting the smaller keys CHANGE
        if not self.leaf:                                   # If the node we are splitting is an internal node
            newEntry.children = self.children[(self.order // 2) + 1:]  # Our new right child gets the existing greater children CHANGE
            self.children = self.children[:(self.order // 2) + 1]      # Our splitting node (left), inherits the smaller children
             
        return val, newEntry                                # Return the value we split on and the new right node
 
    def insert(self, new_key):
        """
        The function to insert a key into a node. 
        Returns a tuple (value, newEntry)
        """
        newEntry = None                                     # We do not need to create a new entry to the tree

        if not self.leaf:                                   # This function always started at the root, is it a leaf?
            i = bisect.bisect_left(self.keys, new_key)      # If so, find the index we would theoretically insert the value
            newEntry = self.children[i].insert(new_key)     # Knowing this is not a leaf, we know at this index there is a node,
                                                            # so call insert again!
            if newEntry is not None:                        # If we are splitting a node (and have a new entry to insert)   
               
                if len(self.keys) < self.order - 1:             # If we have space, insert the new entry normally CHANGE    
                    self.keys.insert(i, newEntry[0])        # Else insert the key value the node was split on into the node
                    self.children.insert(i+1, newEntry[1])  # insert the new entry (right child) into the corresponding child slot
                    newEntry = None                         # Set it to none 
                else:
                    self.keys.insert(i, newEntry[0])        # Otherwise insert the new key
                    self.children.insert(i+1, newEntry[1])  # ... and the child
                    newEntry = self.split()                 # and then split
        else:                                               # Eventually, we will get to a leaf node
            i = bisect.bisect_left(self.keys, new_key)      # If so, we find which index to place our value in
            self.keys.insert(i, new_key)                    # Due to recursive nature, self will always be the node we're searching 
            if len(self.keys) == self.order:                # If this insertion results in a full node, split it! CHANGE
                newEntry = self.split()
         
        return newEntry
 
    def makeNewRoot(self, val, newEntry):
        """
        Makes a new root node with the value as a key.
        The node the function is called in becomes the left child and the node given to the function becomes the right child.
        """
        root = BTreeNode(self.order, False)
        root.keys.append(val)
        root.children.append(self)
        root.children.append(newEntry)
        return root

    def search(self, val, checkNum):
        """
        Search for the key in the given node
        """
        if val in self.keys:                        # If our value is here, print the number of checks
            print(checkNum)                        
        else:
            if self.leaf:                           # Else it could be a leaf node, in which case the value doesnt exist, but still print checks
                print(checkNum)
            else:                                   # Else recursively search the child node
                i = bisect.bisect_left(self.keys, val)
                self.children[i].search(val, checkNum+1)
 
class BTree:
    """A BTree"""
    def __init__(self, order):
        self.order = order
        self.root = BTreeNode(order, True)
 
    def insert(self, key):
        newEntry = self.root.insert(key)                                # Call the insert using the root
         
        if newEntry is not None:                                        # If a new node is required to the root of the tree
            self.root = self.root.makeNewRoot(newEntry[0], newEntry[1]) # Set the root to the node returned by this function

    def search(self, key):
        self.root.search(key, 1)

if __name__ == "__main__":

    # Gather input
    order = int(input("Order of the B Tree: "))
    nums = int(input("How many numbers are present?: "))
    values = []
    while nums != 0:
        values.append(int(input("Enter number: ")))
        nums -= 1
    numsSearch = int(input("How many numbers to search for?: "))
    valuesSearch = []
    while numsSearch != 0:
        valuesSearch.append(int(input("Enter number to search: ")))
        numsSearch -= 1
    
    # Build Tree
    tree = BTree(order)
    for number in values:
        tree.insert(number)
    
    # Search values
    for number in valuesSearch:
        tree.search(number)