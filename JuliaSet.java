import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
public class JuliaSet extends JPanel implements AdjustmentListener {

    private JFrame frame;
    private JScrollBar[] scrollBars;
    private JLabel[] alterations;
    private final static int width = 1000;
    private final static int height = 900;
    private BufferedImage image;
    private double zoom = 1;
    private float iterations = 300;
    private float saturation = 1;
    private float brightness = 1;
    private float power = 2;
    private double a = 0.0;
    private double b = 0.0;

    public JuliaSet() {
        frame = new JFrame("JuliaSet");
        frame.add(this);
        frame.setSize(1000, 800);

        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(7, 2));
        alterations = new JLabel[7];
        alterations[0] = new JLabel("A: 0.0");
        alterations[1] = new JLabel("B: 0.0");
        alterations[2] = new JLabel("Zoom: 1.0");
        alterations[3] = new JLabel("Iterations: " + (int) iterations);
        alterations[4] = new JLabel("Saturation: " + saturation * 100 + "%");
        alterations[5] = new JLabel("Brightness: " + brightness * 100 + "%");
        alterations[6] = new JLabel("Power: " + (int) power);
        scrollBars = new JScrollBar[alterations.length];
        //scrollbars for all the alterations above
        scrollBars[0] = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 20);
        scrollBars[1] = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 20);
        scrollBars[2] = new JScrollBar(JScrollBar.HORIZONTAL, 1, 0, 0, 100);
        scrollBars[3] = new JScrollBar(JScrollBar.HORIZONTAL, 300, 0, 100, 1000);
        scrollBars[3].setUnitIncrement(50);
        scrollBars[4] = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 100);
        scrollBars[5] = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 0, 100);
        scrollBars[6] = new JScrollBar(JScrollBar.HORIZONTAL, 2, 0, 1, 5);



        for (int i = 0; i < scrollBars.length; i++) {
            scrollPanel.add(alterations[i]);
            scrollBars[i].addAdjustmentListener(this);
            scrollPanel.add(scrollBars[i]);
        }

        frame.add(scrollPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	public static double Exponents(int exp, double a) {
		if (exp == 2)
			return a*a;
		if (exp == 3)
			return a*a*a;
		if (exp == 4)
			return a*a*a*a;
		if (exp == 5)
			return a*a*a*a*a;
		return a;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double zx = 1.5 * (x - (width / 2)) / (0.5 * zoom * width);
                double zy = (y - (height / 2)) / (0.5 * zoom * height);
                float iterator = iterations;

                while ((Exponents((int) power, zx) + Exponents((int) power, zy)) < 6 && iterator > 0) {
                    double num = Exponents((int) power, zx) - Exponents((int) power, zy) + a;
                    zy = 2 * zx * zy + b;
                    zx = num;
                    iterator -= 1;
                }

                int c;
                if (iterator > 0)
                    c = Color.HSBtoRGB(((iterations / iterator) % 1), saturation, brightness);
                else
                    c = Color.HSBtoRGB((iterations / iterator), 1, 0);

                image.setRGB(x, y, c);
            }
        }

        g.drawImage(image, 0, 0, null);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        for (int i = 0; i < scrollBars.length; i++) {
            if (e.getSource() == scrollBars[i]) {
                if (i == 0) {
                    a = scrollBars[i].getValue() / 20.0;
                    alterations[i].setText("A: " + a);
                }
                if (i == 1) {
                    b = scrollBars[i].getValue() / 20.0;
                    alterations[i].setText("B: " + a);
                }
                if (i == 2) {
                    zoom = scrollBars[i].getValue();
                    alterations[i].setText("Zoom: " + zoom);
                }
                if (i == 3) {
                    iterations = scrollBars[i].getValue();
                    alterations[i].setText("Iterations: " + (int) iterations);
                }
                if (i == 4) {
                    saturation = (float) (scrollBars[i].getValue() / 100.0);
                    alterations[i].setText("Saturation: " + saturation * 100 + "%");
                }
                if (i == 5) {
                    brightness = (float) (scrollBars[i].getValue() / 100.0);
                    alterations[i].setText("Brightness: " + brightness * 100 + "%");
                }
                if (i == 6) {
                    power = scrollBars[i].getValue();
                    alterations[i].setText("Power: " + (int) power);
                }
            }
        }
        repaint();
    }


     public static void main(String[] args) {
	        JuliaSet app = new JuliaSet();
    }

}