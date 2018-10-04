package vadimjprokopev.imageDecolorizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JTextField imagePathField = new JTextField("Path to image");
        JButton processButton = new JButton("Process image");
        JButton selectButton = new JButton("Select image");
        JLabel depthLabel = new JLabel("Depth (amount of kernels): ");
        JSpinner depthSpinner = new JSpinner(new SpinnerNumberModel(16, 2, 255, 1));

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
                
                JFrame imageWindow = new JFrame();
                imageWindow.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                ImagePanel imagePanel = new ImagePanel();
                imageWindow.add(imagePanel);
                imageWindow.setSize(image.getWidth(), image.getHeight());
                imageWindow.setVisible(true);
                
                new Thread(new ProcessImageRunnable(image, (Integer) depthSpinner.getValue(), imagePanel)).start();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        });

        panel.add(imagePathField);
        panel.add(selectButton);
        panel.add(processButton);
        panel.add(depthLabel);
        panel.add(depthSpinner);

        frame.setContentPane(panel);
        frame.setLayout(null);
        frame.setSize(600, 100);

        imagePathField.setBounds(0, 0, 450, 25);
        selectButton.setBounds(450, 0, 150, 25);
        processButton.setBounds(450, 25, 150, 25);
        depthLabel.setBounds(5, 25, 175, 25);
        depthSpinner.setBounds(180, 25, 100, 25);
        
        frame.setVisible(true);
    }
}
