package RoundingRules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.zkoss.zul.Messagebox;

/**
 * Rounding of data following the rules established by Verma (2005) and Verma
 * (2016)
 *
 * @author ShreyaS
 */
public class RoundingLogic {

    private ArrayList<ArrayList> result = new ArrayList<ArrayList>();
    
    private static int min = 10;
    private static int max = 1000000000;
    private static int factor = 10;

    private static int factorValue;
    private static String roundedValue;
    private static int significantDigits;

    private static boolean strict = true;
    private static boolean flexible = false;
    private static boolean roundingTrue = false;
    
//    public static void main(String args[]){
//        
//        ArrayList a = new ArrayList();
//        a.add("1"); a.add("2");a.add("35.56"); a.add("0.446"); a.add(""); a.add("");
//        ArrayList<ArrayList> vbody = new ArrayList<ArrayList>();
////        vbody.add((ArrayList) Arrays.asList("1", "2", "35.5678", "3.456", "", ""));
////        vbody.add((ArrayList) Arrays.asList("2", "2", "35.5678", "3.489","", ""));
////        vbody.add((ArrayList) Arrays.asList("3", "2", "67.5678", "39.456","", ""));
////        vbody.add((ArrayList) Arrays.asList("4", "2", "45.56", "4.566","", ""));
//        vbody.add(a); vbody.add(a); vbody.add(a); vbody.add(a); vbody.add(a);
//        RoundingLogic roundingLogic = new RoundingLogic(vbody, 3, true, true, 1);
//        
//        System.out.println("hi there");
//    }

    public RoundingLogic(ArrayList<ArrayList> validatedBody, ArrayList headers, int significantDigitsInput, boolean onlyCT, boolean strictInput, int dp, boolean decimal) {
        double val = 2843.6465;
        double val2 = 125.6654;
 
        System.out.println("Rounding");
        try{
            
        System.out.println("signi digits- "+ significantDigitsInput);
        significantDigits = significantDigitsInput;
        strict = strictInput;
        flexible = !(strictInput);
        ArrayList ctData = new ArrayList();
        ArrayList ctRounded = new ArrayList();
        ArrayList rules = new ArrayList();
        System.out.println("size: "+validatedBody.get(0).size());
        //printConsole(validatedBody);
        
        for(int i=0; i<validatedBody.size(); i++){
            ctData.add(validatedBody.get(i).get(2));
        }
        
    
        //to check preferred dp for rounding off
        if(!onlyCT){
            boolean complete1 = true;
            boolean complete2 = true;
            boolean complete3 = true;
            boolean complete4 = true;

            ArrayList sdData = new ArrayList();
            ArrayList rsdData = new ArrayList();
            ArrayList ucData1 = new ArrayList();
            ArrayList ucData2 = new ArrayList();

            if(dp==1){
                for(int i=0; i<validatedBody.size(); i++){
                    if(validatedBody.get(i).get(3) == ""){
                        complete1 = false;
                    }
                    sdData.add(validatedBody.get(i).get(3));
                }
            }
            else if(dp==2){
                for(int i=0; i<validatedBody.size(); i++){
                    if(validatedBody.get(i).get(4) == ""){
                        complete2 = false;
                    }
                    rsdData.add(validatedBody.get(i).get(4));
                } 
            }          
            else if(dp==3){
                for(int i=0; i<validatedBody.size(); i++){
                    if(validatedBody.get(i).get(5) == ""){
                        complete3 = false;
                    }
                    ucData1.add(validatedBody.get(i).get(5));
                }
            }
            else if(dp==4){
                for(int i=0; i<validatedBody.size(); i++){
                    if(validatedBody.get(i).get(6) == ""){
                        complete4 = false;
                    }
                    ucData2.add(validatedBody.get(i).get(6));
                }
            }
            //System.out.println("2");
            ArrayList dpRounded = new ArrayList();
            ArrayList output = new ArrayList();
            ArrayList resultHead =  new ArrayList();
            resultHead.add(headers.get(2).toString()+"_rounded");
            
            if (dp==1 && complete1){
                resultHead.add(headers.get(3).toString()+"_rounded");
                resultHead.add("Rules");
                result.add(resultHead);
                //System.out.println(ctData.isEmpty());
                for(int i=0; i<ctData.size(); i++){
                    output =  roundWithDP(ctData.get(i).toString(),sdData.get(i).toString(),strict);
                    ctRounded.add(output.get(0));
                    dpRounded.add(output.get(1));
                    rules.add(output.get(2).toString() + ", " + output.get(3).toString() + ", " + output.get(4).toString() + ", " + output.get(5).toString());
                }
            }
            else if (dp==2 && complete2){
                resultHead.add(headers.get(4).toString()+"_rounded");
                resultHead.add("Rules");
                result.add(resultHead);
                for(int i=0; i<ctData.size(); i++){
                    output =  roundWithDP(ctData.get(i).toString(),rsdData.get(i).toString(),strict);
                    ctRounded.add(output.get(0));
                    dpRounded.add(output.get(1));
                    rules.add(output.get(2).toString() + ", " + output.get(3).toString() + ", " + output.get(4).toString() + ", " + output.get(5).toString());
                }
                //result.get(0).add("rsd_rounded");
            }
            else if(dp==3 && complete3){
                resultHead.add(headers.get(5).toString()+"_rounded");
                resultHead.add("Rules");
                result.add(resultHead);
                for(int i=0; i<ctData.size(); i++){
                    output =  roundWithDP(ctData.get(i).toString(),ucData1.get(i).toString(),strict);
                    ctRounded.add(output.get(0));
                    dpRounded.add(output.get(1));
                    rules.add(output.get(2).toString() + ", " + output.get(3).toString() + ", " + output.get(4).toString() + ", " + output.get(5).toString());
                }
                //result.get(0).add("uc_rounded");
            }
            else if(dp==4 && complete4){
                resultHead.add(headers.get(6).toString()+"_rounded");
                resultHead.add("Rules");
                result.add(resultHead);
                for(int i=0; i<ctData.size(); i++){
                    output =  roundWithDP(ctData.get(i).toString(),ucData2.get(i).toString(),strict);
                    ctRounded.add(output.get(0));
                    dpRounded.add(output.get(1));
                    rules.add(output.get(2).toString() + ", " + output.get(3).toString() + ", " + output.get(4).toString() + ", " + output.get(5).toString());
                }
                //result.get(0).add("uc_rounded");
            }
            else{
                Messagebox.show("The chosen dispersion parameter has incomplete data");
            }
            //System.out.println("3");
            for(int i=0; i<ctRounded.size(); i++){
                ArrayList resultRow = new ArrayList();
                resultRow.add(ctRounded.get(i));
                resultRow.add(dpRounded.get(i));
                resultRow.add(rules.get(i));
                result.add(resultRow);
            }
            //printConsole(result);
            if ((dp==1 && complete1) || (dp==2 && complete2) || (dp==3 && complete3) || (dp==4 && complete4)){
                roundingTrue = true;
            }
        }
        else{
            ArrayList resultHead =  new ArrayList();
            ArrayList output = new ArrayList();
            resultHead.add(headers.get(2).toString()+"_rounded");
            resultHead.add("Rules");
            result.add(resultHead);
            System.out.println("non decimal rounding");
            if(!decimal){
                for(int i=0; i<ctData.size(); i++){
                    output = direct_rounding((ctData.get(i).toString()), significantDigitsInput);
                    ctRounded.add(output.get(0));
                    rules.add(output.get(1));
                    System.out.println(output.get(1));
                }
            }
            else{
                String s  = "";
                int deciPos = 0;
                for(int i=0; i<ctData.size(); i++){
                    s = ctData.get(i).toString();
                    deciPos = s.indexOf(".");
                    output = direct_rounding(s, deciPos+significantDigitsInput);
                    ctRounded.add(output.get(0));
                    rules.add(output.get(1));
                    System.out.println(output.get(1));
                }
            }
            
            for(int i=0; i<ctRounded.size(); i++){
                ArrayList resultRow = new ArrayList();
                resultRow.add(ctRounded.get(i));
                resultRow.add(rules.get(i));
                result.add(resultRow); 
            }  
            
            roundingTrue = true;
        }
        //System.out.println("Results are here -----------");
        //printConsole(result);
        } catch(Exception e){
        System.out.println("Error encountered." + e);
        }
                
    }
    
    private void printConsole(ArrayList<ArrayList> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                System.out.print(list.get(i).get(j) + "\t");
            }
            System.out.println();
        }
    }
   
    private static ArrayList<String> direct_rounding(String c, int sigd){
       if(c==""){
            ArrayList<String> a = new ArrayList<String>();
            a.add("");
            a.add("");
            return a;
       } 
        
       String RuleA="";
       String digits="";
       String rounded="";
       int number=0;
       
       if(c.indexOf(".")==-1){
            //case of integer
            int index = 0;
            while(c.charAt(index)=='0'){
                index++;
            }
            digits = c.substring(index);
            int length = digits.length();
            if(length < sigd){
                rounded = digits + ".";
                for(int i=0; i<sigd-length; i++){
                    rounded = rounded + "0";
                }
                RuleA = "";
            }
            else{
                rounded = digits.substring(0,sigd);
                if(Character.getNumericValue(digits.charAt(sigd)) > 5){
                    RuleA = "Rule 1";
                    number = Integer.parseInt(rounded);
                    number = number+1;
                    System.out.println("numb "+number);
                    rounded = Integer.toString(number);
                    System.out.println("rounded "+rounded);
                }
                else if(Character.getNumericValue(digits.charAt(sigd)) < 5){
                    RuleA = "Rule 2";
                    System.out.println("rounded "+rounded);
                }
                else{
                    int curr = 0;
                    for(int i=sigd+1; i<length; i++){
                        if(digits.charAt(i)!='0'){
                            curr=1;
                            break;
                        }
                    }
                    if(curr==1){ //of type 50067
                        RuleA = "Rule 1";
                        number = Integer.parseInt(rounded);
                        number = number+1;
                        System.out.println("numb "+number);
                        rounded = Integer.toString(number);
                        System.out.println("rounded "+rounded);
                    }
                    else{
                        RuleA = "Rule 3";
                        if(Character.getNumericValue(digits.charAt(sigd-1))%2 == 1){  //odd and 50000.. types
                        number = Integer.parseInt(rounded);
                        number = number+1;
                        System.out.println("numb "+number);
                        rounded = Integer.toString(number);
                        System.out.println("rounded "+rounded);
                        }
                        else{

                        }
                    }
                }
                for(int i=0; i<length-sigd; i++){
                    rounded = rounded + "0";
                }
            }
       }

       else{
            int deciPos = c.indexOf(".");
            int index = 0;
            while(c.charAt(index)=='0' || c.charAt(index)=='.'){
                index++;
            }
            //String extra = c.substring(0,index);
            int deciZeroes = 0; //number of zeroes after decimal; ex: 0.000567
            if(index>deciPos){
                deciZeroes = index-deciPos-1;
                digits = c.substring(index);
            }
            else{
                digits = c.substring(index,deciPos)+c.substring(deciPos+1); //extracted number
            }
            System.out.println("digits: "+digits);
            int length = digits.length();
            
            if(length <= sigd){
                rounded = digits;
                for(int i=0; i<sigd-length; i++){
                    rounded = rounded + "0";
                }
                RuleA = "no rounding needed";
            }
            else{
                rounded = digits.substring(0,sigd);
                if(Character.getNumericValue(digits.charAt(sigd)) > 5){
                    RuleA = "Rule 1";
                    number = Integer.parseInt(rounded);
                    number = number+1;
                    System.out.println("numb "+number);
                    rounded = Integer.toString(number);
                    System.out.println("rounded "+rounded);
                }
                else if(Character.getNumericValue(digits.charAt(sigd)) < 5){
                    RuleA = "Rule 2";
                    System.out.println("rounded "+rounded);
                }
                else{
                    int curr = 0;
                    for(int i=sigd+1; i<length; i++){
                        if(digits.charAt(i)!='0'){
                            curr=1;
                            break;
                        }
                    }
                    if(curr==1){ //of type 50067
                        RuleA = "Rule 1";
                        number = Integer.parseInt(rounded);
                        number = number+1;
                        System.out.println("numb "+number);
                        rounded = Integer.toString(number);
                        System.out.println("rounded "+rounded);
                    }
                    else{
                        RuleA = "Rule 3";
                        if(Character.getNumericValue(digits.charAt(sigd-1))%2 == 1){  //odd and 50000.. types
                        number = Integer.parseInt(rounded);
                        number = number+1;
                        System.out.println("numb "+number);
                        rounded = Integer.toString(number);
                        System.out.println("rounded "+rounded);
                        }
                        else{

                        }
                    }
                }
            }
            if(index>deciPos){
                    for(int i=0; i<deciZeroes; i++){
                        rounded = "0" + rounded;
                    }
                    rounded = "0."+rounded;
                }
                else{
                    if (deciPos-index<rounded.length())
                        rounded = rounded.substring(0,deciPos-index)+"."+rounded.substring(deciPos-index);
                    else{
                        int l = rounded.length();
                        for(int i=0; i<deciPos-index-l;i++){
                            rounded = rounded + "0";
                            }
                    }
                }
        }
        
        ArrayList<String> a = new ArrayList<String>();
        a.add(rounded);
        a.add(RuleA);
        System.out.println(a.get(0));
        System.out.println(a.get(1));
        return a;
    }
        
    public static ArrayList<String> roundWithDP(String c_str, String d_str, boolean strict){
        
        if(c_str=="" || d_str== ""){
            ArrayList<String> a = new ArrayList<String>();
            a.add("");
            a.add("");
            a.add("");
            a.add("");
            a.add("");
            a.add("");
        return a;
        }
        
        String dr = ""; 
        double c = Double.parseDouble(c_str);
        double d = Double.parseDouble(d_str);
        String RuleA1="";
        String RuleA2="";
        String RuleB="";
        String RuleC="";
        ArrayList list = new ArrayList();
        if(strict){
            RuleC = "Rule 6";
            if(d<=0.45){
                RuleB = "Rule 4";
                list = direct_rounding(d_str,2);
            }
            else if(d>1 && d<=4.5){
                RuleB = "Rule 5";
                list = direct_rounding(d_str,2);
            }
            else if(d>0.45 && d<=1){
                RuleB = "Rule 4";
                list = direct_rounding(d_str,1);
            }
            else if(d>4.5){
                RuleB = "Rule 5";
                list = direct_rounding(d_str,1);
            }
        }
        else{
            RuleC = "Rule 9";
            if(d<=0.45){
                RuleB = "Rule 7";
                list = direct_rounding(d_str,3);
            }
            else if(d>1 && d<=4.5){
                RuleB = "Rule 8";
                list = direct_rounding(d_str,3);
            }
            else if(d>0.45 && d<=1){
                RuleB = "Rule 7";
                list = direct_rounding(d_str,2);
            }
            else if(d>4.5){
                RuleB = "Rule 8";
                list = direct_rounding(d_str,2);
            }
        }
        dr = list.get(0).toString();
        RuleA1 = list.get(1).toString();
        System.out.println("dr: "+RuleA1);

        int sigd=0;
        if(dr.indexOf(".")==-1){
            sigd = 0;
        }
        else  sigd = dr.length() - (dr.indexOf(".")+1);

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
        list = direct_rounding(c_str,predeci+sigd);
        c_str = list.get(0).toString();
        RuleA2 = list.get(1).toString();
        //check for special cases too
        ArrayList<String> a = new ArrayList<String>();
        a.add(c_str);
        a.add(dr);
        a.add(RuleC);
        a.add(RuleB);
        a.add(RuleA1);
        a.add(RuleA2);
        System.out.println("RuleA1: "+RuleA1);
        System.out.println("RuleA2: "+RuleA2);

        
        return a;

    }
    public static boolean getRoundingTrue() {
    return roundingTrue;
    }
        
    public static String getRoundedValue() {
        return roundedValue;
    }

    public static void setRoundedValue(String roundedValue) {
        RoundingLogic.roundedValue = roundedValue;
    }

    public static int getFactorValue() {
        return factorValue;
    }

    public static void setFactorValue(int factorValue) {
        RoundingLogic.factorValue = factorValue;
    }
    
 public ArrayList<ArrayList> getResult() {
        return result;
    }

    public void setResult(ArrayList<ArrayList> result) {
        this.result = result;
    }

    public static int getSignificantDigits() {
        return significantDigits;
    }

    public static void setSignificantDigits(int significantDigits) {
        RoundingLogic.significantDigits = significantDigits;
    }
}// End of Redondeo
