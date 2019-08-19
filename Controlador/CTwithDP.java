/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

/**
 *
 * @author ShreyaS
 */
public class CTwithDP {
    @Wire("#options")
    private Window options;
    
    private boolean flexible = true;
    private int dp=3;
    
    public Window getOpciones() {
        return options;
    }

    public void setOpciones(Window options) {
        this.options = options;
    }
    
    public int getDP() {
        return dp;
    }
    
    @Command
    @NotifyChange({"dp"})
    public void setDP_1() {
        this.dp = 1;
    }
    
    @Command
    @NotifyChange({"dp"})
    public void setDP_2() {
        this.dp = 2;
    }
    
    @Command
    @NotifyChange({"dp"})
    public void setDP_3() {
        this.dp = 3;
    }
    
    @Command
    @NotifyChange({"dp"})
    public void setDP_4() {
        this.dp = 4;
    }
    
    @Command
    @NotifyChange({"flexible"})
    public void setFlexibilty(){
        this.flexible = true;
    
    }
    
    @Command
    @NotifyChange({"flexible"})
    public void setStrict(){
        this.flexible = false;
    
    }

    public boolean getFlexibilty (){
        return flexible;
    }

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {

        Selectors.wireComponents(view, this, false);
    }
    
    @Command
    public void process_file(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("significantDigits", 1);
        args.put("strictInput", !flexible);
        args.put("dp", dp);
        args.put("decimal", true);
        System.out.println("strictInput: "+ !flexible);
        System.out.println("dp: "+ dp);
        BindUtils.postGlobalCommand(null, null, "RoundingApply", args);
        Clients.clearBusy();
        options.detach();
    }
    
}
