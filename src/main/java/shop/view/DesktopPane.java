package shop.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.persistence.*;
import javax.swing.*;

import shop.dao.JPAProvider;
import shop.entity.Credentials;


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


        /*
        Configuration configuration = new Configuration();
        configuration.configure("application.properties");
        */



        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();

        em.createNativeQuery("DROP TABLE if exists Credentials", Credentials.class);
        em.getTransaction().begin();

        Credentials credentials = new Credentials();
        credentials.setUsername("shop");
        credentials.setPassword("shop");

        if (em.find(Credentials.class, credentials) == null) {
            em.persist(credentials);
            em.getTransaction().commit();
        }

        em.clear();
        em.close();


        /*
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Credentials credentials = new Credentials();
        credentials.setUsername("shop");
        credentials.setPassword("shop");
        session.save(credentials);

        //Commit the transaction
        session.getTransaction().commit();
        HibernateUtil.shutdown();
        session.close();
        */

        /*
        try {
            Connection con = (new DB_INFO()).getConnection();
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
            DB_INFO connectionManager= new DB_INFO();
            Connection con = connectionManager.getConnection();
            Statement stmt = con.createStatement();
            String QUERY="INSERT INTO Credentials VALUES ('" + connectionManager.getUsername() + "','" + connectionManager.getPassword() + "')";
            stmt.executeUpdate(QUERY);
            stmt.close();
            con.close();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

         */
    }

}

