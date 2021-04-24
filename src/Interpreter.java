import java.io.*;

public class Interpreter{
	static PrintStream o;
	static PrintStream o2;
	static PrintStream console;
	public static void main(String[] args) throws FileNotFoundException, IOException{
		if(args.length != 4){
			System.out.println("Input must be 4 files\nUsage java Interpreter classes.txt classoutput expression.txt expressionoutput");
			return;
		}
		//set up files
		File classFile = new File(args[0]);
		BufferedReader brClass = new BufferedReader(new FileReader(classFile));
		File expFile = new File(args[2]);
		BufferedReader brExp = new BufferedReader(new FileReader(expFile));
		o = new PrintStream(new File(args[1]));
		o2 = new PrintStream(new File(args[3]));
		console = System.out;
		//get parse tree
		multipleClassDef completeParse = Parser.getParse(brClass, o);
		//start paring the expression file
		String token = LexAnalyzer.getToken(brExp);
		String[] token_split = token.split(" ");
		if(token_split[0].equals("(")){
			//begin parse
			//get identifier token
			token = LexAnalyzer.getToken(brExp);
			token_split = token.split(" ");
			//go to expression function
			//arith
			if(token_split[0].equals("+") || token_split[0].equals("*") || token_split[0].equals("-") || token_split[0].equals("/")){
				
			}
			//boolean
			else if(token_split[0].equals("|") || token_split[0].equals("&") || token_split[0].equals("!")){
				
			}
			//comparison
			else if(token_split[0].equals("<") || token_split[0].equals(">") || token_split[0].equals("<=") || token_split[0].equals(">=") || token_split[0].equals("=")){
				
			}
			//constructor
			else if(token_split[1].equals("id")){
				
			}
			//field getter
			else if(token_split[0].equals(".")){
				
			}
			//error
			else{
				
			}
		}
		else{
			//error invalid start
			System.setOut(o2);
			System.out.println(token_split[0] + " Error, invalid start of expression");
			return;
		}
	}
	static int arithExp(BufferedReader br){
		//temp
		return 0;
	}
	static boolean boolExp(BufferedReader br){
		//temp
		return true;
	}
	static String constExp(BufferedReader br){
		//temp
		return "";
	}
	static String fieldExp(BufferedReader br){
		//temp
		return "";
	}
}