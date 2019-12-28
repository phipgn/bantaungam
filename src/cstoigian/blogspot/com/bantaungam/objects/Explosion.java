/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cstoigian.blogspot.com.bantaungam.objects;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

import cstoigian.blogspot.com.bantaungam.utils.Consts;

/**
 *
 * @author Administrator
 */
public class Explosion {

    int x;
    int y;
    Image explosionImage;
    boolean onFire = false;
    int clock = 0;

    public Explosion() {
        explosionImage = new ImageIcon(Consts.IMAGE_PATH + "explosion.gif").getImage();
    }

    public void fire(int x, int y) {        
        this.x = x;
        this.y = y;
        onFire = true;
    }

    public void resetClock() {
        clock = 0;
    }

    public void show(Graphics g) {
        clock++;
        if (clock == 50) {
            resetClock();
            onFire = false;
        }
        if (onFire) {
            g.drawImage(explosionImage, x, y, null);
        }
    }
}
