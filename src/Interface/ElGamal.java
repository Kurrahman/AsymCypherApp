package Interface;

import Engine.ElGamalCypher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ElGamal extends Template implements ActionListener {
    private JTextField prime, g, xField, cipherAField, yField;
    private JButton genPrime,
            loadPrivateKey, savePrivateKey,
            loadPublicKey, savePublicKey,
            calcNValue;
    private ElGamalCypher elGamalCypher;

    public void displayElGamal(JFrame frame){
        elGamalCypher = new ElGamalCypher();
        displayBase(frame);

        JLabel keyLabel = new JLabel("Prime Number : ");
        keyLabel.setBounds(30, 430, 720, 20);
        frame.add(keyLabel);

        JLabel p = new JLabel("P");
        p.setBounds(30, 450, 10, 20);
        frame.add(p);
        prime = new JTextField();
        prime.setBounds(40, 450, 250, 20);
        frame.add(prime);

        JLabel q = new JLabel("G");
        q.setBounds(300, 450, 10, 20);
        frame.add(q);
        g = new JTextField();
        g.setBounds(310, 450, 250, 20);
        frame.add(g);

        genPrime = new JButton("Generate Num");
        genPrime.addActionListener(this);
        genPrime.setBounds(580, 450, 170, 20);
        frame.add(genPrime);

        JLabel x = new JLabel("X");
        x.setBounds(30, 480, 250, 20);
        frame.add(x);
        xField = new JTextField();
        xField.setBounds(30, 500, 240, 20);
        frame.add(xField);

        JLabel cipherA = new JLabel("Cipher A");
        cipherA.setBounds(280, 480, 250, 20);
        frame.add(cipherA);
        cipherAField = new JTextField();
        cipherAField.setBounds(280, 500, 240, 20);
        frame.add(cipherAField);

        JLabel y = new JLabel("Y Value");
        y.setBounds(540, 480, 250, 20);
        frame.add(y);
        yField = new JTextField();
        yField.setBounds(540, 500, 210, 20);
        frame.add(yField);

        loadPublicKey = new JButton("Load");
        loadPublicKey.addActionListener(this);
        loadPublicKey.setBounds(30, 530, 70, 20);
        frame.add(loadPublicKey);

        savePublicKey = new JButton("Save");
        savePublicKey.addActionListener(this);
        savePublicKey.setBounds(115, 530, 70, 20);
        frame.add(savePublicKey);

        loadPrivateKey = new JButton("Load");
        loadPrivateKey.addActionListener(this);
        loadPrivateKey.setBounds(280, 530, 70, 20);
        frame.add(loadPrivateKey);

        savePrivateKey = new JButton("Save");
        savePrivateKey.addActionListener(this);
        savePrivateKey.setBounds(365, 530, 70, 20);
        frame.add(savePrivateKey);

        calcNValue = new JButton("Calculate Y");
        calcNValue.addActionListener(this);
        calcNValue.setBounds(540, 530, 210, 20);
        frame.add(calcNValue);

        frame.getContentPane().setVisible(false);
        frame.getContentPane().setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (templateAction(e)){
            super.actionPerformed(e);
        } else {
            if (e.getSource() == genPrime){
                elGamalCypher.generateAttribute();
                prime.setText(String.valueOf(elGamalCypher.getAttribute('P')));
                g.setText(String.valueOf(elGamalCypher.getAttribute('G')));

                yField.setText(String.valueOf(elGamalCypher.getAttribute('Y')));

                xField.setText(String.valueOf(elGamalCypher.getAttribute('X')));
            }
            if (e.getSource() == encryptBtn){
                List<String> ciphers = elGamalCypher.encrypt(plain.getText());
                cipherAField.setText(ciphers.get(0));
                cypher.setText(ciphers.get(1));
            }
            if (e.getSource() == decryptBtn){
                plain.setText(elGamalCypher.decrypt(cipherAField.getText(),cypher.getText()));
            }
            if (e.getSource() == savePrivateKey){
                int retVal = fc.showSaveDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> contents = new ArrayList<>();
                    contents.add(elGamalCypher.getAttribute('X'));
                    contents.add(elGamalCypher.getAttribute('P'));
                    writeKey(fc.getSelectedFile().getAbsolutePath(), contents);
                }
            }
            if (e.getSource() == savePublicKey){
                int retVal = fc.showSaveDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> contents = new ArrayList<>();
                    contents.add(elGamalCypher.getAttribute('Y'));
                    contents.add(elGamalCypher.getAttribute('G'));
                    contents.add(elGamalCypher.getAttribute('P'));
                    writeKey(fc.getSelectedFile().getAbsolutePath(), contents);
                }
            }
            if (e.getSource() == loadPrivateKey){
                int retVal = fc.showOpenDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> key = readKey(fc.getSelectedFile());
                    if (key != null) {
                        elGamalCypher.setAttribute('X', key.get(0));
                        elGamalCypher.setAttribute('P', key.get(1));
                        xField.setText(String.valueOf(key.get(0)));
                        prime.setText(String.valueOf(key.get(1)));
                    }
                }
            }
            if (e.getSource() == loadPublicKey){
                int retVal = fc.showOpenDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> key = readKey(fc.getSelectedFile());
                    if (key != null) {
                        elGamalCypher.setAttribute('Y', key.get(0));
                        elGamalCypher.setAttribute('G', key.get(1));
                        elGamalCypher.setAttribute('P', key.get(2));
                        yField.setText(String.valueOf(key.get(0)));
                        g.setText(String.valueOf(key.get(1)));
                        prime.setText(String.valueOf(key.get(2)));
                    }
                }
            }
            if (e.getSource() == calcNValue){
                long
                        a = Long.parseLong(g.getText()),
                        b = Long.parseLong(xField.getText()),
                        c = Long.parseLong(prime.getText());

                long y = elGamalCypher.modularPower(a,b,c);
                elGamalCypher.setAttribute('Y', y);
                yField.setText(String.valueOf(y));
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        ElGamal rsa = new ElGamal();
        rsa.displayElGamal(frame);
    }
}
