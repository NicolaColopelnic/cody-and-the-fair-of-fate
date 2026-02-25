package mygame.mainLogic;

import mygame.entity.Entity;
import mygame.object.Signs;

// consists of methods to check collisions between the player (or other entities)
// and different objects, entities, or tiles in the game
// based on their collision area intersecting
public class CollisionCheck {
    GamePanel gp;

    public CollisionCheck(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftX = entity.x + entity.solidArea.x;
        int entityRightX = entity.x + entity.solidArea.x + entity.solidArea.width;
        int entityTopY = entity.y + entity.solidArea.y;
        int entityBottomY = entity.y + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftX / gp.tileSize;
        int entityRightCol = entityRightX / gp.tileSize;
        int entityTopRow = entityTopY / gp.tileSize;
        int entityBottomRow = entityBottomY / gp.tileSize;

        int tileNr1, tileNr2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopY - entity.speed) / gp.tileSize;
                if (entityTopRow < 0) {
                    entity.collisionOn = true;
                    return;
                }
                tileNr1 = gp.tileM.mapTileNr[entityLeftCol][entityTopRow];
                tileNr2 = gp.tileM.mapTileNr[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNr1].collision || gp.tileM.tile[tileNr2].collision) {
                    entity.collisionOn = true;
                }
                break;

            case "down":
                entityBottomRow = (entityBottomY + entity.speed) / gp.tileSize;
                if (entityBottomRow >= gp.tileM.mapTileNr[0].length) {
                    entity.collisionOn = true;
                    return;
                }
                tileNr1 = gp.tileM.mapTileNr[entityLeftCol][entityBottomRow];
                tileNr2 = gp.tileM.mapTileNr[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNr1].collision || gp.tileM.tile[tileNr2].collision) {
                    entity.collisionOn = true;
                }
                break;

            case "left":
                entityLeftCol = (entityLeftX - entity.speed) / gp.tileSize;
                if (entityLeftCol < 0) {
                    entity.collisionOn = true;
                    return;
                }
                tileNr1 = gp.tileM.mapTileNr[entityLeftCol][entityTopRow];
                tileNr2 = gp.tileM.mapTileNr[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNr1].collision || gp.tileM.tile[tileNr2].collision) {
                    entity.collisionOn = true;
                }
                break;

            case "right":
                entityRightCol = (entityRightX + entity.speed) / gp.tileSize;
                if (entityRightCol >= gp.tileM.mapTileNr.length) {
                    entity.collisionOn = true;
                    return;
                }
                tileNr1 = gp.tileM.mapTileNr[entityRightCol][entityTopRow];
                tileNr2 = gp.tileM.mapTileNr[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNr1].collision || gp.tileM.tile[tileNr2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gp.objects.length; i++) {
            if (gp.objects[i] != null) {

                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                gp.objects[i].solidArea.x = gp.objects[i].x + gp.objects[i].solidArea.x;
                gp.objects[i].solidArea.y = gp.objects[i].y + gp.objects[i].solidArea.y;

                move(entity);

                if (entity.solidArea.intersects(gp.objects[i].solidArea)) {
                    if (gp.objects[i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.objects[i].solidArea.x = gp.objects[i].solidAreaDefaultX;
                gp.objects[i].solidArea.y = gp.objects[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    private void move(Entity entity) {
        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }
    }

    public int checkFlower(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gp.flowers.length; i++) {
            if (gp.flowers[i] != null) {

                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                gp.flowers[i].solidArea.x = gp.flowers[i].x + gp.flowers[i].solidArea.x;
                gp.flowers[i].solidArea.y = gp.flowers[i].y + gp.flowers[i].solidArea.y;

                move(entity);

                if (entity.solidArea.intersects(gp.flowers[i].solidArea)) {
                    if (gp.flowers[i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.flowers[i].solidArea.x = gp.flowers[i].solidAreaDefaultX;
                gp.flowers[i].solidArea.y = gp.flowers[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;
        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                entity.solidArea.x = entity.x + entity.solidArea.x;
                entity.solidArea.y = entity.y + entity.solidArea.y;
                target[i].solidArea.x = target[i].x + target[i].solidArea.x;
                target[i].solidArea.y = target[i].y + target[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;

                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;

                    case "down":
                        entity.solidArea.y += entity.speed;

                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }

                        break;

                    case "left":
                        entity.solidArea.x -= entity.speed;

                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;

                    case "right":
                        entity.solidArea.x += entity.speed;

                        if (entity.solidArea.intersects(target[i].solidArea)) {
                            index = i;
                            entity.collisionOn = true;
                        }
                        break;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return index;
    }

    public int checkMan(Entity entity, Entity oldMan) {
        int index = 0;

        if (oldMan != null) {
            entity.solidArea.x = entity.x + entity.solidArea.x;
            entity.solidArea.y = entity.y + entity.solidArea.y;
            oldMan.solidArea.x = oldMan.x + oldMan.solidArea.x;
            oldMan.solidArea.y = oldMan.y + oldMan.solidArea.y;

            switch (entity.direction) {
                case "up":
                    entity.solidArea.y -= entity.speed;

                    if (entity.solidArea.intersects(oldMan.solidArea)) {
                        entity.collisionOn = true;
                        index = 1;
                    }
                    break;

                case "down":
                    entity.solidArea.y += entity.speed;

                    if (entity.solidArea.intersects(oldMan.solidArea)) {
                        entity.collisionOn = true;
                        index = 1;
                    }

                    break;

                case "left":
                    entity.solidArea.x -= entity.speed;

                    if (entity.solidArea.intersects(oldMan.solidArea)) {
                        entity.collisionOn = true;
                        index = 1;
                    }
                    break;

                case "right":
                    entity.solidArea.x += entity.speed;

                    if (entity.solidArea.intersects(oldMan.solidArea)) {
                        index = 1;
                        entity.collisionOn = true;
                    }
                    break;
            }

            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            oldMan.solidArea.x = oldMan.solidAreaDefaultX;
            oldMan.solidArea.y = oldMan.solidAreaDefaultY;
        }
        return index;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        entity.solidArea.x = entity.x + entity.solidArea.x;
        entity.solidArea.y = entity.y + entity.solidArea.y;
        gp.player.solidArea.x = gp.player.x + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.y + gp.player.solidArea.y;

        move(entity);

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }

    public int checkSign(Entity entity, Signs[] signs) {
        int index = 999;

        for (int i = 0; i < signs.length; i++) {
            if (signs[i] != null) {
                entity.solidArea.x = entity.x + entity.solidAreaDefaultX;
                entity.solidArea.y = entity.y + entity.solidAreaDefaultY;

                signs[i].collisionBox.x = signs[i].x;
                signs[i].collisionBox.y = signs[i].y;

                move(entity);

                if (entity.solidArea.intersects(signs[i].collisionBox)) {
                    entity.collisionOn = true;
                    index = i;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                signs[i].collisionBox.x = signs[i].x;
                signs[i].collisionBox.y = signs[i].y;
            }
        }
        return index;
    }
}
