
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.CapabilityNotSetException;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Locale;
import javax.media.j3d.RestrictedAccessException;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author veneg
 */
public class prueba extends JPanel{
    
    //Configuracion del Universo
    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    //Universo
    SimpleUniverse universo;
    //Player
    BranchGroup escenaPlayer;
    Locale maLocale;
    
    //Posicion del player en el Mapa3D
    float possX3D = 0f;
    float possY3D = 0f;
    //aux cordenadas
    float auxpossX3D = 1f;
    float auxpossY3D = 1f;
    //Movimiento
    float movimiento =2f;
    //Camara
    float camaraX = 1f;
    float camaraY = 1f;
    float camaraZ = 35f;
    int angulo = -45;
    
    public prueba() {
        setLayout(new BorderLayout());
        universo = new SimpleUniverse(canvas3D);
        universo.getViewingPlatform().setNominalViewingTransform();
        add(canvas3D);
        
        escenaPlayer = new BranchGroup();
        escenaPlayer.setCapability(escenaPlayer.ALLOW_DETACH);
        escenaPlayer.compile();
        universo.addBranchGraph(escenaPlayer);
        maLocale = universo.getLocale();
        
        
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
    
    public BranchGroup dibujar(){
        Appearance apariencia = new Appearance();
        Color3f color = new Color3f(0.0f,0.0f,1.0f);
        ColoringAttributes ca = new ColoringAttributes(color,ColoringAttributes.NICEST);
        apariencia.setColoringAttributes(ca);
        
        BranchGroup escenaP= new BranchGroup();
        escenaP.setCapability(escenaP.ALLOW_DETACH);
            
        Sphere player = new Sphere(1f,apariencia);
        
        Transform3D traslacionPlayer = new Transform3D();
        traslacionPlayer.set(new Vector3f(auxpossX3D,auxpossY3D,0));
        TransformGroup grupoPlayer = new TransformGroup(traslacionPlayer);
        grupoPlayer.addChild(player);
        escenaP.addChild(grupoPlayer);
        escenaP.compile();
        return escenaP;
    }
    
    public void actualizar(BranchGroup scene) {
        if (auxpossX3D==possX3D && auxpossY3D==possY3D){return;}
        possX3D=auxpossX3D;
        possY3D=auxpossY3D;
        try {
            
            maLocale.replaceBranchGraph(this.escenaPlayer,scene);
            this.escenaPlayer = scene;
        } catch(CapabilityNotSetException e) {
            System.out.println("probleme de Capacite");
        } catch(RestrictedAccessException e) {
            System.out.println("probleme d'Acces");
        } catch(Exception e) {
            System.out.println("probleme autre");
        }
    }
    
    public void moverGirarCamara() {
        //Traslacion de Camara
        Transform3D traslacionVista = new Transform3D();
        traslacionVista.setTranslation(new Vector3f(camaraX,camaraY,camaraZ));
        //Rotacion de la camara
        //Transform3D rotacion = new Transform3D();
        //rotacion.rotZ(Math.toRadians(angulo));
        //rotacion.rotX(Math.toRadians(0));
        //rotacion.mul(traslacionVista);
        //Añadimos la rotacion y la traslacion de la camara
        //universo.getViewingPlatform().getViewPlatformTransform().setTransform(rotacion);
        universo.getViewingPlatform().getViewPlatformTransform().getTransform(traslacionVista);
    }
    
    public void teclaPresionada(KeyEvent evento) {
        
        if (evento.getKeyCode() == KeyEvent.VK_UP) {//Arriba
            auxpossY3D = auxpossY3D + movimiento;
            camaraY = camaraY + 2f;
            System.out.println("Up");
        }
        if (evento.getKeyCode() == KeyEvent.VK_DOWN) {//Abajo
            auxpossY3D = auxpossY3D - movimiento;
            camaraY = camaraY - 2f;
            System.out.println("Down");
        }
        if (evento.getKeyCode() == KeyEvent.VK_LEFT) {//Izquierda
            auxpossX3D = auxpossX3D - movimiento;
            camaraX = camaraX - 1f;
            System.out.println("Left");
        }
        if (evento.getKeyCode() == KeyEvent.VK_RIGHT) {//Derecha
            auxpossX3D = auxpossX3D + movimiento;
            camaraX = camaraX + 1f;
            System.out.println("Right");
        }
    }
    
    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");
        JFrame ventana = new JFrame("LABERINTO 3D");
        prueba panel = new prueba();
        ventana.add(panel);
        ventana.setSize(1280,1024);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int frame = 1000/30;
        while (true) {
            panel.actualizar(panel.dibujar());
            panel.moverGirarCamara();
            try {
                
                Thread.sleep(30);
            }
            catch(InterruptedException ex){
            }
            panel.repaint();
        }
    } 
}
