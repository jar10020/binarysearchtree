/**
 * This is an implementation of a binary search tree.
 * Items can be added, removed, etc.
 * 
 * @author Jackson Reinhart
 * @version 4/26/2022
 */

package project4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class BST<E extends Comparable<E>> implements Iterable<E>{
	
	private Node root; //reference to root node of the tree
	private int size = 0; //keeps track of the amount of nodes in the tree
	
	
	/**
	 * Constructs a new, empty tree, sorted according to the natural ordering of its elements.
	 * All elements inserted into the tree must implement the Comparable interface.
	 */
	public BST() {
		root = null;
	}
	
	/**
	 * Constructs a new tree containing the elements in the specified collection,
	 * sorted according to the natural ordering of its elements.
	 * All elements inserted into the tree must implement the Comparable interface.
	 *
	 * @param collection Collection to create the tree out of
	 * @throws NullPointerException if the given set is null
	 */
	public BST(E[] collection) throws NullPointerException {
		if(collection == null) 
			throw new NullPointerException("Given set cannot be null");
		//guarantee that it is o(n log n) by sorting the array
		Arrays.sort(collection);
		for(E item : collection) {
			add(item);
		}
	}
	
	/**
	 * Adds the specified element to this set if it is not already present.
	 * More formally, adds the specified element e to this tree if the set contains
	 * no element e2 such that Objects.equals(e, e2). If this set already contains
	 * the element, the call leaves the set unchanged and returns false.
	 * 
	 * @param e element to be added to this set
	 * @return true if this set did not already contain the specified element
	 * @throws NullPointerException if the specified element is null and this set uses
	 * 		natural ordering, or its comparator does not permit null elements
	 */
	public boolean add(E e) throws NullPointerException {
		if(e == null)
			throw new NullPointerException("Cannot input null element");
		
		if(root == null) { //if this is the first node to be added
			root = new Node(e); //make the root the first item
			size++;
			return true;
		}
		return add(e, root); //otherwise, recursive add
	}
	
	private boolean add(E e, Node curRoot) {
		//compare
		int comp = e.compareTo(curRoot.data);
		
		//left node if smaller
		if(comp < 0) {
			if(curRoot.left == null) {
				curRoot.left = new Node(e);
				if(curRoot.right == null || curRoot.left.height >= curRoot.right.height) {
					curRoot.height = curRoot.left.height + 1;
				}
				updateNodeSize(curRoot);
				updateLeftSize(curRoot);
				size++;
				return true;
			}
			
			boolean added = add(e, curRoot.left); //we recurse
			if(added) {
				//update heights: if there is no right node, or if the left node's height is now
				//larger than the right node's, set this node's height to the left's height plus one
				if(curRoot.right == null || curRoot.left.height >= curRoot.right.height) {
					curRoot.height = curRoot.left.height + 1;
				}
				updateNodeSize(curRoot);
				updateLeftSize(curRoot);
			return added;
			}
			
		} else if(comp > 0) { //right node if bigger
			if(curRoot.right == null) {
				curRoot.right = new Node(e);
				if(curRoot.left == null || curRoot.left.height <= curRoot.right.height) {
					curRoot.height = curRoot.right.height + 1;
				}
				updateNodeSize(curRoot);
				updateLeftSize(curRoot);
				size++;
				return true;
			}
			boolean added = add(e, curRoot.right);
			if(added) {
				//update heights: if there is no left node, or if the right node's height is larger
				//than the left node's, set this node's height to it plus one
				if(curRoot.left == null || curRoot.left.height <= curRoot.right.height) {
					curRoot.height = curRoot.right.height + 1;
				}
				updateNodeSize(curRoot);
				updateLeftSize(curRoot);
			}
			return added;
		}
		return false; //will only be reached if the item is a duplicate
	}
	
	
	/**
	 * Returns the element at the specified position in this tree. The order of the indexed
	 * elements is the same as provided by this tree's iterator. The indexing is zero based
	 * (i.e., the smallest element in this tree is at index 0 and the largest one is at
	 * index size()-1).
	 * 
	 * @param index Index of the element to return
	 * @return Element at given index
	 * @throws IndexOutOfBoundsException If index is negative or larger than the tree size
	 */
	public E get(int index) throws IndexOutOfBoundsException {
		if(index < 0 || index >= size) //range check 
			throw new IndexOutOfBoundsException("Index cannot be negative or exceed tree size");
		
		return get(index, root); //recurse
	}
	//recursive
	private E get(int index, Node curRoot) {
		if(index == curRoot.leftSize) //base case: if index matches
			return curRoot.data;
		if(index < curRoot.leftSize) //otherwise, if index is less, move left
			return get(index, curRoot.left);
		if(index > curRoot.leftSize) //when we reach the point where the index
			//is smaller, we know the item is at index - left size - 1 of the right subtree
			return get(index - curRoot.leftSize - 1, curRoot.right);
		return null; //unreachable
	}
	
	/**
	 * Private method to maintain nodes' subtrees' size records
	 * Updates given nodes' sizes by summing the sizes of their
	 * children and adding 1 (because it's inclusive)
	 * 
	 * @param n Node to update
	 */
	private void updateNodeSize(Node n) {
		if(n.left == null && n.right == null)
			n.size = 1; //base case: if no children, zero size
		
		int s = 0; //helper var 
		if(n.left != null)
			s += n.left.size; //if a left node exists, add its size to s
		if(n.right != null)
			s += n.right.size; //if a right node exists, add its size to s
		
		n.size = s + 1; //size is sum of left and right sizes plus one
	}
	
	/**
	 * Private method to update the left size variable in nodes to
	 * keep track of their left subtree size. Sets it to the size of 
	 * the left child (therefore the left subtree).
	 * 
	 * @param n Node to update
	 */
	private void updateLeftSize(Node n) {
		if(n.left != null)
			n.leftSize = n.left.size; //leftsize is just the size of the left subtree
	}
	
	/**
	 * Adds all of the elements in the specified collection to this tree.
	 * 
	 * @param collection to be added
	 * @return true if this set changed as a result of the call
	 * @throws NullPointerException if the specified collection is null or
	 * 		if any element of the collection is null
	 */
	public boolean addAll(Collection<? extends E> collection) throws NullPointerException {
		for(E item : collection) {
			add(item);
		}
		return true; 
	}
	
	/**
	 * Returns true if this set contains the specified element.
	 * More formally, returns true if and only if this set contains
	 * an element e such that Objects.equals(o, e).
	 * 
	 * @param o Object to look for
	 * @return true if the tree already contains the object
	 * @throws ClassCastException if the specified object cannot be compared
	 * 		with the elements currently in the set
	 * @throws NullPointerException if the specified element is null and this
	 * 		set uses natural ordering, or its comparator does not permit null elements
	 */
	public boolean contains(Object o) throws ClassCastException, NullPointerException {
		//null check
		if(o == null)
			throw new NullPointerException("Given item cannot be null");
		
		return contains(o, root);
	}
	
	//recursive search
	private boolean contains(Object o, Node curRoot) throws ClassCastException {
		if(Objects.equals(o, curRoot.data)) //base case: if we find the element
			return true;
		
		if(curRoot.left == null && curRoot.right == null) {
			return false; //if we reach a leaf (end of the line) but haven't found it yet
		}
		
		//if we're not at a leaf and we haven't found it, keep looking, recurse down
		//get the comparison 
		@SuppressWarnings("unchecked")
		int comp = curRoot.data.compareTo((E) o);
		
		if(comp < 0) { //right
			if(curRoot.right != null) { //make sure we have a node to look at
				return contains(o, curRoot.right);
			} else {
				return false; //if we can't go right, we can't find it
			}
		} else if(comp > 0) { //left
			if(curRoot.left != null) { //make sure we have a node to look at
				return contains(o, curRoot.left);
			} else {
				return false; //if we can't go left anymore, we can't find it
			}
		}
		return false;
	}
	
	/**
	 * Returns true if this collection contains all of the elements in the specified
	 * collection. This implementation iterates over the specified collection, checking each
	 * element returned by the iterator in turn to see if it's contained in this tree. If all
	 * elements are so contained true is returned, otherwise false.
	 * 
	 * @param c Collection to compare
	 * @return true if this tree contains all of the elements in the specified collection
	 * @throws NullPointerException if the specified collection contains one or more null elements
	 * 		and this collection does not permit null elements, or if the specified collection is null.
	 */
	public boolean containsAll(Collection<?> c) throws NullPointerException {
		if(c == null) //null check
			throw new NullPointerException("Given collection cannot be null");
		
		for(Object item : c) { //iterate through each item in given array
			if(item == null) //null check each item
				throw new NullPointerException("Given collection cannot contain null items");
			if(contains(item) == false) //check for containment
				return false; //if any of them are not contained, return false
		}
		return true; //otherwise, return true is reached
	}
	
	/**
	 * Removes all of the elements from this set. The set will be empty
	 * after this call returns.
	 */
	public void clear() {
		size = 0;
		root = null;
	}
	
	/**
	 * Removes the specified element from this tree if it is present. More formally,
	 * removes an element e such that Objects.equals(o, e), if this tree contains such
	 * an element. Returns true if this tree contained the element (or equivalently, if
	 * this tree changed as a result of the call). (This tree will not contain the
	 * element once the call returns.)
	 * 
	 * @param o Object to be removed
	 * @return true if item is removed
	 * @throws ClassCastException if the specified object cannot be compared with the
	 * 		elements currently in this tree
	 * @throws NullPointerException if the specified element is null
	 */
	public boolean remove(Object o) throws ClassCastException, NullPointerException {
		if(o == null) //null check
			throw new NullPointerException("Given element cannot be null");
		
		if(isEmpty())
			return false;
		
		return remove(o, root, null); //call recursion from the root
	}
	
	//recursive
	private boolean remove(Object o, Node curRoot, Node parent) throws ClassCastException {
		
		if(curRoot == null) { //if we've fallen off the tree
			return false;
		}
		
		//base case: if we've found the target object
		if(Objects.equals(o, curRoot.data)) {
			
			//if it's a node with two children
			if(curRoot.left != null && curRoot.right != null) {
				
				//edge case: if left child is predecessor
				if(curRoot.left.right == null) {
					curRoot.data = curRoot.left.data; //replace the target node with predecessor
					curRoot.left = null; //delete predecessor
				} else {
					//normal scenario: predecessor is somewhere down the tree
					Node predecessor = curRoot.left; //go left one
					
					while(predecessor.right != null) {
						predecessor = predecessor.right; //go right until you can't anymore
					}
					
					//now that we have the predecessor, we must replace and delete
					curRoot.data = predecessor.data;
					remove(predecessor.data, curRoot.left, curRoot);
				}
				
			} else {
				//edge case: if this is the root of the tree
				if(parent == null) {
					root = null;
					size--;
					return true;
				}
				
				//check to see if this is the left or right child of the parent
				boolean isLeftChild = (curRoot == parent.left);
				
				//if it's a leaf
				if(curRoot.left == null && curRoot.right == null) {
					
					if(curRoot == root) { //edge case: if leaf is the root of a size 1 tree
						root = null;
						size--;
						return true;
					}
					
					if(isLeftChild) { //if it's the left child
						parent.left = null; //disconnect
					} else { //if it's the right child
						parent.right = null; //disconnect
					}
				}
				
				//if it's a node with only one child
				//if it has only a left child
				if(curRoot.left != null && curRoot.right == null) {
					//set that left child to the left or right child of the parent,
					//depending on the former position of the deleted node
					if(isLeftChild) {
						parent.left = curRoot.left;
					} else {
						parent.right = curRoot.left;
					}
				//if it has only a right child
				} else if(curRoot.right != null && curRoot.left == null) {
					//set that right child to the left or right child of the parent,
					//depending on the former position of the deleted node
					if(isLeftChild) {
						parent.left = curRoot.right;
					} else {
						parent.right = curRoot.right;
					}
				}
			
			}
			if(parent != null)
				parent.height--;
			size--;
			return true;
			
		//recursing to the node
		} else {
			int comp = curRoot.data.compareTo((E) o);
			
			if(comp < 0) { //right
				if(remove(o, curRoot.right, curRoot)) {
					updateHeight(curRoot);
					updateNodeSize(curRoot);
					updateLeftSize(curRoot);
					
					return true;
				}
			} else if(comp > 0) { //left
				if(remove(o, curRoot.left, curRoot)) {
					
					//every node that executes this is guaranteed affected by a deletion
					updateHeight(curRoot);
					updateNodeSize(curRoot);
					updateLeftSize(curRoot);
					
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * Private method that updates heights of individual nodes
	 * 
	 * @param n Node to be updated
 	 */
	private void updateHeight(Node n) {
		//if a leaf
		if(n.left == null && n.right == null)
			n.height = 1;
		
		int heightLeft;
		int heightRight;
		//use helper variables to have height so we don't have to risk trying to access nulls
		if(n.left == null) {
			heightLeft = 0;
		} else {
			heightLeft = n.left.height;
		}
		if(n.right == null) {
			heightRight = 0;
		} else {
			heightRight = n.right.height;
		}
		//simply set the height to the maximum child height + 1
		n.height = Math.max(heightLeft, heightRight) + 1;
	}
	
	/**
	 * Returns the number of elements in this tree.
	 * 
	 * @return number of elements in this tree
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Returns the height of this tree. The height of a leaf is 1. The height of
	 * the tree is the height of its root node.
	 * 
	 * @return the height of this tree or zero if the tree is empty
	 */
	public int height() {
		return root.height; //height of root is height of tree
	}
	
	/**
	 * Returns true if this set contains no elements.
	 * 
	 * @return true if this set contains no elements.
	 */
	public boolean isEmpty() {
		return root == null;
	}
	
	/**
	 * Returns true if this tree is a full tree (i.e., a binary
	 * tree in which each node has either two children or is a leaf).
	 * 
	 * @return true if this tree is a full tree
	 */
	public boolean isFull() {
		if(isEmpty())
			return true;
		return isFull(root);
	}
	//recursive
	private boolean isFull(Node curRoot) {
		if(curRoot.height == 1) {
			return true; //if it's a leaf, return true
		} else if(curRoot.left != null  && curRoot.right != null) {
			//if the left and right nodes are both not null
			if(isFull(curRoot.left) && isFull(curRoot.right))
				return true; //recurse onto left and then right nodes
		}
		return false; //else return false
	}
	
	/**
	 * Returns true if this tree is balanced based on the AVL tree balancing requirements
	 * (i.e., for every node, the difference in height between its two sub-trees is at most 1).
	 * 
	 * @return true if this tree is balanced
	 */
	public boolean isBalanced() {
		if(isEmpty())
			return true;
		return isBalanced(root);
	}
	//recursive
	private boolean isBalanced(Node curRoot) {
		
		if(curRoot == null) //if we've reached a leaf
			return true;
		
		//helper variables so we can safely fall off the tree
		int leftChildHeight;
		int rightChildHeight;
		
		//null check the children and update the helper variables so we can
		//get left and right height even if left or right subtrees are empty
		//(which we treat as a height of zero)
		if(curRoot.left == null) {
			leftChildHeight = 0;
		} else {
			leftChildHeight = curRoot.left.height;
		}
		
		if(curRoot.right == null) {
			rightChildHeight = 0;
		} else {
			rightChildHeight = curRoot.right.height;
		}
		
		//ensure we are only checking full nodes
		//this node is balanced if the difference between left height and right height is one or 
		boolean thisNodeBalanced = !(Math.abs(leftChildHeight - rightChildHeight) > 1);
		//return true if both children are also balanced, false if any of them aren't
		return (isBalanced(curRoot.left) == true && isBalanced(curRoot.right) == true && thisNodeBalanced == true);
		//} else {
			//return true; //reached if leaf or has only one child, in which case it is balanced
		//}
	}
	
	/**
	 * Compares the specified object with this tree for equality. Returns true if the
	 * given object is also a tree, the two trees have the same size, and every member
	 * of the given tree is contained in this tree.
	 * 
	 * @return true if the specified object is equal to this tree
	 */
	public boolean equals(Object obj) {
		//if given object is not a BST
		if(!(obj instanceof BST<?>))
			return false;
		
		//cast given object as a BST
		@SuppressWarnings("unchecked")
		BST<E> treeObj = (BST<E>) obj;
		//check size for equality
		if(treeObj.size() != this.size())
			return false;
		//check each node for equality
		if(!compareTreeNodes(root, treeObj.root))
			return false;
		//if we reach this point, everything works, return true
		return true;
	}
	/**
	 * Private method for equals(Object obj) that returns true if a tree's
	 * objects are identical to another given tree
	 * 
	 * @param n1 Root of tree 1
	 * @param n2 Root of tree 2
	 * @return True if both trees have identical elements
	 */
	private boolean compareTreeNodes(Node n1, Node n2) {
		//if both nodes are null, true
		if(n1 == null && n2 == null)
			return true;
		//else, compare: if they're both non-null (we haven't fallen off the tree)
		//and they have matching data, recurse left and then right and do the same
		//and if all children are equal, return true.
		return (n1 != null && n2 != null) && (n1.data.equals(n2.data)) &&
				compareTreeNodes(n1.left, n2.left) &&
				compareTreeNodes(n1.right, n2.right);
	}
	
	/**
	 * Returns the first (lowest) element currently in this tree.
	 * 
	 * @return the first (lowest) element in this tree
	 * @throw NoSuchElementException if tree is empty
	 */
	public E first() throws NoSuchElementException {
		if(isEmpty()) //empty check
			throw new NoSuchElementException("Tree is empty"); //recurse
		
		return first(root); //recurse
	}
	//recursive
	private E first(Node curRoot) {
		if(curRoot.left == null) { //if we've gone as left as possible
			return curRoot.data;
		} else { //otherwise keep going left
			return first(curRoot.left);
		}
	}
	
	/**
	 * Returns the last (highest) element currently in this tree.
	 * 
	 * @return last (highest) element in this tree
	 * @throws NoSuchElementException if tree is empty
	 */
	public E last() throws NoSuchElementException {
		if(isEmpty()) //empty check
			throw new NoSuchElementException("Tree is empty");
		
		return last(root); //recurse
	}
	//recursive
	private E last(Node curRoot) {
		if(curRoot.right == null) { //base case: if we have gone as right as possible, return
			return curRoot.data;
		} else { //otherwise, keep going right
			last(curRoot.right);
		}
		return curRoot.data; //unreachable
	}
	
	/**
	 * Returns the greatest element in this set strictly less than the
	 * given element, or null if there is no such element.
	 * 
	 * @param e the value to match
	 * @return the greatest element less than e, or null if there is no such element
	 * @throws ClassCastException if the specified element cannot be compared with the
	 * 		elements currently in the set
	 * @throws NullPointerException if the specified element is null
	 */
	public E lower(E e) throws ClassCastException, NullPointerException {
		if(e == null) //null check
			throw new NullPointerException("Given element cannot be null");
		
		return lower(e, root);
	}
	//recursive
	private E lower(E e, Node curRoot) {
		//if we fall off the tree on the way there, there is no ceiling element, return null
		if(curRoot == null)
			return null;
		
		//compare node with e
		boolean nodeIsGreaterThanOrEqualToTarget = e.compareTo(curRoot.data) <= 0;
		//if node is less than e, we go left
		if(nodeIsGreaterThanOrEqualToTarget) {
			return lower(e, curRoot.left);
		}
		
		//if the lower is not in the left subtree, it is in the right subtree or current node
		E rightResult = lower(e, curRoot.right);
		//stop leftResult from being null
		if(rightResult == null)
			return curRoot.data;
		//compare rightResult with e
		boolean rightResultIsLess = rightResult.compareTo(e) < 0;
		if(rightResultIsLess) {
			return rightResult;
		} else {
			return curRoot.data;
		}
	}
	
	/**
	 * Returns the least element in this tree strictly greater than the given element,
	 * or null if there is no such element.
	 * 
	 * @param e the value to match
	 * @return the least element greater than e, or null if there is no such element
	 * @throws ClassCastException if the specified element cannot be compared with the
	 * 		elements currently in the set
	 * @throws NullPointerException if the specified element is null
	 */
	public E higher(E e) throws ClassCastException, NullPointerException {
		if(e == null) //null check
			throw new NullPointerException("Given element cannot be null");
		
		return higher(e, root);
	}
	//recursive
	private E higher(E e, Node curRoot) {
		//if we fall off the tree on the way there, there is no ceiling element, return null
		if(curRoot == null)
			return null;
		
		//compare node with e
		boolean nodeIsLessThanOrEqualToTarget = e.compareTo(curRoot.data) >= 0;
		//if node is less than e, we go right
		if(nodeIsLessThanOrEqualToTarget) {
			return higher(e, curRoot.right);
		}
		
		//if the higher is not in the right subtree, it is in the left subtree or current node
		E leftResult = higher(e, curRoot.left);
		//stop leftResult from being null
		if(leftResult == null)
			return curRoot.data;
		//compare leftResult with e
		boolean leftResultIsLarger = leftResult.compareTo(e) > 0;
		if(leftResultIsLarger) {
			return leftResult;
		} else {
			return curRoot.data;
		}
	}
	
	/**
	 * Returns the least element in this tree greater than or equal to the given element,
	 * or null if there is no such element.
	 * 
	 * @param e element to compare
	 * @return the least element in this tree greater than or equal to the given element
	 * @throws ClassCastException if the specified element cannot be compared with the
	 * 		elements currently in the set
	 * @throws NullPointerException if the specified element is null
	 */
	public E ceiling(E e) throws ClassCastException, NullPointerException {
		if(e == null) //null check
			throw new NullPointerException("Given element cannot be null");
		
		return ceiling(e, root);
	}
	//recursive
	private E ceiling(E e, Node curRoot) {
		//if we fall off the tree on the way there, there is no ceiling element, return null
		if(curRoot == null)
			return null;
		//if we found a match, we have our ceiling
		if(curRoot.data.equals(e))
			return curRoot.data;
		
		//compare node with e
		boolean nodeIsLessThanTarget = e.compareTo(curRoot.data) > 0;
		//if node is less than e, we go right
		if(nodeIsLessThanTarget) {
			return ceiling(e, curRoot.right);
		}
		
		//if the ceiling is not in the right subtree, it is in the left subtree or current node
		E ceil = ceiling(e, curRoot.left);
		//stop ceil from being null
		if(ceil == null)
			return curRoot.data;
		//compare ceil with e
		boolean ceilIsLargerOrEqual = ceil.compareTo(e) >= 0;
		if(ceilIsLargerOrEqual) {
			return ceil;
		} else {
			return curRoot.data;
		}
	}
	
	/**
	 * Returns the greatest element in this set less than or equal to the given
	 * element, or null if there is no such element.
	 * 
	 * @param e element to compare
	 * @return the greatest element less than or equal to e, or null if there is no such element
	 * @throws ClassCastException if the specified element cannot be compared with the
	 * 		elements currently in the set
	 * @throws NullPointerException if the specified element is null
	 */
	public E floor(E e) throws ClassCastException, NullPointerException {
		if(e == null) //null check
			throw new NullPointerException("Given element cannot be null");
		
		return floor(e, root);
	}
	//recursive
	private E floor(E e, Node curRoot) {
		//if we fall off the tree on the way there, there is no ceiling element, return null
		if(curRoot == null)
			return null;
		
		//if we found a match, we have our floor
			if(curRoot.data.equals(e))
				return curRoot.data;
		
		//compare node with e
		boolean nodeIsGreaterThanToTarget = e.compareTo(curRoot.data) < 0;
		//if node is less than e, we go left
		if(nodeIsGreaterThanToTarget) {
			return floor(e, curRoot.left);
		}
		
		//if the lower is not in the left subtree, it is in the right subtree or current node
		E rightResult = floor(e, curRoot.right);
		//stop leftResult from being null
		if(rightResult == null)
			return curRoot.data;
		//compare rightResult with e
		boolean rightResultIsLess = rightResult.compareTo(e) <= 0;
		if(rightResultIsLess) {
			return rightResult;
		} else {
			return curRoot.data;
		}
	}
	
	/**
	 * This function returns an array containing all the elements returned by this tree's iterator,
	 * in the same order, stored in consecutive elements of the array, starting with index 0.
	 * The length of the returned array is equal to the number of elements returned by the iterator.
	 * 
	 * @return an array, whose runtime component type is Object, containing all of the elements in this tree
	 */
	public Object[] toArray() {
		
		//make array of tree size
		Object[] arr = new Object[size];
		//get in order iterator
		Iterator<E> itr = iterator();
		int i = 0;
		
		while(itr.hasNext()) {
			arr[i] = itr.next();
			i++;
		}
		
		return arr;
	}
	
	/**
	 * Returns a collection whose elements range from fromElement, inclusive, to toElement,
	 * inclusive. The returned collection/list is backed by this tree, so changes in the
	 * returned list are reflected in this tree, and vice-versa (i.e., the two structures
	 * share elements. The returned collection should be organized according to the natural
	 * ordering of the elements (i.e., it should be sorted).
	 * 
	 * @param fromElement Lower bound of range
	 * @param toElement Higher bound of range
	 * @return a collection containing a portion of this tree whose elements range from
	 * 		fromElement, inclusive, to toElement, inclusive
	 * @throws NullPointerException if either fromElement or toElement are null
	 * @throws IllegalArgumentException if fromElement is larger than toElement
	 */
	public ArrayList<E> getRange(E fromElement, E toElement) throws NullPointerException, IllegalArgumentException {
		//null check
		if(fromElement == null || toElement == null)
			throw new NullPointerException("fromElement and toElement cannot be null");
		//comparing variable, >0 if fromElement is larger than toElement
		int elementComp = fromElement.compareTo(toElement);
		if(elementComp > 0)
			throw new IllegalArgumentException("fromElement cannot be larger than toElement");
		//recurse once all our checks are cleared
		return getRange(fromElement, toElement, root, new ArrayList<E>());
	}
	//recurse
	private ArrayList<E> getRange (E fromElement, E toElement, Node curRoot, ArrayList<E> list) {
		if(curRoot == null) {
			//if we fall off the tree, return empty list
			return list;
		}
		//compare bounds with current node's data
		int lowBoundComp = fromElement.compareTo(curRoot.data);
		int highBoundComp = toElement.compareTo(curRoot.data);
		
		if(lowBoundComp > 0) { //if we're below the lower bound, go right
			return getRange(fromElement, toElement, curRoot.right, list);
		} else if(highBoundComp < 0) { //if we're above the higher bound, go left
			return getRange(fromElement, toElement, curRoot.left, list);
		} else {
			//helper list variable to simplify our adding code
			ArrayList<E> localList = new ArrayList<E>();
			//if we are within the bounds, search the left subtree
			localList.addAll(getRange(fromElement, toElement, curRoot.left, list));
			//then, add this node's data
			localList.add(curRoot.data);
			//then search the right subtree
			localList.addAll(getRange(fromElement, toElement, curRoot.right, list));
			return localList; //return the whole list put together
		}
	}
	
	/**
	 * Returns a string representation of this tree. The string representation consists of a list of the tree's
	 * elements in the order they are returned by its iterator (inorder traversal), enclosed in square brackets
	 * ("[]"). Adjacent elements are separated by the characters ", " (comma and space). Elements are converted
	 * to strings as by String.valueOf(Object).
	 * 
	 * @return String representation of this tree, inorder
	 */
	@Override
	public String toString() {
		String rawStr = buildString(root); //build string
		rawStr = rawStr.substring(0, rawStr.length() - 2); //remove last comma and space
		return "[" + rawStr + "]"; //add brackets and return 
	}
	//recursive string building
	private String buildString(Node curRoot) {
		//if we've fallen off the tree
		if(curRoot == null)
			return "";
		
		String str = ""; //helper variable
		str += buildString(curRoot.left); //recurse to left, add that to string
		str += String.valueOf(curRoot.data) + ", "; //add this node in between left and right
		str += buildString(curRoot.right); //recurse to right
		return str; //return the string put together
	}
	
	/**
	 * Produces a string representation of this tree that contains, one per line,
	 * every path from the root of this tree to a leaf node in the tree. The order of
	 * the paths should be from left to right. In each path, the values in the nodes
	 * should be separated by a comma and a single space. The following multi-line string
	 *  K, D, B, A
	 *  K, D, J
	 *  K, P, M, L
	 *  K, P, M, O, N
	 * is a string representation of this tree:
	 *            K
	 *         /     \
	 *       D        P
	 *     /   \     /
	 *    B     J   M
	 *    /        /   \
	 *   A        L     O
	 *                 /
	 *                N
	 *
	 * @return string containing all root-leaf paths of this tree
	 */
	public String toStringAllPaths() {
		StringBuilder sb = new StringBuilder(); //string builder will build our string
		toStringAllPaths(root, "", sb); //recurse
		return sb.toString();
	}
	private String toStringAllPaths(Node curRoot, String pathStr, StringBuilder sb) {
		if(curRoot == null)
			return "";
		
		if(curRoot.left != null)
			//recurse, with the path now containing this node's data and ", "
			toStringAllPaths(curRoot.left, pathStr + String.valueOf(curRoot.data) + ", ", sb);
		if(curRoot.right != null)
			toStringAllPaths(curRoot.right, pathStr + String.valueOf(curRoot.data) + ", ", sb);
		//until we hit a leaf, in which case we can add this path to the string builder and
		//recurse back up to the next path
		if(curRoot.left == null && curRoot.right == null) {
			sb.append(pathStr += curRoot.data + "\n");
		}
		return "";
	}
	
	
	/**
	 * Produces a string representation of this tree that contains, one per line,
	 * every path from the root of this tree to a leaf node in the tree whose length
	 * is maximal (i.e., whose length matches the height of the tree). The order of
	 * the paths should be from left to right. In each path, the values in the nodes
	 * should be separated by a comma and a single space.
	 * 
	 * @return string containing all maximal root-leaf paths of this tree.
	 */
	public String toStringAllMaxPaths() {
		StringBuilder sb = new StringBuilder(); //string builder will build our string
		toStringAllMaxPaths(root, "", sb); //recurse
		return sb.toString();
	}
	//recursive
	private String toStringAllMaxPaths(Node curRoot, String pathStr, StringBuilder sb) {
		if(curRoot == null)
			return "";
		
		if(curRoot.left != null)
			//recurse, with the path now containing this node's data and ", "
			toStringAllMaxPaths(curRoot.left, pathStr + String.valueOf(curRoot.data) + ", ", sb);
		if(curRoot.right != null)
			toStringAllMaxPaths(curRoot.right, pathStr + String.valueOf(curRoot.data) + ", ", sb);
		//until we hit a leaf, in which case we can add this path to the string builder and
		//recurse back up to the next path
		if(curRoot.left == null && curRoot.right == null) {
			//if the amount of elements we've added so far +1 is equal to the tree's height, it's
			//a maximal path.
			if(pathStr.split(", ").length + 1 == this.height())
				sb.append(pathStr += String.valueOf(curRoot.data) + "\n");
		}
		return "";
	}
	
	/**
	 * Produces tree like string representation of this tree. Returns a string representation of
	 * this tree in a tree-like format. The string representation consists of a tree-like
	 * representation of this tree. Each node is shown in its own line with the indentation showing
	 * the depth of the node in this tree. The root is printed on the first line, followed by its
	 * left subtree, followed by its right subtree.
	 * 
	 * @return string containing tree-like representation of this tree.
	 */
	public String toStringTreeFormat() {
		//return empty string if tree is empty
		if(isEmpty())
			return "";
		
		StringBuilder sb = new StringBuilder();
		//recurse with starting index of 0
		toStringTreeFormat(root, 0, sb);
		return sb.toString(); //return finished string builder string
	}
	//recursive
	private void toStringTreeFormat(Node curRoot, int indent, StringBuilder sb) {
		//adds filler, indent - 1 so that "|--" gets a head start
		for(int i = 0; i < indent - 1; i++) {
			sb.append("   ");
		}
		//skips root, but is placed after filler on every subsequent node
		if(indent != 0)
			sb.append("|--");
		
		if(curRoot == null) {
			//if we hit the nulls, just print null and space and move onto next 
			//line so that we null protect the necessary curRoot.left and curRoot.right calls
			sb.append("null\n");
		} else {
			sb.append(String.valueOf(curRoot.data) + "\n"); //append the data and new line
			toStringTreeFormat(curRoot.left, indent + 1, sb); //recurse left, increase indent
			toStringTreeFormat(curRoot.right, indent + 1, sb); //recurse right, increase indent
		}
	}
	
	
	/**
	 * Returns an iterator over the elements in this tree in ascending order.
	 * 
	 * @return an iterator over the elements in this set in ascending order
	 */
	@Override
	public Iterator<E> iterator() { //in-order iterator
		return new inorderIterator(this); //return inorder iterator
	}
	
	/**
	 * Returns an iterator over the elements in this tree in order of the
	 * preorder traversal.
	 * 
	 * @return an iterator over the elements in this tree in order of the
	 *		preorder traversal
	 */
	public Iterator<E> preorderIterator() {
		return new preorderIterator(this);
	}
	
	/**
	 * Returns an iterator over the elements in this tree in order of
	 * the postorder traversal.
	 * 
	 * @return an iterator over the elements in this tree in order of
	 * 		the postorder traversal
	 */
	public Iterator<E> postorderIterator() {
		return new postorderIterator(this);
	}
	
	/**
	 * Node class
	 * Contains local field for height, size of subtree, and size of its left child's subtree
	 */
	private class Node {
		E data;
		Node left;
		Node right;
		int height = 1;
		int size = 1;
		int leftSize = 0;
		
		//constructor with just data
		public Node(E data) {
			this.data = data;
		}
		
		//constructor with data, left and right
		public Node(E data, Node left, Node right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}
		
	}
	
	/**
	 * Inorder iterator
	 * Iterates through the tree with inorder traversal
	 */
	private class inorderIterator implements Iterator<E> {
		
		BST<E> bst;
		ArrayList<Node> nodeList = new ArrayList<Node>(); //where we'll cache the nodes
		int index; //keep track of where we are in the list
		
		/**
		 * Default constructor, generates iterator in O(n)
		 */
		public inorderIterator(BST<E> inBST) {
			this.bst = inBST;
			index = 0;
			
			genNodeList(root); //generate inorder list
		}
		
		//inorder recursive method
		private void genNodeList(Node curRoot) {
			if(curRoot != null) {
				genNodeList(curRoot.left); //go left,
				nodeList.add(curRoot); //this node,
				genNodeList(curRoot.right); //and finally, right
			}
		}
		
		/**
		 * Checks if there are any elements left to be iterated over
		 * 
		 * @return True if there are elements left to return
		 */
		@Override
		public boolean hasNext() {
			//if the index is the same as the size of the array, there's nothing left
			return !(nodeList.size() == index);
		}
		
		/**
		 * Returns next element
		 * 
		 * @return The next element in the iteration
		 */
		@Override
		public E next() {
			//check to make sure there are items left
			if(hasNext()) {
				//cache and return item
				Node n = nodeList.get(index);
				index++;
				return n.data;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Preorder iterator
	 * Iterates through the tree with a preorder traversal
	 */
	private class preorderIterator implements Iterator<E> {

		BST<E> bst;
		ArrayList<Node> nodeList = new ArrayList<Node>(); //where we'll cache the nodes
		int index;
		
		/**
		 * Default constructor, generates iterator in O(n)
		 */
		public preorderIterator(BST<E> inBST) {
			this.bst = inBST;
			index = 0;
			
			genNodeList(root); //generate preorder list
		}
		
		//preorder recursive method
		private void genNodeList(Node curRoot) {
			if(curRoot != null) {
				nodeList.add(curRoot); //this node,
				genNodeList(curRoot.left); //go left,
				genNodeList(curRoot.right); //and finally, right
			}
		}
		
		/**
		 * Checks if there are any elements left to be iterated over
		 * 
		 * @return True if there are elements left to return
		 */
		@Override
		public boolean hasNext() {
			//if the index is the same as the size of the array, there's nothing left
			return !(nodeList.size() == index);
		}
		
		/**
		 * Returns next element
		 * 
		 * @return The next element in the iteration
		 */
		@Override
		public E next() {
			Node n = nodeList.get(index);
			index++;
			return n.data;
		}
	}
	
		/**
		 * Postorder iterator
		 * Iterates through the tree with a postorder traversal
		 */
	private class postorderIterator implements Iterator<E> {

		BST<E> bst;
		ArrayList<Node> nodeList = new ArrayList<Node>(); //where we'll cache the nodes
		int index;
		
		/**
		 * Default constructor, generates iterator in O(n)
		 */
		public postorderIterator(BST<E> inBST) {
			this.bst = inBST;
			index = 0;
			
			genNodeList(root); //generate postorder list
		}
		
		//postorder recursive method
		private void genNodeList(Node curRoot) {
			if(curRoot != null) {
				genNodeList(curRoot.left); //go left,
				genNodeList(curRoot.right); //right,
				nodeList.add(curRoot); //and finally this node
			}
		}
		
		/**
		 * Checks if there are any elements left to be iterated over
		 * 
		 * @return True if there are elements left to return
		 */
		@Override
		public boolean hasNext() {
			//if the index is the same as the size of the array, there's nothing left
			return !(nodeList.size() == index);
		}
		
		/**
		 * Returns next element
		 * 
		 * @return The next element in the iteration
		 */
		@Override
		public E next() {
			Node n = nodeList.get(index);
			index++;
			return n.data;
		}
	}
}