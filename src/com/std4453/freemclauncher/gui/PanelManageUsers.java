package com.std4453.freemclauncher.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;

import com.std4453.freemclauncher.files.FileHelper;
import com.std4453.freemclauncher.i18n.I18NHelper;
import com.std4453.freemclauncher.util.ArrayListModel;
import com.std4453.freemclauncher.util.StructuredDataArray;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;

import java.awt.Dimension;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class PanelManageUsers extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7298401201687285341L;
	private JTextField textField;
	private JTextField textField_1;
	private JPasswordField passwordField;
	private JComboBox<String> comboBox;

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
		btnNewConf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				users.add(newUserConf());
				refreshUsers();
				writeUsers();

				list.setSelectedIndex(model.getSize() - 1);
			}
		});
		panel_1.add(btnNewConf);

		JButton btnDeleteConf = new JButton(
				"i18n:gui.tabs.manageUsers.deleteUserConfiguration");
		btnDeleteConf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selection = list.getSelectedIndex();
				if (selection != -1) {
					if (!DialogBoxHelper.confirm(
							I18NHelper
									.getFormattedLocalization("gui.dialog.delectConfConfirm.title"),
							I18NHelper.getFormattedLocalization(
									"gui.dialog.delectConfConfirm.content",
									users.get(selection).getId())))
						return;

					users.remove(selection);
					refreshUsers();
					writeUsers();

					if (users.size() == 0) {
						list.setSelectedIndex(-1);

						textField.setText("");
						textField.setEnabled(false);
						textField_1.setText("");
						textField_1.setEnabled(false);
						passwordField.setText("");
						passwordField.setEnabled(false);

						comboBox.setSelectedIndex(0);
						comboBox.setEnabled(false);

						ckbxdemo.setSelected(false);
						ckbxdemo.setEnabled(false);

						return;
					}

					textField.setText(users.get(selection).getId());
					textField.setEnabled(true);
					textField_1.setText(users.get(selection).getName());
					textField_1.setEnabled(true);
					passwordField.setText(users.get(selection).getPassword());
					passwordField.setEnabled(true);

					comboBox.setSelectedIndex(users.get(selection).getMode());
					comboBox.setEnabled(true);

					ckbxdemo.setSelected(users.get(selection).isDemo());
					ckbxdemo.setEnabled(true);
				}
			}
		});
		panel_1.add(btnDeleteConf);

		JSplitPane splitPane = new JSplitPane();
		add(splitPane);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		list = new JList<String>();
		list.setPreferredSize(new Dimension(200, 0));
		list.setVisibleRowCount(30);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int selection = list.getSelectedIndex();
				if (selection < 0) {
					textField.setText("");
					textField.setEnabled(false);
					textField_1.setText("");
					textField_1.setEnabled(false);
					passwordField.setText("");
					passwordField.setEnabled(false);

					comboBox.setSelectedIndex(0);
					comboBox.setEnabled(false);

					ckbxdemo.setSelected(false);
					ckbxdemo.setEnabled(false);
				} else {
					textField.setText(users.get(selection).getId());
					textField.setEnabled(true);
					textField_1.setText(users.get(selection).getName());
					textField_1.setEnabled(true);
					passwordField.setText(users.get(selection).getPassword());
					passwordField.setEnabled(true);

					comboBox.setSelectedIndex(users.get(selection).getMode());
					comboBox.setEnabled(true);

					ckbxdemo.setSelected(users.get(selection).isDemo());
					ckbxdemo.setEnabled(true);
				}
			}
		});
		list.setModel(model = new ArrayListModel());
		scrollPane.setViewportView(list);

		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(null);

		JLabel lblInguitabsmanageusersuserconfigurationname = new JLabel(
				"i18n:gui.tabs.manageUsers.userConfigurationName");
		lblInguitabsmanageusersuserconfigurationname.setBounds(10, 10, 225, 15);
		panel.add(lblInguitabsmanageusersuserconfigurationname);

		textField = new JTextField();
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				int selection = list.getSelectedIndex();
				boolean conflicts = hasConfWithName(textField.getText()) ? !textField
						.getText().equals(users.get(selection).getId()) : false;
				if (selection < 0)
					return;
				if (!conflicts) {
					users.get(selection).setId(textField.getText());
					refreshUsers();
					writeUsers();
				} else {
					DialogBoxHelper.warn(
							I18NHelper
									.getFormattedLocalization("gui.dialog.userConfNameConflict.title"),
							I18NHelper.getFormattedLocalization(
									"gui.dialog.userConfNameConflict.content",
									textField.getText()));
					textField.setText(users.get(selection).getId());
					textField.grabFocus();
					textField.setCaretPosition(0);
					textField.setSelectionStart(0);
					textField.setSelectionEnd(textField.getText().length());
				}
			}
		});
		textField.setBounds(10, 35, 154, 21);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblInguitabsmanageusersusername = new JLabel(
				"i18n:gui.tabs.manageUsers.userName");
		lblInguitabsmanageusersusername.setBounds(10, 88, 225, 15);
		panel.add(lblInguitabsmanageusersusername);

		textField_1 = new JTextField();
		textField_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				int selection = list.getSelectedIndex();
				if (selection < 0)
					return;

				users.get(selection).setName(textField_1.getText());
				writeUsers();
			}
		});
		textField_1.setBounds(10, 113, 154, 21);
		panel.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblInguitabsmanageuserspassword = new JLabel(
				"i18n:gui.tabs.manageUsers.password");
		lblInguitabsmanageuserspassword.setBounds(10, 144, 225, 15);
		panel.add(lblInguitabsmanageuserspassword);

		passwordField = new JPasswordField();
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				int selection = list.getSelectedIndex();
				if (selection < 0)
					return;

				users.get(selection).setPassword(
						new String(passwordField.getPassword()));
				writeUsers();
			}
		});
		passwordField.setBounds(10, 169, 154, 21);
		panel.add(passwordField);

		JLabel lblInguitabsmanageusersloginmode = new JLabel(
				"i18n:gui.tabs.manageUsers.loginMode");
		lblInguitabsmanageusersloginmode.setBounds(10, 235, 225, 15);
		panel.add(lblInguitabsmanageusersloginmode);

		comboBox = new JComboBox<String>();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int selection = list.getSelectedIndex();
				if (selection < 0)
					return;

				if (users.size() <= selection)
					return;
				int select = comboBox.getSelectedIndex();
				users.get(selection).setMode(select);
				writeUsers();
			}
		});
		comboBox.setModel(new DefaultComboBoxModel<String>(
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

		ckbxdemo = new JCheckBox("i18n:gui.tabs.manageUsers.launchDemo");
		ckbxdemo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selection = list.getSelectedIndex();
				if (selection < 0)
					return;

				users.get(selection).setDemo(ckbxdemo.isSelected());
				writeUsers();
			}
		});
		ckbxdemo.setBounds(6, 323, 229, 23);
		panel.add(ckbxdemo);

		init();
	}

	protected ArrayListModel model;

	protected java.util.List<UserConf> users;
	private JList<String> list;
	private JCheckBox ckbxdemo;

	protected void init() {
		users = new Vector<UserConf>();

		createDefaultUsersFileIfNessessary();

		readUsers();
		refreshUsers();

		textField.setText("");
		textField.setEnabled(false);
		textField_1.setText("");
		textField_1.setEnabled(false);
		passwordField.setText("");
		passwordField.setEnabled(false);

		comboBox.setSelectedIndex(0);
		comboBox.setEnabled(false);

		ckbxdemo.setSelected(false);
		ckbxdemo.setEnabled(false);
	}

	protected void readUsers() {
		File file = new File("users.conf");
		String content = FileHelper.getFileContentAsString(file);

		try {
			JSONArray json = new JSONArray(content);
			StructuredDataArray sda = StructuredDataHelper.fromJSONArray(json)
					.toStructuredDataArray();

			for (Object object : sda.getChildren()) {
				if (!(object instanceof StructuredDataObject))
					continue;

				StructuredDataObject sdo = (StructuredDataObject) object;

				String id = sdo.getString("id");
				String name = sdo.getString("name");
				String password = sdo.getString("password");
				int mode = sdo.getInt("mode");
				boolean demo = sdo.getBoolean("demo");

				users.add(new UserConf(id, name, password, mode, demo));
			}
		} catch (Throwable t) {
			t.printStackTrace();
			createDefaultUsersFile();
		}
	}

	protected void writeUsers() {
		StructuredDataArray sda = new StructuredDataArray();

		for (UserConf conf : users) {
			StructuredDataObject sdo = new StructuredDataObject();
			sdo.put("id", conf.getId());
			sdo.put("name", conf.getName());
			sdo.put("password", conf.getPassword());
			sdo.put("mode", conf.getMode());
			sdo.put("demo", conf.isDemo());

			sda.put(sdo);
		}

		JSONArray arr = StructuredDataHelper.toJSONArray(sda);
		File file = new File("users.conf");

		FileHelper.writeToFile(file, arr.toString());
	}

	protected String getFreeConfName() {
		int index = 1;

		while (hasConfWithName("conf-" + index))
			++index;

		return "conf-" + index;
	}

	protected boolean hasConfWithName(String name) {
		if (name.trim().isEmpty())
			return true;

		for (UserConf conf : users)
			if (conf.getId().equals(name))
				return true;

		return false;
	}

	protected void refreshUsers() {
		model.clear();

		for (UserConf user : users)
			model.add(user.getId());
	}

	protected void createDefaultUsersFileIfNessessary() {
		File file = new File("users.conf");
		if (!file.isFile())
			createDefaultUsersFile();
	}

	protected void createDefaultUsersFile() {
		File file = new File("users.conf");
		String data = "[]";

		FileHelper.writeToFile(file, data);
	}

	protected UserConf newUserConf() {
		String id = getFreeConfName();
		return new UserConf(id, "My User Name", "", 2, false);
	}

	public static class UserConf {
		protected String id;
		protected String name;
		protected String password;
		protected int mode;
		protected boolean demo;

		public UserConf(String id, String name, String password, int mode,
				boolean demo) {
			this.id = id;
			this.name = name;
			this.password = password;
			this.mode = mode;
			this.demo = demo;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getPassword() {
			return password;
		}

		public int getMode() {
			return mode;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setMode(int mode) {
			this.mode = mode;
		}

		public boolean isDemo() {
			return demo;
		}

		public void setDemo(boolean demo) {
			this.demo = demo;
		}
	}
}
