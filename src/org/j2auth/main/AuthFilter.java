package org.j2auth.main;

public interface AuthFilter {

	AuthInfo process(AuthInfo info,AuthChain chain);
}
