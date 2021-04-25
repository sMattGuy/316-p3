import java.io.*;

public class Interpreter{
	static PrintStream o;
	static PrintStream o2;
	static PrintStream console;
	static boolean isDouble = false;
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
				System.setOut(o2);
				double result = arithExp(brExp,o2,token_split[0]);
				if(isDouble){
					System.out.println(result);
				}
				else{
					System.out.println((int)result);
				}
			}
			//boolean
			else if(token_split[0].equals("|") || token_split[0].equals("&") || token_split[0].equals("!")){
				System.setOut(o2);
				System.out.println(boolExp(brExp,o2,token_split[0]));
			}
			//comparison
			else if(token_split[0].equals("<") || token_split[0].equals(">") || token_split[0].equals("<=") || token_split[0].equals(">=") || token_split[0].equals("=")){
				System.setOut(o2);
				System.out.println(compExp(brExp,o2,token_split[0]));
			}
			//condition
			else if(token_split[0].equals("if")){
				System.setOut(o2);
				System.out.println(condExp(brExp,o2,token_split[0]));
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
	static double arithExp(BufferedReader br, PrintStream fileOut, String readToken) throws IOException{
		//get arthimatic type
		double unit1 = -1;
		double unit2 = -1;
		String token = LexAnalyzer.getToken(br);
		String[] token_split = token.split(" ");
		if(token_split[0].equals("(")){
			//first unit is new expression
			token = LexAnalyzer.getToken(br);
			token_split = token.split(" ");
			if(token_split[0].equals("+") || token_split[0].equals("*") || token_split[0].equals("-") || token_split[0].equals("/")){
				unit1 = arithExp(br,fileOut,token_split[0]);
				if(unit1 % 1 != 0){
					isDouble = true;
				}
			}
			else if(token_split[0].equals("if")){
				unit1 = condExp(br,fileOut,token_split[0]);
				if(unit1 % 1 != 0){
					isDouble = true;
				}
			}
			else{
				//error
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected arithmatic expression");
				return -1;
			}
		}
		else{
			//first unit is number
			try{
				unit1 = Double.parseDouble(token_split[0]);
				if(unit1 % 1 != 0){
					isDouble = true;
				}
			}
			catch(Exception e){
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected number");
				return -1;
			}
		}
		//gets next token
		token = LexAnalyzer.getToken(br);
		token_split = token.split(" ");
		if(token_split[0].equals("(")){
			//first unit is new expression
			token = LexAnalyzer.getToken(br);
			token_split = token.split(" ");
			if(token_split[0].equals("+") || token_split[0].equals("*") || token_split[0].equals("-") || token_split[0].equals("/")){
				unit2 = arithExp(br,fileOut,token_split[0]);
				if(unit2 % 1 != 0){
					isDouble = true;
				}
			}
			else if(token_split[0].equals("if")){
				unit2 = condExp(br,fileOut,token_split[0]);
				if(unit2 % 1 != 0){
					isDouble = true;
				}
			}
			else{
				//error
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected arithmatic expression");
				return -1;
			}
		}
		else{
			//first unit is number
			try{
				unit2 = Double.parseDouble(token_split[0]);
				if(unit2 % 1 != 0){
					isDouble = true;
				}
			}
			catch(Exception e){
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected number");
				return -1;
			}
		}
		token = LexAnalyzer.getToken(br);
		token_split = token.split(" ");
		if(!token_split[0].equals(")")){
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected expression close");
			return -1;
		}
		if(readToken.equals("+")){
			//System.out.println("add:" + (unit1) + " " +(unit2) + " " + (unit1 + unit2));
			return unit1 + unit2;
		}
		else if(readToken.equals("*")){
			//System.out.println("times:" + (unit1) + " " +(unit2) + " " + (unit1 * unit2));
			return unit1 * unit2;
		}
		else if(readToken.equals("/")){
			//System.out.println("divide:" + (unit1) + " " +(unit2) + " " + (unit1 / unit2));
			return unit1 / unit2;
		}
		else if(readToken.equals("-")){
			//System.out.println("minus:" + (unit1) + " " +(unit2) + " " + (unit1 - unit2));
			return unit1 - unit2;
		}
		else{
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected result to return");
			return -1;
		}
	}
	
	static boolean boolExp(BufferedReader br, PrintStream fileOut, String readToken) throws IOException{
		//get arthimatic type
		boolean unit1 = false;
		boolean unit2 = false;
		String token = LexAnalyzer.getToken(br);
		String[] token_split = token.split(" ");
		if(token_split[0].equals("(")){
			//first unit is new expression
			token = LexAnalyzer.getToken(br);
			token_split = token.split(" ");
			if(token_split[0].equals("<") || token_split[0].equals(">") || token_split[0].equals("<=") || token_split[0].equals(">=") || token_split[0].equals("=")){
				unit1 = compExp(br,fileOut,token_split[0]);
			}
			else if(token_split[0].equals("|") || token_split[0].equals("&") || token_split[0].equals("!")){
				unit1 = boolExp(br,fileOut,token_split[0]);
			}
			else{
				//error
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected comparison expression");
				return false;
			}
		}
		else{
			//first unit is boolean
			try{
				unit1 = Boolean.parseBoolean(token_split[0]);
			}
			catch(Exception e){
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected boolean");
				return false;
			}
		}
		if(readToken.equals("!")){
			//System.out.println("divide:" + (unit1) + " " +(unit2) + " " + (unit1 / unit2));
			return !unit1;
		}
		//gets next token
		token = LexAnalyzer.getToken(br);
		token_split = token.split(" ");
		if(token_split[0].equals("(")){
			//first unit is new expression
			token = LexAnalyzer.getToken(br);
			token_split = token.split(" ");
			if(token_split[0].equals("<") || token_split[0].equals(">") || token_split[0].equals("<=") || token_split[0].equals(">=") || token_split[0].equals("=")){
				unit2 = compExp(br,fileOut,token_split[0]);
			}
			else if(token_split[0].equals("|") || token_split[0].equals("&") || token_split[0].equals("!")){
				unit2 = boolExp(br,fileOut,token_split[0]);
			}
			else{
				//error
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected comparison expression");
				return false;
			}
		}
		else{
			//first unit is boolean
			try{
				unit2 = Boolean.parseBoolean(token_split[0]);
			}
			catch(Exception e){
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected boolean");
				return false;
			}
		}
		token = LexAnalyzer.getToken(br);
		token_split = token.split(" ");
		if(!token_split[0].equals(")")){
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected expression close");
			return false;
		}
		if(readToken.equals("|")){
			//System.out.println("add:" + (unit1) + " " +(unit2) + " " + (unit1 + unit2));
			return unit1 || unit2;
		}
		else if(readToken.equals("&")){
			//System.out.println("times:" + (unit1) + " " +(unit2) + " " + (unit1 * unit2));
			return unit1 && unit2;
		}
		else{
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected result to return");
			return false;
		}
	}
	
	static boolean compExp(BufferedReader br, PrintStream fileOut, String readToken) throws IOException{
		//get arthimatic type
		double unit1 = -1;
		double unit2 = -1;
		boolean unit1b = false;
		boolean unit2b = false;
		
		boolean unit1Bool = false;
		boolean unit2Bool = false;
		boolean unit1Double = false;
		boolean unit2Double = false;
		String token = LexAnalyzer.getToken(br);
		String[] token_split = token.split(" ");
		if(token_split[0].equals("(")){
			//first unit is new expression
			token = LexAnalyzer.getToken(br);
			token_split = token.split(" ");
			if(token_split[0].equals("+") || token_split[0].equals("*") || token_split[0].equals("-") || token_split[0].equals("/")){
				unit1 = arithExp(br,fileOut,token_split[0]);
				if(unit1 % 1 != 0){
					unit1Double = true;
				}
			}
			else if(token_split[0].equals("<") || token_split[0].equals(">") || token_split[0].equals("<=") || token_split[0].equals(">=") || token_split[0].equals("=")){
				unit1b = compExp(br,fileOut,token_split[0]);
				unit1Bool = true;
			}
			else{
				//error
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected arithmatic expression");
				return false;
			}
		}
		else{
			//first unit is number
			try{
				unit1 = Double.parseDouble(token_split[0]);
				if(unit1 % 1 != 0){
					unit1Double = true;
				}
			}
			catch(Exception e){
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected number");
				return false;
			}
		}
		//gets next token
		token = LexAnalyzer.getToken(br);
		token_split = token.split(" ");
		if(token_split[0].equals("(")){
			//first unit is new expression
			token = LexAnalyzer.getToken(br);
			token_split = token.split(" ");
			if(token_split[0].equals("+") || token_split[0].equals("*") || token_split[0].equals("-") || token_split[0].equals("/")){
				unit2 = arithExp(br,fileOut,token_split[0]);
				if(unit2 % 1 != 0){
					unit2Double = true;
				}
			}
			else if(token_split[0].equals("<") || token_split[0].equals(">") || token_split[0].equals("<=") || token_split[0].equals(">=") || token_split[0].equals("=")){
				unit2b = compExp(br,fileOut,token_split[0]);
				unit2Bool = true;
			}
			else{
				//error
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected arithmatic expression");
				return false;
			}
		}
		else{
			//first unit is number
			try{
				unit2 = Double.parseDouble(token_split[0]);
				if(unit2 % 1 != 0){
					unit2Double = true;
				}
			}
			catch(Exception e){
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected number");
				return false;
			}
		}
		token = LexAnalyzer.getToken(br);
		token_split = token.split(" ");
		if(!token_split[0].equals(")")){
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected expression close");
			return false;
		}
		if(readToken.equals("<")){
			//System.out.println("<:" + (unit1) + " " +(unit2) + " " + (unit1 < unit2));
			return unit1 < unit2;
		}
		else if(readToken.equals(">")){
			//System.out.println(">:" + (unit1) + " " +(unit2) + " " + (unit1 > unit2));
			return unit1 > unit2;
		}
		else if(readToken.equals("<=")){
			//System.out.println("<=:" + (unit1) + " " +(unit2) + " " + (unit1 <= unit2));
			return unit1 <= unit2;
		}
		else if(readToken.equals(">=")){
			//System.out.println(">=:" + (unit1) + " " +(unit2) + " " + (unit1 >= unit2));
			return unit1 >= unit2;
		}
		else if(readToken.equals("=")){
			//System.out.println("equal:" + (unit1) + " " +(unit2) + " " + (unit1 == unit2));
			if(unit1Bool && unit2Bool){
				return unit1b == unit2b;
			}
			else if(unit1Double && unit2Double){
				return unit1 == unit2;
			}
			else{
				return false;
			}
		}
		else{
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected result to return");
			return false;
		}
	}
	
	static double condExp(BufferedReader br, PrintStream fileOut, String readToken) throws IOException{
		//get arthimatic type
		double unit1 = -1;
		double unit2 = -1;
		boolean unit1b = false;
		boolean unit2b = false;
		
		boolean unit1Double = false;
		boolean unit2Double = false;
		String token = LexAnalyzer.getToken(br);
		String[] token_split = token.split(" ");
		if(token_split[0].equals("(")){
			//first unit is new expression
			token = LexAnalyzer.getToken(br);
			token_split = token.split(" ");
			if(token_split[0].equals("<") || token_split[0].equals(">") || token_split[0].equals("<=") || token_split[0].equals(">=") || token_split[0].equals("=")){
				unit1b = compExp(br,fileOut,token_split[0]);
			}
			else if(token_split[0].equals("|") || token_split[0].equals("&") || token_split[0].equals("!")){
				unit1b = boolExp(br,fileOut,token_split[0]);
			}
			else{
				//error
				System.setOut(fileOut);
				System.out.println(token_split[0] + " Error, expected boolean expression");
				return -1;
			}
		}
		else{
			//comp expected
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected boolean expression");
			return -1;
		}
		//gets next token
		token = LexAnalyzer.getToken(br);
		token_split = token.split(" ");
		//get first number for true statement 
		try{
			unit1 = Double.parseDouble(token_split[0]);
			if(unit1 % 1 != 0){
				unit1Double = true;
			}
		}
		catch(Exception e){
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected number");
			return -1;
		}
		//get second number for false statement
		token = LexAnalyzer.getToken(br);
		token_split = token.split(" ");
		try{
			unit2 = Double.parseDouble(token_split[0]);
			if(unit2 % 1 != 0){
				unit2Double = true;
			}
		}
		catch(Exception e){
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected number");
			return -1;
		}
		
		token = LexAnalyzer.getToken(br);
		token_split = token.split(" ");
		if(!token_split[0].equals(")")){
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected expression close");
			return -1;
		}
		if(unit1b){
			return unit1;	
		}
		if(!unit1b){
			return unit2;
		}
		else{
			System.setOut(fileOut);
			System.out.println(token_split[0] + " Error, expected result to return");
			return -1;
		}
	}
	
	static String constExp(BufferedReader br, PrintStream fileOut, String readToken) throws IOException{
		//temp
		return "";
	}
	static String fieldExp(BufferedReader br, PrintStream fileOut, String readToken) throws IOException{
		//temp
		return "";
	}
}