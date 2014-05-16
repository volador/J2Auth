package org.j2auth.steps;

import org.j2auth.main.AuthInfo;
import org.j2auth.main.DutyChain;
import org.j2auth.main.Step;
/**
 * ��������У���� ������������
 * @author voaldor
 *
 */
public class AnonymousVerifier implements Step{

	public static final String ANONYMOUS_ACCOUNT = "j_anonymous";
	
	@Override
	public AuthInfo process(AuthInfo info, DutyChain chain) {
		if(isAnonymous(info)){
			return chain.next(info);
		}
		info.setAccount(ANONYMOUS_ACCOUNT);
		return chain.next(info);
	}
	
	/**
	 * �ж��Ƿ���������¼���������������account����������
	 * @param info ����������
	 * @return true/false
	 */
	protected boolean isAnonymous(AuthInfo info){
		return null == info.getAccount();
	}
}
