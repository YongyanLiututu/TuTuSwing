package com.one.util;

import java.awt.*;
import java.awt.i.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class CheckCodeUtils {


    public static BufferedImage createCheckImg() throws IOException {
        int width = 300;
        int height = 200;

     
        BufferedImage i = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        Graphics g = i.getGraphics();
        g.setColor(Color.PINK);
        g.fillRect(0,0,width,height);


        g.setColor(Color.YELLOW);
        g.drawRect(0,0,width - 1,height - 1);

        String str = "ABCDEF";
        Random ran = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 4; i++) {
            int index = ran.nextInt(str.length());
            char ch = str.charAt(index);
            sb.append(ch);

            g.drawString(ch+"",width/5*i,height/2);
        }
        String checkCode_session = sb.toString();
        g.setColor(Color.GREEN);

        for (int i = 0; i < 5; i++) {
            int x1 = ran.nextInt(width);
            int x2 = ran.nextInt(width);

            int y1 = ran.nextInt(height);
            int y2 = ran.nextInt(height);
            g.drawLine(x1,y1,x2,y2);
        }
        return i;
    }
}
