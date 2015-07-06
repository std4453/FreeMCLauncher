package com.std4453.freemclauncher.gui;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JButton;

public class PanelManageUsers extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7298401201687285341L;
	private JTextField textField;
	private JTextField textField_1;
	private JPasswordField passwordField;
	private JComboBox comboBox;

	/**
	 * Create the panel.
	 */
	public PanelManageUsers() {
		setLayout(new BorderLayout(10, 10));
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		
		JButton button = new JButton("\u65B0\u5EFA\u5B58\u6863");
		panel_1.add(button);
		
		JButton button_1 = new JButton("\u5220\u9664\u5B58\u6863");
		panel_1.add(button_1);
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u7528\u6237\u914D\u7F6E\u540D\u79F0\uFF1A");
		label.setBounds(10, 10, 116, 15);
		panel.add(label);
		
		textField = new JTextField();
		textField.setBounds(10, 35, 154, 21);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("\u7528\u6237\u540D\uFF1A");
		label_1.setBounds(10, 88, 54, 15);
		panel.add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(10, 113, 154, 21);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel label_2 = new JLabel("\u5BC6\u7801(\u6CA1\u6709\u53EF\u4E0D\u586B)\uFF1A");
		label_2.setBounds(10, 144, 116, 15);
		panel.add(label_2);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(10, 169, 154, 21);
		panel.add(passwordField);
		
		JLabel label_3 = new JLabel("\u767B\u9646\u65B9\u5F0F\uFF1A");
		label_3.setBounds(10, 235, 116, 15);
		panel.add(label_3);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"\u79BB\u7EBF", "\u6B63\u7248\u9A8C\u8BC1", "skinme", "yggdrasil", "\u65E0"}));
		comboBox.setBounds(10, 260, 154, 21);
		panel.add(comboBox);
		
		JCheckBox chckbxdemo = new JCheckBox("\u4F7F\u7528-demo\u5F00\u5173\u542F\u52A8\u6F14\u793A\u7248\u6E38\u620F");
		chckbxdemo.setBounds(6, 323, 198, 23);
		panel.add(chckbxdemo);

	}
}
