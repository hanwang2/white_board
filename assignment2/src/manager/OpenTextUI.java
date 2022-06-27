package manager;

import java.awt.Font;
import java.awt.SystemColor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class OpenTextUI {
    JFrame frmOpen;
    private JTextField txtFile;
    private JTextField txtWrong;
    private ManagerUI whiteBoard;
    public OpenTextUI() {
        initialize();
    }
    public OpenTextUI(ManagerUI whiteBoard) {
        this.whiteBoard = whiteBoard;
        initialize();
    }
    @SuppressWarnings("resource")
    private void initialize(){
        frmOpen = new JFrame();
        frmOpen.setTitle("Open File");
        frmOpen.setBounds(400, 400, 450, 200);
        frmOpen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmOpen.getContentPane().setLayout(null);

        JButton btnNewButton = new JButton("Open");
        btnNewButton.setBounds(290, 60, 85, 35);
        frmOpen.getContentPane().add(btnNewButton);

        txtFile = new JTextField();
        txtFile.setText("White_Board");
        txtFile.setBounds(148, 60, 109, 35);
        frmOpen.getContentPane().add(txtFile);
        txtFile.setColumns(10);

        JLabel lblOpenfile = new JLabel("Input file name:");
        lblOpenfile.setBounds(36, 70, 102, 15);
        frmOpen.getContentPane().add(lblOpenfile);

        txtWrong = new JTextField();
        txtWrong.setForeground(SystemColor.inactiveCaptionText);
        txtWrong.setHorizontalAlignment(SwingConstants.CENTER);
        txtWrong.setEditable(false);
        txtWrong.setBackground(SystemColor.control);
        txtWrong.setText("File not exist");
        txtWrong.setBounds(100, 220, 240, 25);
        txtWrong.setBorder(null);
        frmOpen.getContentPane().add(txtWrong);
        txtWrong.setColumns(10);
        
        JLabel lblNewLabel = new JLabel("Load File");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
        lblNewLabel.setBounds(126, 11, 109, 38);
        frmOpen.getContentPane().add(lblNewLabel);
        txtWrong.setVisible(false);

        btnNewButton.addActionListener(e -> {
            String name = txtFile.getText();
            String file = name;
            try {
                new Scanner(new FileInputStream(file));
            } catch (FileNotFoundException e1) {
                txtWrong.setVisible(true);
                JOptionPane.showMessageDialog(frmOpen, "File not exist");
                
            }
            whiteBoard.openFile(file);
            frmOpen.dispose();
            System.out.println("Opened");
        });
    }

}

