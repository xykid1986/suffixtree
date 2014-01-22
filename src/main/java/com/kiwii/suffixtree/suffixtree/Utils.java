package com.kiwii.suffixtree.suffixtree;

import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

	public static int height(int i) {
		return Integer.numberOfTrailingZeros(i);
	}

	public static int depth(int i) {
		return 31 - Integer.numberOfLeadingZeros(i);
	}

	public static int LCAinB(int x, int y) {
		int h = height(x) >= height(y) ? height(x) : height(y);
		int s = h >= depth(x ^ y) ? h : depth(x ^ y);
		return shift(x, s);
	}

	public static int shift(int i, int s) {
		return ((i >> s) | 1) << s;
	}

	/**
	 * Return the 1 bit exist in both x and y equals or higher to height. 
	 * @param height
	 * @param x
	 * @param y
	 * @return
	 */
	public static int findOneBitLeftPosition(int height, int x, int y) {
		x >>= height;
		y >>= height;
		int counter = height;
		while (x > 0 && y > 0) {
			if (x % 2 == 1 && y % 2 == 1) {
				return counter;
			} else {
				counter++;
				x >>= 1;
				y >>= 1;
			}
		}
		throw new IllegalArgumentException(
				"Cannot find the 1 bit on both x and y");
	}
	
	/**
	 * Return the 1 bit exist in x lower than height
	 * @param height
	 * @param x
	 * @return
	 */
	public static int findOneBitRightPosition(int height, int x){
		while((x>>--height)>0){
			if((x>>height)%2==1)
				return height;
		}
		throw new IllegalArgumentException(
				"Cannot find the 1 bit on x");
	}
	
	/**
	 * Return the length of 
	 * @param s1
	 * @param s2
	 * @param i
	 * @param j
	 * @return
	 */
	public static int longestCommonExtention(String s1, String s2, int i, int j){
		SuffixTree st = new SuffixTree(new ArrayList<String>(Arrays.asList(s1,s2)));
		SuffixTreeNode n1 = st.findNode(0,i,s1.length()-1);
		SuffixTreeNode n2 = st.findNode(0,j,s2.length()-1);
		SuffixTreeNode v = st.lowestCommonAncestor(n1, n2);
		if(v==st.root) return 0;
		return v.end-v.start+1;
	}
	
	/**
	 * Find longest extention
	 * @param st SuffixTree
	 * @param index1 StringIndex of string 1
	 * @param index2 StringIndex of string 2
	 * @param i position on string 1
	 * @param j position on string 2
	 * @return
	 */
	public static int longestCommonExtention(SuffixTree st, int index1, int index2, int i, int j){
		SuffixTreeNode n1 = st.findNode(index1,i,st.inputs.get(index1).length()-1);
		SuffixTreeNode n2 = st.findNode(index2,j,st.inputs.get(index2).length()-1);
		SuffixTreeNode v = st.lowestCommonAncestor(n1, n2);
		if(v==st.root) return 0;
		return v.end-v.start+1;
	}

	public static void main(String[] args) {
		System.out.println(depth(10));
	}
}
