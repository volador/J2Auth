package org.j2auth.main;

import org.j2auth.main.AuthInfo;

public interface AuthChain {

	AuthInfo next(AuthInfo info);

	AuthInfo next(AuthInfo info,String nextProvider);
}