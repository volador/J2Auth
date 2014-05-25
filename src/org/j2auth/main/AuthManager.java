package org.j2auth.main;

import java.util.LinkedHashMap;  
import java.util.Map;

import org.j2auth.util.SaltatoryList;

public class AuthManager implements Auth{
	
	static Map<String,Step> steps = new LinkedHashMap<String,Step>();

	private static class ProviderChain implements AuthChain{
		
		static SaltatoryList<String,Step> list = new SaltatoryList<String,Step>(steps);
		
		private SaltatoryList.Node<Step> index = list.getHeader().next();
		
		@Override
		public AuthInfo next(AuthInfo info) {
			if(index == null) return info;
			Step step = index.getValue();
			index = index.next();
			return step.process(info,this);
		}
		
		@Override
		public AuthInfo next(AuthInfo info, String nextProvider) {
			SaltatoryList.Node<Step> node = list.getNode(nextProvider);
			if(node == null) return info;
			index = node;
			Step step = index.getValue();
			index = index.next();
			return step.process(info,this);
		}
	}
	
	//setter
	public void setProviders(Map<String,Object> providers){
		if(providers != null && providers.size() > 0){
			for(Map.Entry<String, Object> entry : providers.entrySet()){
				AuthManager.steps.put(entry.getKey(), (Step)entry.getValue());
			}
		}
	}

	@Override
	public AuthInfo doAuth(AuthInfo info) {
		AuthChain chain = new ProviderChain();
		return chain.next(info);
	}
}
