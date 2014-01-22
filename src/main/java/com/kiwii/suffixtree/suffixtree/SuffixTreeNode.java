package com.kiwii.suffixtree.suffixtree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuffixTreeNode extends GeneralNode {
	//the key of the suffixtreenode is the start.
	final int start;//the key of the node
	final int end;//the lower bound of the edge. for leaf nodes it's also the end of the string.
	final String stringRepr;
	int cut;//the upper bound of the edge. useless when node is active
	int position;//current index on its host. useless when node is leaf
	SuffixTreeNode host;
	final List<Integer> stringIndex = new ArrayList<Integer>();
	final Set<SuffixTreeNode> children = new HashSet<SuffixTreeNode>();
	SuffixTreeNode parent;
	boolean visited;
	boolean isLeaf;
	int preorder;
	public int getStart() {
		return start;
	}

	public String getStringRepr() {
		return stringRepr;
	}

	public int getCut() {
		return cut;
	}

	public void setCut(int cut) {
		this.cut = cut;
	}

	public SuffixTreeNode getParent() {
		return parent;
	}

	public void setParent(SuffixTreeNode parent) {
		this.parent = parent;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public int getPreorder() {
		return preorder;
	}

	public void setPreorder(int preorder) {
		this.preorder = preorder;
	}

	public SuffixTreeNode getI() {
		return I;
	}

	public void setI(SuffixTreeNode i) {
		I = i;
	}

	public int getA() {
		return A;
	}

	public void setA(int a) {
		A = a;
	}

	public int getEnd() {
		return end;
	}


	SuffixTreeNode I;//highest node in the run, use in LCA algorithm
	int A;//recording the hight(1 bit at that hight) met from root to this node.
	SuffixTreeNode suffixLink;//for now, suffix link is only used to build non-general suffxi tree
	SuffixTreeNode buildLink;
	
	public SuffixTreeNode(int start, int end, int stringIndex, String incomingString) {
		this(start, end, new ArrayList<Integer>(), incomingString);
		this.stringIndex.add(stringIndex);
	}
	
	public SuffixTreeNode(int start, int end, List<Integer> stringIndecies, String incomingString) {
		super.I=this;
		this.start = start;
		this.end = end;
		if(start>=0&&end>=0)
			this.stringRepr = incomingString.substring(start, end+1);
		else
			this.stringRepr = incomingString;
		for(Integer i: stringIndecies){
			if(!stringIndex.contains(i))
				stringIndex.add(i);
		}
			
		this.cut = start;
		position = cut;
	}
	
	public int getFirstStringIndex(){
		return stringIndex.get(0);
	}
	
	public void addStringIndex(int i){
		if(!stringIndex.contains(i))
			stringIndex.add(i);
	}
	
	protected boolean isImplicit() {
		return this.position != host.end;
	}
	
	public void addChild(SuffixTreeNode node){
		children.add(node);
	}
	
	public boolean removeChild(SuffixTreeNode node){
		return children.remove(node);
	}
	
	public List<Integer> getAllStringIndex(){
		return stringIndex;
	}
	
	@Override
	public String toString(){
		if(start<0) return "ROOT";
		StringBuilder sb = new StringBuilder("<");
		for(Integer i: stringIndex){
			sb.append(i+",");
		}
		sb = sb.deleteCharAt(sb.length()-1);
		//sb.append(">("+position+")"+"["+cut+","+end+"]:"+"{"+start+","+end+"}");
		if(cut<start){
			throw new IllegalStateException("Cut is larger than start! cut:"+cut+" start:"+start);
		}
		sb.append(">("+position+")"+"["+stringRepr.substring(cut-start)+"]:"+"{"+stringRepr+"}");
		/*show suffix link
		if(suffixLink!=null && suffixLink!=this)
			sb.append("->"+suffixLink.toString());*/
		return sb.toString();
	}

	@Override
	public boolean equals(Object n){
		if(!(n instanceof SuffixTreeNode))
			return false;
		for(Integer i: ((SuffixTreeNode)n).stringIndex){
			if(!stringIndex.contains(i)){
				return false;
			}
		}
		//return start==((SuffixTreeNode)n).start && cut == ((SuffixTreeNode)n).cut && end==((SuffixTreeNode)n).end;
		return start==((SuffixTreeNode)n).start && end==((SuffixTreeNode)n).end && cut == ((SuffixTreeNode)n).cut && parent == ((SuffixTreeNode)n).parent;
	}
	
	
	@Override
	public int hashCode(){
		return start+end*13;
	}

}
