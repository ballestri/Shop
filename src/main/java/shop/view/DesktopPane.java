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
    private static final String USERNAME="shop";
    private static final String PASSWORD="shop";


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
        setIconImage(new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("images/window.png"))).getImage());
        setLayout(new BorderLayout());
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
        EntityManager em = JPAProvider.getEntityManagerFactory().createEntityManager();
        em.createNativeQuery("DROP TABLE if exists Credentials", Credentials.class);
        em.getTransaction().begin();
        Credentials credentials = new Credentials();
        credentials.setUsername(USERNAME);
        credentials.setPassword(PASSWORD);

        if (em.find(Credentials.class, credentials) == null) {
            em.persist(credentials);
            em.getTransaction().commit();
        }

        em.clear();
        em.close();

    }

}

