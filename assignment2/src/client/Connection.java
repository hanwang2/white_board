package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Connection {
    public Socket socket;
    public DataInputStream dis = null;
    public DataOutputStream outputStream = null;

    String status;
    boolean kick = false;

    Connection(Socket socket) {
        resetStatus();
        try {
            this.socket = socket;
            outputStream = new DataOutputStream(this.socket.getOutputStream());
            dis = new DataInputStream(this.socket.getInputStream());
        } catch (Exception e) {
        }
    }

    String getCurrentStatus() {
        return status;
    }

    public void resetStatus() {
        status = "wait";
        return;
    }
    @SuppressWarnings("unchecked")
    public void launch() {
        try {
        	while (true) {
                String string = dis.readUTF();
                String order = string.split(" ", 2)[0];
                String[] request = string.split(" ", 2);
                if (order.equals("draw")) {
                    ClientUI.Listener.updata(request[1]);
                    ClientUI.painter.repaint();
                }
                if (order.equals("userlist") && LoginUI.gui != null) {
                    LoginUI.gui.list.setListData(request[1].split(" "));
                }
                if (order.equals("delete")) {
                    JOptionPane.showMessageDialog(LoginUI.gui.frame, request[1].split(" ", 2)[0] + " has been kicked out by manager");
                    LoginUI.gui.list.setListData(request[1].split(" ", 2)[1].split(" "));
                }
                if (order.equals("kick")) {
                    kick = true;
                    JOptionPane.showMessageDialog(LoginUI.gui.frame, "You have been kicked out by manager");
                }
                if (order.equals("feedback")) {
                    switch (request[1]) {
                        case "no":
                            status = "no";
                            break;
                        case "yes":
                            status = "yes";
                            break;
                        case "rejected":
                            status = "rejected";
                            break;
                    }
                }
                if (order.equals("clientout")) {
                    JOptionPane.showMessageDialog(LoginUI.gui.frame, "user " + request[1].split(" ", 2)[0] + " leaves!");
                    LoginUI.gui.list.setListData(request[1].split(" ", 2)[1].split(" "));
                }
                if (order.equals("new")) {
                    clean();
                }
        	}
        } catch (IOException e1) {
            try {
                if (!kick) {
                    JOptionPane.showMessageDialog(LoginUI.gui.frame, "Disconnected with server");
                }
            } catch (Exception e) {
            
            }
            System.exit(0);
        }
    }
    
    public void clean(){
        ClientUI.painter.removeAll();
        ClientUI.painter.updateUI();
        ClientUI.Listener.clearRecord();
    }
}
