import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


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

    private File buffer = null;

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
                buffer = infFile.getAbsoluteFile();
                fileWriter("Name, Companian, Invited By");



            }else{
                buffer = infFile.getAbsoluteFile();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void fileWriter(String context){
        try{

            FileWriter fw = new FileWriter(buffer, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(context);
            bw.newLine();
            bw.flush();

            bw.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public formCreate() {

        txaAcompany.setFocusable(false);

        tableCreate();

        fileCreate();

        focusableListener();

        chbtAcompany.addActionListener(e ->{

            txaAcompany.setText("");

            if (!txaAcompany.isFocusable()){
                txaAcompany.setFocusable(true);
            }else{
                txaAcompany.setFocusable(false);
            }

        });

        saveBt.addActionListener(e -> {

            if (!chbtAcompany.isSelected()){
                txaAcompany.setText("Sem Acompanhante");
            }

            insertTable(firstName.getText() +" "+ lastName.getText(), txaAcompany.getText().replaceAll("\n", " - "), invitedBy.getSelectedItem().toString());

            fileWriter(firstName.getText() + " " + lastName.getText() +","+ txaAcompany.getText().replaceAll("\n", " - ") +","+ invitedBy.getSelectedItem().toString());

            clearInfo();



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
            infTable.getModel().setValueAt(txaAcompany.getText(), rowIndex, 1);
            infTable.getModel().setValueAt(invitedBy.getSelectedItem().toString(), rowIndex, 2);

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

    }

    private static void insertTable(String name, String acompanhantes, String invited){
        model.addRow(new Object[]{name, acompanhantes, invited});
    }

    private void indexFind(String name, int cbIndex){
        invitedBy.setSelectedIndex(cbIndex);
        if (!Objects.equals(name, invitedBy.getSelectedItem().toString())){
            cbIndex ++;
            indexFind(name, cbIndex);
        }else{
            invitedBy.setSelectedIndex(cbIndex);
        }

    }

    private void clearInfo(){
        firstName.setText("");
        lastName.setText("");
        txaAcompany.setText("");
        invitedBy.setSelectedIndex(0);
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

}