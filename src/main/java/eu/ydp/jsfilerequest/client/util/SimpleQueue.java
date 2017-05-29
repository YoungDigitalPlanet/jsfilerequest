package eu.ydp.jsfilerequest.client.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleQueue<E> {

	private List<E> list;
	
	public SimpleQueue(){
		list = new ArrayList<E>();
	}
		
	public boolean offer(E e) {
		return list.add(e);
	}

	public E peek() {
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	public E poll() {
		if (list.size() > 0){
			E e = list.get(0);
			list.remove(0);
			return e;
		}
		return null;
	}

	public Iterator<E> iterator() {
		return list.iterator();
	}

	public int size() {
		return list.size();
	}

}
