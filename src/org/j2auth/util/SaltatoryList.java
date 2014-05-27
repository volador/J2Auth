package org.j2auth.util;

import java.util.HashMap;
import java.util.Map;
/**
 * 过滤器链表结构
 * @author volador
 *
 * @param <KEY> 节点key类型
 * @param <VALUE> 节点value类型
 */
public class SaltatoryList<KEY,VALUE> {
	
	public static class Node<VALUE>{
		private VALUE value;
		private Node<VALUE> next;
		
		public Node(VALUE value,Node<VALUE> next) {
			this.value = value;
			this.next = next;
		}

		public VALUE getValue() {
			return value;
		}

		public Node<VALUE> next(){
			return next;
		}
		
		public void setNext(Node<VALUE> next){
			this.next = next;
		}
	}
	
	private Map<KEY,Node<VALUE>> source = new HashMap<KEY,Node<VALUE>>();
	
	private Node<VALUE> header = new Node<VALUE>(null,null);
	
	private Node<VALUE> end = header;
	
	public SaltatoryList(Map<KEY,VALUE> source) {
		if(source != null && source.size() > 1){
			for(Map.Entry<KEY, VALUE> entry : source.entrySet()){
				Node<VALUE> node = new Node<VALUE>(entry.getValue(),null);
				end.setNext(node);
				end = node;
				this.source.put(entry.getKey(), node);
			}
		}
	}
	
	public Node<VALUE> getHeader(){
		return header;
	}
	
	public Node<VALUE> getNode(KEY sign){
		return source.get(sign);
	}

}
