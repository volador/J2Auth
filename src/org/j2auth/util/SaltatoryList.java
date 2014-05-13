package org.j2auth.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 跳跃式链表结构
 * @author volador
 *
 * @param <KEY> 链表节点的标示符类型
 * @param <VALUE> 链表节点的值类型
 */
public class SaltatoryList<KEY,VALUE> {
	
	/**
	 * 链表节点
	 * @author volador
	 *
	 * @param <VALUE> 节点值类型
	 */
	public static class Node<VALUE>{
		/*
		 * 节点所包含的内容
		 */
		private VALUE value;
		/*
		 * 下一个节点的引用
		 */
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
	
	//链表头
	private Node<VALUE> header = new Node<VALUE>(null,null);
	
	//链表尾
	private Node<VALUE> end = header;
	
	/**
	 * 初始化链表
	 * @param source 能保持顺序的map结构 ，map的key为链表节点标示符，value为链表节点值
	 */
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
	
	/**
	 * 获取链表头节点
	 */
	public Node<VALUE> getHeader(){
		return header;
	}
	
	/**
	 * 获取链表中某一个节点
	 * @param sign 该节点的标示符
	 * @return 节点引用，若节点不存在，返回null.
	 */
	public Node<VALUE> getNode(KEY sign){
		return source.get(sign);
	}

}
