import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

/**
 * Created by azaz on 17.03.15.
 */
public class Main extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JButton startRunButton;
    private JTextArea textArea1;
    private Color defaultColor;
    public Main() {

        this.add(panel1);
        panel1.setVisible(true);
        textArea1.setEditable(false);
        defaultColor=textField1.getSelectionColor();

        textArea1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField1.setSelectionColor(defaultColor);
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        startRunButton.addActionListener(e -> {
            Variables.const_list=new ArrayList<String>();
            Variables.ident_list=new ArrayList<String>();
            Variables.input = textField1.getText().toLowerCase();
            Variables.str = Variables.input.toCharArray();
            Variables.pos = 0;
            Variables.endFlag=true;
            Variables.IsTo=true;
            try {
                boolean ss = Checkers.CheckAll();
                System.out.println(ss);
                textArea1.setText("Строка принадлежит языку\n");
                textArea1.append("Константы: " + Variables.const_list.toString() + "\n");
                textArea1.append("Идентификаторы: " + Variables.ident_list.toString()+"\n");

                if(Variables.endVal>Integer.MIN_VALUE&&Variables.startVal>Integer.MIN_VALUE) {
                    int delta=Variables.endVal-Variables.startVal;
                    if(!Variables.IsTo){
                        delta=-delta;
                    }
                    if(delta>0) {
                        textArea1.append("Кол-во итераций: " + Math.abs(Variables.endVal - Variables.startVal) + "\n");
                    }
                }
            } catch (MyException e1) {
                //System.out.println("@!#!@#!@#!@#   " + e1.getMessage() + " near by " + Variables.input.substring(Variables.pos - 2, Variables.pos + 2));
                textArea1.setText("Строка не принадлежит языку\n");
                textArea1.append(e1.getMessage());// + " рядом с " + Variables.input.substring(Variables.pos+1, Variables.pos+2));
                textField1.requestFocus();
                textField1.setSelectionColor(Color.RED);
                textField1.select(Variables.pos+1 , Variables.pos+2);
                e1.printStackTrace();
            }
            this.pack();

        });
    }

}