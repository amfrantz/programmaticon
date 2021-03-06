package com.github.amfrantz.programmaticon;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Programmaticon {

    private static final boolean drawWallpaper = false;
    private static final boolean drawInitials = false;

    private static final int CANVAS_WIDTH = 227;
    private static final int CANVAS_HEIGHT = 227;

    private static final int LOGO_HEIGHT_MIN = 45;
    private static final int LOGO_HEIGHT_MAX = 60;

    private static final int BODY_WIDTH_MIN = 125;
    private static final int BODY_WIDTH_MAX = 160;
    private static final int BODY_HEIGHT_MIN = 200;
    private static final int BODY_HEIGHT_MAX = 250;

    private static final int HEAD_SIZE_MIN = 100;
    private static final int HEAD_SIZE_MAX = 115;

    private static Color transparentCoal = new Color(61, 69, 77, 70);
    private static Color stone = new Color(158, 164, 171);
    private static Color seattle = new Color(225, 227, 232);
    private static Color cotton = new Color(243, 244, 246);

    private static Color gold = new Color(242, 174, 0);
    private static Color teal = new Color(66, 172, 180);
    private static Color ruby = new Color(225, 91, 79);
    private static Color blue = new Color(66, 130, 226);

    private static final List<Color> availableColors = Arrays.asList(gold, teal, ruby, blue);

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

    public static void main(String[] args) throws IOException {
        BufferedImage composite = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) composite.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBackground(graphics, cotton);

        Color wallpaperColor = getRandomColor(null);
        int logoHeight = getRandomInRange(LOGO_HEIGHT_MIN, LOGO_HEIGHT_MAX);
        if (drawWallpaper) {
            drawWallpaper(graphics, logoHeight, wallpaperColor);
        } else {
            drawBackground(graphics, wallpaperColor);
        }

        drawPerson(graphics, wallpaperColor);

        ImageIO.write(composite, "png", new File("composite.png"));
    }

    private static void drawBackground(Graphics2D graphics, Color color) {
        graphics.setColor(color);
        graphics.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    private static void drawWallpaper(Graphics2D graphics, int height, Color color) {
        int width = ((Double) (height * 1.5)).intValue();
        int halfW = width / 2;
        int halfH = height / 2;
        int offsetX = -getRandomInRange(width / 16, halfW);
        int offsetY = -getRandomInRange(height / 16, halfH);
        int x = offsetX;
        int y = offsetY + halfH;

        int rowCount = 0;
        while (y <= CANVAS_HEIGHT + halfH) {
            if (rowCount % 2 == 0) { // normal row
                drawRow(graphics, color.brighter(), color, seattle, stone, x, y, width, height);

            } else { // inverted row
                x -= halfW;
                drawRow(graphics, color.brighter(), stone, seattle, color, x, y, width, height);
            }
            x = offsetX;
            y += height;
            rowCount++;
        }

        drawBackground(graphics, transparentCoal);
    }

    private static int drawRow(Graphics2D graphics, Color evenLight, Color evenDark, Color oddLight, Color oddDark, int x, int y, int width, int height) {
        int count = 0;
        while (x <= CANVAS_WIDTH) {
            if (count % 2 == 0) {
                drawLogo(graphics, evenLight, evenDark, x, y, width, height);
            } else {
                drawLogo(graphics, oddLight, oddDark, x, y, width, height);
            }
            x += width;
            count++;
        }
        return x;
    }

    private static void drawLogo(Graphics2D graphics, Color left, Color right, int x, int y, int width, int height) {
        int halfW = width / 2;
        int halfH = height / 2;
        graphics.setColor(left);
        Path2D.Double leftHalf = new Path2D.Double();
        leftHalf.moveTo(x, y);
        leftHalf.lineTo(x + halfW, y - halfH);
        leftHalf.lineTo(x + halfW, y);
        leftHalf.lineTo(x, y + halfH);
        leftHalf.closePath();
        graphics.fill(leftHalf);

        graphics.setColor(right);
        Path2D.Double rightHalf = new Path2D.Double();
        rightHalf.moveTo(x + halfW, y - halfH);
        rightHalf.lineTo(x + width, y);
        rightHalf.lineTo(x + width, y + halfH);
        rightHalf.lineTo(x + halfW, y);
        rightHalf.closePath();
        graphics.fill(rightHalf);
    }

    private static void drawPerson(Graphics2D graphics, Color exclusion) {
        int bodyW = getRandomInRange(BODY_WIDTH_MIN, BODY_WIDTH_MAX);
        int bodyH = getRandomInRange(BODY_HEIGHT_MIN, BODY_HEIGHT_MAX);
        int bodyX = CANVAS_WIDTH / 2 - bodyW / 2;
        int bodyY = CANVAS_HEIGHT - bodyH / 2;
        Color bodyColor = getRandomColor(exclusion);
        graphics.setColor(bodyColor);
        graphics.fillOval(bodyX, bodyY, bodyW, bodyH);

        int head = getRandomInRange(HEAD_SIZE_MIN, HEAD_SIZE_MAX);
        int headX = CANVAS_WIDTH / 2 - head / 2;
        int headY = CANVAS_HEIGHT - bodyH / 2 - (head / 4 * 3);

        int shadowW = ((Double)(bodyW * .45)).intValue();
        int shadowH = head / 4;
        int shadowX = (bodyW - shadowW) / 2 + bodyX + 5;
        int shadowY = headY + head - ((Double)(shadowH * .65)).intValue();
        graphics.setColor(bodyColor.darker());
        graphics.fillOval(shadowX, shadowY, shadowW, shadowH);

        graphics.setColor(cotton);
        graphics.fillOval(headX, headY, head, head);

        if (drawInitials) {
            String initials = getRandomInitials();
            graphics.setFont(new Font(Font.MONOSPACED, Font.BOLD, 70));
            FontMetrics fm = graphics.getFontMetrics();
            int initialsW = fm.stringWidth(initials);
            int initialsH = fm.getAscent();
            int initialsX = CANVAS_WIDTH / 2 - initialsW / 2;
            // position vertically between the "chin" and the bottom of the canvas.
            int chinY = headY + head;
            int initialsY = chinY + (CANVAS_HEIGHT - chinY) / 2 + initialsH / 2 - 5;
            graphics.drawString(initials, initialsX, initialsY);
        }
    }

    private static String getRandomInitials() {
        Random r = new Random();
        String a = String.valueOf((char) (r.nextInt(26) + 'a')).toUpperCase();
        String b = String.valueOf((char) (r.nextInt(26) + 'a')).toUpperCase();
        return a + b;
    }
}
