import javax.swing.*;
/**
 * Created by azaz on 17.03.15.
 */
public class Starter {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main mf=new Main();
            mf.setTitle("Лабораторная работа");
            mf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mf.pack();
            mf.setVisible(true);

        });
    }
}
