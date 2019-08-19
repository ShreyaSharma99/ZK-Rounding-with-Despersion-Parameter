/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;

/**
 * @author Abde
 */
public class LongOperationComposer extends SelectorComposer<Component>{

    @Wire
    private Button procButton;

    @Listen("onClick=#procButton")
    public void onBusy() {
        Clients.showBusy("System is processing your request. If the data file contains more than 1000 rows, this process will take several minutes");        
        Events.echoEvent("onLongOp", procButton, null);
        //System.out.println("Hola aux ");
    }

    public Button getProcButton() {
        return procButton;
    }

    public void setProcButton(Button procButton) {
        this.procButton = procButton;
    }

    
}
