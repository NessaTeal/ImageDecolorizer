package vadimjprokopev.imageDecolorizer;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JTextField imagePathField = new JTextField("Path to image");
        JButton processButton = new JButton("Process image");
        JButton selectButton = new JButton("Select image");

        selectButton.addActionListener(actionEvent -> {
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.showOpenDialog(frame);
	        if (fileChooser.getSelectedFile() != null) {
	        	imagePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
	        }
        });

        processButton.addActionListener(actionEvent -> {
            try {
                File file = new File(imagePathField.getText());
                BufferedImage image = ImageIO.read(file);
                ImageProcessor imageProcessor = new ImageProcessor(image, 16);
                imageProcessor.process();
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        });

        panel.add(imagePathField);
        panel.add(selectButton);
        panel.add(processButton);

        frame.setContentPane(panel);
        frame.setLayout(null);
        frame.setSize(620, 100);

        imagePathField.setBounds(0, 0, 450, 25);
        selectButton.setBounds(450, 0, 150, 25);
        processButton.setBounds(450, 25, 150, 25);

        frame.setVisible(true);
    }

}
