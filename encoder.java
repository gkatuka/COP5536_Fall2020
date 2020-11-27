import java.util.*; 

import javax.swing.text.html.HTMLDocument.Iterator;

import java.io.*;

class Binary4WayNode{
	int frequency;
    int element;
	Binary4WayNode leftChild;
	Binary4WayNode rightChild;

    public Binary4WayNode(int elm, int val){
        this.element=elm;
        this.frequency=val;
    }
    public Binary4WayNode(int val){
        this.frequency = val;
        this.element=-1;
    }
	public Binary4WayNode(int  elm, int freq, Binary4WayNode left, Binary4WayNode right){
		this.element =  elm;
		this.frequency = freq;
		this.leftChild = left;
		this.rightChild = right;
	}
}


class FourWayHeap{
	Binary4WayNode[] minHeap;
	int count;
	int heapSize;
	int order=4;
	
	public FourWayHeap(int size){
		this.heapSize = size+3;
		this.count = 0;
		this.minHeap = new Binary4WayNode[heapSize];
	}
	
	public void insert(Binary4WayNode node){
		if(count == heapSize-3){
			resizeHeap();
		}
		int index = ++count+2;
		while(index >3 && node.frequency < minHeap[(index/4)+2].frequency){
			minHeap[index] = minHeap[(index/4)+2];
			index = (index/4)+2;
		}
		minHeap[index] = node;
	}
	
	public void resizeHeap(){
		Binary4WayNode[] temp = minHeap;
		minHeap = new Binary4WayNode[2*heapSize];
		for(int i=0;i<temp.length;i++){
			minHeap[i] = temp[i];
		}
	}
	
	public Binary4WayNode getMin(){
		if(count == 0){
			return null;
		}
		return minHeap[0];
	}
	
	public Binary4WayNode extractMin(){
		if(count == 0){
			return null;
		}
		Binary4WayNode node = minHeap[3];
		minHeap[3] = minHeap[count+2];
		minHeap[count+2] = null;
		count--;
		shiftDown(1);
		return node;
	}
	
	public void shiftDown(int index){
		int child;
		Binary4WayNode temp = minHeap[index+2];
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
class Read4WayInput{
	
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


public class encoder {
	
	static String ENCODEDFILE = "encoded.bin";
	static String CODE_TABLE_FILE = "code_table.txt";
	static String INPUT_FILE = ""; 
	
	public static void main(String[] args)
	{
		INPUT_FILE = args[0];	
		//INPUT_FILE = "/Users/gloriakatuka/COP5536Fall2020/COP5536Fall2020/src/sample_input_small.txt";
		try{
			/*if(args.length<1){
				System.out.println("No input provided for Input Filename.");
				return;
			}*/
			long startTimeEncoder = System.currentTimeMillis();
			encoder encoder = new encoder();
			
			//File f = new File(INPUT_FILE);
			Read4WayInput in = new Read4WayInput();
			Vector<Integer> input = in.readInput(INPUT_FILE);
			
			HashMap<Integer, Integer> hash = in.frequency_table(input);
			
			Binary4WayNode huffmanRoot = encoder.buildHuff4Cache(hash);
			
			HashMap<Integer, String> codeTable = encoder.codeTableGenerator(huffmanRoot);
			
			encoder.codeTableWriter(codeTable);
			
			encoder.encodedBinaryWriter(codeTable,input);
			long endTimeEncoder = System.currentTimeMillis();
			System.out.println("The elapsed time for Encoder is "+
			(float)(endTimeEncoder-startTimeEncoder)/1000+" s");
			
			System.out.println("Encoded file created: " + ENCODEDFILE);
			System.out.println("Code Table file created: " + CODE_TABLE_FILE);
			System.out.println("Time for encoding (milliseconds): "+(endTimeEncoder-startTimeEncoder));			
		} catch (Exception e) {
			System.out.println("File not found at the specified location: " + args[0]);
		}
	}					
	public Binary4WayNode buildHuff4Cache(HashMap<Integer, Integer> hash){
		FourWayHeap heap = new FourWayHeap(hash.size());
		for(Map.Entry<Integer, Integer> entry : hash.entrySet()){
			Binary4WayNode node = new Binary4WayNode(entry.getKey(), entry.getValue(), null, null);
			heap.insert(node);
		}
		
		while(heap.count > 1){
			Binary4WayNode node1 = heap.extractMin();
			Binary4WayNode node2 = heap.extractMin();
			heap.insert(new Binary4WayNode(-1, node1.frequency + node2.frequency, 
					node1.frequency < node2.frequency ? node1:node2, node1.frequency < node2.frequency? node2:node1));
		}
		Binary4WayNode  root = heap.extractMin();
		return root;
	}
	
	 public HashMap<Integer, String> codeTableGenerator(Binary4WayNode node) {
	        HashMap<Integer, String> map = new HashMap<Integer, String>();
	        codeTableGenerator(node, map, "");
	        return map;
	     }
	public void codeTableGenerator(Binary4WayNode Root, HashMap<Integer, String> hMap, String code) {
		Binary4WayNode root= Root;
		if(root==null)
			return;
		if(root.leftChild==null && root.rightChild==null){
			 hMap.put(root.element, code);
		}
		codeTableGenerator(root.leftChild, hMap, code + '0');
		codeTableGenerator(root.rightChild, hMap, code + '1' );		
	}
	public void codeTableWriter(Map<Integer, String> codeMap) {
  		BufferedWriter bw = null;
  		FileWriter fw = null;
  		StringBuilder content =new StringBuilder("");
  		try {
  			for(Map.Entry<Integer, String> code : codeMap.entrySet()){
  				content.append(code.getKey());
  				content.append(" ");
  				content.append(code.getValue());
  				content.append("\n");
  			}
  			fw = new FileWriter(CODE_TABLE_FILE);
  			bw = new BufferedWriter(fw);
  			bw.write(content.toString());
  		} catch (IOException e) {
  			e.printStackTrace();
  		} finally {
  			try {
  				if (bw != null){
  					bw.close();
  				}

  				if (fw != null){
  					fw.close();
  				}

  			} catch (IOException ex) {
  				ex.printStackTrace();
  			}

  		}
  	}
  	
  	public void encodedBinaryWriter(Map<Integer, String> codeMap, Vector<Integer> input) {
  		FileOutputStream outputStream= null;
  		File file;
  		try {
			file = new File(ENCODEDFILE);
			outputStream = new FileOutputStream(file);
			
			if(!file.exists()){
				file.createNewFile();
			}
			
			StringBuilder test = new StringBuilder("");
			for(int in : input){
				String code = codeMap.get(in);
				test.append(code);
			}
			
			int index=0;
			byte[] byteArray = new byte[test.length()/8];
			int count=0;
			while(index < test.length()-7){
			       byte nextByte = 0x00;
			       for(int i=0;i<8; i++){
			           nextByte = (byte) (nextByte << 1);
			           nextByte += test.charAt(index+i)=='0'?0x0:0x1;
			       }
			       byteArray[count] = nextByte;
			       count++;
			       index+=8;
			}
			outputStream.write(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
  	}
}
