package com.kiwii.suffixtree.suffixtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class StringService {

	private static final Logger log = Logger.getLogger(StringService.class);
	private static final int DEFAULT_THRESHOLD = 2;

	// find longest palindrome (length>=3) of a string
	public static List<String> longestPalindrome(String s) {
		String r = reversed(s);
		Set<String> palindromes = new HashSet<String>();
		SuffixTree st = new SuffixTree(Arrays.asList(s,r));
		int startCount = 0;
		char lastChar = '#';
		for(int i=0;i<s.length();i++){
			if(lastChar!=s.charAt(i)){
				startCount = i;
				lastChar = s.charAt(i);
			}
			int len = Utils.longestCommonExtention(st, 0, 1, startCount, s.length()-1-i)-1;
			if(len>=1){
				if(startCount!=i){//even
					palindromes.add(s.substring(startCount-len,startCount+1));
				}else{
					palindromes.add(s.substring(startCount-len,startCount+len+1));
				}
			}
		}
		Set<String> maxPalindromes = new HashSet<String>();
		int maxLen = Integer.MIN_VALUE;
		for(String str: palindromes){
			if(str.length()<maxLen)
				continue;
			if(str.length()>maxLen)
				maxPalindromes.clear();
			maxPalindromes.add(str);
		}
		return new ArrayList<String>(maxPalindromes);
	}

	private static String reversed(String s) {
		char[] c = s.toCharArray();
		int left = 0;
		int right = s.length() - 1;
		while (left < right) {
			c = swap(left++, right--, c);
		}
		return new String(c);
	}

	private static char[] swap(int i, int j, char[] c) {
		char temp = c[i];
		c[i] = c[j];
		c[j] = temp;
		return c;
	}

	// ======================================================
	/**
	 * Find the longest common substring of the input strings
	 * 
	 * @param args
	 *            Input strings
	 * @return Longest common substring of the input strings
	 */
	public static List<String> longestCommonSubstring(String... args) {
		List<String> input = new ArrayList<String>();
		for (String s : args)
			input.add(s);
		return longestCommonSubstring(input);
	}

	/**
	 * Find the longest common substring of the input strings. The principle is
	 * to find the shared path of all input strings with maximum length.
	 * 
	 * @param input
	 *            Input strings
	 * @return Longest common substring of the input strings
	 */
	public static List<String> longestCommonSubstring(List<String> input) {
		if (input.size() == 1)
			return null;
		SuffixTree st = new SuffixTree(input);
		// find the longest path of internal nodes
		List<SuffixTreeNode> commonNodes = new ArrayList<SuffixTreeNode>();
		List<String> result = new ArrayList<String>();

		for (SuffixTreeNode n : st.internalNodes) {
			// System.out.println("checking internal node: "+n);
			if (n.stringIndex.size() == input.size()) {
				commonNodes.add(n);
				// System.out.println("common ancestor added: "+n);
			}
		}
		// find the longest common ancestor
		int maxLen = Integer.MIN_VALUE;
		for (SuffixTreeNode n : commonNodes) {
			int edgeLength = n.end - n.start + 1;
			if (edgeLength < maxLen)
				continue;
			if (edgeLength > maxLen)
				result.clear();
			result.add(input.get(0).substring(n.start, n.end + 1));
			maxLen = edgeLength;
		}
		return result;
	}

	// ========================================================================
	/**
	 * Find and return the longest repeat sequence in a string.
	 * 
	 * @param s
	 *            The input string
	 * @param threshold
	 *            The minimal length of repeat returned
	 * @return The longest repeat sequence in a string
	 */
	public static List<String> longestRepeat(String s, int threshold) {
		int thre = threshold;
		SuffixTree st = new SuffixTree(s);
		List<SuffixTreeNode> diverseNodes = new ArrayList<SuffixTreeNode>();
		List<String> result = new ArrayList<String>();
		for (SuffixTreeNode n : st.getInternalNodes()) {
			log.debug("Examing internal node " + n);
			if (diverseNode(st, n)) {
				diverseNodes.add(n);
			}
		}
		int maxLen = Integer.MIN_VALUE;
		for (SuffixTreeNode n : diverseNodes) {
			int edgeLength = n.end - n.start + 1;
			if (edgeLength < thre || edgeLength < maxLen)
				continue;
			if (edgeLength > maxLen)
				result.clear();
			result.add(st.inputs.get(0).substring(n.start, n.end + 1));
			maxLen = edgeLength;
		}
		return result;
	}

	/**
	 * Find and return the longest repeat sequence in a string.
	 * 
	 * @param s
	 *            The input string
	 * @return The longest repeat sequence in a string
	 */
	public static List<String> longestRepeat(String s) {
		return longestRepeat(s, DEFAULT_THRESHOLD);
	}

	/**
	 * Return True if all children of the given node are leaf nodes and have
	 * different left character. Left character is the character left to the
	 * start of the given node.
	 * 
	 * @param st
	 *            The suffix tree
	 * @param n
	 *            The input node
	 * @return True if all children of the given node are leaf nodes and have
	 *         different left character
	 */
	public static boolean diverseNode(SuffixTree st, SuffixTreeNode n) {
		Set<Character> set = new HashSet<Character>();
		for (SuffixTreeNode child : n.children) {
			log.debug("checking child " + child + " is leaf? " + child.isLeaf);
			if (isEndNode(child)==false) {
				log.debug("Not a leaf node");
				return false;
			} else {
				char leftChar = '\u0001';
				if (child.start - 1 >= 0) {
					leftChar = st.inputs.get(0).charAt(child.start - 1);
				}
				log.debug("Leaf node with left character " + leftChar);
				if (set.contains(leftChar))
					return false;
				else
					set.add(leftChar);
			}
		}
		return true;
	}
	
	/**
	 * Return true if all children of node are "$" or if the node is leaf node.
	 * This means the node represents the a suffix substring.
	 * @param node 
	 * @return
	 */
	public static boolean isEndNode(SuffixTreeNode node) {
		if(node.isLeaf){
			return true;
		}
		for(SuffixTreeNode n:node.children){
			if(!n.getStringRepr().equals("$")){
				return false;
			}
		}
		return true;
	}

	// ====================================================================================
	/**
	 * Return the indices of all occurrence of pattern in the text. Exact matching.
	 * @param text
	 * @param pattern
	 * @return
	 */
	public static int[] findSubstringIndex(String text, String pattern){
		SuffixTree st = new SuffixTree(text);
		SuffixTreeNode current = st.root;
		List<SuffixTreeNode> indexLeaves = new ArrayList<SuffixTreeNode>();
		int pointer = 0;
		while(pointer<pattern.length()){
			SuffixTreeNode next = st.searchEndChild(current, pattern.charAt(pointer));
			if(next!=null){
				current = next;
				int matchPtr = current.cut;
				while(matchPtr<=current.end){
					if(current.stringRepr.charAt(matchPtr++-current.start)!=pattern.charAt(pointer++)){
						return new int[0];
					}
					if(pointer>=pattern.length()){
						indexLeaves = findAllLeaves(current);
						int[] res = new int[indexLeaves.size()];
						for(int i=0;i<res.length;i++){
							res[i]=indexLeaves.get(i).start;
						}
						return res;
					}
				}
			}else{
				return new int[0];
			}
		}
		throw new IllegalStateException();
	}
	
	/**
	 * Given a node, find all leaf nodes in the subtree of this node.
	 * @param n
	 * @return
	 */
	public static List<SuffixTreeNode> findAllLeaves(SuffixTreeNode n){
		List<SuffixTreeNode> leaves = new ArrayList<SuffixTreeNode>();
		boolean done =false;
		LinkedList<SuffixTreeNode> queue = new LinkedList<SuffixTreeNode>();
		queue.add(n);
		while(!done){
			done = true;	
			LinkedList<SuffixTreeNode> innerQueue = new LinkedList<SuffixTreeNode>();
			while(queue.size()>0){
				SuffixTreeNode current = queue.poll();
				if(n.children.size()>0){
					for(SuffixTreeNode child: current.children){
						if(child.isLeaf){
							leaves.add(child);
						}else{
							innerQueue.add(child);
							done = false;
						}		
					}						
				}
			}
			while(innerQueue.size()>0)
				queue.add(innerQueue.poll());		
		}
		Collections.sort(leaves,new Comparator<SuffixTreeNode>(){
			public int compare(SuffixTreeNode o1, SuffixTreeNode o2) {
				return o1.start - o2.start;
			}			
		});
		return leaves;
	}

	// =====================================================================
	public static int[] matchingStatistics(String text, String pattern){
		SuffixTree st = new SuffixTree(pattern);
		//st.root.parent = st.root;
		st.root.suffixLink = st.root;
		SuffixTreeNode current = st.root;
		st.root.cut = -1;
		int position = -1;
		int[] ms = new int[text.length()];
		for(int i=0;i<text.length();i++){
			int count = 0;
			current = current.parent.suffixLink;	
			position = current.cut;
			log.debug("Now i = "+i+" current = "+current);
			for(int j=i;j<text.length();j++){
					
				if(current.end == position){
					SuffixTreeNode child = st.searchEndChild(current, text.charAt(j));
					if(child==null){
						ms[i] = count;
						log.debug("ms["+i+"] = "+ms[i]);
						break;
					}
					current = child;
					position = child.cut;
					count++;
					if(j<text.length()-1)
						continue;
				}
				if(text.charAt(j)!=pattern.charAt(++position)){
					ms[i] = count;
					log.debug("ms["+i+"] = "+ms[i]);
					break;
				}else{
					count++;
				}
				ms[i] = count;
				log.debug("ms["+i+"] = "+ms[i]);
			}
		}
		return ms;
		
	}
	
	/**
	 * Find all patterns in the text that has at most k differences to the given pattern (the order of chars cannot be different). 
	 * @param text
	 * @param pattern
	 * @param k
	 * @return
	 */
	public static String[] kMismatch(String text, String pattern, int k){
		SuffixTree st = new SuffixTree(new ArrayList<String>(Arrays.asList(text,pattern)));
		List<String> res = new ArrayList<String>();
		//int j=0;//start of pattern	
		for(int i=0;i<text.length();i++){
			int counter = 0;
			int j=0;
			int temp = i;
			while(counter<=k){
				//SuffixTreeNode p = st.findNode(1,j);
				//SuffixTreeNode t = st.findNode(0,temp);
				if(temp>=text.length())
					break;
				int len = Utils.longestCommonExtention(st, 0, 1, temp, j);
				if(j+len==pattern.length()){
					res.add(text.substring(i,i+pattern.length()));
					break;
				}else{
					j=j+len+1;
					temp=temp+len+1;
					counter++;
				}
			}	
		}
		return res.toArray(new String[res.size()]);
	}
	
	/**
	 * Find all common substrings 
	 * @param input input strings
	 * @return all common substrings
	 */
	public static List<String> getAllSharedSubstring(List<String> input){
		SuffixTree st = new SuffixTree(input);
		List<String> result = new ArrayList<String>();
		for(SuffixTreeNode node: st.getInternalNodes()){
			if(node.stringIndex.size()==input.size()){
				result.add(node.stringRepr);
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//List<String> result = longestRepeat("sdssgsgsds");
		// List<String> result = LP("xiaotoai");
		// List<String> result = LCS("ryutabab","ryuabab","abstabab");
		//System.out.println("Result: " + result);
		//String s = "abcxabcdex";
		//int[] ms = matchingStatistics(s,"wyabcwzqabcdw");
		//int[] ms = findSubstringIndex("xyxyx","xyx");
		String[] ms = kMismatch("abentbananaend","bend",2);
		//String[] ms = kMismatch("abab","ana",1);
		System.out.println(Arrays.toString(ms));
		
	}

}
