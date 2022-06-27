package client;
import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class LoginUI {
	static String address;
	static int port;
	static String username;

	public static Connection connection;
	public static ClientUI gui;
	public static Socket socket;
	
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
		try {
			socket = new Socket(address, port);
		} catch (IOException e) {
			System.out.println("Connection failed");
			System.exit(1);
		}
		connection = new Connection(socket);
		
		EventQueue.invokeLater(() -> {
			try {
				new LoginUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		connection.launch();

	}
	public LoginUI() {
		initialize();
	}
	private void initialize() {
		Listener Listener = new Listener();
		
		frame = new JFrame();
		frame.setBounds(500, 300, 400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(121, 151, 115, 37);
		
		JLabel usernameLabel = new JLabel("Input username:");
		usernameLabel.setBounds(50, 70, 100, 50);
		frame.getContentPane().add(usernameLabel);
		
		textField = new JTextField();
		textField.setBounds(160, 70, 200, 50);
		frame.getContentPane().add(textField);
		textField.setText(username);
		
		loginButton.addActionListener(Listener);
		loginButton.addActionListener(e -> {
			if (e.getActionCommand().equals("Login")) {
				try {
					username = textField.getText();
					connection.outputStream.writeUTF("request " + username);
					int time = 0;
					while (connection.getCurrentStatus().equals("wait") && time < 1000000) {
						TimeUnit.MILLISECONDS.sleep(100);
						time += 100;
					}
					String allow = connection.getCurrentStatus();
					if (allow.equals("no")) {
						JOptionPane.showMessageDialog(frame, "Username exists");
						connection.resetStatus();
					}else if (allow.equals("rejected")) {
						JOptionPane.showMessageDialog(frame, "Refused by manager");
						frame.dispose();
						try {
							connection.outputStream.writeUTF("Over");
							connection.outputStream.flush();
							socket.close();
							System.exit(1);
						} catch (Exception e1) {
						}
					}else if (allow.equals("yes")) {
						frame.dispose();
						try {
							if (gui == null) {
								gui = new ClientUI(connection, username);
							}
						} catch (Exception e1) {}
					} else {
						JOptionPane.showMessageDialog(frame, "Timeout");
						try {
							socket.close();
                            System.exit(1);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						frame.dispose();
					}
				}
				catch (Exception e2) {
				}
				
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(loginButton);
		frame.setVisible(true);
	}

}



