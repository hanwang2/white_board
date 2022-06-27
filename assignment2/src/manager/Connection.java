package manager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Connection extends Thread {
    public Socket socket;
    public String name;
    public DataInputStream inputStream;
    public DataOutputStream outputStream;
    public boolean kick = false;

    public static List<Connection> connections = new ArrayList<>();
    public static List<String> usernames = new ArrayList<>();
    
    public Connection(Socket socket) {
        this.socket = socket;
    }

    @SuppressWarnings("unchecked")
    public void run(){
        try {
            InputStream input_stream = socket.getInputStream();
            OutputStream outputput_stream = socket.getOutputStream();
            inputStream = new DataInputStream(input_stream);
            outputStream = new DataOutputStream(outputput_stream);
            String string;
            while (((string = inputStream.readUTF()) != null)) {
                String[] out = string.split(" ", 2);
                String order = out[0];
                String compare = out[1];
                if (order.equals("begin")) {
                    @SuppressWarnings("static-access")
                    ArrayList<String> record = LoginUI.createWhiteBoard.listener.getRecord();
                    try {
                        broadcastAll(record);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    String str = "userlist";
                    for (String userName : usernames) {
                        str += " " + userName;
                    }
                    LoginUI.createWhiteBoard.list.setListData(str.split(" ", 2)[1].split(" "));
                    broadcast(str);
                } else if (order.equals("request")) {
                    String currentName = compare;
                    name = currentName;
                    if (usernames.contains(currentName)) {
                        write("no");
                    } else {
                        int ans = LoginUI.createWhiteBoard.showRequest(compare);
                        if (ans == JOptionPane.YES_OPTION) {
                            if (usernames.contains(currentName)) {
                                try {
                                    write("no");
                                    connections.remove(this);
                                    socket.close();
                                    break;
                                } catch (Exception e) {
                                    connections.remove(this);
                                }
                            } else {
                                usernames.add(currentName);
                                write("yes");
                            }
                        } else if (ans == JOptionPane.CANCEL_OPTION || ans == JOptionPane.CLOSED_OPTION) {
                            write("rejected");
                            connections.remove(this);
                        }
                    }
                } else if (order.equals("draw")) {
                    broadcast(string);
                    ManagerUI.listener.updata(compare);
                    ManagerUI.painter.repaint();
                } else if (order.equals("over")) {
                    socket.close();
                    break;                    
                }
                if (order.equals("new")) {
                    clean();
                }
                
            }
        } catch (SocketException e) {
            System.out.println("User " + this.name + " Connection interruption.");
            if (!this.kick) {
                kickClient();
            }
        } catch (Exception w){
            System.out.println("User " + this.name + " Connection interruption.");
        }
    }

    public void clean() {
        ManagerUI.painter.removeAll();
        ManagerUI.painter.updateUI();
        ManagerUI.listener.clearRecord();
    }

    public void write(String string){
        try {
            outputStream.writeUTF("feedback " + string);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }
    
    @SuppressWarnings("unchecked")
    public void kickClient(){
        connections.remove(this);
        usernames.remove(name);
        String str = "kickClient " + name;
        for (String userName : usernames) {
            str += " " + userName;
        }
        for (int i = 0; i < connections.size(); i++) {
            Connection st = connections.get(i);
            try {
                st.outputStream.writeUTF(str);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String client = str.split(" ", 2)[1];
        JOptionPane.showMessageDialog(LoginUI.createWhiteBoard.frame, "user " + client.split(" ", 2)[0] + " leaves!");
        LoginUI.createWhiteBoard.list.setListData(client.split(" ", 2)[1].split(" "));
    }

    public static void broadcast(String message) throws IOException{
        for (int i = 0; i < connections.size(); i++) {
            Connection st = connections.get(i);
            st.outputStream.writeUTF(message);
            st.outputStream.flush();
        }
    }
    
    public static void broadcastAll(ArrayList<String> recordList) throws IOException{
        String[] recordArray = recordList.toArray(new String[recordList.size()]);
        for (String message : recordArray){
            for (int i = 0; i < connections.size(); i++) {
                Connection st = connections.get(i);
                st.outputStream.writeUTF("draw " + message);
                st.outputStream.flush();
            }
        }
    }
    
    protected static void launch(int port, String username) {
        Connection t1 = null;
        ServerSocket server = null;
        usernames.add(username);
        try {
            server = new ServerSocket(port);
            Socket client;
            while (true) {
                client = server.accept();
                t1 = new Connection(client);
                connections.add(t1);
                t1.start();

            }
        } catch (Exception e) {
            System.out.println("Connection failed");
            System.exit(1);
        }
    }

}
