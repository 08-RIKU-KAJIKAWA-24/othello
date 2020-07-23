package othello;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Othello extends JFrame{
	private static final int width = 525;
	private static final int height = 600;

	public static void main(String[] args) {
		Othello othello = new Othello();
		othello.setTitle("Othello");
		othello.setSize(width, height);
		//othello.getContentPane().setBackground(new Color(28,84,56));
		othello.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		OthelloPlayer op = new OthelloPlayer();
		othello.add(op);
		othello.setVisible(true);
	}
}

class OthelloPlayer extends JPanel implements KeyListener{
	private static final int CS = 60;
	private static final int MASU = 8;

	private static final int W = 480;
	private static final int H = 480;

	private static final int BLACK = -1;
	private static final int WHITE = 1;
	private static final int NONE = 0;

	private static boolean FLAG = false;
	private static boolean result = false;

	private static int board[][] = new int[MASU][MASU];
	private static int cursor[]= new int[2];

	private static int count ;
	private static int white = 2;
	private static int black = 2;

	public OthelloPlayer() {

		//boardの初期化
		for(int i=0; i<MASU; i++) {
			for(int j=0; j<MASU; j++) {
				board[i][j] = NONE;
			}
			board[3][3] = WHITE;
			board[4][4] = WHITE;
			board[4][3] = BLACK;
			board[3][4] = BLACK;
		}
		//cursorの初期化
		cursor[0] = MASU/2;
		cursor[1] = MASU/2;

		//KeyListenerの追加
		setFocusable(true);
		addKeyListener(this);
	}

	@Override
	public void paint(Graphics g) {
		drawBoard(g);
		drawCursor(g);
		drawStone(g);
	}

	private void drawBoard(Graphics g) {

		//マスの表示
		for(int i=0; i<MASU; i++) {
			for(int j=0; j<MASU; j++) {
				g.setColor(new Color(41,122, 82));
				g.fillRect(CS*i+20, CS*j+20, CS, CS);
			}
		}

		//縦横の枠線の表示
		for(int i=1; i<MASU; i++) {
			g.setColor(Color.BLACK);
			Graphics2D g2 = (Graphics2D)g;
			BasicStroke bs = new BasicStroke(2);
			g2.setStroke(bs);
			g.drawLine(CS*i+20, 20, CS*i+20,H+20);
			g.drawLine(20, CS*i+20, W+20, CS*i+20);
		}

		//外枠の表示
		g.setColor(Color.BLACK);
		Graphics2D g2 = (Graphics2D)g;
		BasicStroke bs = new BasicStroke(4);
		g2.setStroke(bs);
		g.drawRect(20, 20, W, H);

		//情報の表示
		Font font1 = new Font("ＭＳ Ｐゴシック",Font.PLAIN,15);
		g.setFont(font1);
		FontMetrics fm = g.getFontMetrics(font1);
		g.setColor(Color.BLACK);
		g.drawString(drawTurn(FLAG), 200, 550);
		if(result) {
			g.setColor(Color.RED);
			if(whiteCount() < blackCount()) {
				g.drawString("Winner : BLACK", 200, 580);
			}else {
				g.drawString("Winner : WHITE", 200, 580);
			}
		}
	}

	//黒石のカウント
	private int blackCount() {
		for(int i=0; i<MASU; i++) {
			for(int j=0; j<MASU; j++) {
				if(board[i][j] == WHITE) white++;
			}
		}
		return white;
	}

	//白石の数をカウント
	private int whiteCount() {
		for(int i=0; i<MASU; i++) {
			for(int j=0; j<MASU; j++) {
				if(board[i][j] == BLACK) black++;
			}
		}
		return black;
	}

	//石の合計数をカウント
	private int stoneCount() {
		for(int i=0; i<MASU; i++) {
			for(int j=0; j<MASU; j++) {
				if(board[i][j] == WHITE) white++;
				if(board[i][j] == BLACK) black++;
			}
		}
		count = white + black;
		return count;
	}

	//現在の手番
	private String drawTurn(boolean flag) {
		if(FLAG) { return "Turn : WHITE"; }
		else { return "Turn : BLACK"; }
	}

	//カーソルを表示
	private void drawCursor(Graphics g) {
		g.setColor(new Color(225,225,0));
		Graphics2D g2 = (Graphics2D)g;
		BasicStroke bs = new BasicStroke(4);
		g2.setStroke(bs);
		g.drawRect(cursor[0]*CS+20, cursor[1]*CS+20, CS, CS);
	}

	//石を表示
	private void drawStone(Graphics g) {
		for(int i=0; i<MASU; i++) {
			for(int j=0; j<MASU; j++) {
				if(board[i][j] == BLACK) {
					g.setColor(Color.BLACK);
					g.fillOval(i*CS+CS/2, j*CS+CS/2, 40, 40);
				}
				else if(board[i][j] == WHITE) {
					g.setColor(Color.WHITE);
					g.fillOval(i*CS+CS/2, j*CS+CS/2, 40, 40);
				}
			}
		}
	}

	//周りのマスの石の色をチェック
	private boolean around(int x, int y, int dirX, int dirY) {
		int STONE;
		if(FLAG) { STONE = WHITE; }
		else { STONE = BLACK; }
		x += dirX;
		y += dirY;
		if (x < 0 || x >= MASU || y < 0 || y >= MASU) return false;
		if(board[x][y] == NONE) return false;
		if(board[x][y] == STONE) return false;
		x += dirX;
		y += dirY;
		while(x>=0 && x<MASU && y>=0 && y<MASU) {
			if(board[x][y] == NONE) return false;
			if(board[x][y] == STONE) return true;
			x += dirX;
			y += dirY;
		}
		return false;
	}

	//(x,y)に石が置けるかの判定
	private boolean judge(int x, int y) {
		if(board[x][y] != NONE) return false;
		if(around(x,y,-1,1)) return true;
		if(around(x,y,0,1)) return true;
		if(around(x,y,1,1)) return true;
		if(around(x,y,-1,0)) return true;
		if(around(x,y,1,0)) return true;
		if(around(x,y,-1,-1)) return true;
		if(around(x,y,0,-1)) return true;
		if(around(x,y,1,-1)) return true;

		return false;
	}

	//石を返せるかどうかの確認
	private void preReverse(int x, int y, int dirX, int dirY) throws InterruptedException {
		int STONE = 0;
		if(FLAG) { STONE = WHITE; }
		else { STONE = BLACK; }
		x += dirX;
		y += dirY;
		while(board[x][y] != STONE) {
			board[x][y] = STONE;
			update(getGraphics());
			Thread.sleep(100);
			x += dirX;
			y += dirY;
		}
	}

	//石を返す
	private void reverse(int x, int y) throws InterruptedException {
		if(around(x,y,-1,1)) preReverse(x,y,-1,1);
		if(around(x,y,0,1)) preReverse(x,y,0,1);
		if(around(x,y,1,1)) preReverse(x,y,1,1);
		if(around(x,y,-1,0)) preReverse(x,y,-1,0);
		if(around(x,y,1,0)) preReverse(x,y,1,0);
		if(around(x,y,-1,-1)) preReverse(x,y,-1,-1);
		if(around(x,y,0,-1)) preReverse(x,y,0,-1);
		if(around(x,y,1,-1)) preReverse(x,y,1,-1);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_LEFT:
			if(cursor[0]-1 < 0) { cursor[0] = 0; }
			else if(cursor[0]-1 > MASU-1) { cursor[0] = MASU-1; }
			else { cursor[0]--; }
			break;
		case KeyEvent.VK_RIGHT:
			if(cursor[0]+1 < 0) { cursor[0] = 0; }
			else if(cursor[0]+1 > MASU-1) { cursor[0] = MASU-1; }
			else { cursor[0]++; }
			break;
		case KeyEvent.VK_UP:
			if(cursor[1]-1 < 0) { cursor[1] = 0; }
			else if(cursor[1]-1 > MASU-1) { cursor[1] = MASU-1; }
			else { cursor[1]--; }
			break;
		case KeyEvent.VK_DOWN:
			if(cursor[1]+1 < 0) { cursor[1] = 0; }
			else if(cursor[1]+1 > MASU-1) { cursor[1] = MASU-1; }
			else { cursor[1]++; }
			break;
		case KeyEvent.VK_ENTER:
			int STONE = 0;
			int x = cursor[0];
			int y = cursor[1];
			if(judge(x, y)) {
				if(FLAG) {
					STONE = WHITE;
					board[x][y] = STONE;
					try {
						reverse(x,y);
					} catch (InterruptedException e1) {
					}
					FLAG = ! FLAG;
				}else{
					STONE = BLACK;
					board[x][y] = STONE;
					try {
						reverse(x,y);
					} catch (InterruptedException e1) {
					}
					FLAG = ! FLAG;
				}
			}else {
				FLAG = ! FLAG;
			}
			if(stoneCount() == 62) result = true;
		}
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {}
}