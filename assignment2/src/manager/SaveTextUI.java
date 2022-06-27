package manager;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SaveTextUI {
    JFrame frmSave;
    private JTextField textField;
    private ManagerUI whiteBoard;

    public SaveTextUI() {
        initialize();
    }

    public SaveTextUI(ManagerUI whiteBoard) {
        this.whiteBoard = whiteBoard;
        initialize();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initialize() {
    	frmSave = new JFrame();
        frmSave.setTitle("Save");
        frmSave.setBounds(400, 400, 400, 200);
        frmSave.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmSave.getContentPane().setLayout(null);

        JLabel lblSaveAsfile = new JLabel("Save as:");
        lblSaveAsfile.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblSaveAsfile.setBounds(30, 54, 82, 27);
        lblSaveAsfile.setHorizontalAlignment(SwingConstants.CENTER);
        frmSave.getContentPane().add(lblSaveAsfile);

        JComboBox comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
        comboBox.setBounds(225, 54, 100, 26);
        comboBox.setModel(new DefaultComboBoxModel(new String[]{".jpg", "text file"}));
        frmSave.getContentPane().add(comboBox);

        JButton btnSave = new JButton("Save");
        btnSave.setFont(new Font("Tahoma", Font.PLAIN, 22));
        btnSave.setBounds(146, 104, 82, 29);
        btnSave.addActionListener(e -> {
            String name = textField.getText();
            String format = comboBox.getSelectedItem().toString();
            switch (format) {
                case "text file":
                    whiteBoard.saveText(name);
                    break;
                case ".jpg":
                    whiteBoard.saveImg(name + format);
                    break;
                default:
                    break;
            }
            frmSave.dispose();
        });
        frmSave.getContentPane().add(btnSave);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        textField.setBounds(125, 54, 100, 26);
        frmSave.getContentPane().add(textField);
        textField.setColumns(10);
        textField.setText("white_board");
        
        JLabel lblNewLabel = new JLabel("Save File");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
        lblNewLabel.setBounds(146, 11, 109, 32);
        frmSave.getContentPane().add(lblNewLabel);

    }
}
