package org.j2auth.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ��Ծʽ����ṹ
 * @author volador
 *
 * @param <KEY> ����ڵ�ı�ʾ������
 * @param <VALUE> ����ڵ��ֵ����
 */
public class SaltatoryList<KEY,VALUE> {
	
	/**
	 * ����ڵ�
	 * @author volador
	 *
	 * @param <VALUE> �ڵ�ֵ����
	 */
	public static class Node<VALUE>{
		/*
		 * �ڵ�������������
		 */
		private VALUE value;
		/*
		 * ��һ���ڵ������
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
	
	//����ͷ
	private Node<VALUE> header = new Node<VALUE>(null,null);
	
	//����β
	private Node<VALUE> end = header;
	
	/**
	 * ��ʼ������
	 * @param source �ܱ���˳���map�ṹ ��map��keyΪ����ڵ��ʾ����valueΪ����ڵ�ֵ
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
	 * ��ȡ����ͷ�ڵ�
	 */
	public Node<VALUE> getHeader(){
		return header;
	}
	
	/**
	 * ��ȡ������ĳһ���ڵ�
	 * @param sign �ýڵ�ı�ʾ��
	 * @return �ڵ����ã����ڵ㲻���ڣ�����null.
	 */
	public Node<VALUE> getNode(KEY sign){
		return source.get(sign);
	}

}
