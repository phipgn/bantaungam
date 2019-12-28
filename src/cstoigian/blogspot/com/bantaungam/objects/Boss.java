/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cstoigian.blogspot.com.bantaungam.objects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

import cstoigian.blogspot.com.bantaungam.main.Main;
import cstoigian.blogspot.com.bantaungam.utils.Consts;

/**
 *
 * @author Administrator
 */
public class Boss {

    int x;
    int y;
    int _x = 40;
    int _y = -40;
    int life = 50;
    Image bossImage;
    final byte speedMove = 5;
    public final byte speedFire = 5;
    public boolean isMovingLeft = false;
    public boolean isMovingUp = false;
    final byte maxShot = 3;
    Shot[] shot = new Shot[maxShot];

    public Boss(int x, int y) {
        this.x = x;
        this.y = y;
        bossImage = new ImageIcon(Consts.IMAGE_PATH + "boss.png").getImage();        
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void getHit() {
        this.life--;
    }

    public void setNullShot(int index) {
        shot[index] = null;
    }

    public void createShot(int index) {
        shot[index] = new Shot(x + _x, y + _y, Consts.IMAGE_PATH + "rocket_boss.png");
    }

    public Shot getShot(int index) {
        return shot[index];
    }

    public byte getMaxShot() {
        return this.maxShot;
    }

    public int getLife() {
        return this.life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void move() {
        if (Math.random() < 0.05) {
            isMovingLeft = !isMovingLeft;
        } else {
            isMovingUp = !isMovingUp;
        }

        if (isMovingLeft) {
            x -= speedMove;
            if (x < 0) {
                x = 0;
            }
        } else {
            x += speedMove;
            if (x > Main.gameWidth - 128) {
                x = Main.gameWidth - 128;
            }
        }

        if (isMovingUp) {
            y -= speedMove;
            if (y < 200) {
                y = 200;
            }
        } else {
            y += speedMove;
            if (y > Main.gameHeight - 170) {
                y = Main.gameHeight - 170;
            }
        }
    }

    public void showLifeStatus(Graphics g) {
        int xBar = x - 80;
        int yBar = y + 130;
        g.setColor(Color.black);
        g.drawRect(xBar, yBar, life * 3 + (life - 1) * 2, 7);
        for (int i = 0; i <= life; i++) {
            g.setColor(Color.white);
            g.fillRect(xBar, yBar, 3, 7);
            xBar += 5;
        }
        g.setFont(new Font("Courier New", Font.PLAIN, 12));
        g.drawString("" + life, xBar + 2, yBar + 7);
    }

    public void show(Graphics g) {
        g.drawImage(bossImage, x, y, null);
        for (int i = 0; i < maxShot; i++) {
            if (shot[i] != null) {
                shot[i].show(g);
            }
        }
    }
}
