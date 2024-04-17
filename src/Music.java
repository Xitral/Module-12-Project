import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Music {
    private URL url;
    private Clip clip;

    public Music(String url) {
        this.url = this.getClass().getClassLoader().getResource(url);
    }

    public void playContinuously() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(this.url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        clip.stop();
    }
}