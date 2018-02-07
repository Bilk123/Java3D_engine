package core.graphics;

import util.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

public class Texture {
    private int[] data;
    private int width, height;

    public Texture(String fileName) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(fileName));
            this.width = img.getWidth(null);
            this.height = img.getHeight(null);
            data = new int[width*height];
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    data[i+j*width] = img.getRGB(i,j);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPixel(float t, float s) {
        t = Util.clamp(0f, 1f, t);
        s = Util.clamp(0f, 1f, s);
        System.out.println(t*height);
        System.out.println(s*width);

        return data[(int) (t * height * width) + (int) (s * width)];
    }
}
