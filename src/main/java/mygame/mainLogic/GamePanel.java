package mygame.mainLogic;

import mygame.entity.*;
import mygame.environment.EnvironmentManager;
import mygame.environment.Lighting;
import mygame.object.Flower;
import mygame.object.Signs;
import mygame.object.SuperObject;
import mygame.object.Torch;
import mygame.pathFinder.PathFinder;
import mygame.tiles.Tile;
import mygame.tiles.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 18;
    public final int maxScreenRow = 14;
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;
    public String defaultMap = "/maps/main_map";
    int FPS = 60;

    public TileManager tileM = new TileManager(this, defaultMap);
    KeyHandler keyHandler = new KeyHandler(this);

    Sound sound = new Sound();

    public CollisionCheck collisionCheck = new CollisionCheck(this);

    public UI ui = new UI(this);

    // entities
    public Player player = new Player(this, keyHandler);
    public Entity badGuy;
    public boolean fightMode = false;
    public boolean winFight = false;

    public boolean showDialogue1 = true;
    public boolean showDialogue2 = false;
    public boolean showDialogue3 = true;

    // objects
    public SuperObject[] objects = new SuperObject[2];
    public Entity[] npcs = new Entity[12];
    public Signs[] signs = new Signs[6];
    public Entity oldMan = new OldMan(this);
    public Entity sierra = new Npc(this, 8, 100, 100);

    // flower things:
    public Flower[] flowers = new Flower[300];
    public int flowerCount = 0;
    public boolean startFlowers = false;
    private long lastSpawnTime = System.currentTimeMillis();
    private final Random random = new Random();
    private int currentFlowerCount = 0;
    public boolean collectedFlowers = false;

    // maze/torch things:
    public boolean torch1PickedUp = false;
    public boolean torch2PickedUp = false;
    public boolean lightingActive = false;

    public PathFinder pFinder = new PathFinder(this);

    public boolean changeScene = true;

    public EnvironmentManager eManager = new EnvironmentManager(this);

    Thread gameThread;

    //game states:
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueStateMan = 3;
    public final int dialogueStateNpc = 4;
    public final int dialogueStateSierra = 5;
    public final int finalScreenState = 6;

    public int currentBg = 0; // current background

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    // setting up the initial state of the game
    public void setUpGame() {
        gameState = titleState;
        playMusic(0);
        eManager.setup();
    }

    public void startGame() {
        gameState = playState;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = (double) 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            update();
            repaint();

            // if the loop is running slower than expected, the game skips to the next frame
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0)
                    remainingTime = 0;
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {

        if (gameState == playState) {
            player.update();

            changeBackground();

            if(badGuy != null && currentBg == 5)
                badGuy.update();
        }
    }

    public void changeBackground() {
        switch (currentBg) {
            case 0:
                changeToMain();
                break;
            case 1:
                changeToSaloon();
                break;
            case 2:
                changeToMaze();
                break;
            case 3:
                changeToField();
                break;
            case 4:
                changeToFair();
                break;
            case 5:
                changeToBattleField();
                break;
        }
    }

    public void changeToMain() {
        startFlowers = false;
        showDialogue1 = true;
        currentFlowerCount = 0;
        flowerCount = 0;

        tileM.loadMap("/maps/main_map");

        int playerTileCol = player.x / tileSize;
        int playerTileRow = player.y / tileSize;

        npcs[0] = new Npc(this, 3, 10, 2);
        npcs[1] = new Npc(this, 2, 3, 11);

        // change to saloon
        if (playerTileCol == 0 && playerTileRow == 5 || playerTileCol == 0 && playerTileRow == 6 || playerTileCol == 0 && playerTileRow == 7) {
            currentBg = 1;
            player.x = 14 * tileSize;
            player.y = 7 * tileSize;
            clearElements();
            signs[0] = null;
            signs[1] = null;
            signs[2] = null;
        }

        // change to maze
        else if (playerTileCol == 17 && playerTileRow == 2 || playerTileCol == 17 && playerTileRow == 3 || playerTileCol == 17 && playerTileRow == 1) {
            currentBg = 2;
            player.x = 2 * tileSize;
            player.y = 3 * tileSize;
            clearElements();
            signs[0] = null;
            signs[1] = null;
            signs[2] = null;
        }

        // change to town fair
        else if (playerTileCol == 12 && playerTileRow == 12 || playerTileCol == 11 && playerTileRow == 12 || playerTileCol == 13 && playerTileRow == 12) {
            currentBg = 4;
            player.x = 11 * tileSize;
            player.y = 3 * tileSize;
            clearElements();
            signs[0] = null;
            signs[1] = null;
            signs[2] = null;
        }
    }

    public void changeToSaloon() {
        tileM.loadMap("/maps/saloon");
        oldMan.setPosition(9, 2);

        objects[0] = new Torch();
        objects[0].x = 16 * tileSize;
        objects[0].y = tileSize;
        if (torch1PickedUp) objects[0] = null;

        objects[1] = new Torch();
        objects[1].x = tileSize;
        objects[1].y = 12 * tileSize;
        if (torch2PickedUp) objects[1] = null;

        npcs[2] = new Npc(this, 2, 3, 2);
        npcs[3] = new Npc(this, 1, 2, 10);
        npcs[4] = new Npc(this, 4, 11, 11);

        int playerTileCol = player.x / tileSize;
        int playerTileRow = player.y / tileSize;

        // change to main
        if ((playerTileCol == 17 && playerTileRow == 7) || (playerTileCol == 17 && playerTileRow == 6) || (playerTileCol == 17 && playerTileRow == 5)) {
            currentBg = 0;
            player.x = 3 * tileSize;
            player.y = 7 * tileSize;
            clearElements();
        }
    }

    public void changeToMaze() {
        tileM.loadMap("/maps/maze");
        if (torch1PickedUp || torch2PickedUp) {
            activateLighting();
        }
        int playerTileCol = player.x / tileSize;
        int playerTileRow = player.y / tileSize;

        //change to field
        if (playerTileCol == 14 && playerTileRow == 12 || playerTileCol == 15 && playerTileRow == 12) {
            currentBg = 3;
            player.x = 15 * tileSize;
            player.y = 3 * tileSize;
            clearElements();
            lightingActive = false;
        }

        //change to main
        if (playerTileCol == 0 && playerTileRow == 3 || playerTileCol == 0 && playerTileRow == 2 || playerTileCol == 0 && playerTileRow == 1) {
            currentBg = 0;
            player.x = 12 * tileSize;
            player.y = 3 * tileSize;
            clearElements();
            lightingActive = false;
        }
    }

    public void changeToField() {
        tileM.loadMap("/maps/field");

        if (startFlowers) {
            long spawnInterval = 3000;
            int maxFlowers = 20;
            if (currentFlowerCount < maxFlowers && System.currentTimeMillis() - lastSpawnTime >= spawnInterval) {
                spawnFlower();
                currentFlowerCount++;
                lastSpawnTime = System.currentTimeMillis();
            }

            for (int i = 0; i < flowers.length; i++) {
                if (flowers[i] != null && flowers[i].isExpired()) {
                    flowers[i] = null;
                }
            }
        }

        if (flowerCount == 10) {
            currentBg = 0;
            clearElements();
            player.x = 10 * tileSize;
            player.y = 8 * tileSize;
            ui.showMessage("You did it!");
            currentFlowerCount = 20;
            collectedFlowers = true;
        }

        if (currentFlowerCount == 20 && flowerCount < 10) {
            currentBg = 0;
            clearElements();
            player.x = 10 * tileSize;
            player.y = 8 * tileSize;
            ui.showMessage("Try again!");
        }
    }

    public void changeToFair() {

        tileM.loadMap("/maps/town_fair");

        if (fightMode && currentBg == 4) {
            fightMode = false;
            badGuy = null;
        }

        int playerTileCol = player.x / tileSize;
        int playerTileRow = player.y / tileSize;

        npcs[5] = new Npc(this, 3, 3, 1);
        npcs[6] = new Npc(this, 0, 2, 2);
        npcs[7] = new Npc(this, 4, 15, 5);
        npcs[11] = new Npc(this, 8, 5, 9);

        if(!winFight && collectedFlowers) {
            npcs[8] = new Npc(this, 5, 4, 5);
            npcs[9] = new Npc(this, 6, 6, 5);
            npcs[10] = new Npc(this, 7, 5, 6);
        }

        //change to main
        if (playerTileCol == 11 && playerTileRow == 0 || playerTileCol == 12 && playerTileRow == 0 || playerTileCol == 10 && playerTileRow == 0) {
            currentBg = 0;
            player.x = 13 * tileSize;
            player.y = 10 * tileSize;
            clearElements();
            signs[3] = null;
            signs[4] = null;
            signs[5] = null;
        }
    }

    public void changeToBattleField() {

        tileM.loadMap("/maps/battle_field");
        changeScene = false;

        if(!fightMode) {
            clearElements();
            badGuy = new BadGuy(this);
            badGuy.setPosition(5, 6);
            fightMode = true;
        }

        if(badGuy != null && badGuy.life <= 0) {
            winFight = true;
            fightMode = false;
            badGuy = null;
            currentBg = 4;
            clearElements();
            player.x = 9 * tileSize;
            player.y = 9 * tileSize;
        }

        if(player.life <= 0) {
            currentBg = 4;
            clearElements();
            player.x = 9 * tileSize;
            player.y = 9 * tileSize;
            player.life = 6;
            badGuy = null;
            ui.showMessage("Try again!");
            fightMode = false;
        }

        if(badGuy != null) {
            badGuy.onPath = true;
        }
    }

    public void spawnFlower() {
        for (int i = 0; i < flowers.length; i++) {
            if (flowers[i] == null) {
                boolean validPosition = false;
                while (!validPosition) {

                    int randX = random.nextInt(screenWidth);
                    int randY = random.nextInt(screenHeight);

                    int tileCol = randX / tileSize;
                    int tileRow = randY / tileSize;

                    int tileNum = tileM.mapTileNr[tileCol][tileRow];
                    Tile currentTile = tileM.tile[tileNum];

                    if (currentTile != null && !currentTile.collision) {
                        flowers[i] = new Flower();
                        flowers[i].x = randX;
                        flowers[i].y = randY;
                        validPosition = true;
                    }
                }
                break;
            }
        }
    }

    public void paintMain(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        signs[0] = new Signs("/objects/saloon.png", 1, 3, 2, 2, tileSize, true);
        signs[1] = new Signs("/objects/maze.png", 14, 4, 2, 2, tileSize, true);
        signs[2] = new Signs("/objects/fair.png", 9, 10, 2, 2, tileSize, true);

        for(int i = 0; i <= 2; i++) {
            if (signs[i] != null)
                signs[i].draw(g2);
        }

        for(int i = 0; i <= 1; i++) {
            if (npcs[i] != null)
                npcs[i].drawStaticNpc(g2);
        }
    }

    public void paintSaloon(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for(int i = 0; i <= 1; i++) {
            if (objects[i] != null)
                objects[i].draw(g2, this);
        }

        oldMan.drawOldMan(g2);

        for(int i = 2; i <= 4; i++) {
            if (npcs[i] != null)
                npcs[i].drawStaticNpc(g2);
        }

    }

    public void paintField(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (startFlowers) {
            for (int i = 0; i < flowers.length; i++) {
                if (flowers[i] != null) {
                    flowers[i].draw(g2, this);
                }
            }
        }
    }

    public void paintFair(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for(int i = 5; i <= 11; i++) {
            if(npcs[i] != null)
                npcs[i].drawStaticNpc(g2);
        }

        signs[3] = new Signs("/objects/darts_sign.png", 0, 6, 1, 1, tileSize, true); // 3x2 tiles
        signs[4] = new Signs("/objects/food_sign.png", 12, 11, 2, 2, tileSize, true); // 3x2 tiles
        signs[5] = new Signs("/objects/ring_toss_sign.png", 16, 2, 1, 1, tileSize, true); // 3x2 tiles

        for(int i = 3; i <= 5; i++) {
            if(signs[i] != null)
                signs[i].draw(g2);
        }

        if(badGuy != null)
            badGuy.drawStaticBadGuy(g2);

    }

    public void paintBattleField(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(badGuy != null)
            badGuy.draw(g2);
    }

    // clearing the objects/entities when moving to a different scene
    public void clearElements() {
        for(int i = 0; i < objects.length; i++) {
                objects[i] = null;
        }
        for(int i = 0; i < npcs.length; i++) {
                npcs[i] = null;
        }
        for(int i = 0; i < signs.length; i++) {
                signs[i] = null;
        }
        oldMan.setPosition(100, 100);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        tileM.draw(g2);

        if(gameState != titleState && gameState != finalScreenState) {

            g2.setColor(getBackground());
            g2.fillRect(0, 0, screenWidth, screenHeight);

            tileM.draw(g2);

            switch (currentBg) {
                case 0:
                    paintMain(g2);
                    break;
                case 1:
                    paintSaloon(g2);
                    break;
                case 2:
                    break;
                case 3:
                    paintField(g2);
                    break;
                case 4:
                    paintFair(g2);
                    break;
                case 5:
                    paintBattleField(g2);
                    break;
            }
            player.draw(g2);
        }
        eManager.draw(g2);
        ui.draw(g2);
        g2.dispose();
    }

    // lighting effect for maze scene
    public void activateLighting() {
        if (currentBg == 2 && (torch1PickedUp || torch2PickedUp)) {
            lightingActive = true;
            eManager.lighting = new Lighting(this, 100);
        }
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }
}