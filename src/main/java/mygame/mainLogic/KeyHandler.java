package mygame.mainLogic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gp;
    public boolean upPressed;
    public boolean downPressed;
    public boolean leftPressed;
    public boolean rightPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(gp.gameState == gp.titleState) {
            // the ">" controller
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0)
                    gp.ui.commandNum = 1;
            }

            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1)
                    gp.ui.commandNum = 0;
            }

            if (code == KeyEvent.VK_ENTER) {
                if (gp.ui.commandNum == 0) {
                    gp.gameState = gp.playState;
                    gp.startGame();
                } else if (gp.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
        }

        else if(gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_A) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_S) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_D) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_P) {
                gp.gameState = gp.pauseState;
            }
            if(code == KeyEvent.VK_SPACE) {
                gp.player.attacking = true;
            }

            if(gp.currentBg == 2 && !gp.torch1PickedUp && !gp.torch2PickedUp) {
                if (code == KeyEvent.VK_A) {
                    gp.currentBg = 0;
                    gp.player.x = 12 * gp.tileSize;
                    gp.player.y = 3 * gp.tileSize;
                }
            }

            if(gp.currentBg == 3) {
                if(code == KeyEvent.VK_ENTER) {
                    gp.showDialogue1 = false;
                    gp.startFlowers = true;
                }
            }

            if(gp.currentBg == 4) {
                if(code == KeyEvent.VK_ENTER) {
                    gp.showDialogue3 = false;
                }
            }

            if(gp.currentBg == 5) {
                if(code == KeyEvent.VK_ENTER) {
                    gp.showDialogue2 = false;
                    if(gp.winFight) {
                        gp.currentBg = 4;
                        gp.player.x = 10 * gp.tileSize;
                        gp.player.y = 8 * gp.tileSize;
                        gp.player.setDirection("down");
                    }
                }
            }

        }

        // pause state
        else if(gp.gameState == gp.pauseState) {
            if(code == KeyEvent.VK_P)
                gp.gameState = gp.playState;
        }

        // dialogue states - press enter to continue
        else if (gp.gameState == gp.dialogueStateMan) {
            if (code == KeyEvent.VK_ENTER) {
                gp.oldMan.speak();
                if(gp.oldMan.dialogueIndex <= 8)
                    gp.oldMan.dialogueIndex++;
                else {
                    gp.oldMan.dialogueIndex = 0;
                    gp.gameState = gp.playState;
                }
            }
        }

        else if (gp.gameState == gp.dialogueStateSierra) {
            if (code == KeyEvent.VK_ENTER) {
                gp.npcs[11].speak();
                if (gp.npcs[11].dialogueIndex < gp.npcs[11].dialogues.length - 1 && gp.npcs[11].dialogues[gp.npcs[11].dialogueIndex] != null) {
                    gp.npcs[11].dialogueIndex++;
                } else {
                    gp.npcs[11].dialogueIndex = 0;
                    gp.gameState = gp.finalScreenState;
                }
            }
        }

        else if (gp.gameState == gp.dialogueStateNpc) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
            }
        }

        else if(gp.gameState == gp.finalScreenState) {
            if (code == KeyEvent.VK_ENTER) {
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if(code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}
