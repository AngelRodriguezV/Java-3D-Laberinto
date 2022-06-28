/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author veneg
 */
public class Laberinto3D extends JPanel{
    //Matris del laberinto
    private int[][] mapa = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1,1},
            {1,0,0,0,1,0,1,0,1,0,0,1,1,1,0,0,1},
            {1,1,1,1,1,0,1,1,1,0,1,0,0,0,1,0,1},
            {1,0,0,0,1,0,0,0,1,0,0,0,1,1,0,0,1},
            {1,0,1,0,0,0,1,0,1,1,0,1,0,0,0,1,1},
            {1,1,1,1,0,1,1,0,0,1,0,1,0,1,0,0,1},
            {1,0,0,1,0,0,0,1,1,1,0,1,0,0,1,0,1},
            {1,0,1,1,0,0,0,0,0,0,0,0,1,0,1,0,1},
            {1,0,0,1,0,1,1,1,0,1,1,1,0,0,1,1,1},
            {1,1,0,0,0,0,1,0,0,1,0,1,1,0,0,0,1},
            {1,0,0,1,0,0,0,0,0,0,0,1,0,0,1,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,1}
    };
    
    //posicion del player en el Mapa
    int possX=1;
    int possY=1;
    //Posicion del player en el Mapa3D
    float possX3D = 0.1f;
    float possY3D = -0.1f;
    //Movimiento
    float movimiento = 0.1f;
    //Camara
    float camaraX = 0.5f;
    float camaraY = -0.4f;
    //int angulo = 0;
    
    //Configuracion del Universo
    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    //Universo
    SimpleUniverse universo;
    //Player
    BranchGroup escenaPlayer;
    TransformGroup grupoPlayer;
    Transform3D traslacionPlayer;
    Sphere player;
    
    // Apariencia del cubo
    Appearance apariencia;
    Texture textura;
    
    public Laberinto3D() {
        setLayout(new BorderLayout());
        add(canvas3D);
        
        universo = new SimpleUniverse(canvas3D);
        universo.getViewingPlatform().setNominalViewingTransform();
        
        dibujarPlayer();
        moverGirarCamara();
        
        //Acion de teclado
        addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                teclaPresionada(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {}
            
        });
        setFocusable(true);
    }
    
    public BranchGroup crearGrafoEscena() {
        BranchGroup objetoRaiz = new BranchGroup();
        
        float x=-0f;
        float y=0f;
        for (int j=0; j<mapa.length; j++) {
            for (int i=0; i<mapa[j].length; i++) {

                if (mapa[j][i] == 1) {
                    //Transladar el cubo
                    Transform3D traslacion = new Transform3D();
                    traslacion.set(new Vector3f(x,y,0));
                    TransformGroup tg1 = new TransformGroup(traslacion);

                    //ColorCube cubo1 = new ColorCube(0.05f);
                    Box cubo = new Box(0.05f,0.05f,0.05f,Box.GENERATE_TEXTURE_COORDS, new Appearance());
                    cubo.setAppearance(Box.TOP, textureCube("C:\\Users\\veneg\\Documents\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img/cube_top.png"));
                    cubo.setAppearance(Box.BOTTOM, textureCube("C:\\Users\\veneg\\Documents\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img/cube_bottom.png"));
                    cubo.setAppearance(Box.LEFT, textureCube("C:\\Users\\veneg\\Documents\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img/cube_bottom.png"));
                    cubo.setAppearance(Box.RIGHT, textureCube("C:\\Users\\veneg\\Documents\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img/cube_bottom.png"));
                    cubo.setAppearance(Box.FRONT, textureCube("C:\\Users\\veneg\\Documents\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img/cube_bottom.png"));
                    cubo.setAppearance(Box.BACK, textureCube("C:\\Users\\veneg\\Documents\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img/cube_bottom.png"));
                    tg1.addChild(cubo);

                    objetoRaiz.addChild(tg1);
                }
                x = x + 0.1f;
            }
            x = 0f;
            y = y - 0.1f;
        }
        
        return objetoRaiz;
    }
    
    
    public void dibujarPlayer() {
        universo.cleanup();
        universo = new SimpleUniverse(canvas3D);
        universo.getViewingPlatform().setNominalViewingTransform();
       
        BranchGroup escena = crearGrafoEscena();
        escena.compile();
        universo.addBranchGraph(escena);
        
        Appearance apariencia = new Appearance();
        Color3f color = new Color3f(0.0f,0.0f,1.0f);
        ColoringAttributes ca = new ColoringAttributes(color,ColoringAttributes.NICEST);
        apariencia.setColoringAttributes(ca);
        
        escenaPlayer = new BranchGroup();
        player = new Sphere(0.05f,apariencia);
        
        traslacionPlayer = new Transform3D();
        traslacionPlayer.set(new Vector3f(possX3D,possY3D,0));
        grupoPlayer = new TransformGroup(traslacionPlayer);
        grupoPlayer.addChild(player);
        escenaPlayer.addChild(grupoPlayer);
        escenaPlayer.compile();
        universo.addBranchGraph(escenaPlayer);
        moverGirarCamara();
    }
    
    public void moverGirarCamara() {
        //Traslacion de Camara
        Transform3D traslacionVista = new Transform3D();
        traslacionVista.setTranslation(new Vector3f(camaraX,camaraY,1.5f));
        //Rotacion de la camara
        Transform3D rotacion = new Transform3D();
        //rotacion.rotZ(Math.toRadians(angulo));
        rotacion.rotX(Math.toRadians(45));
        rotacion.mul(traslacionVista);
        //Añadimos la rotacion y la traslacion de la camara
        universo.getViewingPlatform().getViewPlatformTransform().setTransform(rotacion);
        universo.getViewingPlatform().getViewPlatformTransform().getTransform(traslacionVista);
    }
    
    //Para validar la casilla del mapa
    public boolean validarCasilla(int x, int y) {
        if (y >= 0 && y < mapa.length) {
            if (x >= 0 && x < mapa[y].length) {
                if (mapa[y][x] == 0)
                    return true;
            } 
        }
        return false;
    }
    
    /**
     * Metodo para cargar la textura
     * @param ruta
     * @return 
     */
    public Appearance textureCube(String ruta) {
        Appearance aparienciaCube = new Appearance();
        TextureLoader loader = new TextureLoader(ruta, this);
        ImageComponent2D imagen = loader.getImage();
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL,Texture.RGBA,imagen.getWidth(),imagen.getHeight());
        texture.setImage(0,imagen);
        texture.setEnable(true);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
        aparienciaCube.setTexture(texture);
        TextureAttributes texturaAtributo = new TextureAttributes();
        texturaAtributo.setTextureMode(TextureAttributes.MODULATE);
        aparienciaCube.setTextureAttributes(texturaAtributo);
        return aparienciaCube;
    }
    
    //Eventos de teclado
    public void teclaPresionada(KeyEvent evento) {
        
        if (evento.getKeyCode() == KeyEvent.VK_UP) {//Arriba
            if (validarCasilla(possX, possY-1)) {
                possY = possY - 1;
                possY3D = possY3D + movimiento;
                camaraY = camaraY + 0.05f;
                dibujarPlayer();
                System.out.println("Arriba");
            }
        }
        if (evento.getKeyCode() == KeyEvent.VK_DOWN) {//Abajo
            if (validarCasilla(possX, possY+1)) {
                possY = possY + 1;
                possY3D = possY3D - movimiento;
                camaraY = camaraY - 0.05f;
                dibujarPlayer();
                System.out.println("Abajo");
            }
        }
        if (evento.getKeyCode() == KeyEvent.VK_LEFT) {//Izquierda
            if (validarCasilla(possX-1, possY)) {
                possX = possX - 1;
                possX3D = possX3D - movimiento;
                camaraX = camaraX - 0.06f;
                dibujarPlayer();
                System.out.println("Izquierda");
            }
        }
        if (evento.getKeyCode() == KeyEvent.VK_RIGHT) {//Derecha
            if (validarCasilla(possX+1, possY)) {
                possX = possX + 1;
                possX3D = possX3D + movimiento;
                camaraX = camaraX + 0.06f;
                dibujarPlayer();
                System.out.println("Derecha");
            }
        }
    }
    
    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");
        JFrame ventana = new JFrame("Laberinto 3D");
        Laberinto3D panel = new Laberinto3D();
        ventana.add(panel);
        ventana.setSize(1280,1024);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        while (true) {
            try {
                Thread.sleep(10);
            }
            catch(InterruptedException ex){
            }
            panel.repaint();
        }
    } 
}
 