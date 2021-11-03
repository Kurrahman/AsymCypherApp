import Interface.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface implements ActionListener {

    JMenuItem rsa, elgamal, paillier, ecc;
    JMenu cypherMenu;
    JMenuBar menuBar;
    JFrame frame;

    private void initFrame() {
        frame = new JFrame("AsymCypherApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.setSize(800, 640);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        menuBar.add(cypherMenu);
    }

    private void initCypherMenu() {
        cypherMenu = new JMenu("Cypher");

        rsa = new JMenuItem("RSA");
        elgamal = new JMenuItem("ElGamal");
        paillier = new JMenuItem("Paillier");
        ecc = new JMenuItem("ECC");

        rsa.addActionListener(this);
        elgamal.addActionListener(this);
        paillier.addActionListener(this);
        ecc.addActionListener(this);

        cypherMenu.add(rsa);
        cypherMenu.add(elgamal);
        cypherMenu.add(paillier);
        cypherMenu.add(ecc);
    }

    public void initInterface() {
        initCypherMenu();
        initMenuBar();
        initFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rsa) {
            RSA display = new RSA();
            display.displayRSA(frame);
        }
        if (e.getSource() == elgamal) {
            ElGamal display = new ElGamal();
            display.displayElGamal(frame);
        }
        if (e.getSource() == paillier) {
            Paillier display = new Paillier();
            display.displayPaillier(frame);
        }
//        if (e.getSource() == ecc) {
//            ExtendedVigenere display = new ExtendedVigenere();
//            display.displayExtendedVigenere(frame);
//        }
    }

    public static void main(String[] args) {
        Interface i = new Interface();
        i.initInterface();
    }
}
