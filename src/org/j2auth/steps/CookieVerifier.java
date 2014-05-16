package org.j2auth.steps;

import org.j2auth.main.AuthInfo;
import org.j2auth.main.DutyChain;
import org.j2auth.main.Step;
import org.j2auth.util.Decoder;
/**
 * ��cookie����֤�û���Ϣ
 * @author volador
 *
 */
public class CookieVerifier implements Step{

	//j2auth��cookie��"j_c"��ͷ
	public static final String USER_ACCOUNT = "j_c_user_account";
	public static final String USER_PASSWORD = "j_c_user_password";
	
	//����������� ��cookie�е���������Ǿ������ܵ�
	private Decoder decoder = null;
	
	private UserService userService = null;
	
	//setter
	public void setDecoder(Decoder decoder){
		this.decoder = decoder;
	}
	
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	
	@Override
	public AuthInfo process(AuthInfo info, DutyChain chain) {
		//����Ѿ���¼����������
		if(isLogin(info)){
			return chain.next(info);
		}
		
		//����cookie�д���û�����������е�¼
		String account = info.getCookie(USER_ACCOUNT);
		String password = info.getCookie(USER_PASSWORD);
		
		//cookie�Ƿ�����֤��Ϣ
		if(noValue(account) && noValue(password)){
			return chain.next(info);
		}
		
		//�������
		if(this.decoder != null){
			password = this.decoder.decode(password);
		}
		
		boolean result = userService.check(account,password);
		if(result){
			cookieLoginSuccess(account,info);
		}else{
			info.delCookie(USER_ACCOUNT);
			info.delCookie(USER_PASSWORD);
		}
		
		return chain.next(info);
	}
	
	/**
	 * cookie��¼�ɹ���Ĵ����߼���������չ����������cookie����ʱ���
	 * @param account �û���¼�ʺ�
	 * @param info ������
	 */
	protected void cookieLoginSuccess(String account,AuthInfo info){
		info.setAccount(account);
	}
	/*
	 * �ж�ֵ�ǿ�
	 */
	private boolean noValue(String value){
		return value == null || value.length() == 0;
	}
	
	/**
	 * �ж��û��Ƿ��Ѿ���¼����������������û���Ϣ(session)����ʾ�û��Ѿ���¼����
	 * @param info ����������
	 * @return true/false
	 */
	protected boolean isLogin(AuthInfo info){
		return null == info.getAccount();
	}

}
