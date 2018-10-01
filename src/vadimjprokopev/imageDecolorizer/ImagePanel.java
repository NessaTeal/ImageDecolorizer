package vadimjprokopev.imageDecolorizer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
    private int width;
    private int height;
    private int indices[][];
    private List<ColorPoint> dictionary;

    public ImagePanel(int width, int height, int indexes[][], List<ColorPoint> dictionary) {
        this.width = width;
        this.height = height;
        this.indices = indexes;
        this.dictionary = dictionary;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
            	int index = indices[x][y];
            	ColorPoint pixelColor = dictionary.get(index);
            	int red = pixelColor.red;
            	int green = pixelColor.green;
            	int blue = pixelColor.blue;
            	Color color = new Color(red, green, blue);
                g.setColor(color);
                g.drawLine(x, y, x, y);
            }
        }
    }
}
