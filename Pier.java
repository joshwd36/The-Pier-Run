import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.util.Random;
import java.lang.Math;
import java.awt.Font;
import java.util.ArrayList;

public class Pier extends Sprite {
    private final float GRAVITY = 9f;
    public static final int WIDTH = 1400;
    public static final int HEIGHT = 800;
    public static final int XPOS = WIDTH/10;
    public static final int YPOS = HEIGHT/2-(int)HEIGHT/8;
    public static final float RATIO = 63/58;
    private int dy;
    private int x;
    private int y;
    private boolean dead = false;
    private ArrayList<Golf> golfs;
    private Image image1, image2, tourist, clubs;
    
    public Pier(int x, int y) {
        super(x,y);
        initPier();
    }

    private void initPier() {
		golfs = new ArrayList<>();
		ImageIcon i1 = new ImageIcon("main_small.png");
		image1 = i1.getImage().getScaledInstance((int)HEIGHT/8, (int)RATIO*HEIGHT/8, Image.SCALE_DEFAULT);
		ImageIcon i2 = new ImageIcon("run.png");
		image2 = i2.getImage().getScaledInstance((int)HEIGHT/8, (int)RATIO*HEIGHT/8, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon("tourist.png");
		tourist = i3.getImage().getScaledInstance((int)HEIGHT/6, (int)(HEIGHT*(883/549)/6), Image.SCALE_DEFAULT);
		ImageIcon i4 = new ImageIcon("golf.png");
		clubs = i4.getImage().getScaledInstance((int)HEIGHT/6, (int)(HEIGHT*(599/569)/8), Image.SCALE_DEFAULT);
		golfs.add(new Golf(100000, 100000));
		x = XPOS;
		y = YPOS;
    }


    public void move() {
    	if (y == 260) y += dy;
    }

    public ArrayList<Golf> getGolfs() {
    	return golfs;
    }


    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
    private int[] obx = {
        WIDTH+400, WIDTH+1200, WIDTH+2000, WIDTH+2800, WIDTH+3600
    };
    private int[] golfClubs = { 
        WIDTH+800, WIDTH+1600, WIDTH+2400, WIDTH+3200, WIDTH+4000
    };
    private boolean cDetect() {
    	for (int i = 0; i < 5; i++) {
    		if (obx[i] - this.x < HEIGHT/8 && obx[i] - this.x > -35 && score > 50) return true;
    		if (golfClubs[i] - this.x < HEIGHT/8 && golfClubs[i] - this.x > -35 && YPOS - this.y < 10  && score > 50) return true;
    	}
    	for (Golf g : golfs) {
    		for (int j = 0; j < 5; j++) {
    			if (Math.abs(g.getX() - obx[j]) < 5) {
    				g.setX(WIDTH + 10);
    				obx[j] = - 50;
    			}
    		}
    	}
    	return false;
    }
    private int score = 0;
    public void drawOn(Graphics2D g2d) {
    	if (!dead) {
    		score++;
    		
    		y += GRAVITY;
    		y += dy;
        
    		if(y <= 140) {
    			dy = 0;
    		}
    		/*for (int i = 0; i < 5; i++) {
    			obx[i] -= 8;
    			while (obx[i] < 0) {
    				obx[i] += WIDTH + randInt(0, 3*WIDTH);
    				for (int j = 0; j < 5; j++) {
    					while (Math.abs(obx[i] - obx[j]) < 200 && i != j) obx[i] += 350;
    				}
    			}
    		}*/
    		for (int i = 0; i < 5; i++) {
    			obx[i] -= 8;
    			while (obx[i] < 0) {
    				obx[i] += WIDTH + randInt(0, 3*WIDTH);
    				for (int j = 0; j < 5; j++) {
    					while (Math.abs(obx[i] - obx[j]) < 200 && i != j) obx[i] += 350;
    				}
    			}
    		}
    		for (int i = 0; i < 5; i++) {
    			golfClubs[i] -= 8;
    			while (golfClubs[i] < 0) {
    				golfClubs[i] += WIDTH + randInt(0, 3*WIDTH);
    				for (int j = 0; j < 5; j++) {
    					while (Math.abs(golfClubs[i] - golfClubs[j]) < 200 && i != j) golfClubs[i] += 350;
    				}
    			}
    		}
    		
        	for (int i = 0; i < 5; i++) {
        		//if (obx[i] < WIDTH) g2d.drawRect(obx[i], HEIGHT/2-50, 50, 50);
        		if (obx[i] < WIDTH) g2d.drawImage(this.tourist, obx[i], HEIGHT/2 - HEIGHT/6, null);
        		if (golfClubs[i] < WIDTH) g2d.drawImage(this.clubs, golfClubs[i], HEIGHT/2 - HEIGHT/8, null);
        	}
        	g2d.drawLine(0, HEIGHT/2, WIDTH, HEIGHT/2);
        	y = Math.min(y,YPOS);
        	
        	g2d.drawString("Score: " + score, 10, 10);
        	g2d.drawImage((((score%14) < 7)?this.image1:this.image2), this.x, this.y, null);
        	dead = cDetect();
        } else
			gameOver(g2d, score);
    }

    private void gameOver(Graphics2D g, long score) {
		Font gameOverFont = new Font("Sans-Serif",1,70);
		g.setFont(gameOverFont);
		g.drawString("GAME OVER", 150, 100);
		Font scoreFont = new Font("Sans-Serif",1,40);
		g.setFont(scoreFont);
		score-=1;
		g.drawString("Your score was: "+score, 160, 200);
		Font optionsFont = new Font("Sans-Serif",1,20);
		g.setFont(optionsFont);
		g.drawString("Do you want to play again? Y/N", 210, 270);

	}


    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
          fire();
        }

        if (key == KeyEvent.VK_UP) {
        	if (y == YPOS) dy = -HEIGHT/20;
        }
        char character = e.getKeyChar();
        if ((character=='y'||character=='Y')&&dead) {
        	score = 0;
        	dead = false;
        	y = 100;
        }
        if ((character=='n'||character=='N')&&dead) {
        		System.exit(0);
        }

    }
    public void keyReleased(KeyEvent e) {
        dy = 0;
    }
    public void fire() {
      golfs.add(new Golf(x + width, y + height / 2));
  }
}
