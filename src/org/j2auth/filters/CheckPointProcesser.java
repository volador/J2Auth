package org.j2auth.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.j2auth.main.AuthChain;
import org.j2auth.main.AuthContext;
import org.j2auth.main.AuthFilter;
/**
 * 根据校验上下文，获取用户相关的checkpoint集合
 * @author volador
 *
 */
public class CheckPointProcesser  implements AuthFilter{

	private UserService userService = null;
	
	//开放的资源:所有人都可以访问的资源
	private List<Pattern> openSource = new ArrayList<Pattern>();
	//开放资源checkpoint
	private static final String OPEN_SOURCE_CHECKPOINT = "j_auth_open_source_checkpoint";
	
	@Override
	public AuthContext process(AuthContext info, AuthChain chain) {
		if(userService == null){
			System.out.println("warming from CheckPointProcesser:userService is null.");
			return chain.next(info);
		}
		//开绿灯的资源
		boolean isOpenSource = false;
		if(openSource.size() > 0){
			
			String requestURI = "";
			try {
				requestURI = (String) info.get("requestURI");
			} catch (Exception e) {e.printStackTrace();}
			
			for(Pattern p : openSource){
				if(p.matcher(requestURI).find()){
					info.setUserCheckPoint(OPEN_SOURCE_CHECKPOINT);
					info.setResourceCheckPoint(OPEN_SOURCE_CHECKPOINT);
					isOpenSource = true;
					break;
				}
			}
		}
		if(!isOpenSource){
			String account = info.getAccount();
			//未完成- -
			Set<String> userCheckPoints = userService.fetchUserCheckPoints(account);
			Set<String> resourceCheckPoints = userService.getchResourceCheckPoints();
			info.setUserCheckPoints(userCheckPoints);
			info.setResourceCheckPoints(resourceCheckPoints);
		}
		return chain.next(info);
	}
	
	public void setUserService(UserService userService){
		this.userService = userService;
	}
	/**
	 * 注入/解析 开放资源列表
	 * @param source 开放资源列表
	 */
	public void setOpenSource(List<String> source){
		if(source != null && source.size() != 0){
			for(String prePattent : source){
				Pattern p = Pattern.compile(prePattent);
				this.openSource.add(p);
			}
		}
	}
}