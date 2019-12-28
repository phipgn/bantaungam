/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cstoigian.blogspot.com.bantaungam.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

import cstoigian.blogspot.com.bantaungam.main.Main;
import cstoigian.blogspot.com.bantaungam.utils.Consts;

/**
 *
 * @author Administrator
 */
public class Ship {

    int x;
    int y;
    int _x;
    int _y = 45;
    Image shipImage;
    final byte speedMove = 10;
    final byte speedFire = 7;
    public boolean moveLeft = false;
    public boolean moveRight = false;
    boolean onFire = false;
    final byte maxShot = 5;
    Shot[] shot = new Shot[maxShot];

    public Ship(int x, int y) {
        this.x = x;
        this.y = y;
        shipImage = new ImageIcon(Consts.IMAGE_PATH + "ship_left.png").getImage();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setNullShot(int index) {
        shot[index] = null;
    }

    public byte getMaxShot() {
        return this.maxShot;
    }

    public Shot getShot(int index) {
        return shot[index];
    }

    public void createShot(int index) {
        shot[index] = new Shot(x + 45, y + 25, Consts.IMAGE_PATH + "/Bomb-3-icon.png");
    }

    public void setImage(String s) {
        shipImage = new ImageIcon(s).getImage();
    }

    public int getSpeedFire() {
        return this.speedFire;
    }

    public void setTurnLeft(boolean x) {
        moveLeft = x;
    }

    public void setTurnRight(boolean x) {
        moveRight = x;
    }

    public void move() {
        if (moveLeft) {
            x -= speedMove;
            if (x < 0) {
                x = 0;
            }
        }
        if (moveRight) {
            x += speedMove;
            if (x > Main.gameWidth - 127) {
                x = Main.gameWidth - 127;
            }
        }
    }

    public void fire() {
        for (int i = 0; i < maxShot; i++) {
            if (shot[i] != null) {
                shot[i].launchDown(speedFire);
                if (shot[i].y > Main.gameHeight - 34) {
                    shot[i] = null;
                }
            }
        }
    }

    public void show(Graphics g) {
        if (moveLeft) {
            setImage(Consts.IMAGE_PATH + "ship_left.png");
        } else if (moveRight) {
            setImage(Consts.IMAGE_PATH + "ship_right.png");
        }
        g.setColor(Color.white);
        g.fillOval(x, y + 40, 128, 7);
        g.drawImage(shipImage, x, y, null);
        for (int i = 0; i < shot.length; i++) {
            if (shot[i] != null) {
                shot[i].show(g);
            }
        }
    }
}
