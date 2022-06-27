package manager;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginUI {
	static String address;
	static int port;
	static String username;
	public static ManagerUI createWhiteBoard;
	
	private JFrame frame;
	private JTextField textField;
	public static void main(String[] args) {
		address = "localhost";
		port = 8080;
		username = "Manager";
		if (args.length >= 3) {
			try {
				address = args[0];
				port = Integer.parseInt(args[1]);
				username = args[2];
			} catch (Exception e) {
				System.out.println("Type error!");
				System.exit(1);
			}	
		}else {
			System.out.println("Launch default!");
		}
		EventQueue.invokeLater(() -> {
			try {
				new LoginUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Connection.launch(port, username);
	}
	public LoginUI() {
		initialize();
	}
	private void initialize() {
		Listener createDrawListener = new Listener();
		
		frame = new JFrame();
		frame.setBounds(500, 300, 400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(121, 151, 115, 37);
		frame.getContentPane().setLayout(null);
		
		JLabel usernameLabel = new JLabel("Input username:");
		usernameLabel.setBounds(50, 70, 100, 50);
		frame.getContentPane().add(usernameLabel);
		
		textField = new JTextField();
		textField.setBounds(160, 70, 200, 50);
		frame.getContentPane().add(textField);
		textField.setText(username);
		textField.setEditable(false);

		loginButton.addActionListener(createDrawListener);
		loginButton.addActionListener(e -> {
			if (e.getActionCommand().equals("Login")) {
				frame.dispose();
				try {
					createWhiteBoard = new ManagerUI(username);
					createWhiteBoard.setFrame(createWhiteBoard);
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});
		frame.getContentPane().add(loginButton);
		JLabel lblNewLabel = new JLabel("     White Board");
		lblNewLabel.setBounds(93, 11, 200, 37);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		frame.getContentPane().add(lblNewLabel);
		frame.setVisible(true);
	}

}
