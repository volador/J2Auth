package org.j2auth.filters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * fot test
 * @author volador
 *
 */
public class UserServiceImpl implements UserService{

	private Map<String,String> users = new HashMap<String,String>();
	
	public UserServiceImpl() {
		users.put("zhangyu","zhangyu");
		users.put("voladorzhang","voladorzhang");
	}
	
	@Override
	public boolean check(String account, String password) {
		String pass = users.get(account);
		if(pass != null && pass.equals(password)){
			return true;
		}
		return false;
	}

	@Override
	public Set<String> fetchUserCheckPoints(String account) {
		Set<String> sets = new HashSet<String>();
//		sets.add("fuck:book;");
		return sets;
	}

	@Override
	public Set<String> getchResourceCheckPoints() {
		Set<String> sets = new HashSet<String>();
//		sets.add("fuck:book;..");
		return sets;
	}

}
