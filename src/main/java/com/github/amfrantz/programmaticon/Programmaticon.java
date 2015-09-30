package com.github.amfrantz.programmaticon;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Programmaticon {

    private static final int WIDTH = 227;
    private static final int HEIGHT = 227;

    private static Color cotton = new Color(243, 244, 246);

    private static Color gold = new Color(242, 174, 0);
    private static Color teal = new Color(66, 172, 180);
    private static Color ruby = new Color(225, 91, 79);
    private static Color blue = new Color(66, 130, 226);

    private static final List<Color> availableColors = Arrays.asList(gold, teal, ruby, blue);

    public static void main(String[] args) throws IOException {
        BufferedImage composite = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = (Graphics2D) composite.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        // background
        Color backgroundColor = getRandomColor(null);
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        // body (make sure it isn't the same as the background
        Color bodyColor = getRandomColor(backgroundColor);
        graphics.setColor(bodyColor);
        int bodyWidth = getRandomInRange(125, 160);
        int bodyHeight = getRandomInRange(200, 250);
        int bodyX = WIDTH / 2 - bodyWidth / 2;
        int bodyY = HEIGHT - bodyHeight / 2;

        graphics.fillOval(bodyX, bodyY, bodyWidth, bodyHeight);

        // head
        graphics.setColor(cotton);
        int headSize = getRandomInRange(100, 115);
        int headX = WIDTH / 2 - headSize / 2;
        int headY = HEIGHT - bodyHeight / 2 - ((headSize / 4) * 3);
        graphics.fillOval(headX, headY, headSize, headSize);

        // initials
        graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 65));
        graphics.drawString(getUserInitials(), 74, 210);

        ImageIO.write(composite, "png", new File("composite.png"));
    }

    private static Color getRandomColor(Color exclusion) {
        Color selection;
        do {
            int index = new Random().nextInt(availableColors.size());
            selection = availableColors.get(index);
        } while (selection == exclusion);
        return selection;
    }

    private static int getRandomInRange(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    private static String getUserInitials() {
        Random r = new Random();
        String a = String.valueOf((char) (r.nextInt(26) + 'a')).toUpperCase();
        String b = String.valueOf((char) (r.nextInt(26) + 'a')).toUpperCase();
        return a + b;
    }
}
