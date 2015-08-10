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
	private JButton btnInguitabssettingsbrowse;
	private JCheckBox ckbxShowConsole;
	private JCheckBox ckbxjavaexe;
	private JButton btnAuto;
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
		
		JLabel lblInguitabssettingsbasicsettings = new JLabel("i18n:gui.tabs.settings.basicSettings");
		lblInguitabssettingsbasicsettings.setBounds(10, 10, 428, 15);
		panel.add(lblInguitabssettingsbasicsettings);
		
		JLabel lblInguitabssettingstotalmemory = new JLabel("i18n:gui.tabs.settings.totalMemory");
		lblInguitabssettingstotalmemory.setBounds(10, 50, 93, 15);
		panel.add(lblInguitabssettingstotalmemory);
		
		lblTotalMemory = new JLabel("TOTAL_MEMORY");
		lblTotalMemory.setBounds(124, 50, 399, 15);
		panel.add(lblTotalMemory);
		
		JLabel lblInguitabssettingsfreememory = new JLabel("i18n:gui.tabs.settings.freeMemory");
		lblInguitabssettingsfreememory.setBounds(10, 75, 104, 15);
		panel.add(lblInguitabssettingsfreememory);
		
		lblAvailableMemory = new JLabel("AVAILABLE_MEMORY");
		lblAvailableMemory.setBounds(124, 75, 399, 15);
		panel.add(lblAvailableMemory);
		
		JLabel lblInguitabssettingslaunchmemory = new JLabel("i18n:gui.tabs.settings.launchMemory");
		lblInguitabssettingslaunchmemory.setBounds(10, 103, 104, 15);
		panel.add(lblInguitabssettingslaunchmemory);
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(128, 128, 4096, 1));
		spinner.setBounds(124, 100, 78, 22);
		panel.add(spinner);
		
		JLabel lblMb = new JLabel("MB");
		lblMb.setBounds(212, 100, 54, 15);
		panel.add(lblMb);
		
		btnAuto = new JButton("i18n:gui.tabs.settings.automaticallyAdjustLaunchMemory");
		btnAuto.setBounds(10, 131, 155, 23);
		panel.add(btnAuto);
		
		JButton btnRefresh = new JButton("i18n:gui.tabs.settings.refresh");
		btnRefresh.setBounds(173, 131, 67, 23);
		panel.add(btnRefresh);
		
		JLabel lblInguitabssettingsadvancedsettings = new JLabel("i18n:gui.tabs.settings.advancedSettings");
		lblInguitabssettingsadvancedsettings.setBounds(10, 183, 513, 15);
		panel.add(lblInguitabssettingsadvancedsettings);
		
		ckbxjavaexe = new JCheckBox("i18n:gui.tabs.settings.launchWithJava");
		ckbxjavaexe.setBounds(10, 215, 513, 23);
		panel.add(ckbxjavaexe);
		
		ckbxShowConsole = new JCheckBox("i18n:gui.tabs.settings.showConsole");
		ckbxShowConsole.setBounds(10, 240, 513, 23);
		panel.add(ckbxShowConsole);
		
		JLabel lblJava = new JLabel("i18n:gui.tabs.settings.javaSettings");
		lblJava.setBounds(10, 314, 513, 15);
		panel.add(lblJava);
		
		JLabel lblJavaexejavawexe = new JLabel("i18n:gui.tabs.settings.javaExecutableLocation");
		lblJavaexejavawexe.setBounds(10, 359, 513, 15);
		panel.add(lblJavaexejavawexe);
		
		textField = new JTextField();
		textField.setBounds(10, 383, 216, 21);
		panel.add(textField);
		textField.setColumns(10);
		
		btnInguitabssettingsbrowse = new JButton("i18n:gui.tabs.settings.browse");
		btnInguitabssettingsbrowse.setBounds(232, 382, 93, 23);
		panel.add(btnInguitabssettingsbrowse);
		
		JCheckBox ckbxCloseLauncher = new JCheckBox("i18n:gui.tabs.settings.closeLauncher");
		ckbxCloseLauncher.setBounds(10, 265, 513, 23);
		panel.add(ckbxCloseLauncher);

	}
}
