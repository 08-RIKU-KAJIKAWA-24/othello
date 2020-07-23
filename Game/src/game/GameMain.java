package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameMain extends JFrame{
	private static final int width = 500;
	private static final int height = 400;
	public static void main(String[] args) {
		GameMain gm = new GameMain();
		gm.setTitle("Game Programming");
		gm.setSize(width, height);
		gm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gm.setVisible(true);
	}
}

class GamePlay extends JPanel implements KeyListener{

	public GamePlay() {
		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

}