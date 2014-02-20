package com.kiwii.suffixtree.suffixtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;



public class SuffixTree {

	private static final Logger log = Logger.getLogger(SuffixTree.class);
	final List<String> inputs = new ArrayList<String>();
	SuffixTreeNode root;
	// private int numInternalNodes;
	final Set<SuffixTreeNode> leafNodes = new HashSet<SuffixTreeNode>();
	final Set<SuffixTreeNode> internalNodes = new HashSet<SuffixTreeNode>();
	private SuffixTreeNode nextSuffixLink;
	private boolean isProcessedForLCA;
	private int size;
	
	//same as constructor
	public void initialize(List<String> list){
		for (String s : list) {
			inputs.add(s + "$");
		}
		root = new SuffixTreeNode(-2, -1, 0, "");
		root.host = root;
		root.parent = root;
		createSuffixTree();
	}

	public SuffixTree(){}
	
	// create suffix tree of one string
	public SuffixTree(String s) {
		inputs.add(s + "$");
		root = new SuffixTreeNode(-2, -1, 0, "");
		root.host = root;
		root.parent = root;
		createSuffixTree();
	}

	// create suffix tree with multiple strings
	public SuffixTree(List<String> list) {
		for (String s : list) {
			inputs.add(s + "$");
		}
		root = new SuffixTreeNode(-2, -1, 0, "");
		root.host = root;
		root.parent = root;
		createSuffixTree();
	}

	public SuffixTreeNode getRoot() {
		return root;
	}

	public Set<SuffixTreeNode> getLeafNodes() {
		return leafNodes;
	}

	public Set<SuffixTreeNode> getInternalNodes() {
		return internalNodes;
	}
	public List<String> getInputs(){
		return inputs;
	}

	// ##############################
	// ## Suffix tree construction ##
	// ##############################
	private void createSuffixTree() {
		log.debug("Creating suffix tree");
		for (int i = 0; i < inputs.size(); i++) {
			//log.info("Now processing "+i+"th word.");
			log.debug("Now processing: " + inputs.get(i)); 
			if(inputs.get(i)==null||inputs.get(i).length()==0)
				continue;
			for (int j = 0; j < inputs.get(i).length(); j++) {
				log.debug("Now adding: " + inputs.get(i).charAt(j));//
				nextSuffixLink = null;//always the end suffix link from start
				updateActiveNode(root, j, i);
				log.debug("========================================================");
			}
			//add(inputs.get(i),i);
			finishingTree(i);
			log.debug("========================================================");
		}
		
		if(log.isDebugEnabled()){
			showLeafNodes();
			showInternalNodes();
			//showBuildChain();
		}
	}
	/*
	public void add(String input, int inputIndex){
		if(inputs.get(inputIndex)==null||inputs.get(inputIndex).length()==0)
			return;
		size++;
		input=input+"$";
		log.debug("Adding input string :"+inputs.get(inputIndex));
		for (int j = 0; j < inputs.get(inputIndex).length(); j++) {
			log.debug("Now adding: " + inputs.get(inputIndex).charAt(j));//
			nextSuffixLink = null;//always the end suffix link from start
			updateActiveNode(root, j, inputIndex);
			log.debug("========================================================");
		}
	}
	
	public void add(String input){
		add(input, size);
	}
	*/

	private void updateActiveNode(SuffixTreeNode current, int incoming,
			int stringIndex) {
		if (current == null || current.isLeaf == true)
			return;
		//In case the origin host is split 
		if(current.position<current.host.cut)
			current.host = current.host.parent;
		SuffixTreeNode host = current.host;
		String currentString = inputs.get(stringIndex);
		char incomingChar = currentString.charAt(incoming);	
		log.debug("Now updating: " + current);
		if (current != root && current.isImplicit()) {
			log.debug(current + " is implicit"); 

			char nextChar = inputs.get(host.getFirstStringIndex()).charAt(
					current.position + 1);
			if (nextChar == incomingChar) {
				current.position = current.position + 1;
				log.debug("Move down on the host, increment position to "+current);
			} else {
				log.debug("No endChild found, split");
				split(current, incoming, stringIndex);
			}
		} else {
			log.debug(current + " is explicit");
			if (!host.stringIndex.contains(stringIndex))
				host.stringIndex.add(stringIndex);
			SuffixTreeNode endChild = searchEndChild(host, incomingChar);
			if(current==root){//need to create a current active node as root cannot be manipulated
				current = new SuffixTreeNode(incoming,currentString.length()-1,stringIndex,inputs.get(stringIndex));
				//current.position = incoming-1;
				current.host = root;
				//suffix links created here and only here			
				current.buildLink = root.buildLink;			
				root.buildLink = current;
				/*
				current.suffixLink = root;
				if(current.backSuffixLink!=null)
					current.backSuffixLink.suffixLink = current;
					*/
			}
			if (endChild == null) {
				log.debug("No endChild found, current active node becomes leaf");
				current.cut = incoming;
				current.parent = host;
				current.isLeaf = true;
				current.position = current.cut;
				host.addChild(current);
				log.debug("New leaf node created: " + current);
				log.debug("Adding leaf node: "+current);
				//deal with suffix link
				if(nextSuffixLink==null)
					current.parent.suffixLink=current.parent.parent;
				else
					current.parent.suffixLink = nextSuffixLink;
				nextSuffixLink = current.parent;
				//end suffix link 
				leafNodes.add(current);			
			} else {
				current.host = endChild;
				current.position = endChild.cut;
				if(host==root)
					log.debug("New active node created: "+current);
				else
					log.debug("EndChild found, move active node to " + endChild
						+ ", change active node's position"+current);
			}
		}
		
		updateActiveNode(current.buildLink, incoming, stringIndex);

	}

	public void split(SuffixTreeNode current, int incoming, int stringIndex) {

		
		SuffixTreeNode host = current.host;
		SuffixTreeNode parent = host.parent;
		//String currentString = inputs.get(stringIndex);
		
		//create new internal node
		SuffixTreeNode internalNode = new SuffixTreeNode(host.start,current.position, host.getAllStringIndex(), inputs.get(host.stringIndex.get(0)));
		internalNode.addStringIndex(stringIndex);
		internalNode.cut = host.cut;
		//internalNode.position = current.position+1;
		internalNode.parent = parent;
		log.debug("Adding internal node: "+internalNode);
		internalNodes.add(internalNode);
		//internal node becomes the parent of the new leaf node and the host node
		internalNode.addChild(current);
		internalNode.addChild(host);
		if(nextSuffixLink==null)
			internalNode.suffixLink=internalNode.parent;
		else
			internalNode.suffixLink = nextSuffixLink;
		nextSuffixLink = internalNode;
		//current active node becomes leaf node
		current.cut = incoming;//when active node becomes leaf node, the cut is always the incoming
		current.parent = internalNode;
		current.isLeaf = true;
		current.host = null;
		log.debug("Adding leaf node: "+current);
		leafNodes.add(current);
		
		//host becames the child of the internal node, change cut	
		if(parent.removeChild(host)){//remove the host before the cut changes
			log.debug("Removed child node "+host+" from parent "+parent);
		}
		host.cut = current.position+1;
		host.parent = internalNode;
		
		parent.addChild(internalNode);
		
		log.debug("split result: "+parent+"->"+internalNode+": "+current+" and "+ host+" No.children of internalNode:"+internalNode.children.size());
	}

	public SuffixTreeNode searchEndChild(SuffixTreeNode host,int incomingChar) {
		for(SuffixTreeNode child: host.children){
			log.debug("searching end child, now looking at: "+child);
			char nextChar = inputs.get(child.getFirstStringIndex()).charAt(child.cut);
			if(nextChar == incomingChar){
				return child;
			}
		}
		return null;
	}
    //As we use $ as the ending char, need to consider. Also remove all buildLinks used for this input string
	private void finishingTree(int stringIndex) {
		log.debug("Finishing tree");
		if(root.buildLink==null)
			return;
		SuffixTreeNode current = root.buildLink;
		SuffixTreeNode previous = root;
		while(current!=null){
			if(current.host == null){
				
			}
			else if(current.host.cut==current.host.end){
				log.debug("Working on the end '$' node");
				//TODO: find bug here
				current.host.addStringIndex(stringIndex);
			}
			else if(current.isLeaf==false && current.position == current.host.end){
				log.debug("Split ending nodes: "+current+" and "+current.host);
				//split(current,--current.position,stringIndex);
				current.position--;
				split(current,inputs.get(stringIndex).length()-1,stringIndex);
			}
			previous.buildLink = null;
			previous = current;
			current = current.buildLink;	
		}
	}
	
	// ##############################
	// ## Suffix tree analysis     ##
	// ##############################
	public long size(){
		return this.size;
	}
	
	public void showLeafNodes(){
		System.out.println("All leaf nodes: ");
		for(SuffixTreeNode n: leafNodes){
			System.out.println(n+" is leaf node? "+n.isLeaf);
		}
	}
	public void showInternalNodes(){
		System.out.println("All internal nodes: ");
		for(SuffixTreeNode n: internalNodes){
			System.out.println(n+" is leaf node? "+n.isLeaf+" No.Children:"+n.children.size());
		}
	}
	public void showBuildChain(){
		System.out.println("BackSuffixChain: ");
		SuffixTreeNode current = root;
		while(current!=null){
			System.out.println(current);
			current = current.buildLink;
		}
	}
	/*
	public SuffixTreeNode findNode(int start, int end){
		if(start<0||end<0||start>end)
			throw new IllegalArgumentException();
		SuffixTreeNode current = root;
		String s = inputs.get(0);
		int pointer = start;
		while(pointer<=end){
			SuffixTreeNode next = searchEndChild(current, s.charAt(pointer));
			if(next!=null){
				current = next;
				pointer += (next.end-next.cut)+1;
			}
		}
		return current;
	}
	*/
	public SuffixTreeNode findNode(int stringIndex, int start, int end){
		if(stringIndex<0||start<0||end<0||start>end||start>=inputs.get(stringIndex).length()||end>=inputs.get(stringIndex).length())
			throw new IllegalArgumentException();
		SuffixTreeNode current = root;
		String s = inputs.get(stringIndex);
		int pointer = start;
		while(pointer<=end){
			SuffixTreeNode next = searchEndChild(current, s.charAt(pointer));
			if(next!=null){
				current = next;
				pointer += (next.end-next.cut)+1;
			}
		}
		if(current.stringIndex.contains(stringIndex))
			return current;
		else{// when s1 s2 in a general tree has some same suffix, the right node cannot be found  
			for(SuffixTreeNode n : leafNodes){
				if(n.stringIndex.contains(stringIndex) && n.start==start && n.end==end){
					current = n;
					return current;
				}
			}
		}
		throw new IllegalStateException();
	}
	public SuffixTreeNode findNode(int stringIndex, int start){
		return findNode(stringIndex, start, inputs.get(stringIndex).length()-1);
	}
	//only used for one string suffix tree!
	public SuffixTreeNode findNode(int start){
		return findNode(0, start, inputs.get(0).length()-1);
	}
	
	/**
	 * Trance a str on this suffix tree, return the node it ends at. 
	 * @param str
	 * @return
	 */
	public SuffixTreeNode traceToNode(String str){
		SuffixTreeNode current = this.root;
		int pointer = 0;
		while(pointer<str.length()){
			SuffixTreeNode next = this.searchEndChild(current, str.charAt(pointer));
			if(next!=null){
				current = next;
				int matchPtr = current.cut;
				while(matchPtr<=current.end){
					if(current.stringRepr.charAt(matchPtr++-current.start)!=str.charAt(pointer++)){
						return null;
					}
					if(pointer>=str.length()){
						return current;
					}
				}
			}else{
				return null;
			}
		}
		throw new IllegalStateException();
	}
	
	public int[] findAllOccurance(String str){
		SuffixTreeNode node = traceToNode(str);
		if(node!=null){
			List<SuffixTreeNode> indexLeaves = new ArrayList<SuffixTreeNode>();
			if(node.isLeaf){
				int[] res = new int[1];
				res[0] = node.getStart();
				return res;
			}
			indexLeaves = StringService.findAllLeaves(node);
			int[] res = new int[indexLeaves.size()];
			for(int i=0;i<res.length;i++){
				res[i]=indexLeaves.get(i).start;
			}
			Arrays.sort(res);
			return res;
		}
		return new int[0];
	}
	
	public void displayProcessedTree(){
		if(root == null)
			return;
		boolean done =false;
		LinkedList<SuffixTreeNode> queue = new LinkedList<SuffixTreeNode>();
		queue.add(root);
		while(!done){
			done = true;	
			LinkedList<SuffixTreeNode> innerQueue = new LinkedList<SuffixTreeNode>();
			while(queue.size()>0){
				SuffixTreeNode n = queue.poll();
				System.out.print(n.preorder+" "+n.stringRepr.substring(n.cut-n.start)+" A:"+Integer.toBinaryString(n.A)+" I:"+n.I.preorder+"  |  ");
				if(n.children.size()>0){
					for(SuffixTreeNode child:n.children){
						//System.out.println("child found:"+child);
						innerQueue.add(child);
					}					
					done = false;
				}
				//System.out.println();
			}
			while(innerQueue.size()>0)
				queue.add(innerQueue.poll());		
			System.out.println();
		}
	}
	
	// ##############################
	// ## Suffix tree process      ##
	// ##############################
	//preprocess suffix tree for finding lowest common ancestor in constant time
	public void preProcessTree(){
		if(this.root==null){
			 log.debug("Empty tree");
			 return;
		 }
		 log.debug("Preprocess Tree : ");
		 
		 log.debug("Setting up the preorder numbers");
		 preorderTraverse();
		 
		 log.debug("Generating I value");
		 generateI();
		 
		 log.debug("Generating A value");
		 generateA(root);
		 
		 log.debug("Preprocess finishsed.");
	}
	
	private void preorderTraverse(){
		int[] counter = new int[1];
		counter[0] = 1;
		preorderHelper(root,counter);
	}
	private void preorderHelper(SuffixTreeNode node, int[] counter){
		node.preorder = counter[0];
		node.I = node;
		counter[0]+=1;
		for(SuffixTreeNode child : node.children){
			preorderHelper(child,counter);
		}
	}
	
	/**
	 * A is the number of distinct run encountered on the path from root to current (inclusive).
	 * @param current Current node
	 */
	private void generateA(SuffixTreeNode current){
		//int maxH = Utils.height(current.I.preorder);
		int parentA = 0;
		if(current.parent!=null){
			parentA = current.parent.A;
		}
		current.A = parentA|(1<<(Utils.height(current.I.preorder)));
		for(SuffixTreeNode child: current.children){
            generateA(child);
		}
	}
	
	/**
	 * I is the reference to the node in its subtree that has the maximum height for preordered number
	 * @param root Root of the tree
	 */
	private void generateI(){
		if(root==null)
			return;
		for(SuffixTreeNode leaf : leafNodes){
			SuffixTreeNode current = leaf;
			//int currentH = height(current.getPreorder());
			while(current!=null && current!=root){
				SuffixTreeNode parent = current.parent;
				//log.debug("current: "+current+"  parent "+parent);
				if(parent==null)
					break;
				if(Utils.height(current.I.preorder)>Utils.height(parent.I.preorder))
					parent.I = current.I;
				current = parent;
			}
		}
	}
	
	public SuffixTreeNode lowestCommonAncestor(SuffixTreeNode x, SuffixTreeNode y){
		if(!isProcessedForLCA)
			preProcessTree();
		SuffixTreeNode _x = null;
		SuffixTreeNode _y = null;
		int b = Utils.LCAinB(x.I.preorder, y.I.preorder);
		int heightZ = Utils.findOneBitLeftPosition(Utils.height(b), x.A, y.A);
		if(heightZ==Utils.height(x.I.preorder)){
			_x = x;
		}else{
			int heightW = Utils.findOneBitRightPosition(heightZ,x.A);
			int IW = Utils.shift(x.I.preorder, heightW);
			SuffixTreeNode temp = x;
			while(temp!=root){
				if(temp.I.preorder==IW){
					_x = findRunLeader(temp);
					break;
				}
				temp = temp.parent;
			}
		}
		if(heightZ==Utils.height(y.I.preorder)){
			_y = y;
		}else{
			int heightW = Utils.findOneBitRightPosition(heightZ,y.A);
			int IW = Utils.shift(y.I.preorder, heightW);
			SuffixTreeNode temp = y;
			while(temp!=root){
				if(temp.I.preorder==IW){
					_y = findRunLeader(temp);
					break;
				}
				temp = temp.parent;
			}
		}
		return _x.preorder<=_y.preorder?_x:_y;
	}
	
	public SuffixTreeNode findRunLeader(SuffixTreeNode current){
		int thisRunHeight = Utils.height(current.I.preorder);
		while(current!=root){
			if(Utils.height(current.I.preorder)!=thisRunHeight){
				break;
			}
			current = current.parent;
		}
		return current;
	}

	public static void main(String[] args) {
		//SuffixTree st = new SuffixTree(Arrays.asList(new String[]{"abentbananaend","bend"}));
		//List<String> list = new ArrayList<String>();
		//list.add("今天我们来看看当今世界上有那些名人");
		//list.add("今天");
		//SuffixTree st = new SuffixTree(list);
		//st.preProcessTree();
		//st.displayProcessedTree();
		//System.out.println(st.findNode(0,11));
		
		//list.add("banana");
		//list.add("ryutabab");
		//list.add("ryuabab");
		//list.add("abstabab");
		//List<String> list = new ArrayList<String>(Arrays.asList("ryutabab","ryuabab","abstabab"));
		//SuffixTree st = new SuffixTree(list);
		//st.preProcessTree();
		//st.displayProcessedTree();
		//SuffixTreeNode x = st.findNode(0,1);
		//SuffixTreeNode y = st.findNode(1,1);
		//System.out.println("To find LCA of "+x+" and "+y);
		//SuffixTreeNode z = st.lowestCommonAncestor(x,y);
		//System.out.println(z+" preorder: "+z.preorder+" start: "+z.start+" end: "+z.end);
		//System.out.println("longest common extention: "+Utils.longestCommonExtention(st,0,1,1,0));
		
		SuffixTree st = new SuffixTree(Arrays.asList("ab","ab","ac"));
		//st.add("abac");
		
	}

}
