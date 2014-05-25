package org.j2auth.ioc.inject;

import org.j2auth.ioc.BeanFactory;
import org.w3c.dom.Node; 


public interface Injector {

	Object doInject(Object obj,BeanFactory factory);

	void nodeInfo(Node node,String currentPath);
	
}
