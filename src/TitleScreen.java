import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class TitleScreen extends JFrame {
    private final String[] colors = {"White", "Gold", "Brown", "Black"};
    private Pet gamePet;

    public TitleScreen() {
        setupFrame();
        addPetButtons();
    }

    private void setupFrame() {
        setTitle("Untitled Pet Game - Adopt a Pet!");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout());
    }

    private void addPetButtons() {
        add(createPetButton(new Dog("","")));
        add(createPetButton(new Cat("","")));
        setVisible(true);
    }

    private JButton createPetButton(Pet pet) {
        loadSprites(pet);
        JButton petButton = setupPetButton(pet);
        addAnimationToButton(pet, petButton);
        return petButton;
    }

    private void loadSprites(Pet pet) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 16; j++) {
                pet.getSprites()[i][j] = pet.getSpriteSheet().getSprite(j * 32, i * 32, 32, 32);
            }
        }
    }

    private JButton setupPetButton(Pet pet) {
        JButton petButton = new JButton("<html><div style='text-align:center; margin-top:200px; color: white; font-size: 20px; font-family: Bahnschrift;'>" + pet.getClass().getSimpleName() + "</div></html>");
        petButton.setIcon(new ImageIcon(getPetIcon(pet, 1, 1).getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
        petButton.setHorizontalTextPosition(JButton.CENTER);
        petButton.setVerticalTextPosition(JButton.CENTER);
        petButton.setFocusPainted(false);
        petButton.setBackground(Color.white);
        petButton.addActionListener(e -> createColorButtons(pet));
        petButton.setBorder(BorderFactory.createLineBorder(Color.white, 6));
        return petButton;
    }

    private void addAnimationToButton(Pet pet, JButton petButton) {
        Timer timer = new Timer(100, new ActionListener() {
            int frameIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                petButton.setIcon(new ImageIcon(pet.getSprite(0,frameIndex).getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
                frameIndex = (frameIndex + 1) % 3;
            }
        });

        petButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                petButton.setBackground(Color.white.darker());
                timer.start();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                petButton.setBackground(Color.white);
                timer.stop();
                petButton.setIcon(getPetIcon(pet, 1, 1));
            }
        });
    }

    private ImageIcon getPetIcon(Pet pet, int x, int y) {
        return new ImageIcon(
                pet.getSpriteSheet().getSprite(
                        x,
                        y,
                        32,
                        32
                ).getScaledInstance(128, 128, Image.SCALE_SMOOTH)
        );
    }

    private void createColorButtons(Pet pet) {
        getContentPane().removeAll();
        this.gamePet = pet.getClass().getSimpleName().equals("Dog") ? new Dog("","") : new Cat("","");
        for(int i = 0; i < colors.length; i++) {
            addColorButton(pet, i);
        }
        revalidate();
        repaint();
    }

    private void addColorButton(Pet pet, int i) {
        JButton colorPetButton = new JButton(getPetIcon(pet, 32*(4*i), 1));
        colorPetButton.setFocusPainted(false);
        colorPetButton.setBackground(getPetIconColor(colorPetButton.getIcon()));
        colorPetButton.setBorder(BorderFactory.createLineBorder(colorPetButton.getBackground(), 6));
        addAnimationToColorButton(pet, i, colorPetButton);
        colorPetButton.addActionListener(e -> startGame(i));
        add(colorPetButton);
    }

    private void addAnimationToColorButton(Pet pet, int i, JButton colorPetButton) {
        Timer timer = new Timer(100, new ActionListener() {
            int frameIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                colorPetButton.setIcon(new ImageIcon(pet.getSprite(0,frameIndex + 4*i).getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
                frameIndex = (frameIndex + 1) % 3;
            }
        });

        colorPetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPetButton.setBackground(colorPetButton.getBackground().darker());
                timer.start();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPetButton.setBackground(getPetIconColor(colorPetButton.getIcon()));
                timer.stop();
                colorPetButton.setIcon(getPetIcon(pet, 32*(4*i), 1));
            }
        });
    }

    private Color getPetIconColor(Icon icon) {
        Image image = ((ImageIcon) icon).getImage();
        BufferedImage buffered = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        buffered.getGraphics().drawImage(image, 0, 0 , null);
        int pixel = buffered.getRGB(image.getWidth(null)/2, image.getWidth(null)/2);
        return new Color(pixel, true);
    }

    private void startGame(int i) {
        this.gamePet.setColor(colors[i]);
        Game game = new Game(this.gamePet);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TitleScreen::new);
    }
}