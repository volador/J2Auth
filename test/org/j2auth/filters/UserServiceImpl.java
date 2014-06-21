package org.j2auth.filters;

import java.util.HashSet;
import java.util.Set;

/**
 * fot test
 * @author volador
 *
 */
public class UserServiceImpl implements UserService{

	@Override
	public boolean check(String account, String password) {
		return true;
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
