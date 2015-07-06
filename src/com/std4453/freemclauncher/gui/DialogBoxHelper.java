package com.std4453.freemclauncher.gui;

import javax.swing.JOptionPane;

public class DialogBoxHelper {
	public static void info(String title, String msg) {
		JOptionPane.showMessageDialog(null, msg, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void warn(String title, String msg) {
		JOptionPane.showMessageDialog(null, msg, title,
				JOptionPane.WARNING_MESSAGE);
	}

	public static void error(String title, String msg) {
		JOptionPane.showMessageDialog(null, msg, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static boolean confirm(String title, String msg) {
		return JOptionPane.showConfirmDialog(null, msg, title,
				JOptionPane.OK_CANCEL_OPTION) == 0;
	}
}
