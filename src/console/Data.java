/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import java.util.ArrayList;
import java.util.Observable;

    /**
     * Зберігає всі дані введені в консоль
     * @author Wittmann
     */
    public class Data extends Observable
    {
        private String text;
        private String lastLine;
        private ArrayList<String> history;
        
        public Data(){
            history = new ArrayList<String>();
        }

        public String getText(){
            return text;
        }
        public String getLastLine(){
            return lastLine;
        }
        public ArrayList<String> getHistory(){
            return history;
        }

        protected void setText(String text){
            this.text = text;
        }
        protected void setLastLine(String text){
            this.lastLine = text;
            this.history.add(text);
            this.setChanged();
            this.notifyObservers();
        }
        
        
    }
