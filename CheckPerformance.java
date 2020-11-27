import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.io.*;
import java.lang.*;


//this class is used to access the information used to implement the Binary Heap, four way cache and to build the huffman tree  

class BinaryNode {
	int element; //stores element from input file
	int frequency;  //stores frequency of the entry from the file 
	BinaryNode leftChild; //points to the left child of the node
	BinaryNode rightChild; //points to the right child of the node
	
	public BinaryNode(){
		this. element = -1;
		this.frequency = 0;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	public BinaryNode(int elm, int freq){
		this. element = elm;
		this.frequency = 0;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	
	public BinaryNode(int  elm, int freq, BinaryNode left, BinaryNode right){
		this.element =  elm;
		this.frequency = freq;
		this.leftChild = left;
		this.rightChild = right;
	}

}

class BinaryHeap{
	BinaryNode[] minHeap; // the array of minimum binary  heap
	int count; // the number of items in the array/ minHeap
	int heapSize; //the size of the array/minHeap
	int order = 2; //the degree of the node or order of the tree set to 2 for a binary tree 
	
	public BinaryHeap() {
		
	}

	public BinaryHeap(int size)
	{
		this.count= 0; 
		this.heapSize = size+1; 
		this.minHeap = new BinaryNode[heapSize];
	}

	
	public void insert(BinaryNode bNode)
	{
		if(heapSize == 0)
		{
			increaseHeap();
		}
		if(heapSize == minHeap.length) {
			BinaryNode[] temp = new BinaryNode[heapSize * 2];
			for(int i = 0; i < heapSize; i++) {
				temp [i] = minHeap[i];
			}
			minHeap = temp; 
		}
		int index = ++count;
		while(index >1 && bNode.frequency < minHeap[index/2].frequency){
			minHeap[index] = minHeap[index/2];
			index /= 2;
		}
		minHeap[index] = bNode;
	}
	
	public void increaseHeap(){
		BinaryNode[] temp = minHeap;
		minHeap = new BinaryNode[2*heapSize];
		for(int i=0;i<temp.length;i++){
			minHeap[i] = temp[i];
		}
	}
	/*public BinaryNode getMin(){
		if(count == 0){
			return null;
		}
		return minHeap[0];
	}*/
	
	public BinaryNode extractMin(){
		if(count == 0){
			return null;
		}
		BinaryNode node = minHeap[1];
		minHeap[1] = minHeap[count];
		minHeap[count--] = null;
		shiftDown(1);
		
		return node;
	}
	
	public void shiftDown(int index){
		int child;
		BinaryNode temp = minHeap[index];
		while(2*index <= count){
			child = 2 * index;
			if(child < count && minHeap[child+1].frequency < minHeap[child].frequency){
				child++;
			}
			
			if(temp.frequency > minHeap[child].frequency){
				minHeap[index] = minHeap[child];
			}else{
				break;
			}
			index = child;
		}
		minHeap[index] = temp;
	}
	
	
}

//pairing node 
class PairingNode {
		BinaryNode element;
		PairingNode leftChild;
		PairingNode nextSibling;
		PairingNode prev;
		
		public PairingNode(BinaryNode node){
			this.element=node;
			leftChild=null;
			nextSibling=null;
			prev=null;
		}
}
//pairing heap 
class PairingHeap {
				
	private PairingNode root; 
	private PairingNode[] minHeap = new PairingNode[5];
			
	//constructor
	public PairingHeap()
	{
		root = null; 
	}
			
	//check if heap is empty
	public boolean isEmpty() {
	return root == null;
	}
			
	//insert data into heap
	public void insert(BinaryNode bNode)
		{
		PairingNode newNode = new PairingNode(bNode);
		if(root == null){
			root = newNode;
		}else{
			root = CompareAndLinkTwoNodes(root, newNode);
	}
	
	}
	
	public PairingNode CompareAndLinkTwoNodes(PairingNode first, PairingNode second){
		if(first == null && second == null){
			return null;
		}
		if(second == null){
			return first;
		}
		if(second.element.frequency > first.element.frequency){
			second.prev = first;
			first.nextSibling = second.nextSibling;
			if (first.nextSibling != null)
				first.nextSibling.prev = first;
			second.nextSibling = first.leftChild;
			if (second.nextSibling != null)
				second.nextSibling.prev = second;
			first.leftChild = second;
			return first;
					
		} else {
			second.prev = first.prev;
			first.prev = second;
			first.nextSibling = second.leftChild;
			if (first.nextSibling != null)
				first.nextSibling.prev = first;
			second.leftChild = first;
			return second;
		}
	}
			
	private PairingNode mergeSiblings(PairingNode firstSibling) {
		if (firstSibling.nextSibling == null)
			return firstSibling;
		int numSiblings = 0;
		for (; firstSibling != null; numSiblings++) {
			minHeap = doubleHeapSize(minHeap, numSiblings);
			minHeap[numSiblings] = firstSibling;
			firstSibling.prev.nextSibling = null;
			firstSibling = firstSibling.nextSibling;
		}
		minHeap = doubleHeapSize(minHeap, numSiblings);
		minHeap[numSiblings] = null;
		int i = 0;
		for (; i + 1 < numSiblings; i += 2)
			minHeap[i] = CompareAndLinkTwoNodes(minHeap[i], minHeap[i + 1]);
		int j = i - 2;
		if (j == numSiblings - 3)
			minHeap[j] = CompareAndLinkTwoNodes(minHeap[j], minHeap[j + 2]);
		for (; j >= 2; j -= 2)
			minHeap[j - 2] =CompareAndLinkTwoNodes(minHeap[j - 2], minHeap[j]);
		return minHeap[0];
	}
			
	//remove minimum element 
	public BinaryNode removeMin(){
		if(root==null){
			return null;
		}
		BinaryNode node = root.element;
		if(root.leftChild == null){
			root = null;
		}else{
			root = mergeSiblings(root.leftChild);
		}
		return node;
	}
			
	private PairingNode[] doubleHeapSize(PairingNode[] heap, int index) {
		if (index == heap.length) {
			PairingNode[] oldArray = heap;
			heap = new PairingNode[index * 2];
			for (int i = 0; i < index; i++)
				heap[i] = oldArray[i];
		}
		return heap;
	}
			
}


class FourWayCacheHeap{
	BinaryNode[] minHeap;
	int count;
	int heapSize;
	int order=4;
	
	public FourWayCacheHeap(int size){
		this.heapSize = size+3;
		this.count = 0;
		this.minHeap = new BinaryNode[heapSize];
	}
	
	public void insert(BinaryNode node){
		if(count == heapSize-3){
			increaseHeap();
		}
		int index = ++count+2;
		while(index >3 && node.frequency < minHeap[(index/4)+2].frequency){
			minHeap[index] = minHeap[(index/4)+2];
			index = (index/4)+2;
		}
		minHeap[index] = node;
	}
	
	public void increaseHeap(){
		BinaryNode[] temp = minHeap;
		minHeap = new BinaryNode[2*heapSize];
		for(int i=0;i<temp.length;i++){
			minHeap[i] = temp[i];
		}
	}
	
	
	public BinaryNode extractMin(){
		if(count == 0){
			return null;
		}
		BinaryNode node = minHeap[3];
		minHeap[3] = minHeap[count+2];
		minHeap[count+2] = null;
		count--;
		shiftDown(1);
		return node;
	}
	
	public void shiftDown(int index){
		int child;
		BinaryNode temp = minHeap[index+2];
		while((4*index) <= count+2){
			child = 4*index;
			int minChild = child;
			int value = minHeap[child].frequency;
			if(child < count+2 && minHeap[child+1].frequency < minHeap[child].frequency){
				minChild = child+1;
				value = minHeap[child+1].frequency;
			}
			if(child+1 < count+2 && minHeap[child+2].frequency < value){
				minChild = child+2;
				value = minHeap[child+2].frequency;				
			}
			if(child+2 < count+2 && minHeap[child+3].frequency < value){
				minChild = child+3;
				value = minHeap[child+3].frequency;
			}			
			
			if(temp.frequency > value){
				minHeap[index+2] = minHeap[minChild];
			}else{
				break;
			}
			index = minChild-2;
		}
		minHeap[index+2] = temp;
	}	
}
		
//read input from file
class Read_Input{
	
	public Vector<Integer> readInput(String fileName){
		Vector<Integer> input = new Vector<Integer>();
		BufferedReader buffer = null;
		FileReader fileReader = null;
		
		try{
			fileReader = new FileReader(fileName);
			buffer = new BufferedReader(fileReader);
			
			String line;
			while((line = buffer.readLine()) != null){
				if(!line.trim().equals("")){
					input.add(Integer.parseInt(line.trim()));				
				}
			}
			
		}catch(FileNotFoundException e){
//			e.printStackTrace();
		}catch(IOException io){
			io.printStackTrace();
		}
		finally{
			try{
				if(buffer != null){
					buffer.close();
				}
				if(fileReader != null){
					fileReader.close();
				}
				
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		
		return input;		
	}	
	
	
	//generate frequency table
	public HashMap<Integer, Integer> frequency_table(Vector<Integer> input){
		HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
		for(int key : input){
			if(hash.containsKey(key)){
				hash.put(key, hash.get(key)+1);
			}else{
				hash.put(key, 1);
			}					
		}
		return hash;
	}
}

public class CheckPerformance {
	
	public static void main(String[] args) throws ArrayIndexOutOfBoundsException {
		//String filename = "sample_input_large.txt";
		String filename = args[0];
		CheckPerformance performanceCheck = new CheckPerformance();
		Read_Input in = new Read_Input();
		Vector<Integer> input = in.readInput(filename);
		HashMap<Integer, Integer> hash = in.frequency_table(input);
		
		
		
		//binary heap
		long startTime = System.currentTimeMillis();
		//System.out.println(startTime);

		for(int i=0;i<10;i++){
			performanceCheck.buildHuffBinHeap(hash);
		}
		long endTime = System.currentTimeMillis();
		//long timeElapsed = endTime-startTime;
		//System.out.println("Time elapsed (millisecond):" + endTime);
		System.out.println("Time using binary heap (millisecond):" + (endTime - startTime)/10);
		
	

		//4-way cache heap
		startTime = System.currentTimeMillis();

		//System.out.println(startTime);
		for(int i=0;i<10;i++){
			performanceCheck.buildHuff4Cache(hash);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time using 4-way heap (millisecond):" + (endTime - startTime)/10);
				
		//pairing heap
		startTime = System.currentTimeMillis();
		for(int i=0;i<10;i++){
			performanceCheck.buildHuffPairHeap(hash);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Time using pairing heap (millisecond):" + (endTime - startTime)/10);
			
		
	}
	
	
	//building Huffman Tree Using Binary Heap
	public BinaryNode buildHuffBinHeap(HashMap<Integer, Integer> hash){
		BinaryHeap heap = new BinaryHeap(hash.size());
		for(Map.Entry<Integer, Integer> entry : hash.entrySet()){
			BinaryNode node = new BinaryNode(entry.getKey(), entry.getValue(), null, null);
			heap.insert(node);
		}
		
		while(heap.count > 1){
			BinaryNode node1 = heap.extractMin();
			BinaryNode node2 = heap.extractMin();
			heap.insert(new BinaryNode(-1, node1.frequency + node2.frequency, 
					node1.frequency < node2.frequency ? node1:node2, node1.frequency < node2.frequency ? node2:node1));
		}
		BinaryNode  root = heap.extractMin();
		return root;
	}
	
	//building Huffman Tree Using 4-way Cache Heap	
	public BinaryNode buildHuff4Cache(HashMap<Integer, Integer> hash){
		FourWayCacheHeap heap = new FourWayCacheHeap(hash.size());
		for(Map.Entry<Integer, Integer> entry : hash.entrySet()){
			BinaryNode node = new BinaryNode(entry.getKey(), entry.getValue(), null, null);
			heap.insert(node);
		}
		
		while(heap.count > 1){
			BinaryNode node1 = heap.extractMin();
			BinaryNode node2 = heap.extractMin();
			heap.insert(new BinaryNode(-1, node1.frequency + node2.frequency, 
					node1.frequency < node2.frequency ? node1:node2, node1.frequency < node2.frequency ? node2:node1));
		}
		BinaryNode  root = heap.extractMin();
		return root;
	}

	//building Huffman Tree Using Pairing Heap	
	public BinaryNode buildHuffPairHeap(HashMap<Integer, Integer> hash){
		PairingHeap pairingHeap = new PairingHeap();
		for(Map.Entry<Integer, Integer> entry : hash.entrySet()){
			BinaryNode node = new BinaryNode(entry.getKey(), entry.getValue(), null, null);
			pairingHeap.insert(node);
		}
		
		while(!pairingHeap.isEmpty()){
			BinaryNode node1 = pairingHeap.removeMin();
			BinaryNode node2 = pairingHeap.removeMin();
			if(node1 !=null && node2!=null){
				pairingHeap.insert(new BinaryNode(-1, node1.frequency + node2.frequency, 
						node1.frequency < node2.frequency ? node1:node2, node1.frequency < node2.frequency ? node2:node1));				
			}else{
				if(node1==null){
					return node2;
				}else{
					return node1;
				}
			}
		}
		return null;
	}
}		
	
	

