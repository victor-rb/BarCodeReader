import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;


/**
 * Created by baptisvi on 16/08/2016.
 */
public class formCreate {




    private static DefaultTableModel model = new DefaultTableModel(){
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };

    private int rowIndex;

    private File bufferFile = null;

    private JTextField firstName;
    private JTextField lastName;
    private JComboBox invitedBy;
    private JPanel mainPanel;
    private JTable infTable;
    private JButton saveBt;
    private JScrollPane scroll;
    private JButton changeBT;
    private JButton cancelBT;
    private JTextArea txaAcompany;
    private JCheckBox chbtAcompany;
    private JLabel lbInstruction;

    static void exeForm() {
        JFrame frame = new JFrame("formCreate");
        frame.setContentPane(new formCreate().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void fileCreate(){

        txaAcompany.setFocusable(false);

        try {

            DateFormat dateForm = new SimpleDateFormat("yy-MM-dd");
            Date date = new Date();

            File infFile = new File(dateForm.format(date) + " - access report.csv");

            if (!infFile.exists()) {

                infFile.createNewFile();
                bufferFile = infFile.getAbsoluteFile();
                fileWriter("Name, Companian, Invited By");



            }else{
                bufferFile = infFile.getAbsoluteFile();
                resumeTable();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void fileWriter(String context){
        try{

            FileWriter fw = new FileWriter(bufferFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(context);
            bw.newLine();
            bw.flush();

            bw.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private formCreate() {

        txaAcompany.setFocusable(false);

        tableCreate();

        fileCreate();

        focusableListener();

        chbtAcompany.addActionListener(e ->{

            if (chbtAcompany.isSelected()){
                txaAcompany.setFocusable(true);
                txaAcompany.setText("");
            }else{
                txaAcompany.setFocusable(false);
                txaAcompany.setText("Sem Acompanhante");
            }

        });

        saveBt.addActionListener(e -> {

            if (firstName.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Primeiro nome nescessario para Cadastro");
            }else {

                if (!chbtAcompany.isSelected()) {
                    txaAcompany.setText("Sem Acompanhante");
                }

                changeBT.setSelected(false);

                insertTable(firstName.getText() + " " + lastName.getText(), txaAcompany.getText().replaceAll("\n", " - "), invitedBy.getSelectedItem().toString());

                fileWriter(firstName.getText() + " " + lastName.getText() + "," + txaAcompany.getText().replaceAll("\n", " - ") + "," + invitedBy.getSelectedItem().toString());

                clearInfo();

            }


        });


        infTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                changeBT.setEnabled(true);
                saveBt.setEnabled(false);
                cancelBT.setEnabled(true);



                rowIndex = infTable.getSelectedRow();

                String[] buffName;
                buffName = (infTable.getModel().getValueAt(rowIndex, 0).toString().split(" "));
                firstName.setText(buffName[0]);
                lastName.setText(buffName[1]);

                indexFind(infTable.getModel().getValueAt(rowIndex, 2).toString(), 1);

                String buffIns = infTable.getModel().getValueAt(rowIndex, 1).toString();

                txaAcompany.setText(buffIns.replaceAll(" - ", "\n"));

                if (buffIns.equals("Sem Acompanhante")){
                    chbtAcompany.setSelected(false);
                    txaAcompany.setFocusable(false);
                }else{
                    chbtAcompany.setSelected(true);
                    txaAcompany.setFocusable(true);
                }
            }
        });




        changeBT.addActionListener(e-> {

            if (!chbtAcompany.isSelected()){
                txaAcompany.setText("Sem Acompanhante");
            }


            cancelBT.setEnabled(false);
            saveBt.setEnabled(true);
            changeBT.setEnabled(false);

            infTable.getModel().setValueAt(firstName.getText() +" "+ lastName.getText(), rowIndex, 0);
            infTable.getModel().setValueAt(txaAcompany.getText().replaceAll("\n", " - "), rowIndex, 1);
            infTable.getModel().setValueAt(invitedBy.getSelectedItem().toString(), rowIndex, 2);

            rewrite();

            clearInfo();

        });

        cancelBT.addActionListener(e -> {

            clearInfo();

            saveBt.setEnabled(true);
            changeBT.setEnabled(false);
            cancelBT.setEnabled(false);

        });

    }


    private void tableCreate() {
        infTable.setModel(model);
        model.addColumn("Nome");
        model.addColumn("Acompanhante");
        model.addColumn("Convidado Por:");
        infTable.setFont(new Font("Porsche News Gothic", Font.PLAIN, 12));

    }

    private static void insertTable(String name, String acompanhantes, String invited){
        model.addRow(new Object[]{name, acompanhantes, invited});
    }

    private void indexFind(String name, int cbIndex){
        invitedBy.setSelectedIndex(cbIndex);

        if (!(name == null)) {

            if (!Objects.equals(name, invitedBy.getSelectedItem().toString())) {
                cbIndex++;
                indexFind(name, cbIndex);
            } else {
                invitedBy.setSelectedIndex(cbIndex);
            }
        }

    }

    private void clearInfo(){
        firstName.setText("");
        lastName.setText("");
        txaAcompany.setText("");
        invitedBy.setSelectedIndex(0);
        chbtAcompany.setSelected(false);
        txaAcompany.setFocusable(false);
    }

    private void focusableListener(){

        firstName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                lbInstruction.setText("Digite o Primeiro Nome do Convidado");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        lastName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                lbInstruction.setText("Digite o Sbronome Nome do Convidado");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        invitedBy.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                lbInstruction.setText("Escolha o Convidante");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

        txaAcompany.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                lbInstruction.setText("Adicione aqui os acompanhantes do Convidado separados por Linha [ENTER]");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        });

    }

    private void rewrite(){

        try {

            bufferFile.delete();
            bufferFile.createNewFile();

            fileWriter("Name, Companian, Invited By");

            for (int rowIdx = 0; rowIdx < infTable.getRowCount(); rowIdx++)
                fileWriter(

                        infTable.getModel().getValueAt(rowIdx, 0).toString() + ","
                        + infTable.getModel().getValueAt(rowIdx,1).toString().replaceAll("\n", " - ") + ","
                        + infTable.getModel().getValueAt(rowIdx, 2).toString()

                );
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void resumeTable(){
        try{
            Scanner read = new Scanner(bufferFile);

            while (read.hasNext()){
                String[] buffRead = read.nextLine().split(",");

                if (buffRead[0].equals("Name")) continue;

                insertTable(buffRead[0],buffRead[1], buffRead[2]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}