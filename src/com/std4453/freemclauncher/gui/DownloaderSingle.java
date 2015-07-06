package com.std4453.freemclauncher.gui;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DownloaderSingle {
	protected String url;
	protected OutputStream os;
	
	protected DownloaderSingleDownloader downloader;

	private JFrame frame;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JButton btnNewButton;
	private JProgressBar progressBar;
	private JLabel lblNewLabel_2;
	private JButton btnNewButton_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DownloaderSingle window = new DownloaderSingle();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public DownloaderSingle(String url,OutputStream os) {
		this();
		
		this.url=url;
		this.os=os;
		
		this.downloader=new DownloaderSingleDownloader(this);
	}
	
	public void run() {
		try {
			this.downloader.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public DownloaderSingle() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u4E0B\u8F7D\u6587\u4EF6");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				downloader.requestCancel();
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 619, 177);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 54, 593, 19);
		frame.getContentPane().add(progressBar);
		
		btnNewButton = new JButton("\u53D6\u6D88");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downloader.requestCancel();
			}
		});
		btnNewButton.setBounds(510, 112, 93, 23);
		frame.getContentPane().add(btnNewButton);
		
		lblNewLabel = new JLabel("DOWNLOAD_URL");
		lblNewLabel.setBounds(10, 10, 586, 15);
		frame.getContentPane().add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("DOWNLOAD_INFO");
		lblNewLabel_1.setBounds(10, 111, 483, 15);
		frame.getContentPane().add(lblNewLabel_1);
		
		progressBar.setMinimum(0);
		progressBar.setMaximum(99);
		
		lblNewLabel_2 = new JLabel("DOWNLOAD_INFO_2");
		lblNewLabel_2.setBounds(10, 86, 483, 15);
		frame.getContentPane().add(lblNewLabel_2);
		
		btnNewButton_1 = new JButton("\u6682\u505C");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downloader.togglePause();
			}
		});
		btnNewButton_1.setBounds(510, 83, 93, 23);
		frame.getContentPane().add(btnNewButton_1);
	}

	public String getUrl() {
		return url;
	}

	public OutputStream getOs() {
		return os;
	}
	public JLabel getLblNewLabel_1() {
		return lblNewLabel_1;
	}
	public JLabel getLblNewLabel() {
		return lblNewLabel;
	}
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JFrame getFrame() {
		return frame;
	}
	public JButton getBtnNewButton_1() {
		return btnNewButton_1;
	}
	public JLabel getLblNewLabel_2() {
		return lblNewLabel_2;
	}
	public JButton getBtnNewButton() {
		return btnNewButton;
	}
}
