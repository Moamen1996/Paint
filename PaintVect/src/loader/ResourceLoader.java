package loader;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

final public class ResourceLoader {
  public static Image load(String path){
    InputStream input = ResourceLoader.class.getResourceAsStream(path);
    if(input == null){
      input = ResourceLoader.class.getResourceAsStream("/" + path);
    }
    try {
      return ImageIO.read(input);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
