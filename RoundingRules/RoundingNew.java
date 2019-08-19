import java.math.BigDecimal;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class RoundingNew{

	private static double c;
	private static double d;
	private static int sigd;

	public static void main(String[] args){

		c = 20.000;
		d=3.067;
		sigd=3;

		System.out.println(direct_rounding("2588489",5));
		ArrayList<String> a = (roundWithDP("0.0034567","0.00789",false));
		//System.out.println(Double.parseDouble("4"));

	}

	private static String direct_rounding(String c, int sigd){
		
		int deci_place = c.indexOf(".");
		c = c+"1";
		double x = Double.parseDouble(c);
		System.out.println("x "+x);
		double y = 0;
		int n=sigd;
		int mult10 = 0;
		if(x<0.1){
			while(x<0.1){
				x=x*10;
				mult10++;	
			}
		}
		else if(x>=1){
			while(x>=1){
				x=x/10;	
				mult10--;	
			}
		}

		y=x;
		x=x*10;
		String s= c;
		//System.out.println(c);
		int length = 0;
		if(deci_place != -1){
			length = s.length()-2;
		}
		else length = s.length()-1;

		if(length<=n){

			return s.substring(0,s.length()-1);

		}
		
		int[] a = new int[length];
		a[0] = (int)x%10;

		for(int i=1; i<n; i++){
			x=x*10;
			a[i] = (int)x%10;
		}

		x=x*10;
		a[n] = (int)x%10;
		int round=0;

		if(a[n]>5){
			round = 1;
		}
		else if(a[n]<5){
			round = 0; 
		}
		else{
			int i=n+1;
			int curr=0;
			while(curr==0 && i<length){
				x=x*10;
				a[i] = (int)x%10;
				curr=a[i];
				i++;
			}

			if(i==length && curr==0){
				if(a[n-1]%2==0){
					round=0;
				}
				else{
					round=1;
				}
			}
			else{
				round=1;
			}
		}

		for(int j=0; j<n; j++){
			y=y*10;
		}
		int round_value = (int)y;
		
		if(round==1){
			round_value = round_value+1;
		}

		int ten=0;
		if(deci_place != -1)
			ten = n+mult10;
		else ten = n+mult10+1;
		//double answer = round_value;
		//System.out.println("round_value "+round_value);

		String result = Integer.toString(round_value);
		if(ten<0){
			for(int i=0; i<(-ten); i++){
				result = result +"0";
			}
		}

		if(ten>0){
			if(ten<n){
				result = result.substring(0,result.length()-ten) + "." + result.substring(result.length()-ten);
			}
			else{
				String str = "0.";
				for(int i=0; i<(ten-n); i++){
				str = str +"0";
				}
				str = str+result;
				result = str;
			}
		}

		return result;
	}

	public static ArrayList<String> roundWithDP(String c_str, String d_str, boolean strict){

		String dr = ""; 
		double c = Double.parseDouble(c_str);
		double d = Double.parseDouble(d_str);

		if(strict){
			if(d<=0.45 || (d>1 && d<=4.5)){
				 dr = direct_rounding(d_str,2);
			}
			else if((d>0.45 && d<=1) || (d>4.5)){
				 dr = direct_rounding(d_str,1);
			}
		}
		else{
			if(d<=0.45 || (d>1 && d<=4.5)){
				 dr = direct_rounding(d_str,3);
			}
			else if((d>0.45 && d<=1) || (d>4.5)){
				 dr = direct_rounding(d_str,2);
			}
		}
		System.out.println("dr: "+dr);
		int sigd = 	dr.length() - (dr.indexOf(".")+1);

		//String c_str = Double.toString(c);
		int predeci = c_str.indexOf(".");
		if(c_str.charAt(0) == '0' && predeci == 1){
			predeci--;
			int t = 2;
			while(c_str.charAt(t) == '0'){
				predeci--;
				t++;
			}
		}
	System.out.println("pd "+predeci+sigd);
		c_str = direct_rounding(c_str,predeci+sigd);
		//check for special cases too
		ArrayList<String> a = new ArrayList<String>();
		a.add(c_str);
		a.add(dr);
		System.out.println(a.get(0));
		System.out.println(a.get(1));
		return a;

	}
}


