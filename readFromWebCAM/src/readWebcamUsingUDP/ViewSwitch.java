package readWebcamUsingUDP;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewSwitch extends JFrame {

	private JPanel contentPane;
	Main theMain;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewSwitch frame = new ViewSwitch();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ViewSwitch(Main tm) {
		this.theMain = tm;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 198, 176);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setVisible(true);
		
		JButton camBtn = new JButton("Webcam View");
		camBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				theMain.ViewMode = 1;
			}
		});
		camBtn.setBounds(20, 10, 141, 23);
		contentPane.add(camBtn);
		
		JButton deskBtn = new JButton("Desktop View");
		deskBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				theMain.ViewMode = 2;
			}
		});
		deskBtn.setBounds(20, 44, 141, 23);
		contentPane.add(deskBtn);
	}
}
