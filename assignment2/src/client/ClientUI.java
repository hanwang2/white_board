package client;

import common.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientUI {
    public JFrame frame;
    @SuppressWarnings("rawtypes")
	public JList list;
    public JTextArea guestTextArea;

    public Connection connection;
    static Listener Listener;
    static Drawer painter;
    private String guestUsername;

    public static int x, y;

	ImageIcon line = new ImageIcon(ClientUI.class.getResource("/icon/line.png"));
	ImageIcon circle = new ImageIcon(ClientUI.class.getResource("/icon/circle.png"));
	ImageIcon rect = new ImageIcon(ClientUI.class.getResource("/icon/rectangle.png"));
	ImageIcon oval = new ImageIcon(ClientUI.class.getResource("/icon/oval.png"));
	ImageIcon pencil = new ImageIcon(ClientUI.class.getResource("/icon/pencil.png"));
	ImageIcon more = new ImageIcon(ClientUI.class.getResource("/icon/color.png"));
	ImageIcon[] icons = { line, circle, rect, oval, pencil };

    public ClientUI(Connection connection) {
        this.connection = connection;
        initialize();
    }

    public ClientUI(Connection connection, String username) {
        this.connection = connection;
        this.guestUsername = username;
        initialize();
    }

    private void initialize(){
        frame = new JFrame();
		frame.setTitle("WhiteBoard : " + this.guestUsername);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 1280, 800);
		Listener = new Listener(frame);
		JPanel tool = new JPanel();
		tool.setBounds(0, 0, 1000, 60);
		tool.setLayout(new FlowLayout(FlowLayout.LEFT));
		

        String[] tools = {"Line", "Circle", "Rect", "Oval", "Free"};
		for (int i = 0; i < tools.length; i++) {
			JButton button1 = new JButton("");
			button1.setActionCommand(tools[i]);
			button1.setPreferredSize(new Dimension(60, 60));
			@SuppressWarnings("static-access")
			Image temp = icons[i].getImage().getScaledInstance(21, 21, icons[i].getImage().SCALE_DEFAULT);
			icons[i] = new ImageIcon(temp);
			button1.setIcon(icons[i]);
			button1.addActionListener(Listener);
			tool.add(button1);
		}
		JButton textButton = new JButton("A");
		textButton.setFont(new Font(null, 0, 20));
		textButton.setPreferredSize(new Dimension(60, 60));
		textButton.addActionListener(Listener);
		if (textButton != null) {
			tool.add(textButton);
		}

        JButton btnMore = new JButton("");
		btnMore.setActionCommand("More");
		btnMore.setPreferredSize(new Dimension(60, 60));
		@SuppressWarnings("static-access")
		Image temp = more.getImage().getScaledInstance(22, 22, more.getImage().SCALE_DEFAULT);
		more = new ImageIcon(temp);
		btnMore.setIcon(more);
		
  		tool.add(btnMore);
        btnMore.addActionListener(Listener);
        Listener.setThickness(4);


		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(tool);
		painter = new Drawer();
		painter.setBorder(null);
		painter.setBounds(0, 60, 1150, 780);
		painter.setBackground(Color.WHITE);
		painter.setList(Listener.getRecord());
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
		painter.addMouseListener(Listener);
		painter.addMouseMotionListener(Listener);
		Listener.setG(painter.getGraphics());

        list = new JList<>();
        frame.getContentPane().add(list);
		
        JScrollPane ScrollList = new JScrollPane(list);
		ScrollList.setBounds(1150, 500, 100, 200);
		ScrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(ScrollList);

        try {
            connection.outputStream.writeUTF("begin 11");
            connection.outputStream.flush();
        } catch (Exception e1) {
        }
    }

		

}
