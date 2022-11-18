import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener
{

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 15; 
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int DELAY = 150;
    int bodyParts = 5;
    int applesEaten;
    int appleX;
    int appleY;
    int appleX1;
    int appleY1;
    int handycapX[] = new int[GAME_UNITS];
    int handycapY[] = new int[GAME_UNITS];
    int lvl = 1;
    int direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    boolean golden = false;


    GamePanel()
    {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        StartGame();
    }

    public void StartGame()
    {
        Handycap();
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g)
    {
        if(running)
        {
            /*for(int i = 0; i < (SCREEN_HEIGHT/UNIT_SIZE); i++)
            {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }*/
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < lvl; i++)
            {
                if(i % 2 == 0)
                {
                    g.setColor(Color.GRAY);
                    g.fillRect(handycapX[i], handycapY[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            
            
            if(golden)
            {
                g.setColor(Color.YELLOW);
                g.fillOval(appleX1, appleY1, UNIT_SIZE, UNIT_SIZE);
            }

            for(int i = 0; i < bodyParts; i++)
            {
                if(i == 0)
                {
                    g.setColor(Color.BLUE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else
                {
                    g.setColor(new Color(0,0,139));
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // Score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri Bold", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + applesEaten))/2, g.getFont().getSize());
            // LVL
            g.setColor(Color.CYAN);
            g.setFont(new Font("Calibri Bold", Font.BOLD, 30));
            FontMetrics metrics0 = getFontMetrics(g.getFont());
            g.drawString("LVL: " + lvl, (metrics0.stringWidth("LVL: " + lvl))/2, g.getFont().getSize());
        }
        else
        {
            GameOver(g);
        }
        

    }

    public void newApple()
    {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        Handycap();
    }

    public void GoldenApple()
    {

        appleX1 = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY1 = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;

    }

    public void Handycap()
    {
        for(int i = 0; i < lvl; i++)
        {
            if(i % 2 == 0)
            {
                handycapX[i] = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
                handycapY[i] = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
            }
        }
    }

    public void move()
    {
        for(int i = bodyParts; i > 0; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction)
        {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkApple()
    {
        Random rnd = new Random();
        int los = rnd.nextInt(10);

        if((x[0] == appleX) && (y[0] == appleY))
        {
            bodyParts++;
            applesEaten++;
            lvl++;
            timer.setDelay(DELAY-=5);  
            newApple();
            if(!golden)
            {
                if(los == 5)
                {
                    golden = true;
                    if(golden)
                    {
                        GoldenApple();
                    }
                }
            }
            
        }

        if((x[0] == appleX1) && (y[0] == appleY1))
        {
            bodyParts+=2;
            applesEaten++;
            timer.setDelay(DELAY+=10);  
            golden = false;
        }
        
    }

    public void checkCollisions()
    {
        // checks if head colides with body 
        for(int i = bodyParts; i > 0; i--)
        {
            if((x[0] == x[i]) && (y[0] == y[i]))
            {
                running = false;
            }
        }

        // checks if head touches handycap
        for(int i = 0; i < lvl; i++)
        {
            if(i % 2 == 0)
            {
                if((x[0] == handycapX[i]) && (y[0] == handycapY[i]))
                {
                    bodyParts-=2;
                    applesEaten-=2;
                    newApple();
                    if(bodyParts == 1)
                    {
                        running = false;
                    }
                }
            }
        }

        // checks if head touches left border
        if(x[0] < 0)
        {
            running = false;
        }
        // checks if head touches righ border
        if(x[0] > SCREEN_WIDTH)
        {
            running = false;
        }
        // checks if head touches top border
        if(y[0] < 0)
        {
            running = false;
        }
        // checks if head touches bottom border
        if(y[0] > SCREEN_HEIGHT)
        {
            running = false;
        }

        if(!running)
        {
            timer.stop();

        }

    }

    public void GameOver(Graphics g)
    {
        // Score
        g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        g.setFont(new Font("Calibri Bold", Font.BOLD, 35));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("SCORE: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("SCORE: " + applesEaten))/2, g.getFont().getSize());
        // GameOver text
        g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        g.setFont(new Font("Calibri Bold", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(running)
        {
            move();
            checkApple();
            checkCollisions();

        }
        repaint();
        
    }

    public class MyKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e) 
        {
           switch(e.getKeyCode())
           {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R')
                    {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L')
                    {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D')
                    {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U')
                    {
                        direction = 'D';
                    }
                    break;

           }
        }

    }

    
}
