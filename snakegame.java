import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

public class snakegame extends JPanel implements java.awt.event.ActionListener, KeyListener {
    private class Tile{
        int x;
        int y;
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
 int boardWidth;
 int boardHeight;
 int tileSize = 25;
//snake
 Tile snakeHead;
 ArrayList<Tile> snakeBody;
//food
 Tile food;
 Random random;

 Timer gameLoop;
 int velocityX;
 int velocityY;
 boolean gameOver;

 snakegame(int boardWidth, int boardHeight) {
     this.boardWidth = boardWidth;
     this.boardHeight = boardHeight;
     setPreferredSize(new Dimension(boardWidth, boardHeight));
     setBackground(Color.BLACK);
     setFocusable(true);
     addKeyListener(this);
     setFocusable(true);
//snake
     snakeHead = new Tile(5, 5);
     snakeBody = new ArrayList<Tile>();
//food
     food = new Tile(10,10);
     random = new Random();
     placeFood();

     velocityX = 0;
     velocityY = 0;
     gameOver = false;

    gameLoop = new Timer(100, this); 
    gameLoop.start();
 }

 public void paintComponent(Graphics g) {
     super.paintComponent(g);
     draw(g);
 }

 public void draw(Graphics g) {
/*for(int i=0;i<boardWidth/tileSize;i++){
    g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
    g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
}*/

        g.setColor(Color.RED);
        //g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);
        

     g.setColor(Color.green);
     //g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
     g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize,true);


     for(int i=0;i<snakeBody.size();i++){
         Tile snakePart = snakeBody.get(i);
         //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
         g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize,true);
     }

     g.setFont(new Font("Arial", Font.BOLD, 16));
     if(gameOver){
            g.setColor(Color.RED);
            g.drawString("Game Over!" + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
     }else{
        g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
     }
 }
    public void placeFood() {
        int x = random.nextInt(boardWidth / tileSize);
        int y = random.nextInt(boardHeight / tileSize);
        food.x = x;
        food.y = y;
    }
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        for(int i=snakeBody.size()-1;i>=0;i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        if(gameOver) return;
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize || snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
            JOptionPane.showMessageDialog(this, "Game Over! You hit the wall.");
        }
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
                JOptionPane.showMessageDialog(this, "Game Over! You hit yourself.");
            }
        }

        if(snakeHead.x*tileSize<0 || snakeHead.x*tileSize>boardWidth ||
           snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHeight){
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver) return;

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (key == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (key == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (key == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
