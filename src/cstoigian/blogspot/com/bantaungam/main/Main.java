/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cstoigian.blogspot.com.bantaungam.main;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

import cstoigian.blogspot.com.bantaungam.objects.Boss;
import cstoigian.blogspot.com.bantaungam.objects.Explosion;
import cstoigian.blogspot.com.bantaungam.objects.FunnyObject;
import cstoigian.blogspot.com.bantaungam.objects.Ship;
import cstoigian.blogspot.com.bantaungam.objects.Submarine;
import cstoigian.blogspot.com.bantaungam.utils.Consts;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * 
 * @author songtoigianvn@gmail.com
 * @youtube youtube.com/c/CuocSongToiGian
 * 
 */
public class Main extends Applet implements Runnable {

	private static final long serialVersionUID = 4146087683636730557L;
	Ship ship;
	ArrayList<Submarine> sub = new ArrayList<Submarine>();
	byte maxSubmarine = 20;
	Boss boss;
	Explosion explosion;
	FunnyObject sun;
	FunnyObject palmTree;
	FunnyObject[] cloud = new FunnyObject[15];
	AudioStream auShip;
	AudioStream auSub;
	/* ----------------------------------------------- */
	public static int gameWidth = 800;
	public static int gameHeight = 600;
	int lifePlayer = 12;
	int score = 0;
	final byte scoreStep = 10;
	int niceShot = 0;
	int missedShot = 0;
	int totalShot = 0;
	int hitRate = 0;
	byte level = 1;
	final byte lastLevel = 5;
	int gameSpeed = 20;
	/* ----------------------------------------------- */
	String bg[] = { "", 
			Consts.IMAGE_PATH + "level1.png", 
			Consts.IMAGE_PATH + "level2.png", 
			Consts.IMAGE_PATH + "level3.png", 
			Consts.IMAGE_PATH + "level4.png",
			Consts.IMAGE_PATH + "level5.png" };
	Image background;
	Image statusBar;
	Image heart;
	Image bubbles;
	Image gameStartImage;
	Image gamePausedImage;
	Image gameWonImage;
	Image gameLostImage;
	Image gameLevelUpImage;
	Image gameBossImage;
	byte WarningFontSize = 14;
	/* ----------------------------------------------- */
	Image bufferImage;
	Graphics bufferGraphics;
	Thread game;
	boolean gamePaused = true;
	boolean gameStarted = false;

	@Override
	public void init() {
		setSize(gameWidth, gameHeight);
		background = new ImageIcon(bg[level]).getImage();
		statusBar = new ImageIcon(Consts.IMAGE_PATH + "status_bar.png").getImage();
		heart = new ImageIcon(Consts.IMAGE_PATH + "heart-icon.png").getImage();
		bubbles = new ImageIcon(Consts.IMAGE_PATH + "bubbles.gif").getImage();
		ship = new Ship(this.getSize().width / 2 - 96, 130);
		boss = new Boss(350, 450);
		for (int i = 0; i < maxSubmarine; i++) {
			int xSub = (int) (Math.random() * gameWidth - 48);
			int ySub = (int) (Math.random() * 300 + 230);
			sub.add(new Submarine(xSub, ySub));
		}
		sun = new FunnyObject((int) (Math.random() * 50 + 400), 50, Consts.IMAGE_PATH + "sun-lightcloud-icon-64.png");
		palmTree = new FunnyObject(750, 150, Consts.IMAGE_PATH + "palm.png");
		for (int i = 0; i < cloud.length; i++) {
			cloud[i] = new FunnyObject((int) (Math.random() * 750 + 25), (int) (Math.random() * 80 + 20),
					Consts.IMAGE_PATH + "day-lightcloud-icon-48.png");
		}
		explosion = new Explosion();
		gameStartImage = new ImageIcon(Consts.IMAGE_PATH + "game_start.png").getImage();
		gamePausedImage = new ImageIcon(Consts.IMAGE_PATH + "game_paused.png").getImage();
		gameWonImage = new ImageIcon(Consts.IMAGE_PATH + "game_won.png").getImage();
		gameLostImage = new ImageIcon(Consts.IMAGE_PATH + "game_lost.png").getImage();
		gameLevelUpImage = new ImageIcon(Consts.IMAGE_PATH + "level_up.png").getImage();
		gameBossImage = new ImageIcon(Consts.IMAGE_PATH + "game_boss.png").getImage();
	}

	@Override
	public void start() {
		game = new Thread(this);
		game.start();
	}

	@Override
	public void update(Graphics g) {
		// initialize the off-screen
		if (bufferImage == null) {
			bufferImage = createImage(this.getSize().width, this.getSize().height);
			bufferGraphics = bufferImage.getGraphics();
		}
		// clear the off-screen
		bufferGraphics.setColor(Color.white);
		bufferGraphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
		// draw game elements on off-screen
		paint(bufferGraphics);
		// put the off-screen on the main screen
		g.drawImage(bufferImage, 0, 0, this);
	}

	/* --------------------- */
	/* --- PAINT CONTROL --- */
	/* --------------------- */
	public void paintSomethingFun(Graphics g) {
		sun.show(g);
		sun.move();
		palmTree.show(g);
		for (int i = 0; i < cloud.length; i++) {
			cloud[i].show(g);
			cloud[i].move();
		}
	}

	public void paintGameStartWarning(Graphics g) {
		g.drawImage(gameStartImage, 0, 0, this);
	}

	/* paint the game pause warning */
	public void paintGamePausedWarning(Graphics g) {
		paintStatusBar(g);
		paintSomethingFun(g);
		g.drawImage(gamePausedImage, 150, 100, this);
		g.setFont(new Font("Courier New", Font.BOLD, WarningFontSize));
		g.setColor(Color.white);
		g.drawString("[PRESS SPACEBAR TO CONTINUE THE GAME]", 150, 190);
	}

	public void paintGameWonWarning(Graphics g) {
		paintStatusBar(g);
		paintSomethingFun(g);
		g.drawImage(gameWonImage, 150, 100, this);
		g.setFont(new Font("Courier New", Font.BOLD, WarningFontSize));
		g.setColor(Color.white);
		g.drawString("[PRESS SPACEBAR TO REPLAY THE GAME]", 150, 190);
	}

	public void paintGameLostWarning(Graphics g) {
		paintStatusBar(g);
		paintSomethingFun(g);
		g.drawImage(gameLostImage, 150, 100, this);
		g.setFont(new Font("Courier New", Font.BOLD, WarningFontSize));
		g.setColor(Color.white);
		g.drawString("[PRESS SPACEBAR TO TRY AGAIN]", 150, 190);
	}

	/* warning before playing a normal level */
	public void paintGameLevelUp(Graphics g) {
		paintStatusBar(g);
		paintSomethingFun(g);
		g.drawImage(gameLevelUpImage, 150, 100, this);
		g.setFont(new Font("Courier New", Font.BOLD, WarningFontSize));
		g.setColor(Color.white);
		g.drawString("[PRESS SPACEBAR TO CONTINUE]", 150, 190);
	}

	/* warning before meeting the boss */
	public void paintGameBossLevelUp(Graphics g) {
		paintStatusBar(g);
		paintSomethingFun(g);
		g.drawImage(gameBossImage, 150, 100, this);
		g.setFont(new Font("Courier New", Font.BOLD, WarningFontSize));
		g.setColor(Color.white);
		g.drawString("[PRESS SPACEBAR TO CONTINUE]", 150, 190);
	}

	public void paintStatusBar(Graphics g) {
		g.drawImage(background, 0, 0, this);
		g.drawImage(bubbles, 200, 450, this);
		g.drawImage(bubbles, 400, 490, this);
		g.drawImage(bubbles, 600, 400, this);
		/* top status bar */
		g.drawImage(statusBar, 0, 0, this);

		int xHeart = 5;
		for (int i = 0; i < lifePlayer; i++) {
			g.drawImage(heart, xHeart, 10, this);
			xHeart += 15;
		}

		g.setFont(new Font("Courier New", Font.PLAIN, 12));
		if (totalShot != 0) {
			hitRate = 100 * niceShot / totalShot;
		}
		/* bottom status bar */
		g.setColor(Color.white);
		g.drawString("LEVEL: " + level, 189, 20);
		g.setColor(Color.YELLOW);
		g.drawString("SCORE: " + score, 249, 20);
		g.setColor(Color.black);
		g.fillRect(0, gameHeight - 30, gameWidth, 30); // black background
		g.setColor(Color.white);
		g.drawString("[ + NICE SHOT: " + niceShot + "][ + MISSED SHOT: " + missedShot + "][ + TOTAL SHOT: " + totalShot
				+ "][ + HIT RATE: " + hitRate + "%][cstoigian.blogspot.com]", 80, gameHeight - 10);
	}

	public void paintGameElements(Graphics g) {
		requestFocus();
		try {
			auShip = new AudioStream(new FileInputStream(Consts.SOUND_PATH + "ship.wav"));
			auSub = new AudioStream(new FileInputStream(Consts.SOUND_PATH + "submarine.wav"));
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		paintStatusBar(g);
		explosion.show(g);
		ship.show(g);
		ship.move();
		paintSomethingFun(g);
		for (int i = 0; i < ship.getMaxShot(); i++) {
			if (ship.getShot(i) != null) {
				ship.getShot(i).launchDown(ship.getSpeedFire());
				/* hit the submarine exactly */
				for (int j = 0; j < sub.size(); j++) {
					if (ship.getShot(i).getX() >= sub.get(j).getX() && ship.getShot(i).getX() <= sub.get(j).getX() + 48
							&& ship.getShot(i).getY() >= sub.get(j).getY()
							&& ship.getShot(i).getY() <= sub.get(j).getY() + 48) {
						AudioPlayer.player.start(auShip);
						explosion.fire(sub.get(j).getX(), sub.get(j).getY());
						explosion.resetClock();
						score += scoreStep;
						niceShot++;
						ship.setNullShot(i); // free the shot
						sub.remove(j);
						break;
					}
				}
				/* the rocket has fallen off the game panel, or it has missed the submarine */
				if (ship.getShot(i) != null && ship.getShot(i).getY() > Main.gameHeight) {
					ship.setNullShot(i);
					missedShot++;
				}
			}
		}
		/* if enemies were destroyed all, then level up */
		if (sub.isEmpty()) {
			gamePaused = true;
		}
		/* move the submarines */
		for (int i = 0; i < sub.size(); i++) {
			sub.get(i).show(g);
			sub.get(i).move();
			if (sub.get(i).onFire()) {
				sub.get(i).fire();
				/* you were hit by the submarine */
				if (sub.get(i).getShot().getX() >= ship.getX() && sub.get(i).getShot().getX() <= ship.getX() + 127
						&& sub.get(i).getShot().getY() >= ship.getY()
						&& sub.get(i).getShot().getY() <= ship.getY() + 45) {
					AudioPlayer.player.start(auSub);
					explosion.fire(ship.getX(), ship.getY());
					explosion.resetClock();
					lifePlayer--;
					if (lifePlayer <= 0) {
						gamePaused = true;
					}
					sub.get(i).setFire(false); // stop the fire
				}
			} else {
				sub.get(i).getShot().reset(sub.get(i).getX() + 15, sub.get(i).getY() - 10);
			}
		}
	}

	int bossClock = 0;

	/* battle for the boss */
	public void paintGameBossElements(Graphics g) {
		requestFocus();
		try {
			auShip = new AudioStream(new FileInputStream(Consts.SOUND_PATH + "ship.wav"));
			auSub = new AudioStream(new FileInputStream(Consts.SOUND_PATH + "submarine.wav"));
		} catch (IOException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
		paintStatusBar(g);
		paintSomethingFun(g);
		ship.show(g);
		ship.move();
		boss.show(g);
		boss.move();
		boss.showLifeStatus(g);
		explosion.show(g);
		/* ship control */
		for (int i = 0; i < ship.getMaxShot(); i++) {
			if (ship.getShot(i) != null) {
				ship.getShot(i).launchDown(ship.getSpeedFire());
				/* hit the boss */
				if (ship.getShot(i).getX() >= boss.getX() && ship.getShot(i).getX() <= boss.getX() + 128
						&& ship.getShot(i).getY() >= boss.getY() && ship.getShot(i).getY() <= boss.getY() + 128) {
					AudioPlayer.player.start(auShip);
					niceShot++;
					ship.setNullShot(i);
					boss.getHit();

					if (boss.getLife() <= 0) {
						gamePaused = true;
					}
					explosion.fire(boss.getX(), boss.getY());
					explosion.resetClock();
					score += scoreStep;
				}
				if (ship.getShot(i) != null && ship.getShot(i).getY() > gameHeight) {
					missedShot++;
					ship.setNullShot(i);
				}
			}
		}

		bossClock++;
		if (bossClock > 32000) {
			bossClock = 0;
		}
		/* auto creating shot for boss after 50 * 20 = 1000ms */
		if (bossClock % 50 == 0) {
			for (int i = 0; i < boss.getMaxShot(); i++) {
				if (boss.getShot(i) == null) {
					boss.createShot(i);
					break;
				}
			}
		}

		for (int i = 0; i < boss.getMaxShot(); i++) {
			if (boss.getShot(i) != null) {
				boss.getShot(i).launchUp(boss.speedFire);
				/* hit the ship */
				if (boss.getShot(i).getX() >= ship.getX() && boss.getShot(i).getX() <= ship.getX() + 127
						&& boss.getShot(i).getY() >= ship.getY() && boss.getShot(i).getY() <= ship.getY() + 45) {
					AudioPlayer.player.start(auSub);
					boss.setNullShot(i);
					explosion.fire(ship.getX(), ship.getY());
					explosion.resetClock();
					lifePlayer--;
					if (lifePlayer <= 0) {
						gamePaused = true;
					}
				}

				if (boss.getShot(i) != null && boss.getShot(i).getY() < 0) {
					boss.setNullShot(i);
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		requestFocus();
		switch (gameStatus()) {
		case 0: { // pause game while playing
			paintGamePausedWarning(g);
		}
			break;
		case 1: { // lost the game
			paintGameLostWarning(g);
		}
			break;
		case 2: { // level up
			if (level == lastLevel - 1) {
				paintGameBossLevelUp(g);
			} else {
				paintGameLevelUp(g);
			}
		}
			break;
		case 3: { // won the game
			paintGameWonWarning(g);
		}
			break;
		case 4: { // before playing the game
			paintGameStartWarning(g);
		}
			break;
		default: {
			if (level == lastLevel) {
				paintGameBossElements(g);
			} else {
				paintGameElements(g);
			}
		}
			break;
		}
	}
	/* -------------------- */
	/* --- GAME CONTROL --- */
	/* -------------------- */

	public int gameStatus() {
		if (gamePaused == true) {
			if (lifePlayer <= 0) { // lost the game
				return 1;
			}
			if (sub.isEmpty()) { // win a level
				return 2;
			}
			if (level == lastLevel && boss.getLife() <= 0) { // win the whole fuckin game
				return 3;
			}
			if (!gameStarted) { // the game has not started
				return 4;
			}
			return 0;
		}
		return -1;
	}

	/* prepare for the normal level up */
	public void gameLevelUp() {
		sub.clear();
		ship = null;
		level++;
		maxSubmarine += 10;
		Submarine.speedMove++;
		Submarine.speedFire++;
		init();
	}

	/* reset game settings in the case you had lost the game */
	public void gameReset() {
		gameStarted = false;
		sub.clear();
		ship = null;
		level = 1;
		lifePlayer = 12;
		maxSubmarine = 20;
		Submarine.speedFire = 1;
		Submarine.speedMove = 1;
		niceShot = 0;
		totalShot = 0;
		missedShot = 0;
		hitRate = 0;
		score = 0;
		init();
	}

	/* ------------------------- */
	/* --- KEY EVENT CONTROL --- */
	/* ------------------------- */
	@Override
	public boolean keyDown(Event e, int key) {
		if (key == Event.LEFT) {
			ship.setTurnLeft(true);
		}
		if (key == Event.RIGHT) {
			ship.setTurnRight(true);
		}
		if (key == Event.DOWN) {
			for (int i = 0; i < ship.getMaxShot(); i++) {
				if (ship.getShot(i) == null) {
					ship.createShot(i);
					totalShot++;
					break;
				}
			}
		}
		if (key == 32) {
			switch (gameStatus()) {
			case 1: { // lose
				gameReset();
			}
				break;
			case 2: { // level up
				gamePaused = !gamePaused;
				gameLevelUp();
			}
				break;
			case 3: { // win
				gameReset();
			}
				break;
			case 4: { // before playing the game
				gamePaused = !gamePaused;
				gameStarted = true;
			}
				break;
			default: {
				gamePaused = !gamePaused;
			}
				break;
			}
		}
		if (key == 27) {
			System.exit(0);
		}
		return true;
	}

	@Override
	public boolean keyUp(Event e, int key) {
		if (key == Event.LEFT) {
			ship.moveLeft = false;
		}
		if (key == Event.RIGHT) {
			ship.moveRight = false;
		}
		return true;
	}

	public void run() {
		while (true) {
			try {
				repaint();
				Thread.sleep(gameSpeed);
			} catch (InterruptedException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
