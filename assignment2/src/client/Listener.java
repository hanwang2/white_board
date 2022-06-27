package client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.awt.BasicStroke;


public class Listener implements ActionListener, MouseListener, MouseMotionListener{
    Graphics2D g;
    int startX, startY;
    int endX, endY;
    private int thickness = 4;
    Object type = "Line";
    JFrame frame;
    String message;
    ArrayList<String> messageList = new ArrayList<String>();
    

    static Color color = Color.BLACK;
    String rgb = "0 0 0";
    String draw;

    public Listener(){

    }
    
    public Listener(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("")){
            JButton button = (JButton) e.getSource();
            color = button.getBackground();
        } else if (e.getActionCommand().equals("More")) {
            final JFrame frame = new JFrame("Color");
            frame.setSize(300, 300);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            color = JColorChooser.showDialog(frame, "Choose color", null);
            
        } else {
            this.type = e.getActionCommand();
            if (type.equals("Free") || type.equals("Oval")) {
                Cursor cur = new Cursor(Cursor.DEFAULT_CURSOR);
                frame.setCursor(cur);
            }else if (type.equals("A") || type.equals("Circle") || type.equals("Rect") || type.equals("Line")){
                Cursor cur = new Cursor(Cursor.CROSSHAIR_CURSOR);
                frame.setCursor(cur);
            }
        }  
    } 

    private String getColor (Color color) {
        return color.getRed() + " " + color.getGreen() + " " + color.getBlue();
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        if (!g.getColor().equals(color)) {
            g.setColor(color);
        }
        if (type.equals("Free")) {
            rgb = getColor(color);
            g.setStroke(new BasicStroke(thickness));
            g.drawLine(startX, startY, startX, startY);
            message = "Line " + this.thickness + " " + rgb + " " + startX + " " + startY + " " + startX + " " + startY + " !";
            messageList.add(message);
        }
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        if (type.equals("Line")) {
            rgb = getColor(color);
            g.setStroke(new BasicStroke(thickness));
            g.drawLine(startX, startY, endX, endY);
            message =  formRecord("Line", rgb, startX, startY, endX, endY);
            messageList.add(message);
        } else if (type.equals("Circle")) {
            rgb = getColor(color);
            g.setStroke(new BasicStroke(thickness));
            int diameter = Math.min(Math.abs(startX - endX), Math.abs(startY - endY));
            g.drawOval(Math.min(startX, endX), Math.min(startY, endY), diameter, diameter);
            message =  formRecord("Circle", rgb, startX, startY, endX, endY);
            messageList.add(message);
        } else if (type.equals("Oval")) {
            rgb = getColor(color);
            g.setStroke(new BasicStroke(thickness));
            g.drawOval(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
            message =  formRecord("Oval", rgb, startX, startY, endX, endY);
            messageList.add(message);
        } else if (type.equals("Rect")) {
            rgb = getColor(color);
            g.setStroke(new BasicStroke(thickness));
            g.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
            message =  formRecord("Rect", rgb, startX, startY, endX, endY);
            messageList.add(message);
        } else if (type.equals("A")) {
            String text = JOptionPane.showInputDialog("Please Enter Input Text");
            if (text != null) {
                Font f = new Font(null, Font.PLAIN, this.thickness + 10);
                g.setFont(f);
                g.drawString(text, endX, endY);
                rgb = getColor(color);
                message = "Text " + this.thickness + " " + rgb + " " + endX + " " + endY + " " + text + " !";
                messageList.add(message);
            }
            
        } else {
            return;
        }
        sendDraw();
        
    }


    public void mouseDragged(MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        if (type.equals("Free")) {
            rgb = getColor(color);
            g.setStroke(new BasicStroke(thickness));
            g.drawLine(startX, startY, endX, endY);
            message =  formRecord("Line", rgb, startX, startY, endX, endY);
            messageList.add(message);
            startX = endX;
            startY = endY;
        }  else
        {
            return;
        }
        sendDraw();
    }

    private void sendDraw() {
        draw = "draw " + message;
        try {
            LoginUI.connection.outputStream.writeUTF(draw);
            LoginUI.connection.outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public String formRecord(String format, String rgb, int startX, int startY, int endX, int endY){
        String message = format + " " + this.thickness + " " + rgb + " " + startX + " " + startY + " " + endX + " " + endY + " !";
        return message;
    }

    public void setG(Graphics g) {
        this.g = (Graphics2D) g;
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        
    }

    public void setThickness(int thickness){
        this.thickness = thickness;
    }

    public int getThickness() {
        return this.thickness;
    }

    public ArrayList<String> getRecord() {
        return messageList;
    }

    public void clearRecord() {
        messageList.clear();
    }

    public void updata(String line) {
        messageList.add(line);
    }




}
