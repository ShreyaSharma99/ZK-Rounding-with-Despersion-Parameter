/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.zkoss.zul.Messagebox;

/**
 *
 * @author ShreyaS
 */
public final class LeerArchivoAllCTD {

    private ArrayList headers = new ArrayList();
    private ArrayList variables = new ArrayList();
    private ArrayList<ArrayList> originalBody = new ArrayList<ArrayList>();
    private ArrayList<ArrayList> validatedBody = new ArrayList<ArrayList>();

    private ArrayList<ArrayList> originalData = new ArrayList<ArrayList>();
    private ArrayList<ArrayList> validatedData = new ArrayList<ArrayList>();
    private ArrayList<ArrayList> errorsInData = new ArrayList<ArrayList>();
    private ArrayList<ArrayList> Errors = new ArrayList<ArrayList>();
    private ArrayList rowsWithErrors;

    private static boolean allInteger;
    private static boolean allDecimal;
    private int rows;
    private int cols;
    private int type=0; //get the user input for CT (0) only or CT with DP (1)
    
    private boolean CTonly;
    
    private static boolean m_colm = true;
    private static boolean sd_colm = true;
    private static boolean rsd_colm = true;
    private static boolean uc_colm1 = true;
    private static boolean uc_colm2 = true;

    private boolean []tests = new boolean[5]; // 0 wilksF
    //==========
    
//    public static void main(String args[]){
//        String fileName = "C:\\Users\\hp\\Desktop\\IER_UNAM\\RoundingOff_Template.xlsx";
//        LeerArchivoAllCTD leerArchivo = new LeerArchivoAllCTD(fileName, true );
//        System.out.println(leerArchivo.getRowsWithErrors().isEmpty());
//        System.out.println("m "+ m_colm);
//        System.out.println("sd "+ sd_colm);
//        System.out.println("rsd "+ rsd_colm);
//        System.out.println("uc "+ uc_colm);
//        System.out.println("allInt "+ allInteger);
//        System.out.println("allFrac "+ allDecimal);
//        
//        System.out.println(leerArchivo.getErrorsInData().isEmpty());
//        
//        
//    }
    
    public LeerArchivoAllCTD(String fileName, boolean onlyCT) {
        try {
            setCTonly(onlyCT);
           // System.out.println("hi");
            FileInputStream is = new FileInputStream(fileName);
            Workbook wb = null;
            System.out.println("fname");
            System.out.println(fileName + " " + fileName.endsWith("xlsx"));
            System.out.println(fileName + " " + fileName.endsWith("xls"));
            if (fileName.endsWith("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else if (fileName.endsWith("xls")) {
                wb = new HSSFWorkbook(is);
            } else {
                Messagebox.show("WARNING! - The specified file is not Excel file. See our 'Readme' and use a proper template or verify your data.");
                throw new IllegalArgumentException("The specified file is not Excel file");
            }

            Sheet sheet = wb.getSheetAt(0);
            if (ChangeSheetToArrayList(sheet)) {
                CheckArrayList(getOriginalData());
            } else {
                Messagebox.show("WARNING! - See our 'Readme' and use a proper template or verify your data.");
                wb.close();
                is.close();
            }
            
            allInteger = checkInteger(getOriginalBody());
            allDecimal = checkDecimal(getOriginalBody());

            wb.close();
            is.close();
        } catch (Exception ioe) {
            System.out.println(ioe);
        }
    }

private boolean ChangeSheetToArrayList(Sheet sheet) {
        System.out.println("entering to changeSheetToArrayList");
        ArrayList<ArrayList> ArrayListaDatos = new ArrayList<ArrayList>();

        try {
            ArrayList test = new ArrayList(); //stores indices of null rows
            Row aux = sheet.getRow(0);
            System.out.println(sheet.getLastRowNum() + "\t" + aux.getPhysicalNumberOfCells());
            System.out.println((sheet.getLastRowNum() > 2) + "\t" +  (aux.getPhysicalNumberOfCells() < 7));
            
            if (sheet.getLastRowNum() > 2 && aux.getPhysicalNumberOfCells() == 7) {
                //System.out.println("hola");
                
                ArrayList indexList = new ArrayList();
                for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    ArrayList ArrayListaTemporal = new ArrayList();
                    ArrayList errorline = new ArrayList();
                    //System.out.println(i);
                    if (row == null) {  //in case of a null row just add empty strings uptill the number of cells in the header row.
                        test.add((int) i);
                        errorline.add(" -Empty !");
                        for (int k = 0; k < aux.getPhysicalNumberOfCells(); k++) {
                            ArrayListaTemporal.add("");
                        }
                        ArrayListaDatos.add(ArrayListaTemporal);
                    }else {
                        if(i==0){
                            for (int k = 0; k < aux.getPhysicalNumberOfCells(); k++) {
                                Cell cell = row.getCell(k);
                                String cellStr = cell.toString();
                                ArrayListaTemporal.add(cellStr);
                            }
                        }
                        else{
                        for (int j = 0; j < aux.getPhysicalNumberOfCells(); j++) {
                            Cell cell = row.getCell(j);
                            //System.out.println("j "+row.getCell(j).toString());
                            if (j==0) {  //index column
                                if(cell == null || cell.toString().equals("")){
                                  //  System.out.println("Row: "+i+" -Empty index");
                                    test.add((int) i);
                                    ArrayListaTemporal.add("");
                                    errorline.add(" -Index empty");
                                }
                                else if(indexList.contains(cell.toString())){        //in case of wrong index input empty string in index column
                                    test.add((int) i);
                                    ArrayListaTemporal.add("");
                                  //  System.out.println("Row:"+i+" -Index shuold be uniquie");
                                    errorline.add(" -Index shuold be uniquie");
                                }
                                else{
                                    if(isDecimal(cell.toString())){
                                        indexList.add(cell.toString());
                                        ArrayListaTemporal.add(cell.toString());
                                    }
                                    else{
                                        ArrayListaTemporal.add("");
                                    }
                                    }
                            }
                            else if(j==1  && !CTonly){  //CT and DP type with no n or n<2
                                if((cell == null || cell.toString().equals(""))){
                                    test.add((int) i);
                                    ArrayListaTemporal.add("");
                                    System.out.println("Row:" + i + " -'n' should be specified when using despersion parameters for rounding off. ");
                                    errorline.add(" -'n' should be specified when using despersion parameters for rounding off. ");
                                }
                                else if(isDecimal(cell.toString())){
                                    if(Double.parseDouble(cell.toString())<2.0){
                                        test.add((int) i);
                                        ArrayListaTemporal.add(cell.toString());
                                        System.out.println("Row:" + i + " -'n' should be more than 1  ");
                                        errorline.add(" -number of samples 'n' should be more than 1  ");
                                    }
                                    else{
                                        ArrayListaTemporal.add(cell.toString());
                                    }
                                }
                                else {
                                    test.add((int) i);
                                    errorline.add(" -number of samples 'n' should be an integer ");
                                    ArrayListaTemporal.add(cell.toString());
                                }
                             }
                            else if(j==1  && CTonly){
                                if((cell == null || cell.toString().equals("")))
                                    ArrayListaTemporal.add("");
                                else ArrayListaTemporal.add(cell.toString());
                            }
                            else if(j==2){   //mean
                                if(cell == null || cell.toString().equals("")){
                                    m_colm = false;
                                    test.add((double)i);
                                    ArrayListaTemporal.add("");
                                    System.out.println("Row:"+i+" -Central Tendency missing");
                                    errorline.add(" -Central Tendency missing");
                                }
                                else if(isDecimal(cell.toString())){
                                    ArrayListaTemporal.add(cell.toString());
                                    System.out.println(cell.toString());
                                    System.out.println(cell.getNumericCellValue());
                                    }
                                else{
                                    test.add((int) i);  //elements are not numeric
                                    System.out.println("Row:"+i+" - only number inputs are valid ");
                                    errorline.add("only number inputs are valid");
                                    ArrayListaTemporal.add("");
                                    }
                            }
                            else if(j==3 && !CTonly){ //sd
                                if((cell == null || cell.toString().equals(""))){
                                    Cell cell_rsd = row.getCell(j+1);
                                    if(!(cell_rsd == null || cell_rsd.toString().equals(""))){
                                        if(isDecimal(cell_rsd.toString()) && isDecimal(ArrayListaTemporal.get(1).toString())){
                                            double rsd = Double.parseDouble(cell_rsd.toString());
                                            double n_rsd = Double.parseDouble(ArrayListaTemporal.get(1).toString());
                                            ArrayListaTemporal.add(Double.toString(rsd*(Math.sqrt(n_rsd))));
                                        }
                                        else{
                                            sd_colm = false;
                                            ArrayListaTemporal.add("");
                                            errorline.add(" -Cell is empty.");
                                        }
                                    }
                                    else {
                                        sd_colm = false;
                                        ArrayListaTemporal.add("");
                                        errorline.add(" -Cell is empty.");
                                        
                                    }
                                }
                                else if(isDecimal(cell.toString())){
                                    ArrayListaTemporal.add(cell.toString());
                                    }
                                else {
                                    test.add((int) i);  //elements are not numeric
                                    System.out.println("Row:"+i+" - only number inputs are valid ");
                                    errorline.add("only number inputs are valid");
                                    ArrayListaTemporal.add("");
                                }
                            }
                            else if(j==4 && !CTonly){ //rsd
                                if((cell == null || cell.toString().equals(""))){
                                    Cell cell_sd = row.getCell(j-1);
                                    if(!(cell_sd == null || cell_sd.toString().equals(""))){
                                        if(isDecimal(cell_sd.toString()) && isDecimal(ArrayListaTemporal.get(1).toString())){
                                            double sd = Double.parseDouble(cell_sd.toString());
                                            double n_sd = Double.parseDouble(ArrayListaTemporal.get(1).toString());
                                            if(n_sd!=0.0){
                                                ArrayListaTemporal.add(Double.toString(sd/(Math.sqrt(n_sd))));
                                            }
                                            else{
                                                sd_colm = false;
                                                ArrayListaTemporal.add("");
                                                errorline.add(" -Cell is empty.");
                                            }
                                        }
                                        else{
                                            sd_colm = false;
                                            ArrayListaTemporal.add("");
                                            errorline.add(" -Cell is empty.");
                                        }
                                    }
                                    else {
                                        sd_colm = false;
                                        ArrayListaTemporal.add("");
                                        errorline.add(" -Cell is empty.");
                                    }                                    
                                }
                                else if(isDecimal(cell.toString())){
                                    ArrayListaTemporal.add(cell.toString());
                                    }
                                else {
                                    test.add((int) i);  //elements are not numeric
                                    System.out.println("Row:"+i+" - only number inputs are valid ");
                                    errorline.add("only number inputs are valid");
                                    ArrayListaTemporal.add("");
                                }
                            }
                            else if(j==5 && !CTonly){ //uc95
                                if((cell == null || cell.toString().equals(""))){
                                    Cell cell_sd = row.getCell(3);  
                                    if(!(cell_sd == null || cell_sd.toString().equals(""))){
                                        if(isDecimal(cell_sd.toString()) && isDecimal(ArrayListaTemporal.get(1).toString())){
                                            double sd = Double.parseDouble(cell_sd.toString());
                                            double n_sd = Double.parseDouble(ArrayListaTemporal.get(1).toString());
                                            if(n_sd!=0.0){
                                                double u95 = getUncertainty(95.0, sd, (int)n_sd, 2);
                                                ArrayListaTemporal.add(Double.toString((sd / (Math.sqrt(n_sd))*u95)));
                                            }
                                            else{
                                                uc_colm1 = false;
                                                ArrayListaTemporal.add("");
                                                errorline.add(" -Cell is empty.");
                                            }
                                        }
                                        else{
                                            uc_colm1 = false;
                                            ArrayListaTemporal.add("");
                                            errorline.add(" -Cell is empty.");
                                        }
                                    }
                                    else{
                                        uc_colm1 = false;
                                        ArrayListaTemporal.add("");
                                        errorline.add(" -Cell is empty.");
                                    }
                                }
                                else if(isDecimal(cell.toString())){
                                    ArrayListaTemporal.add(cell.toString());
                                    }
                                else {
                                    test.add((int) i);  //elements are not numeric
                                    System.out.println("Row:"+i+" - only number inputs are valid ");
                                    errorline.add("only number inputs are valid");
                                    ArrayListaTemporal.add("");
                                }
                            }
                            else if(j==6 && !CTonly){ //uc99
                                if((cell == null || cell.toString().equals(""))){
                                    Cell cell_sd = row.getCell(3);  
                                    if(!(cell_sd == null || cell_sd.toString().equals(""))){
                                        if(isDecimal(cell_sd.toString()) && isDecimal(ArrayListaTemporal.get(1).toString())){
                                            double sd = Double.parseDouble(cell_sd.toString());
                                            double n_sd = Double.parseDouble(ArrayListaTemporal.get(1).toString());
                                            if(n_sd!=0.0){
                                                double u99 = getUncertainty(99.0, sd, (int)n_sd, 2);
                                                ArrayListaTemporal.add(Double.toString((sd / (Math.sqrt(n_sd))*u99)));
                                            }
                                            else{
                                                uc_colm2 = false;
                                                ArrayListaTemporal.add("");
                                                errorline.add(" -Cell is empty.");
                                            }
                                        }
                                        else{
                                            uc_colm2 = false;
                                            ArrayListaTemporal.add("");
                                            errorline.add(" -Cell is empty.");
                                        }
                                    }
                                    else{
                                        uc_colm2 = false;
                                        ArrayListaTemporal.add("");
                                        errorline.add(" -Cell is empty.");
                                    }
                                }
                                else if(isDecimal(cell.toString())){
                                    ArrayListaTemporal.add(cell.toString());
                                    }
                                else {
                                    test.add((int) i);  //elements are not numeric
                                    System.out.println("Row:"+i+" - only number inputs are valid ");
                                    errorline.add("only number inputs are valid");
                                    ArrayListaTemporal.add("");
                                }
                            }
                        }
                    }
                
                    ArrayListaDatos.add(ArrayListaTemporal);
                    Errors.add(errorline);
                    
                   // System.out.println("errorline: "+errorline);
                }    
                }
                //printConsole(Errors); 
                setOriginalData(ArrayListaDatos);
                //System.out.println("original data set");
                Set<String> hs = new LinkedHashSet<String>();
                hs.addAll(test);
                test.clear();
                test.addAll(hs);
                setRowsWithErrors(test);
                return true;
            } else {
                setOriginalData(null);
                //System.out.println("original data not set");
                setRowsWithErrors(null);
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }


    ///////////////////////////
    private void CheckArrayList(ArrayList<ArrayList> originalData) {
        System.out.println("entrando a CheckArrayList");
        if (HeadersValidation(originalData)) {
            Messagebox.show("WARNING! - Errors on input file. It will no be processed.");
        } else {
            if (ValidatedArrayListaDatos(originalData)) {

            } else {
                Messagebox.show("WARNING! - Errors on input file. It will no be processed.");
            }
        }
    }

    private boolean HeadersValidation(ArrayList<ArrayList> originalData) {
        System.out.println("HeadersValidation");
        ArrayList<ArrayList> totalErrors = new ArrayList<ArrayList>();
        boolean help = false;

        for (int i = 0; i < originalData.get(0).size(); i++) {
            ArrayList line = new ArrayList();
            if (i == 0 && !originalData.get(0).get(i).toString().toLowerCase().contains("index")) {
                line.add("Column: " + (i + 1));
                line.add("Header must be named as 'index' ");
                line.add(originalData.get(0).get(i).toString());
               // System.out.println(originalData.get(0).get(i).toString().toLowerCase());
            } else if (i == 1 && (!originalData.get(0).get(i).toString().toLowerCase().contains("n") && !originalData.get(0).get(i).toString().toLowerCase().contains("size"))) {
                line.add("Column: " + (i + 1));
                line.add("Header must contain 'n' or 'size' ");
                line.add(originalData.get(0).get(i).toString());
              // System.out.println(originalData.get(0).get(i).toString().toLowerCase().equals(""));
            } else if (i == 2 && !originalData.get(0).get(i).toString().toLowerCase().contains("m")) {
                line.add("Column: " + (i + 1));
                line.add("Header must contain 'm' for mean ");
                line.add(originalData.get(0).get(i).toString());
              // System.out.println(originalData.get(0).get(i).toString().toLowerCase());
            } else if (i == 3 && (!originalData.get(0).get(i).toString().toLowerCase().contains("sd") && !originalData.get(0).get(i).toString().toLowerCase().contains("s"))) {
                line.add("Column: " + (i + 1));
                line.add("Header must contain 'sd' or 's' for standard deviation ");
                line.add(originalData.get(0).get(i).toString());
              // System.out.println(originalData.get(0).get(i).toString().toLowerCase());
            } else if (i == 4 && (!originalData.get(0).get(i).toString().toLowerCase().contains("se") && !originalData.get(0).get(i).toString().toLowerCase().contains("rsd"))) {
                line.add("Column: " + (i + 1));
                line.add("Header must contain 'rsd' or 'se' for relative standard deviation or standard error ");
                line.add(originalData.get(0).get(i).toString());
              // System.out.println(originalData.get(0).get(i).toString().toLowerCase());
            } else if (i == 5 && !originalData.get(0).get(i).toString().toLowerCase().contains("u95")){
                line.add("Column: " + (i + 1));
                line.add("Header must contain 'u95' for uncertainity at 95% confidence level ");
                line.add(originalData.get(0).get(i).toString());
               // System.out.println(originalData.get(0).get(i).toString().toLowerCase());
            } else if (i == 6 && !originalData.get(0).get(i).toString().toLowerCase().contains("u99")){
                line.add("Column: " + (i + 1));
                line.add("Header must contain 'u99' for uncertainity at 99% confidence level");
                line.add(originalData.get(0).get(i).toString());
               // System.out.println(originalData.get(0).get(i).toString().toLowerCase());
            }
            
            if (originalData.get(0).get(i).toString() != null) {
                if (!((originalData.get(0).get(i).toString().indexOf('?') == -1)
                        && (originalData.get(0).get(i).toString().indexOf('¿') == -1)
                        && (originalData.get(0).get(i).toString().indexOf(':') == -1)
                        && (originalData.get(0).get(i).toString().indexOf('&') == -1)
                        && (originalData.get(0).get(i).toString().indexOf('#') == -1)
                        && (originalData.get(0).get(i).toString().indexOf('@') == -1)
                        && (originalData.get(0).get(i).toString().indexOf('$') == -1)
                        && (originalData.get(0).get(i).toString().indexOf('=') == -1)) || (originalData.get(0).get(i).toString().length() < 1)) {
                    line.add("Column: " + (i + 1));
                    if (originalData.get(0).get(i).toString().isEmpty()) {
                        line.add("Missing header.");
                    } else {
                        line.add("Header with unrecognized symbol or had empty spaces");
                    }
                    line.add(originalData.get(0).get(i).toString());
                }
            } else {
                line.add("Column: " + (i + 1));
                line.add("Missing header.");
                line.add("Empy cell");
            }
            
            if (!line.isEmpty()) {
                //Messagebox.show("here line error");
                totalErrors.add(line);
            }
        }

        if (!totalErrors.isEmpty()) {
            setErrorsInData(totalErrors);
            help = true;
        } else {
            help = false;
        }
        
        
        return help;
        //if file is correct help = false else true.
    }

    private boolean ValidatedArrayListaDatos(ArrayList<ArrayList> originalData) {
        ArrayList<ArrayList> oBody = new ArrayList<ArrayList>();
        ArrayList<ArrayList> vBody = new ArrayList<ArrayList>();

        ArrayList<ArrayList> dataChecked = new ArrayList<ArrayList>();
        ArrayList<ArrayList> dataCheckedAux = new ArrayList<ArrayList>();
        ArrayList<ArrayList> totalErrors = new ArrayList<ArrayList>();
        System.out.println("Hola ValidatedArrayListaDatos");
    try{
        int aux [] = new int[originalData.get(0).size()];
        for (int i = 1; i < originalData.size(); i++) {  //number of rows except the header
            ArrayList data = new ArrayList();
            for (int j = 0; j < originalData.get(1).size(); j++) { //traverse along the columns
                data.add((originalData.get(i).get(j).toString()));
            }
            dataCheckedAux.add(data);
        }
        int count=1;
        //In this code dataCheckedAux will check and remove error rows from its array list
        if (!getRowsWithErrors().isEmpty()) {
            loop1:
            //uncommneting the below section will remove the display of error of lines in the final output
            for (int j = 0; j < dataCheckedAux.size(); j++) {
                for (int i = 0; i < getRowsWithErrors().size(); i++) {
                    ArrayList list = new ArrayList();
                    if (j+count == Double.parseDouble(getRowsWithErrors().get(i).toString())) {
                        list.add("In Row: " + getRowsWithErrors().get(i)); //falta verificar infice....
//                        list.add("Incomplete data. This row will not be processed.");
//                        list.add("Empty row or row with empty cells.");
//                        System.out.println("error in " + (j+count) );
//                        System.out.println(getErrors().get(j+count).size());
                        for(int k=0; k<getErrors().get(j+count).size(); k++){
                            list.add(getErrors().get(j+count).get(k));
//                            System.out.println(getErrors().get(j+count).get(k));
                        }
                        totalErrors.add(list);
                        //dataCheckedAux.remove(j);
                        //j--;
                        //count++;
                        continue loop1;
                    }
                }
            }
//        printConsole(getErrors());
        }

        for (int i = 0; i < dataCheckedAux.size(); i++) {
            ArrayList data = new ArrayList();
            for (int j = 0; j < dataCheckedAux.get(i).size(); j++) {
                data.add(dataCheckedAux.get(i).get(j));
            }
            dataChecked.add(data);  //index skipped
        }

        setValidatedData(dataChecked);
        if (!totalErrors.isEmpty()) {
            //System.out.println("eror fnd");
            setErrorsInData(totalErrors);
        }
        for (int i = 0; i < originalData.size(); i++) {
            ArrayList data = new ArrayList();
            if (i == 0) {
                ArrayList vars = new ArrayList();
                for (int j = 0; j < originalData.get(0).size(); j++) {
                    data.add(originalData.get(i).get(j));
                    if (j >= 2) {
                        vars.add(originalData.get(i).get(j));
                    }
                }
                setVariables(vars);
                setHeaders(data);
            } else {
                for (int j = 0; j < originalData.get(i).size(); j++) {
                    data.add(originalData.get(i).get(j));
                }
                oBody.add(data);
            }
        }

        setOriginalBody(oBody);
        //printConsole(dataChecked);
        setValidatedBody(dataChecked);
        if (!getValidatedData().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }catch(Exception e){
        System.out.println(e);
        return false;
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
   
    private boolean checkInteger(ArrayList<ArrayList> data){
        int d;
        for(int i=0; i<data.size(); i++){
            for(int j=0; j<data.get(0).size()-3; j++){
                try {
                d = Integer.parseInt(data.get(i).get(j).toString());
                } catch (NumberFormatException nfe) {
                  return false;
                }  
            }
        }
        return true;
    }
    
    private boolean checkDecimal(ArrayList<ArrayList> data){
        double d;
        for(int i=0; i<data.size(); i++){
            for(int j=0; j<data.get(0).size()-3; j++){  //to check only till mean
                try {
                  d = Double.parseDouble(data.get(i).get(j).toString());
                } catch (NumberFormatException nfe) {
                    //System.out.println(nfe);
                    return false;
                }  
            }
        }
        return true;
    }
    
    private boolean isDecimal(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private double getUncertainty(double cLevel, double stdDev, int n, int oneSidedORtwoSided) {
        T_Test t = new T_Test();
        double tCV = t.Get_cv(n - 1, cLevel, oneSidedORtwoSided);
        System.out.println("getUncertainty - " + tCV);
        System.out.println("getUncertainty - " + stdDev);
        System.out.println("getUncertainty - " + n);
        return (tCV * (stdDev / Math.sqrt((double) n)));
    }
    
    ///////////////
    public ArrayList getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList headers) {
        this.headers = headers;
    }

    public ArrayList getVariables() {
        return variables;
    }

    public void setVariables(ArrayList variables) {
        this.variables = variables;
    }

    public ArrayList<ArrayList> getOriginalBody() {
        return originalBody;
    }

    public void setOriginalBody(ArrayList<ArrayList> originalBody) {
        this.originalBody = originalBody;
    }

    public ArrayList<ArrayList> getValidatedBody() {
        return validatedBody;
    }

    public void setValidatedBody(ArrayList<ArrayList> validatedBody) {
        this.validatedBody = validatedBody;
    }

    public ArrayList<ArrayList> getOriginalData() {
        return originalData;
    }

    public void setOriginalData(ArrayList<ArrayList> originalData) {
        this.originalData = originalData;
    }

    public ArrayList<ArrayList> getValidatedData() {
        return validatedData;
    }

    public void setValidatedData(ArrayList<ArrayList> validatedData) {
        this.validatedData = validatedData;
    }

    public ArrayList<ArrayList> getErrorsInData() {
        return errorsInData;
    }

    public void setErrorsInData(ArrayList<ArrayList> errorsInData) {
        this.errorsInData = errorsInData;
    }

    public ArrayList getRowsWithErrors() {
        return rowsWithErrors;
    }

    public void setRowsWithErrors(ArrayList rowsWithErrors) {
        this.rowsWithErrors = rowsWithErrors;
    }
    
    public void setErrors(ArrayList<ArrayList> Errors) {
        this.Errors = Errors;
    }
    
    public ArrayList<ArrayList> getErrors() {
        return Errors;
    }
    
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
    
    public boolean getAllInteger(){
        return allInteger;
    }
    
    public boolean getAllDecimal(){
        return allDecimal;
    }
    
    public boolean getM_colm(){
        return m_colm;
    }
    
    public boolean getSD_colm(){
        return sd_colm;
    }
    
    public boolean getRSD_colm(){
        return rsd_colm;
    }
    
    public boolean getUC_colm1(){
        return uc_colm1;
    }
    
    public boolean getUC_colm2(){
        return uc_colm2;
    }
    
    public boolean getCTonly() {
        return CTonly;
    }

    public void setCTonly(boolean onlyCT) {
        this.CTonly = onlyCT;
    }
    
}
