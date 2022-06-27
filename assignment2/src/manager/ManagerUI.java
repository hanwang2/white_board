package manager;

import common.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ManagerUI {
	static Listener listener;
	public JFrame frame;
	static ManagerUI createWhiteBoard;
	static Drawer painter;
	@SuppressWarnings("rawtypes")
	public JList list;
	public static int x, y;

	ImageIcon line = new ImageIcon(ManagerUI.class.getResource("/icon/line.png"));
	ImageIcon circle = new ImageIcon(ManagerUI.class.getResource("/icon/circle.png"));
	ImageIcon rect = new ImageIcon(ManagerUI.class.getResource("/icon/rectangle.png"));
	ImageIcon oval = new ImageIcon(ManagerUI.class.getResource("/icon/oval.png"));
	ImageIcon pencil = new ImageIcon(ManagerUI.class.getResource("/icon/pencil.png"));
	ImageIcon color = new ImageIcon(ManagerUI.class.getResource("/icon/color.png"));
	ImageIcon[] icons = { line, circle, rect, oval, pencil };
	
	public ManagerUI(String mname) {
		initialize(mname);
	}
	
	public ManagerUI() {	
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void initialize(String name) {
		frame = new JFrame();
		frame.setTitle("WhiteBoard : " + name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 1280, 800);
		listener = new Listener(frame);
		JPanel tool = new JPanel();
		tool.setBounds(0, 0, 1000, 60);
		tool.setLayout(new FlowLayout(FlowLayout.LEFT));
		JComboBox menuBox = new JComboBox();
		menuBox.setModel(new DefaultComboBoxModel(new String[] { "New", "Save", "Save as", "Open", "Exit"}));
		menuBox.addActionListener(e -> {
			if (menuBox.getSelectedItem().toString().equals("New")) {
				painter.removeAll();
				painter.updateUI();
				listener.clearRecord();
				try {
					Connection.broadcast("new");
				}catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("New WhiteBoard");
			}else if (menuBox.getSelectedItem().toString().equals("Save")) {
				saveText("White_Board");
			}else if (menuBox.getSelectedItem().toString().equals("Save as")) {
				SaveTextUI saveAs = new SaveTextUI(createWhiteBoard);
				saveAs.frmSave.setVisible(true);
			}else if (menuBox.getSelectedItem().toString().equals("Open")) {
				OpenTextUI open = new OpenTextUI(createWhiteBoard);
				open.frmOpen.setVisible(true);
			}else if (menuBox.getSelectedItem().toString().equals("Exit")) {
				System.exit(1);
			}
		});
		tool.add(menuBox);
		String[] tools = {"Line", "Circle", "Rect", "Oval", "Free"};
		for (int i = 0; i < tools.length; i++) {
			JButton button1 = new JButton("");
			button1.setActionCommand(tools[i]);
			button1.setPreferredSize(new Dimension(60, 60));
			@SuppressWarnings("static-access")
			Image temp = icons[i].getImage().getScaledInstance(21, 21, icons[i].getImage().SCALE_DEFAULT);
			icons[i] = new ImageIcon(temp);
			button1.setIcon(icons[i]);
			button1.addActionListener(listener);
			tool.add(button1);
		}
		JButton textButton = new JButton("A");
		textButton.setFont(new Font(null, 0, 20));
		textButton.setPreferredSize(new Dimension(60, 60));
		textButton.addActionListener(listener);
		if (textButton != null) {
			tool.add(textButton);
		}
		listener.setThickness(4);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(tool);
		painter = new Drawer();
		painter.setBorder(null);
		painter.setBounds(0, 60, 1150, 780);
		painter.setBackground(Color.WHITE);
		painter.setList(listener.getRecord());
		frame.getContentPane().add(painter);
		painter.setLayout(null);
		frame.addComponentListener(new ComponentAdapter() {
		    public void componentMoved(ComponentEvent e) {
		    	Component comp = e.getComponent(); 
		    	x = comp.getX() + 116;
		    	y = comp.getY() + 80;
		    }
		});
		frame.setVisible(true);
		frame.setResizable(false);
		painter.addMouseListener(listener);
		painter.addMouseMotionListener(listener);
		listener.setG(painter.getGraphics());
		JButton btncolor = new JButton("");
		btncolor.setActionCommand("More");
		btncolor.setPreferredSize(new Dimension(60, 60));
		@SuppressWarnings("static-access")
		Image temp = color.getImage().getScaledInstance(22, 22, color.getImage().SCALE_DEFAULT);
		color = new ImageIcon(temp);
		btncolor.setIcon(color);
		
  		tool.add(btncolor);
  		btncolor.addActionListener(listener);
  		list = new JList<Object>();
  		frame.getContentPane().add(list);
  		String[] listName = { name };
		list.setListData(listName);
		
  		JScrollPane ScrollList = new JScrollPane(list);
		ScrollList.setBounds(1150, 500, 100, 200);
		ScrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(ScrollList);
		JButton btnKick = new JButton("Kick out");
		btnKick.addActionListener(e -> {
			String user = list.getSelectedValue().toString();
			if (name.equals(user)) {
				return;
			}
			for (int i = 0; i < Connection.connections.size(); i++) {
				Connection connect = Connection.connections.get(i);
				if (user.equals(connect.name)) {
					connect.kick = true;
					try {
						connect.outputStream.writeUTF("kick" + connect.name);
						connect.outputStream.flush();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					try {
						connect.socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					Connection.connections.remove(i);
					Connection.usernames.remove(user);
					JOptionPane.showMessageDialog(frame, user + " is kicked out");
				}
			}
			for (String userName : Connection.usernames){
				user += " " + userName;
			}
			for (int i = 0; i < Connection.connections.size(); i++){
				Connection connect = Connection.connections.get(i);
				try {
					connect.outputStream.writeUTF("delete " + user);
					connect.outputStream.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			String[] k1 = user.split(" ", 2);
			String[] kkk = k1[1].split(" ");
			list.setListData(kkk);
		});
		btnKick.setBounds(1150, 720, 100, 30);
		frame.getContentPane().add(btnKick);	
	}

	@SuppressWarnings("static-access")
	void setFrame(ManagerUI createWhiteBoard) {
		this.createWhiteBoard = createWhiteBoard;
	}

	public void saveText(String file) {
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileOutputStream(file));
		} catch (IOException e1) {
			System.out.println("cannot save " + file);
			return;
		}
		for (String record : listener.getRecord()) {
			outputStream.println(record);
		}
		outputStream.flush();
		outputStream.close();
		System.out.println("Saved");
	}

	public void saveImg(String file) {
		BufferedImage targetImg = new BufferedImage(painter.getWidth(), painter.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = targetImg.createGraphics();
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(0, 0, painter.getWidth(), painter.getHeight());
		painter.draw(graphics2d, listener.getRecord());

		try {
			ImageIO.write(targetImg, "JPEG", new File(file));
		} catch (IOException e1) {
			System.out.println("Wrong file");
		}
		System.out.println("Saved");
	}
	
	public void openFile(String file){
		Scanner inputStream = null;
		try {
			inputStream = new Scanner(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			System.out.println("Can't open");
			return;
		}
		listener.clearRecord();
		while (inputStream.hasNextLine()) {
			String line = inputStream.nextLine();
			listener.updata(line);
		}
		try {
			Connection.broadcast("new");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			Connection.broadcastAll(listener.getRecord());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		painter.repaint();
		inputStream.close();
	}

	public int showRequest(String name) {
		int option = JOptionPane.showConfirmDialog(null, name + " wants to share whiteboard", "Confirm", JOptionPane.OK_CANCEL_OPTION);
		return option;
	}
	


	
	
}
