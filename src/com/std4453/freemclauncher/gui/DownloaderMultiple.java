package com.std4453.freemclauncher.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.OutputStream;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.std4453.freemclauncher.util.ArrayListModel;

public class DownloaderMultiple {
	private Map<String, OutputStream> mapping;
	private String url;
	private OutputStream os;
	private ArrayListModel model;

	private JFrame frame;
	private JLabel lblNewLabel;
	private JList<String> list;
	private JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DownloaderMultiple window = new DownloaderMultiple();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public DownloaderMultiple(Map<String, OutputStream> mapping) {
		this();

		this.mapping = mapping;
	}

	/**
	 * Create the application.
	 */
	public DownloaderMultiple() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		BorderLayout borderLayout = (BorderLayout) frame.getContentPane()
				.getLayout();
		borderLayout.setVgap(10);
		borderLayout.setHgap(10);
		frame.setTitle("\u4E0B\u8F7D\u6587\u4EF6");
		frame.setBounds(100, 100, 469, 445);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(model = new ArrayListModel());
		scrollPane.setViewportView(list);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		progressBar = new JProgressBar();
		panel.add(progressBar, BorderLayout.CENTER);

		lblNewLabel = new JLabel("New label");
		panel.add(lblNewLabel, BorderLayout.NORTH);

		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
	}

	public JLabel getLblNewLabel() {
		return lblNewLabel;
	}

	public JList<String> getList() {
		return list;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void run() {
		int count = 0;
		frame.setTitle("下载文件：" + count + "/" + this.mapping.size());
		for (Map.Entry<String, OutputStream> entry : this.mapping.entrySet()) {
			this.url = entry.getKey();
			if (url.indexOf('|') != -1)
				this.url = url.substring(url.indexOf('|')+1);
			this.os = entry.getValue();

			DownloaderMultipleDownloader downloader = new DownloaderMultipleDownloader(
					this);
			downloader.run();
			
			++count;
			
			frame.setTitle("下载文件：" + count + "/" + this.mapping.size());
		}
	}

	public String getCurrentUrl() {
		return url;
	}

	public OutputStream getCurrentOs() {
		return os;
	}

	public ArrayListModel getModel() {
		return this.model;
	}

	public JFrame getFrame() {
		return frame;
	}
}
