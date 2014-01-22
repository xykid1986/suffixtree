package com.kiwii.suffixtree.services;

import org.junit.Assert;
import org.junit.Test;

import com.kiwii.suffixtree.suffixtree.StringMatch;

import static com.kiwii.suffixtree.suffixtree.DPUtils.*;

public class DPUtilsTest {

	@Test
	public void testLongestCommonSubsequenceLength(){
		Assert.assertEquals(longestCommonSubsequenceLength("tctgatgc","ggcagtct"),4);
		Assert.assertEquals(longestCommonSubsequenceLength("Thisismyway","Thesearemine"),4);
	}
	
	@Test
	public void testLongestCommonSubsequence(){
		String[] res = longestCommonSubsequence("tctgatgc","ggcagtct");
		Assert.assertEquals(res.length,5);
		Assert.assertEquals(res[0],"catc");
		Assert.assertEquals(res[1],"gagc");
		Assert.assertEquals(res[2],"cagc");
		Assert.assertEquals(res[3],"gatc");
		Assert.assertEquals(res[4],"cgtc");
	}
	
	@Test
	public void testStringEditDistance(){
		StringMatch[] sm = stringEditDistance("writers","vintner");
		Assert.assertEquals(sm.length, 3);
		Assert.assertEquals(sm[0],new StringMatch("writ-ers","vintner-",5));
		Assert.assertEquals(sm[1],new StringMatch("wri-t-ers","-vintner-",5));
		Assert.assertEquals(sm[2],new StringMatch("wri-t-ers","v-intner-",5));
	}
}
