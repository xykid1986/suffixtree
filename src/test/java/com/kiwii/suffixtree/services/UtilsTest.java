package com.kiwii.suffixtree.services;

import org.junit.Assert;
import org.junit.Test;

import com.kiwii.suffixtree.suffixtree.Utils;

public class UtilsTest {

	@Test
	public void testHeight() {
		Assert.assertEquals(Utils.height(8),3);
		Assert.assertEquals(Utils.height(10),1);
	}

	@Test
	public void testDepth() {
		Assert.assertEquals(Utils.depth(8),3);
		Assert.assertEquals(Utils.depth(10),3);
		Assert.assertEquals(Utils.depth(4),2);
	}

	@Test
	public void testLCAinB() {
		Assert.assertEquals(Utils.LCAinB(1,3),2);
		Assert.assertEquals(Utils.LCAinB(1,11),8);
		Assert.assertEquals(Utils.LCAinB(1,2),2);
		Assert.assertEquals(Utils.LCAinB(9,15),12);
		Assert.assertEquals(Utils.LCAinB(6,10),8);
		Assert.assertEquals(Utils.LCAinB(4,12),8);
		Assert.assertEquals(Utils.LCAinB(14,15),14);
		Assert.assertEquals(Utils.LCAinB(11,13),12);
	}

	@Test
	public void testShift() {
		Assert.assertEquals(Utils.shift(3,2),4);
		Assert.assertEquals(Utils.shift(5,2),4);
		Assert.assertEquals(Utils.shift(15,3),8);
	}
	
	@Test
	public void testFindOneBitLeftPosition(){
		Assert.assertEquals(Utils.findOneBitLeftPosition(3,14,8),3);
		Assert.assertEquals(Utils.findOneBitLeftPosition(2,11,12),3);
		Assert.assertEquals(Utils.findOneBitLeftPosition(1,11,12),3);
		Assert.assertEquals(Utils.findOneBitLeftPosition(0,11,12),3);
	}
	
	@Test
	public void testFindOneBitRightPosition(){
		Assert.assertEquals(Utils.findOneBitRightPosition(3,14),2);
		Assert.assertEquals(Utils.findOneBitRightPosition(2,14),1);
		Assert.assertEquals(Utils.findOneBitRightPosition(3,10),1);
		Assert.assertEquals(Utils.findOneBitRightPosition(3,9),0);
		Assert.assertEquals(Utils.findOneBitRightPosition(2,9),0);
	}

}
