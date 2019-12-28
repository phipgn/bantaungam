/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cstoigian.blogspot.com.bantaungam.objects;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrator
 */
public class Shot {
    int x, y;
    Image shotImage;

    public Shot (int x, int y, String s) {
        this.x = x;
        this.y = y;
        shotImage = new ImageIcon(s).getImage();
    }

    public void launchDown(int speed) {
        y += speed;
    }

    public void launchUp(int speed) {
        y -= speed;
    }

    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void show(Graphics g) {
        g.drawImage(shotImage, x, y, null);
    }
}
