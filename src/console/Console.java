/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Кастумна консолька
 * @author Wittmann
 */
public class Console extends JPanel{
    
    protected JTextField input;
    protected JTextArea output;
    public Data data;
    protected int fontSize;
    protected boolean response;

    public Console(){
        super();
        initConsole();
        response = true;
    }
    
    public final void initConsole(){
        
        fontSize = 20;
        data = new Data();
        
        input = new JTextField();
        input.addKeyListener(new KeyList());
        input.setBackground(Color.DARK_GRAY);
        input.setForeground(Color.LIGHT_GRAY);
                
        output = new JTextArea();
        output.setEditable(false);
        output.setBackground(Color.DARK_GRAY);
        output.setForeground(Color.LIGHT_GRAY);
        updateFont();
        
        this.setLayout(new BorderLayout());
        
        this.add(output, BorderLayout.CENTER);
        this.add(input, BorderLayout.NORTH);
        
        this.setPreferredSize(new Dimension(820,640));
        
        clearConsole();
    }
    
    protected void updateFont(){
        output.setFont(new Font(Font.DIALOG, Font.PLAIN, fontSize));
        input.setFont(new Font(Font.DIALOG, Font.PLAIN, fontSize));
    }
   
    public void clearConsole(){
        data.setText("");
        data.setLastLine("");
    }

    
    protected class KeyList implements KeyListener{

        
        @Override
        public void keyTyped(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                String text = input.getText();
                data.setLastLine(text);
                if (response){
                    data.setText(text + "\n" +data.getText());
                }
                output.setText(data.getText());
                input.setText("");
            }
            
            if(e.getKeyCode() == 107 && e.isControlDown()){  //+
                fontSize++;
                updateFont();
            }
            if(e.getKeyCode() == 109 && e.isControlDown()){  //-
                fontSize--;
                updateFont();
            }
            if(e.getKeyCode() == 27 ){  //Esc
                System.exit(0);
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            
        }


    
    }
}
