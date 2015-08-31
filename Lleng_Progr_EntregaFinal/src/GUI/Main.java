package GUI;

import GUI.Body.BodyGUI;
import GUI.Header.HeaderGUI;
import Utils.DB.DBI;
import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author Pere Balaguer Gimeno
 * @author Lluc Martorell Medina
 */
public class Main extends javax.swing.JFrame {

    public static DBI connection;
    private BodyGUI body1;
    private HeaderGUI header1;

    public Main() {
        initComponents();
    }

    private void initComponents() {
        header1 = new HeaderGUI(this);
        body1 = new BodyGUI();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Universitat de les Illes Balears");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(0, 0, 0));
        setBounds(new java.awt.Rectangle(100, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(1150, 740));
        setPreferredSize(new java.awt.Dimension(1150, 740));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        getContentPane().add(header1);
        getContentPane().add(body1);

        pack();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        connection.closeDB();
    }

    public BodyGUI getBody() {
        return body1;
    }

    public static void main(String args[]) {
        boolean DbProblem = false;
        
        try {
            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        try {
            connection = new DBI();
        } catch (SQLException ex) {
            DbProblem = true;
            String msg = "<html>"
                    + "<h4>Hi ha hagut un problema al connectar-se a la base de dades local.</h4>"
                    + "<p>"
                        + "Revisi que el servidor MySQL estigui connectat.<br>La configuració sigui la pertinent:"
                        + "<ul>"
                            + "<li>Importar BD pbg162_lmm795"
                            + "<li>User = root"
                            + "<li>Port = 3306"
                        + "</ul>"
                    + "</p></html>";
            javax.swing.JLabel label = new javax.swing.JLabel(msg);
            label.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 12));
            javax.swing.JOptionPane.showMessageDialog(null, label, "Error connexió BD", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            if (!DbProblem) {
                java.awt.EventQueue.invokeLater(() -> {
                    new Main().setVisible(true);
                });
            }
        }
    }
}
