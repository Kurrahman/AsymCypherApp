package Interface;

import Engine.RSACypher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RSA extends Template implements ActionListener {
    private JTextField prime1, prime2, publicKeyField, privateKeyField, nField;
    private JButton genPrime,
            loadPrivateKey, savePrivateKey, calcPrivateKey,
            loadPublicKey, savePublicKey, calcPublicKey,
            calcNValue;
    private RSACypher rsaCypher;

    public void displayRSA(JFrame frame){
        displayBase(frame);
        rsaCypher = new RSACypher();

        JLabel keyLabel = new JLabel("RSA Prime Number Pair : ");
        keyLabel.setBounds(30, 430, 720, 20);
        frame.add(keyLabel);

        JLabel p = new JLabel("P");
        p.setBounds(30, 450, 10, 20);
        frame.add(p);
        prime1 = new JTextField();
        prime1.setBounds(40, 450, 250, 20);
        frame.add(prime1);

        JLabel q = new JLabel("Q");
        q.setBounds(300, 450, 10, 20);
        frame.add(q);
        prime2 = new JTextField();
        prime2.setBounds(310, 450, 250, 20);
        frame.add(prime2);

        genPrime = new JButton("Generate Primes");
        genPrime.addActionListener(this);
        genPrime.setBounds(580, 450, 170, 20);
        frame.add(genPrime);

        JLabel publicKey = new JLabel("Public Key");
        publicKey.setBounds(30, 480, 250, 20);
        frame.add(publicKey);
        publicKeyField = new JTextField();
        publicKeyField.setBounds(30, 500, 240, 20);
        frame.add(publicKeyField);

        JLabel privateKey = new JLabel("Private Key");
        privateKey.setBounds(280, 480, 250, 20);
        frame.add(privateKey);
        privateKeyField = new JTextField();
        privateKeyField.setBounds(280, 500, 240, 20);
        frame.add(privateKeyField);

        JLabel n = new JLabel("N Value");
        n.setBounds(540, 480, 250, 20);
        frame.add(n);
        nField = new JTextField();
        nField.setBounds(540, 500, 210, 20);
        frame.add(nField);

        loadPublicKey = new JButton("Load");
        loadPublicKey.addActionListener(this);
        loadPublicKey.setBounds(30, 530, 70, 20);
        frame.add(loadPublicKey);

        savePublicKey = new JButton("Save");
        savePublicKey.addActionListener(this);
        savePublicKey.setBounds(115, 530, 70, 20);
        frame.add(savePublicKey);

        calcPublicKey = new JButton("Calc");
        calcPublicKey.addActionListener(this);
        calcPublicKey.setBounds(200, 530, 70, 20);
        frame.add(calcPublicKey);

        loadPrivateKey = new JButton("Load");
        loadPrivateKey.addActionListener(this);
        loadPrivateKey.setBounds(280, 530, 70, 20);
        frame.add(loadPrivateKey);

        savePrivateKey = new JButton("Save");
        savePrivateKey.addActionListener(this);
        savePrivateKey.setBounds(365, 530, 70, 20);
        frame.add(savePrivateKey);

        calcPrivateKey = new JButton("Calc");
        calcPrivateKey.addActionListener(this);
        calcPrivateKey.setBounds(450, 530, 70, 20);
        frame.add(calcPrivateKey);

        calcNValue = new JButton("Calculate N");
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
                long[] tmp = rsaCypher.generatePair();
                prime1.setText(String.valueOf(tmp[0]));
                prime2.setText(String.valueOf(tmp[1]));

                rsaCypher.N = tmp[0] * tmp[1];
                nField.setText(String.valueOf(rsaCypher.N));

                publicKeyField.setText(String.valueOf(rsaCypher.PUBLIC_KEY));

                rsaCypher.PRIVATE_KEY = rsaCypher.calculateD(rsaCypher.cotient(tmp[0], tmp[1]));
                privateKeyField.setText(String.valueOf(rsaCypher.PRIVATE_KEY));
            }
            if (e.getSource() == encryptBtn){
                cypher.setText(rsaCypher.encrypt(plain.getText()));
            }
            if (e.getSource() == decryptBtn){
                plain.setText(rsaCypher.decrypt(cypher.getText()));
            }
            if (e.getSource() == savePrivateKey){
                int retVal = fc.showSaveDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> contents = new ArrayList<>();
                    contents.add(rsaCypher.PRIVATE_KEY);
                    contents.add(rsaCypher.N);
                    writeKey(fc.getSelectedFile().getAbsolutePath(), contents);
                }
            }
            if (e.getSource() == savePublicKey){
                int retVal = fc.showSaveDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> contents = new ArrayList<>();
                    contents.add(rsaCypher.PUBLIC_KEY);
                    contents.add(rsaCypher.N);
                    writeKey(fc.getSelectedFile().getAbsolutePath(), contents);
                }
            }
            if (e.getSource() == loadPrivateKey){
                int retVal = fc.showOpenDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> key = readKey(fc.getSelectedFile());
                    if (key != null) {
                        rsaCypher.PRIVATE_KEY = key.get(0);
                        rsaCypher.N = key.get(1);
                        privateKeyField.setText(String.valueOf(rsaCypher.PRIVATE_KEY));
                        nField.setText(String.valueOf(rsaCypher.N));
                    }
                }
            }
            if (e.getSource() == loadPublicKey){
                int retVal = fc.showOpenDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> key = readKey(fc.getSelectedFile());
                    if (key != null) {
                        rsaCypher.PUBLIC_KEY = key.get(0);
                        rsaCypher.N = key.get(1);
                        publicKeyField.setText(String.valueOf(rsaCypher.PUBLIC_KEY));
                        nField.setText(String.valueOf(rsaCypher.N));
                    }
                }
            }
            if (e.getSource() == calcNValue){
                rsaCypher.N = Long.parseLong(prime1.getText()) * Long.parseLong(prime2.getText());
                nField.setText(String.valueOf(rsaCypher.N));
            }
            if (e.getSource() == calcPrivateKey){
                rsaCypher.N = Long.parseLong(prime1.getText()) * Long.parseLong(prime2.getText());
                rsaCypher.PRIVATE_KEY = rsaCypher.calculateD(rsaCypher.cotient(Long.parseLong(prime1.getText()),Long.parseLong(prime2.getText())));
                privateKeyField.setText(String.valueOf(rsaCypher.PRIVATE_KEY));
            }
            if (e.getSource() == calcPublicKey){
                publicKeyField.setText(String.valueOf(rsaCypher.PUBLIC_KEY));
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        RSA rsa = new RSA();
        rsa.displayRSA(frame);
    }
}
