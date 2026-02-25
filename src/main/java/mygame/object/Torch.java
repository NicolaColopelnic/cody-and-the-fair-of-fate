package mygame.object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class Torch extends SuperObject {
    public Torch() {
        name = "torch";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/torch.png")));
        }catch(IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
