package com.ci.group20;

import java.util.ArrayList;

public class Stack<E> {
	
	private ArrayList<E> elements;
	
	public Stack() {
		elements = new ArrayList<E>();
	}
	
	public int size() {
		return elements.size();
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	public void push(E elem) {
		elements.add(0, elem);
	}
	
	public E pop() {
		if (!isEmpty()) {
			E element = elements.get(0);
			for (int i=0;i<elements.size()-1;i++) {
				elements.set(i, elements.get(i+1));
			}
			elements.remove(elements.size()-1);
			return element;
		} else return null;
	}
	
	public E top() {
		return elements.get(0);
	}

}
