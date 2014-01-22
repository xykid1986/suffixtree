package com.kiwii.suffixtree.suffixtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DPUtils {
	
	public static char INSERT = '-';
	public static short UP = 0;
	public static short DIAGNAL = 1;
	public static short LEFT = 2;
	

	public static int longestCommonSubsequenceLength(String x, String y){
		x = '$'+x;
		y = '$'+y;
		int[][] b = new int[x.length()][y.length()];
		int lcs = Integer.MIN_VALUE;
		for(int i=1;i<x.length();i++){
			for(int j=1;j<y.length();j++){
				b[i][j]= Math.max(b[i-1][j],b[i][j-1]);
				if(x.charAt(i)==y.charAt(j)){
					b[i][j]=Math.max(b[i-1][j-1]+1, b[i][j]);
				}
				if(b[i][j]>lcs){
					lcs=b[i][j];
				}
			}
		}
		return lcs;
	}
	
	public static String[] longestCommonSubsequence(String x, String y){
		x = '$'+x;
		y = '$'+y;
		int[][] b = new int[x.length()][y.length()];
		short[][] arrowTable = new short[x.length()][y.length()];
		for(int i=1;i<x.length();i++){
			arrowTable[i][0] |= (1<<UP);
		}
		for(int i=1;i<y.length();i++){
			arrowTable[0][i] |= (1<<LEFT);
		}
		for(int i=1;i<x.length();i++){
			for(int j=1;j<y.length();j++){
				
				if(b[i-1][j]<b[i][j-1]){
					arrowTable[i][j] |= (1<<LEFT); 
					b[i][j] = b[i][j-1];
				}
				else if(b[i-1][j]>b[i][j-1]){
					arrowTable[i][j] |= (1<<UP); 
					b[i][j] = b[i-1][j];
				}else{
					arrowTable[i][j] |= (1<<LEFT);
					arrowTable[i][j] |= (1<<UP);
					b[i][j] = b[i-1][j];
				}
					
				if(x.charAt(i)==y.charAt(j)){
					if(b[i-1][j-1]+1>b[i][j]){
						arrowTable[i][j] = (short)(0|(1<<DIAGNAL));
						b[i][j]=b[i-1][j-1]+1;
					}else if(b[i-1][j-1]+1==b[i][j]){
						arrowTable[i][j] |= (1<<DIAGNAL);
					}
						
				}
			}
		}
		return LCSTraceBack(x,y, arrowTable,x.length()-1,y.length()-1, b[x.length()-1][y.length()-1]);	
	}
	
	private static String[] LCSTraceBack(String s1, String s2, short[][] arrowTable, int x, int y, int maxLen){
		Set<String> result = new HashSet<String>();
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		LCSTraceBackHelper(arrowTable, x, y, new ArrayList<Integer>(), res, maxLen);
		for(List<Integer> seq: res){
			StringBuffer sb = new StringBuffer();
			for(Integer i: seq){
				sb.append(s1.charAt(i));
			}
			result.add(sb.reverse().toString());
		}
		return result.toArray(new String[result.size()]);
	}
	
	private static void LCSTraceBackHelper(short[][] arrowTable, int x, int y, List<Integer> list, List<List<Integer>> res, int maxLen){
		if(x==0 && y==0){
			if(list.size()==maxLen)
				res.add(list);
			return;
		}
		if((arrowTable[x][y]&(1<<UP))>0){
			LCSTraceBackHelper(arrowTable, x-1,y, new ArrayList<Integer>(list), res, maxLen);
		}
		if((arrowTable[x][y]&(1<<LEFT))>0){
			LCSTraceBackHelper(arrowTable, x,y-1, new ArrayList<Integer>(list), res, maxLen);
		}
		if((arrowTable[x][y]&(1<<DIAGNAL))>0){
			list.add(x);
			LCSTraceBackHelper(arrowTable, x-1, y-1, list, res, maxLen);
		}
	}
	//=====================================================================================
	public static void displayMatrix(int[][] m){
		for(int x=0;x<m.length;x++){
			for(int y=0;y<m[0].length;y++){
				if(m[x][y]/100<0)
					System.out.print(" ");
				if(m[x][y]/10<0)
					System.out.print(" ");
				System.out.print(m[x][y]);
			}
			System.out.println();
		}
	}
	public static void displayMatrix(short[][] m){
		for(int x=0;x<m.length;x++){
			for(int y=0;y<m[0].length;y++){
				if(m[x][y]/100<0)
					System.out.print(" ");
				if(m[x][y]/10<0)
					System.out.print(" ");
				System.out.print(m[x][y]);
			}
			System.out.println();
		}
	}
	//========================================================================================
	//D(i,j) = min[D(i-1,j)+1, D(i,j-1)+1, D(i-1,j-1)+t(i,j)], t(i,j)= x(i)==y(j)?0:1;
	public static StringMatch[] stringEditDistance(String x, String y){
		x = '$'+x;
		y = '$'+y;
		int[][] b = new int[x.length()][y.length()];
		short[][] arrowTable = new short[x.length()][y.length()];
		for(int i=1;i<x.length();i++){
			b[i][0]=i;
			arrowTable[i][0] |= (1<<UP);
		}
		for(int i=1;i<y.length();i++){
			b[0][i]=i;
			arrowTable[0][i] |= (1<<LEFT);
		}
		for(int i=1;i<x.length();i++){
			for(int j=1;j<y.length();j++){
				
				if(b[i-1][j]>b[i][j-1]){
					arrowTable[i][j] |= (1<<LEFT); 
					b[i][j] = b[i][j-1]+1;
				}
				else if(b[i-1][j]<b[i][j-1]){
					arrowTable[i][j] |= (1<<UP); 
					b[i][j] = b[i-1][j]+1;
				}else{
					arrowTable[i][j] |= (1<<LEFT);
					arrowTable[i][j] |= (1<<UP);
					b[i][j] = b[i-1][j]+1;
				}
					
				if(x.charAt(i)==y.charAt(j)){
					if(b[i-1][j-1]<b[i][j]){
						arrowTable[i][j] = (short)(0|(1<<DIAGNAL));
						b[i][j]=b[i-1][j-1];
					}else if(b[i-1][j-1]==b[i][j]){
						arrowTable[i][j] |= (1<<DIAGNAL);
					}
						
				}else{
					if(b[i-1][j-1]+1<b[i][j]){
						arrowTable[i][j] = (short)(0|(1<<DIAGNAL));
						b[i][j]=b[i-1][j-1]+1;
					}else if(b[i-1][j-1]+1==b[i][j]){
						arrowTable[i][j] |= (1<<DIAGNAL);
					}
				}
			}
		}
		return editDistanceTraceBack(x,y, arrowTable,x.length()-1,y.length()-1, b[x.length()-1][y.length()-1]);	
	}
	
	private static StringMatch[] editDistanceTraceBack(String s1, String s2, short[][] arrowTable, int x, int y, int maxChange){
		Set<StringMatch> result = new HashSet<StringMatch>();
		editDistanceTraceBackHelper(s1, s2, arrowTable, x, y, "", "",result, maxChange, 0);
		return result.toArray(new StringMatch[result.size()]);
	}
	
	private static void editDistanceTraceBackHelper(String s1, String s2, short[][] arrowTable, int x, int y, String sb1, String sb2, Set<StringMatch> result, int maxChange, int currentChange){
		if(x==0 && y==0){
			if(currentChange==maxChange)
				result.add(new StringMatch(reverse(sb1),reverse(sb2), maxChange));
			return;
		}
		if((arrowTable[x][y]&(1<<UP))>0){//deletion
			editDistanceTraceBackHelper(s1, s2, arrowTable, x-1,y, sb1+s1.charAt(x),sb2+"-", result, maxChange, currentChange+1);
		}
		if((arrowTable[x][y]&(1<<LEFT))>0){//insertion
			editDistanceTraceBackHelper(s1, s2, arrowTable, x,y-1, sb1+"-",sb2+s2.charAt(y), result, maxChange, currentChange+1);
		}
		if((arrowTable[x][y]&(1<<DIAGNAL))>0){
			if(s1.charAt(x)==s2.charAt(y)){//match
				editDistanceTraceBackHelper(s1, s2, arrowTable, x-1, y-1, sb1+s1.charAt(x), sb2+s2.charAt(y), result, maxChange, currentChange);
			}else{//substitution
				editDistanceTraceBackHelper(s1, s2, arrowTable, x-1, y-1, sb1+s1.charAt(x), sb2+s2.charAt(y), result, maxChange, currentChange+1);
			}
		}
	}
	
	public static String reverse(String s){
		char[] c = s.toCharArray();
		int left=0;
		int right=s.length()-1;
		while(left<right){
			char temp = c[left];
			c[left] = c[right];
			c[right] = temp;
			left++;
			right--;
		}
		return new String(c);
	}
	
	public static void main(String[] args){
		//System.out.println(Arrays.toString(longestCommonSubsequence("tctgatgc","ggcagtct")));
		
		StringMatch[] sm = stringEditDistance("writers","vintner");
		for(StringMatch s: sm){
			System.out.println(s);
		}
		
	}
	
}
