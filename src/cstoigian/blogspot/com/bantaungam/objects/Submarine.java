/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cstoigian.blogspot.com.bantaungam.objects;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

import cstoigian.blogspot.com.bantaungam.main.Main;
import cstoigian.blogspot.com.bantaungam.utils.Consts;

/**
 *
 * @author Administrator
 */
public class Submarine {

    int x;
    int y;
    public static byte speedMove = 1;
    public static byte speedFire = 1;
    Image subImage;
    boolean isMovingLeft = false;
    boolean onFire = false;
    Shot shot;
    int clock = 0;

    public Submarine(int x, int y) {
        this.x = x;
        this.y = y;
        shot = new Shot(x + 15, y - 10, Consts.IMAGE_PATH + "rocket_up.png");
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean onFire() {
        return this.onFire;
    }

    public void setFire(boolean x) {
        this.onFire = x;
    }

    public Shot getShot() {
        return this.shot;
    }

    public void fire() {
        shot.launchUp(speedFire);
        if (shot.y < 0) {
            onFire = false;
        }
    }

    public void move() {
        clock = (int) (Math.random() * 32000);
        if (clock % 150 == 0) {
            onFire = true;
        }
        if (Math.random() < 0.05) {
            isMovingLeft = !isMovingLeft;
        }
        if (isMovingLeft) {
            x -= speedMove;
            if (x < 0) {
                x = 0;
            }
        } else {
            x += speedMove;
            if (x > Main.gameWidth - 48) {
                x = Main.gameWidth - 48;
            }
        }
    }
    
    public void show(Graphics g) {
        if (isMovingLeft) {
            subImage = new ImageIcon(Consts.IMAGE_PATH + "sub_left.png").getImage();
        } else {
            subImage = new ImageIcon(Consts.IMAGE_PATH + "sub_right.png").getImage();
        }
        g.drawImage(subImage, x, y, null);
        shot.show(g);
    }
}
