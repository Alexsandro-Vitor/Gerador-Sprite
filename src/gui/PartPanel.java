package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import classes.Folders;
import classes.PartColor;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;

public class PartPanel extends JPanel {
	private Generator generator;
	public JComboBox<String> cmb;
	private JSpinner spinAlpha;
	private JSpinner spinA;
	private JSpinner spinB;
	private JSpinner spinC;
	public JSpinner spinHueSwap;
	public JComboBox<String> cmbOriginal;
	public JComboBox<String> cmbNew;
	
	public SpinnerNumberModel red = new SpinnerNumberModel(255, 0, 255, 1);
	public SpinnerNumberModel green = new SpinnerNumberModel(255, 0, 255, 1);
	public SpinnerNumberModel blue = new SpinnerNumberModel(255, 0, 255, 1);
	public SpinnerNumberModel hue = new SpinnerNumberModel(0, 0, 360, 1);
	public SpinnerNumberModel sat = new SpinnerNumberModel(0, 0, 100, 1);
	public SpinnerNumberModel bright = new SpinnerNumberModel(0, 0, 100, 1);
	
	public String name;
	public PartColor color = PartColor.WHITE;
	private JCheckBox chckbxLock;

	/**
	 * Create the panel.
	 */
	public PartPanel(Generator generator, String labelName, String tooltipName, String[] cmbOptions) {
		this.generator = generator;
		setLayout(null);
		
		JLabel lblPartName = new JLabel(labelName);
		lblPartName.setBounds(0, 0, 45, 20);
		add(lblPartName);
		
		cmb = new JComboBox(cmbOptions);
		cmb.setBounds(55, 0, 215, 20);
		add(cmb);
		cmb.setBackground(cmbOptions.length > 1 ? Color.WHITE : Color.LIGHT_GRAY);
		
		spinAlpha = new JSpinner();
		spinAlpha.setBounds(280, 0, 60, 20);
		add(spinAlpha);
		
		spinA = new JSpinner();
		spinA.setBounds(350, 0, 60, 20);
		add(spinA);
		
		spinB = new JSpinner();
		spinB.setBounds(420, 0, 60, 20);
		add(spinB);
		
		spinC = new JSpinner();
		spinC.setBounds(490, 0, 60, 20);
		add(spinC);
		
		spinHueSwap = new JSpinner();
		spinHueSwap.setBounds(560, 0, 60, 20);
		add(spinHueSwap);
		
		cmbOriginal = new JComboBox(generator.folders.files(Folders.PartTypes.PALETTES));
		cmbOriginal.setBounds(350, 0, 130, 20);
		cmbOriginal.setBackground(cmbOptions.length > 1 ? Color.WHITE : Color.LIGHT_GRAY);
		
		cmbNew = new JComboBox(generator.folders.files(Folders.PartTypes.PALETTES));
		cmbNew.setBounds(490, 0, 130, 20);
		cmbNew.setBackground(cmbOptions.length > 1 ? Color.WHITE : Color.LIGHT_GRAY);
		
		JButton btnRandom = new JButton("Random");
		btnRandom.setBounds(630, 0, 90, 20);
		add(btnRandom);
		btnRandom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				randomPartColor();
			}
		});
		
		chckbxLock = new JCheckBox("Lock");
		chckbxLock.setBounds(726, 0, 54, 20);
		add(chckbxLock);
		
		this.name = tooltipName;
		this.configSpritePartUI();
	}
	
	/**
	 * Configures the UI components of the sprite part.
	 */
	void configSpritePartUI() {
		this.cmb.addItemListener(this.generator.itemListener);

		this.spinAlpha.setToolTipText("Alpha of " + this.name);
		this.spinAlpha.setModel(new SpinnerNumberModel(255, 0, 255, 1));
		this.spinAlpha.addChangeListener(this.generator.changeListener);

		this.spinA.setToolTipText("Red color of " + this.name);
		this.spinA.setModel(this.red);
		this.spinA.addChangeListener(this.generator.changeListener);

		this.spinB.setToolTipText("Green color of " + this.name);
		this.spinB.setModel(this.green);
		this.spinB.addChangeListener(this.generator.changeListener);

		this.spinC.setToolTipText("Blue color of " + this.name);
		this.spinC.setModel(this.blue);
		this.spinC.addChangeListener(this.generator.changeListener);

		this.spinHueSwap.setToolTipText("Hue Swap of " + this.name);
		this.spinHueSwap.setModel(new SpinnerNumberModel(0, 0, 360, 1));
		this.spinHueSwap.addChangeListener(this.generator.changeListener);
		
		this.cmbOriginal.setToolTipText("Original palette");
		this.cmbOriginal.addItemListener(this.generator.itemListener);
		
		this.cmbNew.setToolTipText("New palette");
		this.cmbNew.addItemListener(this.generator.itemListener);
	}

	/**
	 * Selects a random part in the combo box, if this part is not locked.
	 * @param random A random object for selecting the part.
	 */
	public void selectRandomPart(Random random) {
		if (!this.chckbxLock.isSelected() && this.cmb.getItemCount() > 0)
			this.cmb.setSelectedIndex(random.nextInt(this.cmb.getItemCount()));
	}

	/**
	 * Sets a random color for the part.
	 * @param part The part which colors will be changed.
	 */
	private void randomPartColor() {
		this.generator.shouldUpdate = false;
		this.spinA.setValue(this.generator.random.nextInt((Integer) ((SpinnerNumberModel) this.spinA.getModel()).getMaximum() + 1));
		this.spinB.setValue(this.generator.random.nextInt((Integer) ((SpinnerNumberModel) this.spinB.getModel()).getMaximum() + 1));
		this.spinC.setValue(this.generator.random.nextInt((Integer) ((SpinnerNumberModel) this.spinC.getModel()).getMaximum() + 1));
		this.spinHueSwap.setValue(this.generator.random.nextInt(361));
		this.generator.updateSprite();
		this.generator.shouldUpdate = true;
	}

	/**
	 * Updates the combo box to show the current filenames in the part folder.
	 * @param files The files currently in the part folder.
	 * @param palettes The palettes in the Palettes folder.
	 */
	void updateCmb(String[] files, String[] palettes) {
		this.cmb.setModel(new DefaultComboBoxModel<String>(files));
		this.cmb.setBackground(files.length > 1 ? Color.WHITE : Color.LIGHT_GRAY);
		this.cmbOriginal.setModel(new DefaultComboBoxModel<String>(palettes));
		this.cmbOriginal.setBackground(palettes.length > 1 ? Color.WHITE : Color.LIGHT_GRAY);
		this.cmbNew.setModel(new DefaultComboBoxModel<String>(palettes));
		this.cmbNew.setBackground(palettes.length > 1 ? Color.WHITE : Color.LIGHT_GRAY);
	}

	/**
	 * Reads the current values of the combo boxes to update the current color.
	 * @param rgba If the attributes are RGB (true) or HSB (false).
	 */
	public void updateColor(boolean rgba) {
		int temp;
		if (rgba) {
			this.color = new PartColor((int)spinA.getValue(), (int)spinB.getValue(), (int)spinC.getValue(), (int)spinAlpha.getValue());
		} else {
			temp = (Color.HSBtoRGB((int)spinA.getValue() / 360f, (int)spinB.getValue() / 100f, (int)spinC.getValue() / 100f) & 0x00FFFFFF)
					+ ((int)spinAlpha.getValue() << 24);
			this.color = new PartColor(temp);
		}
	}

	/**
	 * Reads the current value of the color to update the spinners.
	 * @param rgba If the attributes are RGB (true) or HSB (false).
	 */
	public void updateSpinners(boolean rgba) {
		if (rgba) {
			//HSL to RGB conversion
			int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
			spinA.setModel(this.red);
			spinA.setValue(r);
			spinA.setToolTipText("Red color of " + this.name);
			spinB.setModel(this.green);
			spinB.setValue(g);
			spinB.setToolTipText("Green color of " + this.name);
			spinC.setModel(this.blue);
			spinC.setValue(b);
			spinC.setToolTipText("Blue color of " + this.name);
		} else {
			//RGB to HSL conversion
			float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
			spinA.setModel(this.hue);
			spinA.setValue(Math.round(hsb[0] * 360));
			spinA.setToolTipText("Hue of " + this.name);
			spinB.setModel(this.sat);
			spinB.setValue(Math.round(hsb[1] * 100));
			spinB.setToolTipText("Saturation of " + this.name);
			spinC.setModel(this.bright);
			spinC.setValue(Math.round(hsb[2] * 100));
			spinC.setToolTipText("Brightness of " + this.name);
		}
	}
	
	public void setPaletteMode() {
		this.color = new PartColor(255, 255, 255, 255);
		this.updateSpinners(this.generator.rgba);
		this.spinHueSwap.setValue(0);
		
		this.remove(spinA);
		this.remove(spinB);
		this.remove(spinC);
		this.remove(spinHueSwap);

		this.add(cmbOriginal);
		this.add(cmbNew);
		
		this.revalidate();
		this.repaint();
	}
	
	void setColorMode() {
		this.remove(cmbOriginal);
		this.remove(cmbNew);
		
		this.add(spinA);
		this.add(spinB);
		this.add(spinC);
		this.add(spinHueSwap);
		
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Checks if palettes were selected for the palette change.
	 * @return true if original and new palettes were chosen, false otherwise.
	 */
	public boolean isUsingPalettes() {
		return (this.cmbOriginal.getSelectedIndex() > 0) && (this.cmbNew.getSelectedIndex() > 0);
	}
}