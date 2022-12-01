package tareapaint;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

public class TareaPaint extends JFrame implements ActionListener {
    int DEFAULT_STROKE = 3; //esto es para el grosor del puntero
    public int cordsX = 0, cordsY = 0;
    public JMenu coordsShow;
    JMenu archivo, dibujar, opciones;
    JMenuItem salir, nuevo, guardar, abrir, color;
    JRadioButtonMenuItem linea, rectangulo, elipse, lapiz, borrador;
    JCheckBoxMenuItem relleno;
    JColorChooser colorChooser = new JColorChooser();
    ButtonGroup btn;

    PanelPaint Panel;

    public TareaPaint() {
        CrearMenu();
        AgregarBotones();
        Panel = new PanelPaint();
        this.add(Panel);
        this.setSize(1800, 3000);
        this.setVisible(true);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setTitle("Paint");
    }

    private void AgregarBotones() { //aqui se agregan
        salir.addActionListener(this);
        nuevo.addActionListener(this);
        guardar.addActionListener(this);
        abrir.addActionListener(this);
        color.addActionListener(this);
        linea.addActionListener(this);
        rectangulo.addActionListener(this);
        elipse.addActionListener(this);
        relleno.addActionListener(this);
        lapiz.addActionListener(this);
        borrador.addActionListener(this);

    }

    public void CrearMenu() { //Aqui se crean los menus
        JMenuBar menu = new JMenuBar();
        archivo = new JMenu("Archivo");
        nuevo = new JMenuItem("Nuevo");
        abrir = new JMenuItem("Abrir");
        guardar = new JMenuItem("Guardar");
        salir = new JMenuItem("Salir");
        archivo.add(nuevo);
        archivo.add(abrir);
        archivo.add(guardar);
        archivo.add(salir);
        menu.add(archivo);


        dibujar = new JMenu("Dibujar"); // se crea cada menu y sus submenus
        btn = new ButtonGroup();
        linea = new JRadioButtonMenuItem("Linea");
        rectangulo = new JRadioButtonMenuItem("Rectangulo");
        elipse = new JRadioButtonMenuItem("Elipse");
        lapiz = new JRadioButtonMenuItem("Lapiz");
        borrador = new JRadioButtonMenuItem("Borrador");
        btn.add(elipse);
        btn.add(rectangulo);
        btn.add(linea);
        btn.add(lapiz);
        btn.add(borrador);
        btn.setSelected(lapiz.getModel(), true);
        relleno = new JCheckBoxMenuItem("Completo");
        color = new JMenuItem("Color");
        dibujar.add(lapiz);
        dibujar.add(linea);
        dibujar.add(rectangulo);
        dibujar.add(elipse);
        dibujar.add(borrador);
        menu.add(dibujar);

        opciones = new JMenu("Opciones");
        opciones.add(relleno);
        opciones.add(color);
        JSlider slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(1);//slider minimo
        slider.setMaximum(20);//maximo
        slider.setValue(6);//con el que inicia
        ChangeListener cl = e -> {
            JSlider x = (JSlider) e.getSource();
            DEFAULT_STROKE = x.getValue();
        };
        slider.addChangeListener(cl);
        opciones.add(slider);
        menu.add(opciones);

        coordsShow = new JMenu(""+this.cordsX+ ", "+cordsY);
        menu.add(coordsShow);
        this.setJMenuBar(menu);

    }

    public static void main(String[] args) {
        TareaPaint ventana = new TareaPaint();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void newFocus(String target){ //esto es que si se selecciona una opcion, las demas deben estar en blanco
        if (target.equals("lapiz")){
            if (Panel.linea) Panel.linea = false;
            if (Panel.rectangulo) Panel.rectangulo = false;
            if (Panel.elipse) Panel.elipse = false;
            if (!Panel.lapiz) Panel.lapiz = true;
            if (Panel.borrador) Panel.borrador = false;
        } else if (target.equals("linea")) {
            if (!Panel.linea) Panel.linea = true;
            if (Panel.rectangulo) Panel.rectangulo = false;
            if (Panel.elipse) Panel.elipse = false;
            if (Panel.lapiz) Panel.lapiz = false;
            if (Panel.borrador) Panel.borrador = false;
        } else if (target.equals("rectangulo")) {
            if (Panel.linea) Panel.linea = false;
            if (!Panel.rectangulo) Panel.rectangulo = true;
            if (Panel.elipse) Panel.elipse = false;
            if (Panel.lapiz) Panel.lapiz = false;
            if (Panel.borrador) Panel.borrador = false;
        } else if (target.equals("elipse")) {
            if (Panel.linea) Panel.linea = false;
            if (Panel.rectangulo) Panel.rectangulo = false;
            if (!Panel.elipse) Panel.elipse = true;
            if (Panel.lapiz) Panel.lapiz = false;
            if (Panel.borrador) Panel.borrador = false;
        } else if (target.equals("relleno")) {
            Panel.relleno = !Panel.relleno;
        } else if (target.equals("borrador")){
            if (Panel.linea) Panel.linea = false;
            if (Panel.rectangulo) Panel.rectangulo = false;
            if (Panel.elipse) Panel.elipse = false;
            if (Panel.lapiz) Panel.lapiz = false;
            if (!Panel.borrador) Panel.borrador = true;
        }
    }
    public void actionPerformed(ActionEvent e) {//se asigna cada boton con el que es
        if (e.getSource() == nuevo) {
            Panel.resetAll();
        }
        if (e.getSource() == abrir) {
            Panel.abrir();
        }
        if (e.getSource() == guardar) {
            Panel.guardar();
        }
        if (e.getSource() == salir) {
            System.exit(0);
        }
        if (e.getSource() == lapiz) {
            newFocus("lapiz");
        }
        if (e.getSource() == linea) {
            newFocus("linea");
        }
        if (e.getSource() == rectangulo) {
            newFocus("rectangulo");
        }
        if (e.getSource() == elipse) {
            newFocus("elipse");
        }
        if (e.getSource() == relleno) {
            newFocus("relleno");
        }
        if (e.getSource() == color) {
            Color color = JColorChooser.showDialog(this, "Elija un color", this.Panel.getColorActual());
            this.Panel.setColorActual(color);
        }
        if (e.getSource() == borrador) {
            newFocus("borrador");
        }

    }


    class PanelPaint extends JPanel {
        ArrayList<Integer> ax = new ArrayList<>();
        ArrayList<Integer> ay = new ArrayList<>();
        Point p1;
        Point p2;
        Shape figura;
        public Color coloract = Color.blue;
        public Color borrar = Color.white; //   color borrador //
        BufferedImage myImage;
        Graphics2D g2d;
        boolean rectangulo = false;
        boolean linea = false;
        boolean relleno = false;
        boolean lapiz = true;
        boolean elipse = false;
        boolean borrador = false;

        public PanelPaint() {
            MouseControl1 Coords1 = new MouseControl1();
            MouseControl2 Coords2 = new MouseControl2();
            addMouseListener(Coords1);
            addMouseMotionListener(Coords2);
            setBackground(Color.white);
        }
        public Color getColorActual(){
            return coloract;
        }
        public void setColorActual(Color color){
            coloract = color;
        }

        public Graphics2D crearGraphics2D() {
            Graphics2D g2 = null;
            if (myImage == null || myImage.getWidth() != getSize().width || myImage.getHeight() != getSize().height) {

                myImage = (BufferedImage) createImage(getSize().width, getSize().height);
            }
            if (myImage != null) {
                g2 = myImage.createGraphics();
                g2.setColor(coloract);
                g2.setBackground(getBackground());
            }
            if (g2 != null) {
                g2.clearRect(0, 0, getSize().width, getSize().height);
            }
            return g2;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (myImage == null) {
                g2d = crearGraphics2D();
            }
            if (figura != null) {
                if (relleno) {
                    g2d.setColor(coloract);
                    g2d.draw(figura);
                    g2d.fill(figura);
                } else {
                    g2d.setColor(coloract);
                    g2d.draw(figura);
                }
                if (myImage != null && isShowing()) {
                    g.drawImage(myImage, 0, 0, this);
                }
                figura = null;
            }
        }

        public Shape crearFigura(Point p1, Point p2) {//aqui se crean los rectangulos
            double xInicio = Math.min(p1.getX(), p2.getX());
            double yInicio = Math.min(p1.getY(), p2.getY());
            double ancho = Math.abs(p2.getX() - p1.getX());
            double altura = Math.abs(p2.getY() - p1.getY());
            Shape nuevaFig = new Rectangle2D.Double(xInicio, yInicio, ancho, altura);
            return nuevaFig;
        }

        public Shape crearLinea(Point p1, Point p2) { //linea
            Shape nuevaFig = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            return nuevaFig;
        }

        public Shape crearElipse(Point p1, Point p2) {//elipse
            double xInicio = Math.min(p1.getX(), p2.getX());
            double yInicio = Math.min(p1.getY(), p2.getY());
            double ancho = Math.abs(p2.getX() - p1.getX());
            double altura = Math.abs(p2.getY() - p1.getY());
            Shape nuevaFig = new Ellipse2D.Double(xInicio, yInicio, ancho, altura);
            return nuevaFig;

        }
        public void resetAll(){ //esto es para poner en blanco
            myImage=null;
            repaint();
        }

        class MouseControl1 extends MouseAdapter {

            public void mousePressed(MouseEvent evento) {
                PanelPaint.this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                p1 = evento.getPoint();

            }
            public void mouseReleased (MouseEvent evento) {
                PanelPaint.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                if (rectangulo) {
                    p2 = evento.getPoint();
                    g2d.setStroke(new BasicStroke(DEFAULT_STROKE));
                    figura = crearFigura(p1, p2);
                } else if (linea) {
                    p2 = evento.getPoint();
                    g2d.setStroke(new BasicStroke(DEFAULT_STROKE));
                    figura = crearLinea(p1, p2);
                } else if (elipse) {
                    p2 = evento.getPoint();
                    g2d.setStroke(new BasicStroke(DEFAULT_STROKE));
                    figura = crearElipse(p1, p2);
                } else if (lapiz || borrador) {

                    for(int i = 0; i < Panel.ax.size() - 1; i++) {
                        if (Panel.g2d != null) {
                            if (lapiz) Panel.g2d.setPaint(coloract) ;//se agrega el color de cada obj
                            if (borrador) Panel.g2d.setPaint(borrar);
//                            System.out.println("--------");
//                            System.out.println("Lapiz:"+lapiz);
//                            System.out.println("Borrador: "+borrador);
//                            System.out.println("Elipse: "+elipse);
                            Panel.g2d.setStroke(new BasicStroke(DEFAULT_STROKE,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                            Panel.g2d.drawLine(ax.get(i), ay.get(i), ax.get(i+1), ay.get(i+1));
                            Panel.g2d.setStroke(new BasicStroke(1));
                        }
                    }
                    ax.clear();
                    ay.clear();
                    figura = crearElipse(new Point(0, 0), new Point(0, 0)); //esto es porque luego no carga adecuadamente al poner un jpg o dibujar
                }
                validate();
                repaint();
            }
        }
        public void abrir(){
            try {
                JFileChooser jfc = createJFileChooser();
                jfc.showOpenDialog(this);
                File file =jfc.getSelectedFile();
                if(file==null){
                    return;
                }
                myImage=javax.imageio.ImageIO.read(file);
                int w =myImage.getWidth(null);
                int h = myImage.getHeight(null);
                if(myImage.getType()!=BufferedImage.TYPE_INT_RGB){
                    BufferedImage bi2 = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
                    Graphics big = bi2.getGraphics();
                    big.drawImage(myImage,0,0,null);
                }
                g2d =(Graphics2D)myImage.getGraphics();
                repaint();
            } catch(IOException e){
                System.exit(1);
            }
            PanelPaint.this.getParent().validate();
            PanelPaint.this.getParent().repaint();
            figura = PanelPaint.this.crearElipse(new Point(0, 0), new Point(0, 0));
        }
        public void guardar (){
            try {
                JFileChooser jfc = createJFileChooser();
                jfc.showSaveDialog(this);
                File file = jfc.getSelectedFile();
                if(file == null){
                    return;
                }
                javax.swing.filechooser.FileFilter ff = jfc.getFileFilter();
                String fileName = file.getName();
                String extension = "jpg";
                if(ff instanceof MyFileFilter){
                    extension =((MyFileFilter)ff).getExtension();
                }
                fileName = fileName +"."+extension;
                file = new File(file.getParent(),fileName);
                javax.imageio.ImageIO.write(myImage, extension, file);
            }catch (Exception e){
                System.out.println(e);
            }
        }
        public JFileChooser createJFileChooser(){
            JFileChooser jfc = new JFileChooser();
            jfc.setAcceptAllFileFilterUsed(false);
            String [] fileTypes = getFormats();
            for(int i=0;i<fileTypes.length;i++){
                jfc.addChoosableFileFilter (new MyFileFilter(fileTypes[i],fileTypes[i]+"file"));

            }
            return jfc;
        }
        public String[] getFormats(){
            String[] formats =javax.imageio.ImageIO.getWriterFormatNames();
            java.util.TreeSet<String> formatSet = new java.util.TreeSet<String>();
            for(String s : formats){
                formatSet.add(s.toLowerCase());

            }
            return formatSet.toArray(new String[0]);
        }
        class MyFileFilter extends javax.swing.filechooser.FileFilter{
            private String extension, description;
            public MyFileFilter(String extension, String description){
                this.extension =extension;
                this.description=description;
            }
            public boolean accept (File f){
                return f.getName().toLowerCase().endsWith ("."+extension)||f.isDirectory();
            }
            public String getDescription (){
                return description;
            }
            public String getExtension(){
                return extension;
            }
        }
        class MouseControl2 extends MouseMotionAdapter{
            public void configure(Graphics2D graph){
                if(figura != null){
                    graph.setXORMode(PanelPaint.this.getBackground());
                    graph.setColor(coloract);
                    graph.draw(figura);
                }
            }
            public void mouseMoved(MouseEvent e) {
                int x = TareaPaint.this.cordsX = e.getPoint().x;
                int y = TareaPaint.this.cordsY = e.getPoint().y;
                TareaPaint.this.coordsShow.setText(""+x+ ", "+y);
            }
            
            
            public void mouseDragged (MouseEvent evento){
                Graphics2D g2d = null;
                if(rectangulo){
                    if(figura != null){
                        g2d = (Graphics2D) PanelPaint.this.getGraphics();
                        configure(g2d);
                    }
                    p2=evento.getPoint();
                    figura = crearFigura(p1,p2);
                    g2d=(Graphics2D)PanelPaint.this.getGraphics();
                    g2d.setXORMode(PanelPaint.this.getBackground());
                    g2d.setColor(coloract);
                    g2d.draw(figura);
                } else if (linea){
                    if(figura != null){
                        g2d = (Graphics2D) PanelPaint.this.getGraphics();
                        configure(g2d);
                    }
                    p2=evento.getPoint();
                    figura=crearLinea(p1,p2);
                    g2d=(Graphics2D)PanelPaint.this.getGraphics();
                    g2d.setXORMode(PanelPaint.this.getBackground());
                    g2d.setColor(coloract);
                    g2d.draw(figura);
                } else if(lapiz || borrador) {
                    if (figura != null) {
                        g2d = (Graphics2D) PanelPaint.this.getGraphics();
                        configure(g2d);
                    }

                    Panel.ax.add(evento.getX());
                    Panel.ay.add(evento.getY());
                } else if (elipse){
                    if(figura != null){
                        g2d = (Graphics2D) PanelPaint.this.getGraphics();
                        configure(g2d);
                    }
                    p2=evento.getPoint();
                    figura = crearElipse(p1,p2);
                    g2d=(Graphics2D)PanelPaint.this.getGraphics();
                    g2d.setXORMode(PanelPaint.this.getBackground());
                    g2d.setColor(coloract);
                    g2d.draw(figura);
                }
            }
        }

    }
}