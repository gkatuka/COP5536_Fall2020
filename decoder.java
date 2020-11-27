import java.io.*; 
import java.util.*; 


class DecBinaryNode{
	int data;
	String code;
	DecBinaryNode left;
	DecBinaryNode right;
	public DecBinaryNode() {
		data=-1;
		code="";
		left=null;
		right=null;
	}
}

class HuffManTree{
	DecBinaryNode root;

	public HuffManTree() {
		root=new DecBinaryNode();
	}

	void decodeTree(ArrayList<DecBinaryNode> DecBinaryNode){
		for(int i=0;i<DecBinaryNode.size();i++){
			DecBinaryNode node=this.root;
			String code=DecBinaryNode.get(i).code;
			for(int j=0;j<code.length();j++){
				if(code.charAt(j)=='0'){
					if(node.left == null)  node.left = new DecBinaryNode();
					node=node.left;
				}
				else {
					if(node.right == null) node.right=new DecBinaryNode();
					node=node.right;
				}
			}
			node.data=DecBinaryNode.get(i).data;
		}
	}

	void decoder(String binary) throws IOException{
		FileInputStream fis=new FileInputStream(binary);
		String decoderWriter="decoded.txt";
		FileWriter fw = new FileWriter(decoderWriter);
		BufferedWriter bw = new BufferedWriter(fw);
		byte[] buffer=null;
		buffer=new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<buffer.length;i++){
			byte b1 = buffer[i];
			String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
			sb.append(s1);
		}
		DecBinaryNode temp=this.root;
		long l=sb.length();
		for(int i = 0; i <= l; i++){
			if(temp.data != -1){
				StringBuilder st = new StringBuilder();
				st.append(temp.data);
				st.append("\n");
				bw.write(st.toString());
				temp=this.root;
				i--;
			}
			else if (i!=l){
				if(sb.charAt(i)=='0') temp=temp.left;
				else temp=temp.right;
			}
		}
	bw.close();
	}
}

public class decoder {
	public static void main(String args[]){
		long startTime = System.currentTimeMillis();

	   String BinaryFile=args[0];
		//String BinaryFile=  "/Users/gloriakatuka/git/COP5536Fall2020/COP5536Fall2020/encoded.bin";
	   FileInputStream fstream = null;
	try {
		fstream = new FileInputStream(args[1]);
		//fstream = new FileInputStream("/Users/gloriakatuka/git/COP5536Fall2020/COP5536Fall2020/code_table.txt");
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	   DataInputStream in = new DataInputStream(fstream);
       BufferedReader br = new BufferedReader(new InputStreamReader(in));
       ArrayList<DecBinaryNode> DecBinaryNodes=new ArrayList<>();
	   String str;
          try {
			while ((str= br.readLine()) != null)   {
			         String[] tokens = str.split(" ");
					 DecBinaryNode root=new DecBinaryNode();
					 root.code=tokens[1];
					 root.data=Integer.parseInt(tokens[0]);
					 DecBinaryNodes.add(root);

}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

  		HuffManTree ht=new HuffManTree();
		ht.decodeTree(DecBinaryNodes);
		try {
			ht.decoder(BinaryFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long stopTime = System.currentTimeMillis();
		System.out.println("time for decode:"+(stopTime-startTime)+" MilliSec");


}


}
