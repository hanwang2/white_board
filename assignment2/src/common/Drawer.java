package common;


import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.BasicStroke;

public class Drawer extends JPanel{
    private static final long serialVersionUID = 1L;
    private ArrayList<String> recordList = new ArrayList<String>();
    int startX, startY, endX, endY, t, red, green, blue;

    public void setList(ArrayList<String> recordList) {
        this.recordList = recordList;
    }

    public void paint(Graphics gr) {
        super.paint(gr);
        draw((Graphics2D) gr, this.recordList);
    }

    public void draw(Graphics2D g, ArrayList<String> recordList) {
        try {
            String[] recordArray = recordList.toArray(new String[recordList.size()]);
            for (String line : recordArray) {
                String[] record = line.split(" ");
                
                Color color;
                if (record[1].equals("!")) {
                    continue;
                }
                String type = record[0];
                t = Integer.parseInt(record[1]);
                g.setStroke(new BasicStroke(t));
                red = Integer.parseInt(record[2]);
                green = Integer.parseInt(record[3]);
                blue = Integer.parseInt(record[4]);
                color = new Color(red, green, blue);
                g.setColor(color);
                startX = Integer.parseInt(record[5]);
                startY = Integer.parseInt(record[6]);
                if (type.equals("Text")) {
                    String text = record[7];
                    g.drawString(text, startX, startY);
                }
                if (type.equals("Circle")) {
                    endX = Integer.parseInt(record[7]);
                    endY = Integer.parseInt(record[8]);
                    int diameter = Math.min(Math.abs(startX - endX), Math.abs(startY - endY));
                    g.drawOval(Math.min(startX, endX), Math.min(startY, endY), diameter, diameter);
                }
                if (type.equals("Rect")) {
                    endX = Integer.parseInt(record[7]);
                    endY = Integer.parseInt(record[8]);
                    g.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
                }
                if (type.equals("Line")) {
                    endX = Integer.parseInt(record[7]);
                    endY = Integer.parseInt(record[8]);
                    g.drawLine(startX, startY, endX, endY);
                }
                if (type.equals("Oval")) {
                    endX = Integer.parseInt(record[7]);
                    endY = Integer.parseInt(record[8]);
                    g.drawOval(Math.min(startX, endX), Math.min(startY, endY), Math.abs(startX - endX), Math.abs(startY - endY));
                }
            }
        } catch (Exception e) {
        }

    }
}