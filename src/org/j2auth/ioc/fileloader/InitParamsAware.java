package org.j2auth.ioc.fileloader;

import java.util.Map;

public interface InitParamsAware {

	void setInitParams(Map<String,String> params);
}
