# binarysearchtree
Full Java implementation of a binary search tree data structure.


This implementation provides guaranteed O(H) retrieval time. 


Methods:
| Modifier and Type | Method                                     | Description                                                                                                                                                                                                              |
|-------------------|--------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| boolean           | add(E e)                                   | Adds the specified element to this set if it is not already present.                                                                                                                                                     |
| boolean           | addAll(Collection<? extends E> collection) | Adds all of the elements in the specified collection to this tree.                                                                                                                                                       |
| E                 | ceiling(E e)                               | Returns the least element in this tree greater than or equal to the given element, or null if there is no such element.                                                                                                  |
| void              | clear()                                    | Removes all of the elements from this set.                                                                                                                                                                               |
| boolean           | contains(Object o)                         | Returns true if this set contains the specified element.                                                                                                                                                                 |
| boolean           | containsAll(Collection<?> c)               | Returns true if this collection contains all of the elements in the specified collection.                                                                                                                                |
| boolean           | equals(Object obj)                         | Compares the specified object with this tree for equality.                                                                                                                                                               |
| E                 | first()                                    | Returns the first (lowest) element currently in this tree.                                                                                                                                                               |
| E                 | floor(E e)                                 | Returns the greatest element in this set less than or equal to the given element, or null if there is no such element.                                                                                                   |
| E                 | get(int index)                             | Returns the element at the specified position in this tree. The order of the indexed elements is the same as provided by this tree's iterator. The indexing is zero based (i.e., the smallest element in this tree is at index 0 and the largest one is at index size()-1). This operation is O(H).                                                                                                                                                             |
| ArrayList<E>      | getRange(E fromElement, E toElement)       | Returns a collection whose elements range from fromElement, inclusive, to toElement, inclusive. This operation is O(M) where M is the number of items returned.                                                                                                                          |
| int               | height()                                   | Returns the height of this tree.                                                                                                                                                                                         |
| E                 | higher(E e)                                | Returns the least element in this tree strictly greater than the given element, or null if there is no such element.                                                                                                     |
| boolean           | isBalanced()                               | Returns true if this tree is balanced based on the AVL tree balancing requirements (i.e., for every node, the difference in height between its two sub-trees is at most 1).                                              |
| boolean           | isEmpty()                                  | Returns true if this set contains no elements.                                                                                                                                                                           |
| boolean           | isFull()                                   | Returns true if this tree is a full tree (i.e., a binary tree in which each node has either two children or is a leaf).                                                                                                  |
| Iterator<E>       | iterator()                                 | Returns an iterator over the elements in this tree in ascending order.                                                                                                                                                   |
| E                 | last()                                     | Returns the last (highest) element currently in this tree.                                                                                                                                                               |
| E                 | lower(E e)                                 | Returns the greatest element in this set strictly less than the given element, or null if there is no such element.                                                                                                      |
| Iterator<E>       | postorderIterator()                        | Returns an iterator over the elements in this tree in order of the postorder traversal.                                                                                                                                  |
| Iterator<E>       | preorderIterator()                         | Returns an iterator over the elements in this tree in order of the preorder traversal.                                                                                                                                   |
| boolean           | remove(Object o)                           | Removes the specified element from this tree if it is present.                                                                                                                                                           |
| int               | size()                                     | Returns the number of elements in this tree.                                                                                                                                                                             |
| Object[]          | toArray()                                  | This function returns an array containing all the elements returned by this tree's iterator, in the same order, stored in consecutive elements of the array, starting with index 0.                                      |
| String            | toString()                                 | Returns a string representation of this tree.                                                                                                                                                                            |
| String            | toStringAllMaxPaths()                      | Produces a string representation of this tree that contains, one per line, every path from the root of this tree to a leaf node in the tree whose length is maximal (i.e., whose length matches the height of the tree). |
| String            | toStringAllPaths()                         | Produces a string representation of this tree that contains, one per line, every path from the root of this tree to a leaf node in the tree.                                                                             |
| String            | toStringTreeFormat()                       | Produces tree like string representation of this tree.                                                                                                                                                                   |
