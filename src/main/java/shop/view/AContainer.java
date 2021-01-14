package shop.view;

import java.awt.Color;
import javax.swing.*;

public abstract class AContainer {

	protected JPanel container;

	public AContainer() {
		container = new JPanel();
		container.setBackground(new Color(116, 142, 203));
	}

	protected JPanel getPanel() {
		return container;
	}

	protected abstract void initPanel();

}