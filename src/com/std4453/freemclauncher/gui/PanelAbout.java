package com.std4453.freemclauncher.gui;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class PanelAbout extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6124027902716385701L;

	/**
	 * Create the panel.
	 */
	public PanelAbout() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		scrollPane.setViewportView(textPane);

		InputStream is=PanelAbout.class.getResourceAsStream("about.txt");
		byte[] bytes=null;
		try {
			bytes = new byte[is.available()];
			is.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		textPane.setText(new String(bytes));
	}

}
