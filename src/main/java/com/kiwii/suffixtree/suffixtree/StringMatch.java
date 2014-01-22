package com.kiwii.suffixtree.suffixtree;

public class StringMatch {
	public final String s1;
	public final String s2;
	public final String matches;
	public final int score;

	public StringMatch(String s1, String s2, int score) {
		this(s1, s2, null, score);
	}

	public StringMatch(String s1, String s2, String matches, int score) {
		if (s1.length() != s2.length())
			throw new IllegalArgumentException(
					"The length of two strings are different.");
		this.s1 = s1;
		this.s2 = s2;
		if (matches != null) {
			this.matches = matches;
		} else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < s1.length(); i++) {
				if (s1.charAt(i) == s2.charAt(i)) {
					sb.append("|");
				} else {
					sb.append(" ");
				}
			}
			this.matches = sb.toString();
		}
		this.score = score;
	}

	@Override
	public String toString() {
		return s1 + "\n" + matches + "\n" + s2;
	}
	
	@Override
	public int hashCode(){
		return 11*s1.hashCode()+s2.hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null || !(obj instanceof StringMatch))
			return false;
		if(obj == this)
			return true;
		return s1.equals(((StringMatch)obj).s1) && s2.equals(((StringMatch)obj).s2);
	}

	public static void main(String[] args) {
		StringMatch sm = new StringMatch("y-aua", "yajua", 10);
		System.out.println(sm);
	}
}
