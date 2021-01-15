package shop.view;

import org.apache.ibatis.jdbc.ScriptRunner;
import shop.db.ConnectionManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.sql.*;

public class DesktopPane extends JFrame {

    private static final int WIDTH = 1575;
    private static final int HEIGHT = 960;


    public DesktopPane() {
        setTitle("Shop Platform v. 1.0");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        Dimension size = new Dimension(new Dimension(WIDTH, HEIGHT));
        setSize(size);
        setPreferredSize(size);
        setLocationByPlatform(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        setLocation(new Point((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2));
        setIconImage(new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/ico.png"))).getImage());
        getContentPane().removeAll();
        getContentPane().revalidate();
        getContentPane().add(new LoginPane().getPanel());
        getContentPane().repaint();
        getContentPane().doLayout();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String... args) {
        setDefaultLookAndFeelDecorated(true);
        (new DesktopPane()).setVisible(true);

        try {
            Connection con = (new ConnectionManager()).getConnection();
            ScriptRunner sr = new ScriptRunner(con);
            BufferedReader is = new BufferedReader(
                    new InputStreamReader(Objects.requireNonNull(DesktopPane.class.getClassLoader().getResourceAsStream("config/shop.sql"))));
            sr.runScript(is);
            is.close();
            con.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }


        // Connessione al DB
        try {
            ConnectionManager connectionManager= new ConnectionManager();
            Connection con = connectionManager.getConnection();
            Statement stmt = con.createStatement();
            String QUERY="INSERT INTO Credentials VALUES ('" + connectionManager.getUsername() + "','" + connectionManager.getPassword() + "')";
            stmt.executeUpdate(QUERY);
            stmt.close();
            con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

}
