package Interface;

import Engine.PaillierCypher;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Paillier extends Template implements ActionListener {
    private JTextField prime1, prime2, lambdaField, muField, nField, gField;
    private JButton genPrime,
            loadPrivateKey, savePrivateKey, calcPrivateKey,
            loadPublicKey, savePublicKey, calcPublicKey,
            calcNValue;
    private PaillierCypher paillie;

    public void displayPaillier(JFrame frame){
        displayBase(frame);
        paillie = new PaillierCypher();

        JLabel keyLabel = new JLabel("Paillier Prime Number Pair : ");
        keyLabel.setBounds(30, 430, 720, 20);
        frame.add(keyLabel);

        JLabel p = new JLabel("P");
        p.setBounds(30, 450, 10, 20);
        frame.add(p);
        prime1 = new JTextField();
        prime1.setBounds(40, 450, 100, 20);
        frame.add(prime1);

        JLabel q = new JLabel("Q");
        q.setBounds(150, 450, 10, 20);
        frame.add(q);
        prime2 = new JTextField();
        prime2.setBounds(160, 450, 100, 20);
        frame.add(prime2);

        JLabel g = new JLabel("G");
        g.setBounds(270, 450, 10, 20);
        frame.add(g);
        gField = new JTextField();
        gField.setBounds(280, 450, 200, 20);
        frame.add(gField);

        genPrime = new JButton("Generate Numbers");
        genPrime.addActionListener(this);
        genPrime.setBounds(580, 450, 170, 20);
        frame.add(genPrime);

        JLabel lambda = new JLabel("Lambda");
        lambda.setBounds(30, 480, 250, 20);
        frame.add(lambda);
        lambdaField = new JTextField();
        lambdaField.setBounds(30, 500, 240, 20);
        frame.add(lambdaField);

        JLabel mu = new JLabel("Mu");
        mu.setBounds(280, 480, 250, 20);
        frame.add(mu);
        muField = new JTextField();
        muField.setBounds(280, 500, 240, 20);
        frame.add(muField);

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
                paillie.generateAttribute();
                prime1.setText(String.valueOf(paillie.p));
                prime2.setText(String.valueOf(paillie.q));

                gField.setText(String.valueOf(paillie.g));
                nField.setText(String.valueOf(paillie.n));

                lambdaField.setText(String.valueOf(paillie.lambda));

                muField.setText(String.valueOf(paillie.mu));
            }
            if (e.getSource() == encryptBtn){
                cypher.setText(paillie.encrypt(plain.getText()));
            }
            if (e.getSource() == decryptBtn){
                plain.setText(paillie.decrypt(cypher.getText()));
            }
            if (e.getSource() == savePrivateKey){
                int retVal = fc.showSaveDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> contents = new ArrayList<>();
                    contents.add(paillie.lambda);
                    contents.add(paillie.mu);
                    writeKey(fc.getSelectedFile().getAbsolutePath(), contents);
                }
            }
            if (e.getSource() == savePublicKey){
                int retVal = fc.showSaveDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> contents = new ArrayList<>();
                    contents.add(paillie.g);
                    contents.add(paillie.n);
                    writeKey(fc.getSelectedFile().getAbsolutePath(), contents);
                }
            }
            if (e.getSource() == loadPrivateKey){
                int retVal = fc.showOpenDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> key = readKey(fc.getSelectedFile());
                    if (key != null) {
                        paillie.lambda = key.get(0);
                        paillie.mu = key.get(1);
                        muField.setText(String.valueOf(paillie.mu));
                        lambdaField.setText(String.valueOf(paillie.lambda));
                    }
                }
            }
            if (e.getSource() == loadPublicKey){
                int retVal = fc.showOpenDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    List<Long> key = readKey(fc.getSelectedFile());
                    if (key != null) {
                        paillie.g = key.get(0);
                        paillie.n = key.get(1);
                        lambdaField.setText(String.valueOf(paillie.g));
                        nField.setText(String.valueOf(paillie.n));
                    }
                }
            }
            if (e.getSource() == calcNValue){
                paillie.n = Long.parseLong(prime1.getText()) * Long.parseLong(prime2.getText());
                nField.setText(String.valueOf(paillie.n));
            }
            if (e.getSource() == calcPrivateKey){
                paillie.n = Long.parseLong(prime1.getText()) * Long.parseLong(prime2.getText());
                muField.setText(String.valueOf(paillie.mu));
            }
            if (e.getSource() == calcPublicKey){
                lambdaField.setText(String.valueOf(paillie.lambda));
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        Paillier rsa = new Paillier();
        rsa.displayPaillier(frame);
    }
}
