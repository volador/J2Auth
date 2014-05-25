package org.j2auth.main;

public interface AuthInfo {
	
	public static final String SESSION_ACCOUNT_KEY = "i_session_account";
	

	String getAccount();

	String getCookie(String name);

	void setAccount(String account);

	void delCookie(String key);
}