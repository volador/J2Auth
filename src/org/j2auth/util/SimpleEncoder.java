package org.j2auth.util;

public class SimpleEncoder implements Encoder{

	private int charAciiMove = 1;
	
	@Override
	public String encode(String value) {
		StringBuilder sb = new StringBuilder();
		if(value != null && value.length() >= 1){
			char[] chars = value.toCharArray();
			for(char ch : chars){
				char newOne = (char) (((int)ch) + charAciiMove);
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
