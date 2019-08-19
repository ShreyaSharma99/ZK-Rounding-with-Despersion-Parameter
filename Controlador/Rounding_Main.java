/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import RoundingRules.RoundingLogic;
import Reports.RoundingCTonlyReports;

import RoundingRules.RoundingRulesText;
import Reports.RoundingCTwithDpReports;
import java.io.ByteArrayInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.servlet.http.HttpSession;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.io.Files;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author ShreyaS
 */
public class Rounding_Main {

    private boolean inicio = true;
    ////////////////////
    private String filePath=null;
    private String fileName=null;
    private String fileout=null;
    //private boolean fileopen = false;
    private boolean fileOpenAllCTD = false;
    private boolean wefounderrors1 = true;
    private boolean wefounderrors2 = true;
    private boolean resultCTonly = false;
    private boolean resultCTwithDP = false;

    private int numcol=0;
    private String numcol1 = null;
    private boolean onlyCT = false;
    private boolean ctMenuOptions = false;
    private boolean ctdMenuOptions = false;
    private boolean roundingTrue = false;
//    private boolean strictInput=true;

    private boolean openFile = false;

    public boolean isOpenFile() {
        return openFile;
    }

    public void setOpenFile(boolean openFile) {
        this.openFile = openFile;
    }
    
    private LeerArchivoAllCTD leerArchivoAllCTD;
    private ArrayList<ArrayList> fullInfo = new ArrayList<ArrayList>();  //full data
   private ArrayList<ArrayList> datos = new ArrayList<ArrayList>();     //the data from row2
    private ArrayList head = new ArrayList();                            //headings
    private ArrayList<ArrayList> errors;
    private ArrayList rowsWithErrors;
    private ArrayList reportHead = new ArrayList();
    private ArrayList<ArrayList> reportResult = new ArrayList<ArrayList>();
    private ArrayList<ArrayList> result = new ArrayList<ArrayList>();    //final output

    RoundingLogic roundingLogic;  //logic
    RoundingRulesText roundingRulesText;  //rules
    private RoundingCTonlyReports roundingCTReports;  //output file for onlt CT
    private RoundingCTwithDpReports roundingCTwithDpReports;  //output file for CT with DP

    private ArrayList headers = new ArrayList();
    private ArrayList variables = new ArrayList();
    private ArrayList<ArrayList> originalBody = new ArrayList<ArrayList>();
    private ArrayList<ArrayList> validatedBody = new ArrayList<ArrayList>();

    AMedia fileContentOut;
    AMedia fileContentOut2;

    private static boolean m_colm = true;
    private static boolean sd_colm = true;
    private static boolean rsd_colm = true;
    private static boolean uc_colm1 = true;
    private static boolean uc_colm2 = true;

    private int showTableA;
    private String showTableA1;
    private int showTableB;
    private String showTableB1;
    private String showTableResults;
    //===
    @Wire("#test")
    private Window win;

    public Rounding_Main() {

    }

    @Command
    @NotifyChange({"showTableA1", "showTableB1", "showTableResults", "numcol1", "inicio", "fileOpenAllCTD",  "openFile", "result", "resultCTonly", "resultCTwithDP",  "reportHead", "reportResult", 
        "wefounderrors1", "wefounderrors2", "onlyCT","fileOpenAllCTD", "headers", "validatedBody", "ctMenuOptions", "ctdMenuOptions", "errors"})
    public void openFileCT(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {
        onlyCT = true;
        inicio = false;
        openFileCTandD_Template(ctx);
    }

    @Command
    @NotifyChange({"showTableA1", "showTableB1", "showTableResults", "numcol1", "inicio", "fileOpenAllCTD",  "openFile", "result", "resultCTonly", "resultCTwithDP",  "reportHead", "reportResult", 
        "wefounderrors1", "wefounderrors2", "onlyCT","fileOpenAllCTD", "headers", "validatedBody", "ctMenuOptions", "ctdMenuOptions", "errors"})
    public void openFileCTandD(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {
        onlyCT = false;
        inicio = false;
        openFileCTandD_Template(ctx);
    }

    //=====
    //note: check notifychange list 
    public void openFileCTandD_Template(BindContext ctx) throws IOException {
        
        UploadEvent upEvent = null;
        Object objUploadEvent = ctx.getTriggerEvent();
        if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
            upEvent = (UploadEvent) objUploadEvent;
        }
        if (upEvent != null) {
            Media media = upEvent.getMedia();
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH); // Note: zero based!
            int day = now.get(Calendar.DAY_OF_MONTH);
            filePath = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/");
            String yearPath = "\\" + "XLSs" + "\\" + year + "\\" + month + "\\"
                    + day + "\\";
            filePath = filePath + yearPath;
            File baseDir = new File(filePath);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }

            Files.copy(new File(filePath + media.getName()), media.getStreamData());

            filePath = filePath + media.getName();
            System.out.println(media.getName());
            int pos = media.getName().lastIndexOf(".");
            fileName = pos > 0 ? media.getName().substring(0, pos) : media.getName();
            System.out.println("justName - " + fileName);

            try {
                leerArchivoAllCTD = new LeerArchivoAllCTD(filePath, onlyCT);

                if (onlyCT) {
                    ctMenuOptions = true;
                    ctdMenuOptions = false;
                }
                if (!onlyCT) {
                    ctMenuOptions = false;
                    ctdMenuOptions = true;
                }

                if (leerArchivoAllCTD.getErrorsInData().isEmpty()) {
                    fileOpenAllCTD = true;
                    openFile = true;
                    
                    //System.out.println("deci int " + (leerArchivoAllCTD.getAllInteger() || leerArchivoAllCTD.getAllDecimal()));
                    if (onlyCT && (leerArchivoAllCTD.getAllInteger() || leerArchivoAllCTD.getAllDecimal())) {
                        wefounderrors1 = false;
                    }
                    headers = (leerArchivoAllCTD.getHeaders());
                    variables = (leerArchivoAllCTD.getVariables());
                    validatedBody = (leerArchivoAllCTD.getValidatedBody());
                    fullInfo = (leerArchivoAllCTD.getValidatedData());
                    numcol = leerArchivoAllCTD.getHeaders().size() * 150;
                    numcol1 = numcol + "px";
                    m_colm = leerArchivoAllCTD.getM_colm();
                    sd_colm = leerArchivoAllCTD.getSD_colm();
                    rsd_colm = leerArchivoAllCTD.getRSD_colm();
                    uc_colm1 = leerArchivoAllCTD.getUC_colm1();
                    uc_colm2 = leerArchivoAllCTD.getUC_colm2();

                    Messagebox.show("FILE SUCCESSFULLY VALIDATED");

                    if (uc_colm1 ||uc_colm2 || sd_colm || rsd_colm) {
                        wefounderrors2 = false;
                    }

                    setTables(0);
                } else {
                    fileOpenAllCTD = true;
                    openFile = true;
                    
                    headers = (leerArchivoAllCTD.getHeaders());
                    variables = (leerArchivoAllCTD.getVariables());
                    originalBody = (leerArchivoAllCTD.getOriginalBody());
                    validatedBody = (leerArchivoAllCTD.getValidatedBody());
                    fullInfo = (leerArchivoAllCTD.getValidatedData());
                    errors = leerArchivoAllCTD.getErrorsInData();

                    rowsWithErrors = leerArchivoAllCTD.getRowsWithErrors();

                    wefounderrors1 = true;
                    Messagebox.show("WARNING!, some error cells were found; these rows will not be evaluated. Please check the 'File with errors' tab displayed below the 'Open File' tab in the main frame.");
                    setTables(1);
                }
            } catch (Exception e) {
                System.out.println(e);
                Messagebox.show("WARNING! - The specified file is not Excel file. See our 'Readme' and use a proper template or verify your data.");
            }
        }
    }

    private void setTables(int par) {
        showTableResults = "95%";
        if (par == 0) {
            showTableA = 95;
            showTableA1 = showTableA + "%";

            showTableB = 0;
            showTableB1 = showTableB + "%";
        } else {
            showTableA = 60;
            showTableA1 = showTableA + "%";

            showTableB = 35;
            showTableB1 = showTableB + "%";

        }
    }
    
    @Command
    public void windowCTonly() {
        Window window = (Window) Executions.createComponents("menuCTonly.zul", null, null);
        window.doModal();
    }

    @Command
    public void windowCTwithDP() {
        Window window = (Window) Executions.createComponents("menuCTwithDP.zul", null, null);
        window.doModal();
    }

    @GlobalCommand
    @NotifyChange({"fileOpenAllCTD", "wefounderrors", "roundingTrue","roundingLogic", "onlyCT", "result", "resultCTonly", "resultCTwithDP", "reportHead", "reportResult"})
    public void RoundingApply(@BindingParam("significantDigits") int significantDigits, @BindingParam("strictInput") boolean strictInput, @BindingParam("dp") int dp,  @BindingParam("decimal") boolean decimal) throws IOException {

        if (validatedBody.isEmpty()) {
            Messagebox.show("Error with the input file !");
        } else {
            //Rounding done
            roundingLogic = new RoundingLogic(validatedBody, headers, significantDigits, onlyCT, strictInput, dp, decimal);

            for (int i = 0; i < headers.size(); i++) {
                head.add(headers.get(i));
            }
            
            roundingTrue = roundingLogic.getRoundingTrue();

            // datos = validatedBody;
            result = roundingLogic.getResult();
            System.out.println("result after rounding- " + result.size());
            printConsole(result);
            System.out.println("done result");
            //Reports
            fileout = webContentRoot + fileName + "_roundedReport.xlsx";

            if (onlyCT) {
                roundingCTReports = new RoundingCTonlyReports(fileout, validatedBody, result, head);
                reportHead = roundingCTReports.getHead();
                reportResult = roundingCTReports.getResultados();
                resultCTonly = roundingCTReports.getResultBool();

            } else {
                roundingCTwithDpReports = new RoundingCTwithDpReports(fileout, validatedBody, result, head);
                reportHead = roundingCTwithDpReports.getHead();
                reportResult = roundingCTwithDpReports.getResultados();
                resultCTwithDP = roundingCTwithDpReports.getResultBool();
            }
        }
        reportHead = new ArrayList();
        reportHead = reportResult.get(0);
        reportResult.remove(0);
        Clients.clearBusy();
    }
    
    @Command
    @NotifyChange("fileContentOut")
    public void showXLSCT() throws IOException {
        File f = new File(getFileout());
        byte[] buffer = new byte[(int) f.length()];
        FileInputStream fs = new FileInputStream(f);
        fs.read(buffer);
        fs.close();
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        fileContentOut = new AMedia(fileName + "_RoundingReport", "xlsx", "application/xlsx", is);
        Messagebox.show("FILE INFORMATION IS SUCCESSFULLY SENT TO YOUR DOWNLOAD FOLDER");
    }

    @Command
    @NotifyChange({"fileContentOut"})
    public void FinalResultsCT() throws IOException {
        showXLSCT();
    }

    
    //
     @Command
    @NotifyChange("fileContentOut2")
    public void showXLSCTD() throws IOException {
        File f = new File(getFileout());
        byte[] buffer = new byte[(int) f.length()];
        FileInputStream fs = new FileInputStream(f);
        fs.read(buffer);
        fs.close();
        ByteArrayInputStream is = new ByteArrayInputStream(buffer);
        fileContentOut2 = new AMedia(fileName + "_RoundingReport", "xlsx", "application/xlsx", is);
        Messagebox.show("FILE INFORMATION IS SUCCESSFULLY SENT TO YOUR DOWNLOAD FOLDER");
    }

    @Command
    @NotifyChange({"fileContentOut2"})
    public void FinalResultsCTD() throws IOException {
        showXLSCTD();
    }
    //---------------
    // seccion es para obtener la sesion aqr ******
    String webContentRoot;

    public String getApplicationPath() {
        return Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
    }

    public String getSessionId() {
        Session zkses = Sessions.getCurrent();
        HttpSession httpses = (HttpSession) zkses.getNativeSession();
        return httpses.getId();
    }

    public void createDir(String dirName) {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            System.out.println("creating directory: " + dirName);
            boolean result = false;
            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
            }
            if (result) {
                System.out.println("DIR created");
            }
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

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
        String sessionId = this.getSessionId();   // esto es para obtener el id de sesion al inicion del programa
        webContentRoot = this.getApplicationPath();
        createDir(webContentRoot + "/tmp");
        createDir(webContentRoot + "/tmp/" + sessionId);
        webContentRoot += "/tmp/" + sessionId + "/"; //ruta completa
        Selectors.wireComponents(view, this, false);
    }

    ////////////////////////////////////////////////
    public boolean isInicio() {
        return inicio;
    }

    public void setInicio(boolean inicio) {
        this.inicio = inicio;
    }

    ////////////////////////////////////////////////
    //Setters and Getters
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileout() {
        return fileout;
    }

    public void setFileout(String fileoutOrdinary) {
        this.fileout = fileout;
    }

    //Central Tendency and Dispersion Template
    public boolean isfileOpenAllCTD() {
        return fileOpenAllCTD;
    }

    public void setfileOpenAllCTD(boolean fileOpenAllCTD) {
        this.fileOpenAllCTD = fileOpenAllCTD;
    }

    public boolean getWefounderrors1() {
        return wefounderrors1;
    }

    public void setWefounderrors1(boolean wefounderrors) {
        this.wefounderrors1 = wefounderrors;
    }

    public boolean getWefounderrors2() {
        return wefounderrors2;
    }

    public void setWefounderrors2(boolean wefounderrors) {
        this.wefounderrors2 = wefounderrors;
    }

    public boolean getResultCTonly() {
        return resultCTonly;
    }

    public void setResultCTonly(boolean resultCTonly) {
        this.resultCTonly = resultCTonly;
    }

    public boolean getResultCTwithDP() {
        return resultCTwithDP;
    }

    public void setResultCTwithDP(boolean resultCTwithDP) {
        this.resultCTwithDP = resultCTwithDP;
    }

    public boolean getOnlyCT() {
        return onlyCT;
    }

    public void setOnlyCT(boolean onlyCT) {
        this.onlyCT = onlyCT;
    }

    public boolean isCtMenuOptions() {
        return ctMenuOptions;
    }

    public void setCtMenuOptions(boolean ctMenuOptions) {
        this.ctMenuOptions = ctMenuOptions;
    }

    public boolean isCtdMenuOptions() {
        return ctdMenuOptions;
    }

    public void setCtdMenuOptions(boolean ctdMenuOptions) {
        this.ctdMenuOptions = ctdMenuOptions;
    }

    public String getNumcol1() {
        return numcol1;
    }

    public void setNumcol1(String numcol1) {
        this.numcol1 = numcol1;
    }

    public String getShowTableA1() {
        return showTableA1;
    }

    public void setShowTableA1(String showTableA1) {
        this.showTableA1 = showTableA1;
    }

    public String getShowTableB1() {
        return showTableB1;
    }

    public void setShowTableB1(String showTableB1) {
        this.showTableB1 = showTableB1;
    }
    
    public ArrayList getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList head) {
        this.headers = head;
    }

    public ArrayList<ArrayList> getValidatedBody() {
        return validatedBody;
    }

    public void setValidatedBody(ArrayList<ArrayList> tabla) {
        this.validatedBody = tabla;
    }

    public ArrayList getReportHead() {
        return reportHead;
    }

    public void setReportHead(ArrayList head) {
        this.reportHead = head;
    }

    public ArrayList<ArrayList> getReportResult() {
        return reportResult;
    }

    public void setReportResult(ArrayList<ArrayList> reportResult) {
        this.reportResult = reportResult;
    }

    public AMedia getFileContentOut() {
        return fileContentOut;
    }

    public void setFileContentOut(AMedia file) {
        this.fileContentOut = file;
    }

    public AMedia getFileContentOut2() {
        return fileContentOut2;
    }

    public void setFileContentOut2(AMedia fileContentOut2) {
        this.fileContentOut2 = fileContentOut2;
    }

    ////////////////////////////////////////////////
    
    public String getWebContentRoot() {
        return webContentRoot;
    }

    public void setWebContentRoot(String value) {
        webContentRoot = value;
    }
    
    ////////////////////////////////////////////////

    public ArrayList<ArrayList> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<ArrayList> errors) {
        this.errors = errors;
    }
    
    public String getShowTableResults() {
        return showTableResults;
    }

    public void setShowTableResults(String showTableResults) {
        this.showTableResults = showTableResults;
    }
    
    
    ///////////////////////

    public boolean isFileOpenAllCTD() {
        return fileOpenAllCTD;
    }

    public void setFileOpenAllCTD(boolean fileOpenAllCTD) {
        this.fileOpenAllCTD = fileOpenAllCTD;
    }

    public boolean isRoundingTrue() {
        return roundingTrue;
    }

    public void setRoundingTrue(boolean roundingTrue) {
        this.roundingTrue = roundingTrue;
    }

    public ArrayList<ArrayList> getResult() {
        return result;
    }

    public void setResult(ArrayList<ArrayList> result) {
        this.result = result;
    }

    public RoundingCTonlyReports getRoundingCTReports() {
        return roundingCTReports;
    }

    public void setRoundingCTReports(RoundingCTonlyReports roundingCTReports) {
        this.roundingCTReports = roundingCTReports;
    }

    public RoundingCTwithDpReports getRoundingCTwithDpReports() {
        return roundingCTwithDpReports;
    }

    public void setRoundingCTwithDpReports(RoundingCTwithDpReports roundingCTwithDpReports) {
        this.roundingCTwithDpReports = roundingCTwithDpReports;
    }
    
}