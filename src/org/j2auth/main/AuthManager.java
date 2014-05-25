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
	
	static Map<String,AuthFilter> filters = new LinkedHashMap<String,AuthFilter>();

	private static class ProviderChain implements AuthChain{
		
		static SaltatoryList<String,AuthFilter> list = new SaltatoryList<String,AuthFilter>(filters);
		
		private SaltatoryList.Node<AuthFilter> index = list.getHeader().next();
		
		@Override
		public AuthInfo next(AuthInfo info) {
			if(index == null) return info;
			AuthFilter filter = index.getValue();
			index = index.next();
			return filter.process(info,this);
		}
		
		@Override
		public AuthInfo next(AuthInfo info, String nextProvider) {
			SaltatoryList.Node<AuthFilter> node = list.getNode(nextProvider);
			if(node == null) return info;
			index = node;
			AuthFilter filter = index.getValue();
			index = index.next();
			return filter.process(info,this);
		}
	}
	
	//setter
	public void setProviders(Map<String,Object> providers){
		if(providers != null && providers.size() > 0){
			for(Map.Entry<String, Object> entry : providers.entrySet()){
				AuthManager.filters.put(entry.getKey(), (AuthFilter)entry.getValue());
			}
		}
	}

	@Override
	public AuthInfo doAuth(AuthInfo info) {
		AuthChain chain = new ProviderChain();
		return chain.next(info);
	}
}
