package org.j2auth.main;

import java.util.LinkedHashMap;  
import java.util.Map;

import org.j2auth.util.SaltatoryList;
/**
 * 权限管理器
 * @author volador
 *
 */
public class AuthManager implements Auth{
	
	//过滤器列表
	static Map<String,AuthFilter> filters = new LinkedHashMap<String,AuthFilter>();
	/**
	 * 过滤链实现
	 * @author volador
	 *
	 */
	private static class FilterChain implements AuthChain{
		//可以跳跃的链式结构
		static SaltatoryList<String,AuthFilter> list = new SaltatoryList<String,AuthFilter>(filters);
		//链表头节点
		private SaltatoryList.Node<AuthFilter> index = list.getHeader().next();
		
		@Override
		public AuthContext next(AuthContext info) {
			if(index == null) return info;
			AuthFilter filter = index.getValue();
			index = index.next();
			return filter.process(info,this);
		}
		
		@Override
		public AuthContext next(AuthContext info, String nextFilter) {
			SaltatoryList.Node<AuthFilter> node = list.getNode(nextFilter);
			if(node == null) return info;
			index = node;
			AuthFilter filter = index.getValue();
			index = index.next();
			return filter.process(info,this);
		}
	}
	
	//setter
	public void setAuthFilters(Map<String,Object> authFilters){
		if(authFilters != null && authFilters.size() > 0){
			for(Map.Entry<String, Object> entry : authFilters.entrySet()){
				AuthManager.filters.put(entry.getKey(), (AuthFilter)entry.getValue());
			}
		}
	}

	@Override
	public AuthContext doAuth(AuthContext info) {
		//开始链式处理
		AuthChain chain = new FilterChain();
		return chain.next(info);
	}
}
