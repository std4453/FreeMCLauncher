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
import javax.swing.border.EmptyBorder;

import com.std4453.freemclauncher.i18n.I18NHelper;

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
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(5, 5));

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);

		JButton btnNewConf = new JButton(
				"i18n:gui.tabs.manageUsers.newUserConfiguration");
		panel_1.add(btnNewConf);

		JButton btnDeleteConf = new JButton(
				"i18n:gui.tabs.manageUsers.deleteUserConfiguration");
		panel_1.add(btnDeleteConf);

		JSplitPane splitPane = new JSplitPane();
		add(splitPane);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		JList list = new JList();
		scrollPane.setViewportView(list);

		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(null);

		JLabel lblInguitabsmanageusersuserconfigurationname = new JLabel(
				"i18n:gui.tabs.manageUsers.userConfigurationName");
		lblInguitabsmanageusersuserconfigurationname.setBounds(10, 10, 225, 15);
		panel.add(lblInguitabsmanageusersuserconfigurationname);

		textField = new JTextField();
		textField.setBounds(10, 35, 154, 21);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblInguitabsmanageusersusername = new JLabel(
				"i18n:gui.tabs.manageUsers.userName");
		lblInguitabsmanageusersusername.setBounds(10, 88, 225, 15);
		panel.add(lblInguitabsmanageusersusername);

		textField_1 = new JTextField();
		textField_1.setBounds(10, 113, 154, 21);
		panel.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblInguitabsmanageuserspassword = new JLabel(
				"i18n:gui.tabs.manageUsers.password");
		lblInguitabsmanageuserspassword.setBounds(10, 144, 225, 15);
		panel.add(lblInguitabsmanageuserspassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(10, 169, 154, 21);
		panel.add(passwordField);

		JLabel lblInguitabsmanageusersloginmode = new JLabel(
				"i18n:gui.tabs.manageUsers.loginMode");
		lblInguitabsmanageusersloginmode.setBounds(10, 235, 225, 15);
		panel.add(lblInguitabsmanageusersloginmode);

		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(
				new String[] {
						I18NHelper
								.getFormattedLocalization("gui.tabs.manageUsers.loginModeOffline"),
						I18NHelper
								.getFormattedLocalization("gui.tabs.manageUsers.loginModeLegacy"),
						I18NHelper
								.getFormattedLocalization("gui.tabs.manageUsers.loginModeYggdrasil"),
						I18NHelper
								.getFormattedLocalization("gui.tabs.manageUsers.loginModeNone") }));
		comboBox.setBounds(10, 260, 154, 21);
		panel.add(comboBox);

		JCheckBox ckbxdemo = new JCheckBox(
				"i18n:gui.tabs.manageUsers.launchDemo");
		ckbxdemo.setBounds(6, 323, 229, 23);
		panel.add(ckbxdemo);

	}
}
