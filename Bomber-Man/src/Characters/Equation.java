package Characters;
import java.util.Random;


public class Equation {
	 Random rand= new Random();
	 private int max= 99;
	 private int min=10;
	 private int firstNum;
	 private int secondNum;
	 private String equation="";
	 private int trueValue;
	public  Equation(){
		String equ="";
		 firstNum = (rand.nextInt(max - min + 1) + min);
		 secondNum= (rand.nextInt(max - min + 1) + min);
		 while(secondNum>firstNum)secondNum= (rand.nextInt(max - min + 1) + min);
		 char []elements = {'+','-','*'};
		 int temp = rand.nextInt(elements.length);
		// char o = elements[temp];
		 int []position = {0,1,3,4};
		 int value=0;
		 if(temp==0){
			 value = firstNum+ secondNum;
			 equ=firstNum+"+"+secondNum+"="+value;
		 }
		 else if(temp==1){
			 value=firstNum- secondNum;
			 equ=firstNum+"-"+secondNum+"="+value;
		 }
		 else {
			 value = firstNum* secondNum;
			 equ=firstNum+"*"+secondNum+"="+value;
		 }
		  temp= rand.nextInt(position.length);
		 int pos = position[temp];
		 System.out.println("equation "+equ);
		 trueValue = equ.charAt(pos)-48;
		 System.out.println("trueee value = "+ trueValue);
		 equation = equ.substring(0, pos)+"?"+equ.substring(pos+1);
		 System.out.println("equation 2 "+equation);

		   
	} 
	// return the full equation
	public String getEquation(){
		return equation;
	}
	// return the value of the replaced character
	public int getTrueValue(){
		return trueValue;
	}
}
