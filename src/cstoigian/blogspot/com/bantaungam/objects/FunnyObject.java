/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cstoigian.blogspot.com.bantaungam.objects;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

import cstoigian.blogspot.com.bantaungam.main.Main;

/**
 *
 * @author Administrator
 */
public class FunnyObject {
    int x;
    int y;
    Image funnyImage;
    int speedMove;
    
    public FunnyObject(int x, int y, String s) {
        this.x = x;
        this.y = y;
        if (Math.random() < 0.05) {
            speedMove = 1;
        } else {
            speedMove = -1;
        }
        funnyImage = new ImageIcon(s).getImage();
    }

    int clock = 0;
    public void move() {
        clock ++;
        if (clock > 32000) {
            clock = 0;
        }
        if (clock % 5 == 0) {
            x += speedMove;
            if (x <= 20 || x >= Main.gameWidth - 20) {
                speedMove = -speedMove;
            }
        }            
    }

    public void show(Graphics g) {
        g.drawImage(funnyImage, x, y, null);
    }
}
