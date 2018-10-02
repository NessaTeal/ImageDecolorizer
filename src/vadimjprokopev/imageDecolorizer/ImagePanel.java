package vadimjprokopev.imageDecolorizer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.stream.Collectors;

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
        
        List<Color> colors = dictionary.parallelStream()
		        		.map(colorPoint -> new Color(colorPoint.red, colorPoint.green, colorPoint.blue))
		        		.collect(Collectors.toList());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                g.setColor(colors.get(indices[x][y]));
                g.drawLine(x, y, x, y);
            }
        }
    }
}
