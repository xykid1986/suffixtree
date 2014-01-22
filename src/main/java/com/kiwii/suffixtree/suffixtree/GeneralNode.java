package com.kiwii.suffixtree.suffixtree;

import java.util.ArrayList;
import java.util.List;


public abstract class GeneralNode {
	protected int key;
	protected final List<GeneralNode> children = new ArrayList<GeneralNode>();
	protected GeneralNode parent;

	protected GeneralNode I; // the node in subtree with maximum height(preorder)
	protected int A; //
	protected int preorder; // the preorder number of this node


	@Override
	public String toString() {
		return key + " ";
	}

	@Override
	public boolean equals(Object n) {
		if (!(n instanceof GeneralNode))
			return false;
		return this.key == ((GeneralNode) n).key;
	}
}
