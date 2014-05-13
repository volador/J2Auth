package org.j2auth.util;
/**
 * 简单加密器:加密规则通过更改字符串里面每个字符的acii码
 * @author volador
 *
 */
public class SimpleDecoder implements Decoder{

	//每个字符的ACII码-5
	private int charAciiMove = 1;
	
	@Override
	public String decode(String value) {
		StringBuilder sb = new StringBuilder();
		if(value != null && value.length() >= 1){
			char[] chars = value.toCharArray();
			for(char ch : chars){
				char newOne = (char) (((int)ch) - charAciiMove);
				sb.append(newOne);
			}
		}
		return sb.toString();
	}
	
	public void setCharAciiMove(String move){
		try{
			this.charAciiMove = Integer.parseInt(move);
		}catch(Exception e){}
	}

}
