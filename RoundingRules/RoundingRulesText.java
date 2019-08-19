/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RoundingRules;

/**
 * Descripcion de cada una de las reglas de redondeo
 * @author MAURICIO
 */
public class RoundingRulesText {
    public String Rule1(){
        String arg = "If the excess fraction is greater than 1/2, increase the new least significant digit by 1.";
        return arg;
    }
    
    public String Rule2(){
        String arg = "If the excess fraction is less than 1/2, maintain the new least significant digit as it is. This will amount to simmply "
                + "truncate the original number to the desired number of significant digits.";
        return arg;
    }
    
    public String Rule3(){
        String arg = "If the excess fraction equals exactly 1/2, increase the new least significant digit by 1 only if it is odd; otherwise if "
                + "it is even, keep the new least significant digit as such.";
        return arg;
    }
    
    public String Rule4(){
        String arg = "For relatively small dispersion (strict rounding): Depending on the measurement unit, the dispersion (99% uncertainty of the mean) may be <1 (a purely"
                + " fractional number, without any integer part); it is rounded to two significant digits when it represents a relatively small number, "
                + " viz., <= 0.45 and to one significant digit when it is rounded to a relatively larger number, viz., > 0.45.";
        return arg;
    }
    
    public String Rule5(){
        String arg = "For relatively large dispersion (strict rounding): Depending on the measurement unit, the dispersion (99% uncertainty of the mean) may be > 1 (a"
                + " fractional number having an integer part); it is rounded to two significant digits when it is <= 4.5 and to one significant digit when it is > 4.5.";
        return arg;
    }
    
    public String Rule6(){
        String arg = "The central tendency parameter, e.g, the mean value, will be reported as rounded to the same digits after the decimal points as"
                + " the dispersion parameter (99% uncertainty of the mean) or without any decimal point if the rounded value of the dispersion resulted"
                + " as an integer, i.e., follow Rules 4 and 5.";
        return arg;
    }
    
    public String Rule7(){
        String arg = "For relatively small dispersion (flexible rounding): Deoending on the measurement unit, the dispersion (99% uncertainty of the mean) may be <1 (a fractional number, "
                + " without any integer part); it is rounded to three significant digits when it represents a relatively small number, viz., <= 0.45 and to two "
                + " significant digits when it is rounded to a relatively larger number, viz., > 0.45.";
        return arg;
    }
    
    public String Rule8(){
        String arg = "For relatively large dispersion (flexible rounding): Depending on the measurement unit, the dispersion (99% uncertainty of the mean) may be > 1 (a"
                + " fractional number having an integer part); it is rounded to three significant digits when it is <= 4.5 and to two significant digits when it is > 4.5.";
        return arg;
    }
    
    public String Rule9(){
        String arg = "The central tendency parameter, e.g, the mean value, will be reported as rounded to the same digits after the decimal points as"
                + " the dispersion parameter (99% uncertainty of the mean) or without any decimal point if the rounded value of the dispersion resulted"
                + " as an integer, i.e., follow Rules 7 and 8.";
        return arg;
    }
    
    public String Rule10(){
        String arg = "When a zero results at the right side of an integer dispersion parameter rounded in a single step or in a simple way, for the strict rounding"
                + " option, it is not necessary to force zero at the right for the integer rounded central tendency parameter.";
        return arg;
    }
    
    public String Rule11(){
        String arg = "When a zero results at the right side of an integer rounded dispersion parameter as a consequence of two rounding steps, zero is to be forced"
                + " at the right side of the integer rounded central tendency parameter.";
        return arg;
    }
    
    public String Rule12(){
        String arg = "As an alternative to the above rules, the standard deviation (s) can be rounded from the standard error of the standard deviation (se_s)."
                + " This means that first this error is rounded from Rules 4, 5, 7, or 8 and then the s can be rounded from se_s, following Rule 6 (strict)"
                + " or Rule 9 (flexible), depending on whether Rule 4 or 5 (strict), or 7 or 8 (flexible) was used.";
        return arg;
    }
    
    public String Rule13(){
        String arg = "The indications of Rule 12 can be applied to the mean whose rounding will be based on the standard error of the mean (se_x) in the same"
                + " way as done for s and se_s.";
        return arg;
    }
}




