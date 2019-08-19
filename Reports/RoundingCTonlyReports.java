/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author ShreyaS and Mau
 */
public class RoundingCTonlyReports  {

    private static ArrayList<ArrayList> datos = new ArrayList<ArrayList>();
    private static ArrayList head = new ArrayList();
    private static ArrayList<ArrayList> resultados = new ArrayList<ArrayList>();

    private Sheet sheet;
    private boolean resultCTonly;
    
    AMedia fileContent;
    private int global;
    
//    public static void  main (String args[]){
//        //ArrayList h = new ArrayList();
//        head.add("index"); head.add("n");head.add("m"); head.add("sd"); head.add("rsd"); head.add("uc");
//        ArrayList a = new ArrayList();
//        a.add("1"); a.add("2");a.add("0.5678"); a.add(""); a.add(""); a.add("");
//        datos.add(a); datos.add(a); datos.add(a); datos.add(a); datos.add(a);
//        ArrayList b = new ArrayList();
//        b.add("m_rounded"); 
//        resultados.add(b);
//        ArrayList c = new ArrayList();
//        c.add("35.6"); 
//        resultados.add(c); resultados.add(c); resultados.add(c); resultados.add(c); resultados.add(c); 
//        
//        RoundingCTonlyReports roundingRepo = new RoundingCTonlyReports("roundedReport.xlsx");
//        roundingRepo.printConsole(datos);
//        roundingRepo.printConsole(resultados);
//        
//    }
    
    public  RoundingCTonlyReports(String fileout , ArrayList<ArrayList> data,  ArrayList<ArrayList> result, ArrayList header) {
        this.resultCTonly = false;

        try {
            System.out.println("Hola residuales !!");
            FileOutputStream fileOutputStream = new FileOutputStream(fileout);
            Workbook wb = new XSSFWorkbook();              
            setDatos(data);
            setResultados(result);
            setHead(header);
            System.out.println("data to be printed "+!getDatos().isEmpty());
            printConsole(getDatos());
            System.out.println("result to be printed "+!getResultados().isEmpty());
            printConsole(getResultados());
            if(!getDatos().isEmpty() && !getResultados().isEmpty()){
                createSheetNormalResults(wb);
                global = 0;
            }
            
            System.out.println("11");
            wb.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            
            this.resultCTonly = true;
            System.out.println("12");
        } catch (Exception e) {
            Messagebox.show("something did not go well in the report" + e);
            e.printStackTrace();
        }
    }
    
    private void createSheetNormalResults(Workbook wb) {
        try{
            sheet = wb.createSheet("CT_Results");
            System.out.println("HOLA createSheetNormalResults");
            Object test = IndexedColors.BLUE.getIndex();
            Object test2 = IndexedColors.RED.getIndex();
            Row row0 = sheet.createRow((short) 0);
            for(int i = 0; i < getHead().size(); i++){
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                Font font = sheet.getWorkbook().createFont();
                font.setFontHeightInPoints((short) 8);
                font.setFontName("Arial");
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                font.setColor(Short.parseShort(test2.toString()));
                cellStyle.setFont(font);
                Cell cell;
                cell = row0.createCell(i);
                cell.setCellValue(getHead().get(i).toString());
                cell.setCellStyle(cellStyle);
                sheet.autoSizeColumn(12+i);
            }

            for(int i = 0; i < getDatos().size(); i++){
                Row row = sheet.createRow((short) i+1);
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                Font font = sheet.getWorkbook().createFont();
                font.setFontHeightInPoints((short) 8);
                font.setFontName("Arial");

                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                cellStyle.setFont(font);
                Cell cell;
                for(int j = 0; j < getDatos().get(i).size(); j++){
                    cell = row.createCell((short) j);
                    try {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(getDatos().get(i).get(j).toString()));
                    } catch (NumberFormatException e) {
                        cell.setCellValue(getDatos().get(i).get(j).toString());
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            for(int i = 0; i < getResultados().size(); i++){
                Row row = sheet.getRow((short) i);
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                Font font = sheet.getWorkbook().createFont();
                font.setFontHeightInPoints((short) 8);
                font.setFontName("Arial");
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
                cellStyle.setFont(font);
                Cell cell;

                for(int j = 0; j < getResultados().get(i).size(); j++){
                    cell = row.createCell(9+j);
                    if(i==0){
                    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                    font.setColor(Short.parseShort(test.toString()));
                    }
                    System.out.println("i: "+i+" j: "+j);
                    cell.setCellValue(getResultados().get(i).get(j).toString());              
                    cell.setCellStyle(cellStyle);
                    System.out.println(getResultados().get(i).get(j).toString());
                }

            }
            System.out.println("done here");
        }catch(NullPointerException e){
            System.out.println("e- "+e);
        }
    }
    
    public ArrayList<ArrayList> getDatos() {
        return datos;
    }

    public void setDatos(ArrayList<ArrayList> datos) {
        this.datos = datos;
    }

    public ArrayList<ArrayList> getResultados() {
        return resultados;
    }

    public void setResultados(ArrayList<ArrayList> resultados) {
        this.resultados = resultados;
    }
    
    public ArrayList getHead() {
        return head;
    }

    public void setHead(ArrayList head) {
        this.head = head;
    }
    
    public AMedia getFileContent() {
        return fileContent;
    }

    public void setFileContent(AMedia fileContent) {
        this.fileContent = fileContent;
    }
    
    public boolean getResultBool() {
        return this.resultCTonly;
    }
   
    private void printConsole(ArrayList<ArrayList> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                System.out.print(list.get(i).get(j) + "\t");
            }
            System.out.println();
        }
    }

}
