package vadimjprokopev.imageDecolorizer;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class ImageProcessor {
	
	private static final int ITERATION_LIMIT = 10;
	
	private BufferedImage image;
	private int depth;
	
	public ImageProcessor(BufferedImage image, int depth) {
		this.image = image;
		this.depth = depth;
	}
	
    public void process() {
        int width = image.getWidth();;
        int height = image.getHeight();
        int indices[][] = new int[width][height];
        List<ColorPoint> kernels = new ArrayList<>(depth);

        ColorPoint imagePixels[][] = new ColorPoint[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                imagePixels[x][y] = new ColorPoint(image.getRGB(x, y));
            }
        }
        
        for (int i = 0; i < depth; i++) {
            ColorPoint randomColor;
            do {
            	randomColor = new ColorPoint((int)(Math.random() * Math.pow(2, 24)));
            } while (kernels.contains(randomColor));
            kernels.add(randomColor);
        }

        for (int iterationIndex = 0; iterationIndex < ITERATION_LIMIT; iterationIndex++) {
            int amount[] = new int[depth];
            int redSum[] = new int[depth];
            int greenSum[] = new int[depth];
            int blueSum[] = new int[depth];
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    double minDist = Double.MAX_VALUE;
                    int index = -1;
                    for (int kernelIndex = 0; kernelIndex < depth; kernelIndex++) {
                        double currentDist = imagePixels[x][y].vectorDistance(kernels.get(kernelIndex));
                        if (currentDist < minDist) {
                            minDist = currentDist;
                            index = kernelIndex;
                        }
                    }
                    
                    indices[x][y] = index;
                    amount[index]++;
                    redSum[index] += imagePixels[x][y].red;
                    greenSum[index] += imagePixels[x][y].green;
                    blueSum[index] += imagePixels[x][y].blue;
                }
            }
            for (int j = 0; j < depth; j++) {
                if (amount[j] == 0) {
                    continue;
                }
                kernels.get(j).red = redSum[j] / amount[j];
                kernels.get(j).green = greenSum[j] / amount[j];
                kernels.get(j).blue = blueSum[j] / amount[j];
            }
        }

        saveFile(width, height, indices, kernels, depth);

        JFrame newWindow = new JFrame();
        newWindow.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        newWindow.add(new ImagePanel(width, height, indices, kernels));
        newWindow.setSize(width, height);
        newWindow.setVisible(true);

    }

    private void saveFile(int width, int height, int indexes[][], List<ColorPoint> dictionary, int depth) {
        try {
            FileOutputStream fileStream = new FileOutputStream("output.ejb");
            DataOutputStream out = new DataOutputStream(fileStream);

            out.write(new byte[] {'E', 'J', 'B'});

            out.writeShort(width);
            out.writeShort(height);
            out.write(depth);
            for (int i = 0; i < depth; i++) {
                out.write(dictionary.get(i).red);
                out.write(dictionary.get(i).green);
                out.write(dictionary.get(i).blue);
            }
            byte buffer = 0;
            int length = (int) Math.round((Math.log(depth) / Math.log(2)));
            int offset = 8 - length;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (offset == 0) {
                        buffer += (byte) (indexes[x][y]);
                        out.write(buffer);
                        offset = 8 - length;
                    } else if (offset > 0) {
                        buffer = (byte) indexes[x][y];
                        buffer = (byte)(buffer << offset);
                        offset -= length;
                    } else if (offset < 0) {
                        buffer |= (byte) (indexes[x][y] >> -offset);
                        out.write(buffer);
                        buffer = (byte) ((indexes[x][y] << (8 - length - offset)) & 0b11111111);
                        offset += 8;
                    }
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
