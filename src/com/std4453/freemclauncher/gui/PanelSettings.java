package com.std4453.freemclauncher.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

public class PanelSettings extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6240645669642961473L;
	private JLabel lblTotalMemory;
	private JLabel lblAvailableMemory;
	private JTextField textField;
	private JButton button_1;
	private JCheckBox checkBox;
	private JCheckBox chckbxjavaexe;
	private JButton button;
	private JSpinner spinner;

	/**
	 * Create the panel.
	 */
	public PanelSettings() {
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u57FA\u7840\u8BBE\u7F6E\uFF1A");
		label.setBounds(10, 10, 67, 15);
		panel.add(label);
		
		JLabel label_1 = new JLabel("\u672C\u673A\u5185\u5B58\uFF1A");
		label_1.setBounds(10, 50, 67, 15);
		panel.add(label_1);
		
		lblTotalMemory = new JLabel("TOTAL_MEMORY");
		lblTotalMemory.setBounds(87, 50, 399, 15);
		panel.add(lblTotalMemory);
		
		JLabel label_2 = new JLabel("\u53EF\u7528\u5185\u5B58\uFF1A");
		label_2.setBounds(10, 75, 67, 15);
		panel.add(label_2);
		
		lblAvailableMemory = new JLabel("AVAILABLE_MEMORY");
		lblAvailableMemory.setBounds(87, 75, 399, 15);
		panel.add(lblAvailableMemory);
		
		JLabel label_3 = new JLabel("\u542F\u52A8\u5185\u5B58\uFF1A");
		label_3.setBounds(10, 103, 67, 15);
		panel.add(label_3);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(128, 128, 4096, 1));
		spinner.setBounds(87, 100, 78, 22);
		panel.add(spinner);
		
		JLabel lblMb = new JLabel("MB");
		lblMb.setBounds(172, 103, 54, 15);
		panel.add(lblMb);
		
		button = new JButton("\u81EA\u52A8\u9009\u62E9\u542F\u52A8\u5185\u5B58\u5927\u5C0F");
		button.setBounds(10, 131, 155, 23);
		panel.add(button);
		
		JLabel label_4 = new JLabel("\u9AD8\u7EA7\u8BBE\u7F6E\uFF1A");
		label_4.setBounds(10, 183, 67, 15);
		panel.add(label_4);
		
		chckbxjavaexe = new JCheckBox("\u4F7F\u7528java.exe\u542F\u52A8\u6E38\u620F");
		chckbxjavaexe.setBounds(10, 215, 212, 23);
		panel.add(chckbxjavaexe);
		
		checkBox = new JCheckBox("\u663E\u793A\u6E38\u620F\u547D\u4EE4\u884C\u7A97\u53E3");
		checkBox.setBounds(10, 240, 155, 23);
		panel.add(checkBox);
		
		JLabel lblJava = new JLabel("Java\u8BBE\u7F6E\uFF1A");
		lblJava.setBounds(10, 314, 67, 15);
		panel.add(lblJava);
		
		JLabel lblJavaexejavawexe = new JLabel("java.exe,javaw.exe\u4F4D\u7F6E\uFF1A");
		lblJavaexejavawexe.setBounds(10, 359, 155, 15);
		panel.add(lblJavaexejavawexe);
		
		textField = new JTextField();
		textField.setBounds(10, 383, 216, 21);
		panel.add(textField);
		textField.setColumns(10);
		
		button_1 = new JButton("\u6D4F\u89C8...");
		button_1.setBounds(232, 382, 93, 23);
		panel.add(button_1);
		
		JCheckBox checkBox_1 = new JCheckBox("\u542F\u52A8\u540E\u5173\u95ED\u542F\u52A8\u5668\u7A97\u53E3");
		checkBox_1.setBounds(10, 265, 155, 23);
		panel.add(checkBox_1);

	}
}
