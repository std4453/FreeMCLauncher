package com.std4453.freemclauncher.gui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GuiManager {
	public static MainWindow mainWindow;
	
	public static void create() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		
		mainWindow=new MainWindow();
		mainWindow.getFrame().setVisible(true);
	}
	
	public static void main(String[] args) {
		GuiManager.create();
	}
}
