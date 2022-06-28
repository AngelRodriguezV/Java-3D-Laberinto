
package laberinto3d;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.CapabilityNotSetException;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Locale;
import javax.media.j3d.Material;
import javax.media.j3d.RestrictedAccessException;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 *
 * @author veneg
 */
public class Laberinto extends JPanel{
    
    //Configuracion del Universo
    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    //Universo
    SimpleUniverse universo;
    Locale miLocale;
    //Player
    BranchGroup escenaPlayer;
    
    //posicion del player en el Mapa
    int possX=1;
    int possY=1;
    //Camara
    float camaraX = 1f;
    float camaraY = -1.5f;
    float camaraZ = 15f;
    int angulo = -45;
    //Posicion del player en el Mapa3D
    float possX3D = 0f;
    float possZ3D = 0f;
    //aux cordenadas
    float auxpossX3D = 2f;
    float auxpossZ3D = 2f;
    //Movimiento
    float movimiento = 2f;
    
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
    
    /**
     * 
     */
    public Laberinto() {
        setLayout(new BorderLayout());
        universo = new SimpleUniverse(canvas3D);
        universo.getViewingPlatform().setNominalViewingTransform();
        add(canvas3D);
        
        escenaPlayer = new BranchGroup();
        escenaPlayer.setCapability(escenaPlayer.ALLOW_DETACH);
        escenaPlayer.compile();
        universo.addBranchGraph(escenaPlayer);
        miLocale = universo.getLocale();
        
        dibujarFondo();
        dibujarLaberinto();
        
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
    
    /**
     * 
     */
    public void dibujarFondo() {
        BranchGroup escenaFondo = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);
        TextureLoader textureLoad = new TextureLoader("D:\\Usuarios\\veng\\Documentos\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img\\background_3.png",this);
        Background bgImage = new Background(textureLoad.getImage());
        bgImage.setApplicationBounds(bounds);
        escenaFondo.addChild(bgImage);
        escenaFondo.compile();
        universo.addBranchGraph(escenaFondo);
    }
    
    /**
     * 
     */
    public void dibujarLaberinto() {
        BranchGroup objetoRaiz = new BranchGroup();
        objetoRaiz.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        float x = 0;
        float z = 0;
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length; j++) {
                if (mapa[i][j] == 1) {
                    //Transladar el cubo
                    Transform3D traslacion = new Transform3D();
                    traslacion.set(new Vector3f(x,0,z));
                    TransformGroup tg1 = new TransformGroup(traslacion);
                    tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                    //Apariencia
                    Appearance app = new Appearance();
                    TextureAttributes atributosTex = new TextureAttributes();
                    atributosTex.setTextureMode(TextureAttributes.MODULATE);
                    app.setTextureAttributes(atributosTex);
                    
                    //Cubo
                    Box cubo = new Box(1f,1f,1f,Box.GENERATE_TEXTURE_COORDS,app);
                    cubo.setAppearance(Box.TOP, textureCube("D:\\Usuarios\\veng\\Documentos\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img\\cube_top.png"));
                    cubo.setAppearance(Box.BOTTOM, textureCube("D:\\Usuarios\\veng\\Documentos\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img\\cube_bottom.png"));
                    cubo.setAppearance(Box.LEFT, textureCube("D:\\Usuarios\\veng\\Documentos\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img\\cube_1.png"));
                    cubo.setAppearance(Box.RIGHT, textureCube("D:\\Usuarios\\veng\\Documentos\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img\\cube_1.png"));
                    cubo.setAppearance(Box.FRONT, textureCube("D:\\Usuarios\\veng\\Documentos\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img\\cube_1.png"));
                    cubo.setAppearance(Box.BACK, textureCube("D:\\Usuarios\\veng\\Documentos\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img\\cube_1.png"));
                    tg1.addChild(cubo);
                    objetoRaiz.addChild(tg1);
                }
                else{
                    //Transladar el cubo
                    Transform3D traslacion = new Transform3D();
                    traslacion.set(new Vector3f(x,-1f,z));
                    TransformGroup tg1 = new TransformGroup(traslacion);
                    tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                    //Cubo
                    Box cubo = new Box(1f,0f,1f,Box.GENERATE_TEXTURE_COORDS, new Appearance());
                    cubo.setAppearance(Box.TOP, textureCube("D:\\Usuarios\\veng\\Documentos\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img\\piso.png"));
                    tg1.addChild(cubo);
                    objetoRaiz.addChild(tg1);
                }
                x = x + 2f;
            }
            x = 0f;
            z = z + 2f;
        }
        //Iluminación
        Color3f colorAmbiente = new Color3f(Color.LIGHT_GRAY);
        AmbientLight luzAmbiente = new AmbientLight(colorAmbiente);
        luzAmbiente.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0),100));
        Color3f colorLuz = new Color3f(Color.WHITE);
        Vector3f dirLuz = new Vector3f(2f,2f,2f);
        DirectionalLight luz = new DirectionalLight(colorLuz,dirLuz);
        luz.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0),100));
        objetoRaiz.addChild(luzAmbiente);
        objetoRaiz.addChild(luz);
        objetoRaiz.compile();
        universo.addBranchGraph(objetoRaiz);
    }
    
    /**
     * 
     * @return 
     */
    public BranchGroup dibujar() {
        BranchGroup escena= new BranchGroup();
        escena.setCapability(escena.ALLOW_DETACH);
        //Apariencia
        Appearance apariencia = new Appearance();
        TexCoordGeneration texCoord = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,TexCoordGeneration.TEXTURE_COORDINATE_2);
        apariencia.setTexCoordGeneration(texCoord);
        String nombreImg="D:\\Usuarios\\veng\\Documentos\\NetBeansProjects\\Laberinto3D\\src\\laberinto3d\\img\\esfera.png";
        TextureLoader loader = new TextureLoader(nombreImg,this);
        ImageComponent2D image = loader.getImage();
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL,Texture.RGBA,image.getWidth(),image.getHeight());
        texture.setImage(0,image);
        texture.setEnable(true);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
        apariencia.setTexture(texture);
        TextureAttributes atributosTex = new TextureAttributes();
        atributosTex.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(atributosTex);
        //Material
        Material material = new Material();
        material.setAmbientColor(new Color3f(Color.DARK_GRAY));
        material.setDiffuseColor(new Color3f(Color.WHITE));
        material.setSpecularColor(new Color3f(Color.WHITE));
        material.setEmissiveColor(new Color3f(Color.BLACK));
        material.setShininess(20.0f);
        apariencia.setMaterial(material);
        //esfera
        Sphere player = new Sphere(1f,apariencia);
        //Ubicar el Player
        Transform3D traslacionPlayer = new Transform3D();
        traslacionPlayer.set(new Vector3f(auxpossX3D,0,auxpossZ3D));
        TransformGroup grupoPlayer = new TransformGroup(traslacionPlayer);
        grupoPlayer.addChild(player);
        //Iluminación
        Color3f colorAmbiente = new Color3f(Color.DARK_GRAY);
        AmbientLight luzAmbiente = new AmbientLight(colorAmbiente);
        luzAmbiente.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0),100));
        Color3f colorLuz = new Color3f(Color.WHITE);
        Vector3f dirLuz = new Vector3f(-1f,-1f,1f);
        DirectionalLight luz = new DirectionalLight(colorLuz,dirLuz);
        luz.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0),100));
        
        escena.addChild(grupoPlayer);
        escena.addChild(luzAmbiente);
        escena.addChild(luz);
        escena.compile();
        return escena;
    }
    
    /**
     * 
     * @param scene 
     */
    public void actualizar(BranchGroup scene) {
        if (auxpossX3D==possX3D && auxpossZ3D==possZ3D){return;}
        possX3D=auxpossX3D;
        possZ3D=auxpossZ3D;
        try {
            miLocale.replaceBranchGraph(this.escenaPlayer,scene);
            this.escenaPlayer = scene;
        } catch(CapabilityNotSetException e) {
            System.out.println("probleme de Capacite");
        } catch(RestrictedAccessException e) {
            System.out.println("probleme d'Acces");
        } catch(Exception e) {
            System.out.println("probleme autre");
        }
    }
    
    /**
     * 
     * @param url
     * @return 
     */
    public Appearance textureCube(String url) {
        Appearance aparienciaCube = new Appearance();
        TextureLoader loader = new TextureLoader(url,this);
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
        //Material
        Material material = new Material();
        material.setAmbientColor(new Color3f(Color.DARK_GRAY));
        material.setDiffuseColor(new Color3f(Color.WHITE));
        material.setSpecularColor(new Color3f(Color.WHITE));
        material.setEmissiveColor(new Color3f(Color.BLACK));
        material.setShininess(20.0f);
        aparienciaCube.setMaterial(material);
        return aparienciaCube;
    }
    /**
     * Para mover la camara del universo
     */
    public void moverGirarCamara() {
        //Traslacion de Camara
        Transform3D traslacionVista = new Transform3D();
        traslacionVista.setTranslation(new Vector3f(camaraX,camaraY,camaraZ));
        //Rotacion de la camara
        Transform3D rotacion = new Transform3D();
        //rotacion.rotZ(Math.toRadians(angulo));
        rotacion.rotX(Math.toRadians(angulo));
        rotacion.mul(traslacionVista);
        //Añadimos la rotacion y la traslacion de la camara
        universo.getViewingPlatform().getViewPlatformTransform().setTransform(rotacion);
        universo.getViewingPlatform().getViewPlatformTransform().getTransform(traslacionVista);
    }
    
    /**Para validar la casilla del mapa
     * 
     * @param x
     * @param y
     * @return 
     */
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
     * Eventos de teclado
     * @param evento 
     */
    public void teclaPresionada(KeyEvent evento) {
        
        if (evento.getKeyCode() == KeyEvent.VK_UP) {//Arriba
            if (validarCasilla(possX, possY-1)) {
                possY = possY - 1;
                auxpossZ3D = auxpossZ3D - movimiento;
                camaraY = camaraY + 1.5f;
                camaraZ = camaraZ - 1f;
                angulo = angulo + 1;
                System.out.println("Arriba");
            }
        }
        if (evento.getKeyCode() == KeyEvent.VK_DOWN) {//Abajo
            if (validarCasilla(possX, possY+1)) {
                possY = possY + 1;
                auxpossZ3D = auxpossZ3D + movimiento;
                camaraY = camaraY - 1.5f;
                camaraZ = camaraZ + 1f;
                angulo = angulo - 1;
                System.out.println("Abajo");
            }
        }
        if (evento.getKeyCode() == KeyEvent.VK_LEFT) {//Izquierda
            if (validarCasilla(possX-1, possY)) {
                possX = possX - 1;
                auxpossX3D = auxpossX3D - movimiento;
                camaraX = camaraX - 2f;
                System.out.println("Izquierda");
            }
        }
        if (evento.getKeyCode() == KeyEvent.VK_RIGHT) {//Derecha
            if (validarCasilla(possX+1, possY)) {
                possX = possX + 1;
                auxpossX3D = auxpossX3D + movimiento;
                camaraX = camaraX + 2f;
                System.out.println("Derecha");
            }
        }
    }
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");
        JFrame ventana = new JFrame("LABERINTO 3D");
        Laberinto panel = new Laberinto();
        ventana.add(panel);
        ventana.setSize(1280,1024);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        while (true) {
            panel.actualizar(panel.dibujar());
            panel.moverGirarCamara();
            try {
                Thread.sleep(10);
            }
            catch(InterruptedException ex){
            }
            panel.repaint();
        }
    } 
}
