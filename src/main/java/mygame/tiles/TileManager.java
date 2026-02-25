package mygame.tiles;

import mygame.mainLogic.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNr;

    public TileManager(GamePanel gp, String filePath) {
        this.gp = gp;
        tile = new Tile[100];
        mapTileNr = new int [gp.maxScreenCol][gp.maxScreenRow];
        getTileImage();
        loadMap(filePath);
    }

    public void getTileImage() {
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/arrow_up.png")));
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dirt.png")));
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/cowboy_grass_table.png")));
            tile[2].collision = true;
            tile[3] = new Tile();
            tile[3].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/arrow_down.png")));
            tile[4] = new Tile();
            tile[4].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/arrow_left.png")));
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/arrow_right.png")));
            tile[6] = new Tile();
            tile[6].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/cowboy_grass.png")));
            tile[7] = new Tile();
            tile[7].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/entrance.png")));
            tile[8] = new Tile();
            tile[8].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/margin.png")));
            tile[8].collision = true;
            tile[9] = new Tile();
            tile[9].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/path.png")));
            tile[10] = new Tile();
            tile[10].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/path_margin.png")));
            tile[11] = new Tile();
            tile[11].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/bar_1.png")));
            tile[11].collision = true;
            tile[12] = new Tile();
            tile[12].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/bar_2.png")));
            tile[12].collision = true;
            tile[13] = new Tile();
            tile[13].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/bar_33.png")));
            tile[13].collision = true;
            tile[14] = new Tile();
            tile[14].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/chair.png")));
            tile[14].collision = true;
            tile[15] = new Tile();
            tile[15].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/parchet.png")));
            tile[16] = new Tile();
            tile[16].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/table.png")));
            tile[16].collision = true;
            tile[17] = new Tile();
            tile[17].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/parchet_margine.png")));
            tile[17].collision = true;
            tile[18] = new Tile();
            tile[18].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/bar_11_v.png")));
            tile[18].collision = true;
            tile[19] = new Tile();
            tile[19].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/bar_22_v.png")));
            tile[19].collision = true;
            tile[20] = new Tile();
            tile[20].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/bar_33_v.png")));
            tile[20].collision = true;
            tile[21] = new Tile();
            tile[21].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/down_fence.png")));
            tile[21].collision = true;
            tile[22] = new Tile();
            tile[22].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/up_fence.png")));
            tile[22].collision = true;
            tile[23] = new Tile();
            tile[23].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/right_fence.png")));
            tile[23].collision = true;
            tile[24] = new Tile();
            tile[24].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/left_fence.png")));
            tile[24].collision = true;
            tile[25] = new Tile();
            tile[25].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/grass.png")));
            tile[26] = new Tile();
            tile[26].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/tree.png")));
            tile[26].collision = true;
            tile[27] = new Tile();
            tile[27].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/left_up_fence.png")));
            tile[27].collision = true;
            tile[28] = new Tile();
            tile[28].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/left_down_fence.png")));
            tile[28].collision = true;
            tile[29] = new Tile();
            tile[29].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/right_up_fence.png")));
            tile[29].collision = true;
            tile[30] = new Tile();
            tile[30].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/right_down_fence.png")));
            tile[30].collision = true;
            tile[31] = new Tile();
            tile[31].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/maze_size.png")));
            tile[32] = new Tile();
            tile[32].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/maze_up_down.png")));
            tile[33] = new Tile();
            tile[33].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/maze_wall.png")));
            tile[33].collision = true;
            tile[34] = new Tile();
            tile[34].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/left_down.png")));
            tile[34].collision = true;
            tile[35] = new Tile();
            tile[35].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/left_middle.png")));
            tile[35].collision = true;
            tile[36] = new Tile();
            tile[36].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/left_up.png")));
            tile[36].collision = true;
            tile[37] = new Tile();
            tile[37].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/right_down.png")));
            tile[37].collision = true;
            tile[38] = new Tile();
            tile[38].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/right_middle.png")));
            tile[38].collision = true;
            tile[39] = new Tile();
            tile[39].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/right_up.png")));
            tile[39].collision = true;
            tile[40] = new Tile();
            tile[40].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/down_right.png")));
            tile[40].collision = true;
            tile[41] = new Tile();
            tile[41].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/down_middle.png")));
            tile[41].collision = true;
            tile[42] = new Tile();
            tile[42].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/down_left.png")));
            tile[42].collision = true;
            tile[43] = new Tile();
            tile[43].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/cowboy_grass_chair.png")));
            tile[43].collision = true;
            tile[44] = new Tile();
            tile[44].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dirt_chair.png")));
            tile[44].collision = true;
            tile[45] = new Tile();
            tile[45].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/left_up_fence_dirt.png")));
            tile[45].collision = true;
            tile[46] = new Tile();
            tile[46].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/left_down_fence_dirt.png")));
            tile[46].collision = true;
            tile[47] = new Tile();
            tile[47].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/up_right_fence_dirt.png")));
            tile[47].collision = true;
            tile[48] = new Tile();
            tile[48].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/right_down_fence_dirt.png")));
            tile[48].collision = true;
            tile[49] = new Tile();
            tile[49].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/down_fence_dirt.png")));
            tile[49].collision = true;
            tile[50] = new Tile();
            tile[50].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/up_fence_dirt.png")));
            tile[50].collision = true;
            tile[51] = new Tile();
            tile[51].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/right_fence_dirt.png")));
            tile[51].collision = true;
            tile[52] = new Tile();
            tile[52].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/left_fence_dirt.png")));
            tile[52].collision = true;
            tile[53] = new Tile();
            tile[53].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/up_middle.png")));
            tile[53].collision = true;
            tile[54] = new Tile();
            tile[54].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/up_left.png")));
            tile[54].collision = true;
            tile[55] = new Tile();
            tile[55].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/up_right.png")));
            tile[55].collision = true;
            tile[56] = new Tile();
            tile[56].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/bar.png")));
            tile[56].collision = true;

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    // load each background from the matrix in the text file
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int row = 0;

            String line;
            while((line = br.readLine()) != null) {
                String[] numbers = line.split(" ");
                for (int i = 0; i < numbers.length; i++) {
                    int tileNum = Integer.parseInt(numbers[i]);
                    mapTileNr[i][row] = tileNum;
                }
                row++;
            }
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(row < gp.maxScreenRow) {
            while(col < gp.maxScreenCol) {
                int tileNum = mapTileNr[col][row];
                g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null); // Draw the tile
                col++;
                x += gp.tileSize;
            }
            col = 0;
            x = 0;
            row++;
            y += gp.tileSize;
        }
    }
}
