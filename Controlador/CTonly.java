/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

/**
 *
 * @author ShreyaS
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class CTonly {
    @Wire("#options")
    private Window options;
    
    private int significant = 2;
    private boolean decimal = true; //default fractional input
    
    public Window getOpciones() {
        return options;
    }

    public void setOpciones(Window options) {
        this.options = options;
    }
    
    public void setSignificant (int significant){
        this.significant = significant;
    }

    public int getSignificant (){
        return significant;
    }
    
    public void setDecimal (boolean decimal){
        this.decimal = decimal;
    }

    public boolean getDecimal (){
        return decimal;
    }

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {

        Selectors.wireComponents(view, this, false);
    }
    
    @Command
    @NotifyChange({"decimal"})
    public void deci_true() {
        decimal = true;
        System.out.println(decimal);
    }
    
    @Command
    @NotifyChange({"decimal"})
    public void deci_false() {
        decimal = false;
        System.out.println(decimal);
    }
    
    @Command
    @NotifyChange({"significant"})
    public void sig_1() {
        significant = 1;
        System.out.println(significant);
    }
    
    @Command
    @NotifyChange({"significant"})
    public void sig_2() {
        significant = 2;
        System.out.println(significant);
    }
    
    @Command
    @NotifyChange({"significant"})
    public void sig_3() {
        significant = 3;
        System.out.println(significant);
    }
    
    @Command
    @NotifyChange({"significant"})
    public void sig_4() {
        significant = 4;
        System.out.println(significant);
    }
    
    @Command
    @NotifyChange({"significant"})
    public void sig_5() {
        significant = 5;
        System.out.println(significant);
    }
    
    @Command
    @NotifyChange({"significant"})
    public void sig_6() {
        significant = 6;
        System.out.println(significant);
    }
    
    @Command
    @NotifyChange({"significant"})
    public void sig_7() {
        significant = 7;
        System.out.println(significant);
    }
    
    @Command
    public void process_file(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("significantDigits", significant);
        args.put("strictInput", true);
        args.put("dp", 3);
        args.put("decimal", decimal);
        System.out.println("Rounding Apply");
        System.out.println("Significant: "+significant);
        BindUtils.postGlobalCommand(null, null, "RoundingApply", args);
        System.out.println("Rounding Apply done ");
        Clients.clearBusy();
        options.detach();
    }
    
}
