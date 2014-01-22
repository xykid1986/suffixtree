package com.kiwii.suffixtree.services;


import org.junit.Assert;
import org.junit.Test;

import com.kiwii.suffixtree.suffixtree.SuffixTree;
import com.kiwii.suffixtree.suffixtree.SuffixTreeNode;

public class SuffixTreeTest {

	@Test
	public void testLowestCommonAncestor() {
		SuffixTree st = new SuffixTree("banana");
		st.preProcessTree();
		//test 1
		SuffixTreeNode x = st.findNode(2);
		SuffixTreeNode y = st.findNode(4);
		SuffixTreeNode z = st.lowestCommonAncestor(x, y);
		Assert.assertEquals(z.getPreorder(),4);
		Assert.assertEquals(z.getStart(),2);	
		//test 2
		x=st.findNode(1);
		y=st.findNode(5);
		z = st.lowestCommonAncestor(x, y);
		Assert.assertEquals(z.getPreorder(),7);
		Assert.assertEquals(z.getStart(),1);	
		Assert.assertEquals(z.getEnd(),1);	
	}

}
