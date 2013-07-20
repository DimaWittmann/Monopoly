/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import java.util.Observable;
import java.util.Observer;
/**
 * Визнааються команди для консолі,
 * для доданн команд потрібно перевизначити методи
 * executeConsoleCommand або/і executeGeneralCommand
 * @author Wittmann
 */
public class ConsoleExecuter implements Observer{
    Console console;
    
    public ConsoleExecuter(Console console){
        this.console = console; 
        this.console.data.addObserver(this);
        
    }

    @Override
    public void update(Observable o, Object arg) {
        String command = console.data.getLastLine();
        System.out.println(command);
        boolean exec = executeConsoleCommand(command);
        if (!exec){
            exec = executeGeneralCommand(command);
        }
        
    }
    protected boolean executeConsoleCommand(String command){
        boolean exec = false;
        switch (command.trim()){
            case "clr": console.clearConsole();
                      exec = true;
                      break;
        }
        
        
        return exec;
    }
    
    public boolean executeGeneralCommand(String command){
        boolean exec = false;
        return exec;
    }
}
