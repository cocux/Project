package Interface;

import Data.Contact;
import Data.Message;
import Data.User;
import ClientServer.ClientQuery;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ClientServer.Connexion;
import com.google.gson.Gson;

public class MainConsole {

    private int numberUser;
    private static JComboBox listeConv;
    private JLabel msg = new JLabel("Message : ");
    private JTextArea convText = new JTextArea(12,35);
    private JScrollPane scrollPane;
    private JLabel userOnLine = new JLabel("Nombre d'User en ligne : " + numberUser);
    private JTextField msgText = new JTextField();
    private JButton newGroup = new JButton("Nouveau Groupe");
    private JButton sendMsg = new JButton("Envoyer");
    private JButton disconnect = new JButton("Déconnexion");
    private SimpleDateFormat formater = new SimpleDateFormat("h:mm a");
    private static Connexion connection;
    private static ArrayList<String> tabContact;
    private User User;
    private String[] tabVerdict;
    public Thread t;
    static JPanel northPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel eastPanel = new JPanel();
    JPanel southPanel = new JPanel();
    JPanel centerNorthPanel = new JPanel();
    JPanel centerSouthPanel = new JPanel();
    JPanel southWestPanel = new JPanel();
    JPanel southEastPanel = new JPanel();

    public MainConsole(User user) {

        User = user;
        JFrame mainWindows = new JFrame();
        mainWindows.setMinimumSize(new Dimension(640, 380));
        mainWindows.setLayout(new BorderLayout());
        mainWindows.setLocationRelativeTo(null);

        mainWindows.add(northPanel, BorderLayout.NORTH);
        mainWindows.add(centerPanel, BorderLayout.CENTER);
        mainWindows.add(eastPanel, BorderLayout.EAST);
        eastPanel.setLayout(new GridLayout(4, 0));
        mainWindows.add(southPanel, BorderLayout.SOUTH);
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));

        scrollPane = new JScrollPane(convText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        centerPanel.add(centerNorthPanel, BorderLayout.NORTH);
        centerPanel.add(centerSouthPanel, BorderLayout.SOUTH);
        southPanel.add(southWestPanel, BorderLayout.WEST);
        southPanel.add(southEastPanel, BorderLayout.EAST);
        centerPanel.add(scrollPane);

        convText.setEditable(false);
        convText.setLineWrap(true);
        convText.setWrapStyleWord(true);

        tabContact = new ArrayList<>();
        for (Contact c : User.getContacts()) {
            tabContact.add(c.getLogin());
        }
        listeConv = new JComboBox(tabContact.toArray());

        for (Contact contact : User.getContacts()) {
            if (contact.getLogin().equals(listeConv.getSelectedItem())) {
                for (Message message : contact.getMessage()) {
                    convText.append(message.getContent());

                }
            }
        }

        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 85, 10));
        northPanel.add(listeConv);
        northPanel.add(disconnect);
        listeConv.setPreferredSize(new Dimension(300, 30));
        eastPanel.add(newGroup);
        eastPanel.add(userOnLine);
        southPanel.add(msg, BorderLayout.WEST);
        southPanel.add(msgText);
        southPanel.add(sendMsg);
        msgText.setPreferredSize(new Dimension(300, 50));

        mainWindows.getContentPane().add(centerPanel);
        mainWindows.setVisible(true);
        mainWindows.revalidate();
        mainWindows.repaint();
        t = new Thread(connection = new Connexion(ClientQuery.getSock(), User, convText, listeConv));
        t.start();
        t.setPriority(Thread.MAX_PRIORITY);

        sendMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Date aujourdhui = new Date(); //date de l'envoi du message
                String message = msgText.getText();
                if (!message.isEmpty()) {
                    convText.append(formater.format(aujourdhui) + " : " + message + "\n"); //format du message : date + contenu
                    msgText.setText(""); //RAZ du jtextfield à chaque envoi de message
                    try {
                        ClientQuery.SendMsg(user.getUserName(), (String) listeConv.getSelectedItem(), formater.format(aujourdhui) + " : " + message + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static ArrayList<String> getTabContact() {
        return tabContact;
    }

}
