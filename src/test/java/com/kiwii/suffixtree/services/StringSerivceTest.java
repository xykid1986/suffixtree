package com.kiwii.suffixtree.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import static com.kiwii.suffixtree.suffixtree.StringService.*;

import com.kiwii.suffixtree.suffixtree.StringService;

public class StringSerivceTest {

	@Test
	public void testLongestPalindrome() {
		String test1 = "xiti";
		String test2 = "xiaotoaix";
	    String test3 = "iaotoaix";
	    String test4 = "sexiaotoaixti";
	    String test5 ="deal with host split problxiaotoaixem. Modified StringServices methods";
	    String test6 = "abcdecba";
	    String test7 = "aaaacb";
	    Assert.assertEquals(StringService.longestPalindrome(test1).get(0),"iti");
	    Assert.assertEquals(StringService.longestPalindrome(test2).get(0),"xiaotoaix");
	    Assert.assertEquals(StringService.longestPalindrome(test3).get(0),"iaotoai");
	    Assert.assertEquals(StringService.longestPalindrome(test4).get(0),"xiaotoaix");
	    Assert.assertEquals(StringService.longestPalindrome(test5).get(0),"xiaotoaix");
	    Assert.assertEquals(StringService.longestPalindrome(test6).size(), 0);
	    Assert.assertEquals(StringService.longestPalindrome(test7).get(0), "aaaa");
	}

	@Test
	public void testLongestCommonSubstring() {
		List<String> test1 = new ArrayList<String>(Arrays.asList("ryutabab","ryuabab","abstabab"));
		List<String> test2 = new ArrayList<String>(Arrays.asList("ababryut","ryababu","abstabab"));
		List<String> test3 = new ArrayList<String>(Arrays.asList("ababryut","ababryu","abstabab"));
		List<String> test4 = new ArrayList<String>(Arrays.asList("ababryut","ababryu","abstabac"));
		List<String> test5 = new ArrayList<String>(Arrays.asList("abab","tabab","abac"));
		List<String> test6 = new ArrayList<String>(Arrays.asList("一加一等于几","1加1等于几","1023加25等于多少"));
		Assert.assertEquals(StringService.longestCommonSubstring(test1).get(0), "abab");
		Assert.assertEquals(StringService.longestCommonSubstring(test2).get(0), "abab");
		Assert.assertEquals(StringService.longestCommonSubstring(test3).get(0), "abab");
		Assert.assertEquals(StringService.longestCommonSubstring(test4).get(0), "aba");
		Assert.assertEquals(StringService.longestCommonSubstring(test5).get(0), "aba");
		Assert.assertEquals(StringService.longestCommonSubstring(test6).get(0), "等于");
	}

	@Test
	public void testLongestRepeat() {
		String test1 = "sdssds";
		String test2 = "sdsssds";
		String test3 = "sdssgsgsds";
		//the tests may fail because of the order of the results
		Assert.assertEquals(StringService.longestRepeat(test1, 2).get(0), "sds");
		Assert.assertEquals(StringService.longestRepeat(test2, 2).get(0), "sds");
		Assert.assertEquals(StringService.longestRepeat(test3, 2).size(), 2);
		Assert.assertEquals(StringService.longestRepeat(test3, 2).get(0), "sgs");
		Assert.assertEquals(StringService.longestRepeat(test3, 2).get(1), "sds");
		
	}
	@Test
	public void testMatchingStatistics(){
		String test1 = "anbcabana";
		int[] res1 = StringService.matchingStatistics(test1, "banana");
		Assert.assertEquals(res1[0],2);
		Assert.assertEquals(res1[1],1);
		Assert.assertEquals(res1[2],1);
		Assert.assertEquals(res1[3],0);
		Assert.assertEquals(res1[4],1);
		Assert.assertEquals(res1[5],4);
		Assert.assertEquals(res1[6],3);
		Assert.assertEquals(res1[7],2);
		Assert.assertEquals(res1[8],1);
		String test2 = "abcxabcdex";
		int[] res2 = StringService.matchingStatistics(test2, "wyabcwzqabcdw");
		Assert.assertEquals(res2[0],3);
		Assert.assertEquals(res2[4],4);
		
	}
	@Test
	public void testFindSubStringIndex(){
		int[] test1 = StringService.findSubstringIndex("banana", "an");
		Assert.assertEquals(test1.length,2);
		Assert.assertEquals(test1[0],1);
		Assert.assertEquals(test1[1],3);	
		int[] test2 = StringService.findSubstringIndex("xyxyx", "xyx");
		Assert.assertEquals(test2.length,2);
		Assert.assertEquals(test2[0],0);
		Assert.assertEquals(test2[1],2);
	}
	@Test
	public void testKMismatch(){
		Assert.assertEquals(kMismatch("banana","aba",1).length,1);
		Assert.assertEquals(kMismatch("banana","tab",2).length,2);
		Assert.assertEquals(kMismatch("banana","tab",2)[0],"ban");
		Assert.assertEquals(kMismatch("banana","tab",2)[1],"nan");
		Assert.assertEquals(kMismatch("abentbananaend","bend",2).length,3);
		Assert.assertEquals(kMismatch("abentbananaend","bend",2)[0],"bent");
		Assert.assertEquals(kMismatch("abentbananaend","bend",2)[1],"bana");
		Assert.assertEquals(kMismatch("abentbananaend","bend",2)[2],"aend");
		
	}
}
