package org.j2auth.main;

public interface Step {

	AuthInfo process(AuthInfo info,AuthChain chain);
}
