import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Tablero extends JFrame {

    private final int LARGO = 600;
    private final int ALTO = 600;
    private final int FILAS = 10;
    private final int COLUMNAS = 10;
    private int NUM_FICHASPC = 0;
    private final int MAX_NUM_FICHAS = 4;
    private int vidaPC;
    private JButton[][] botonesPC;
    private Casilla[][] casillasPC;
    private Tetramino [] tetraminosPC = new Tetramino[4];
    private Tetramino [] tetraminos = new Tetramino[4];
    private int NUM_FICHASJUGADOR = 0;
    private int vidaJugador;
    private JButton[][] botonesJugador;
    private Casilla[][] casillasJugador;
    private JComboBox<String> tetraminosComboBox;
    private JComboBox<String> rotacionesComboBox;
    private boolean yaAtacado[][] = new boolean[10][10];

    /**
     * Constructor por defecto del tablero que genera todos los botones que van a estar en la ventana y las casillas que corresponden a cada boton
     */
    public Tablero() {
        setTitle("Hundir los tetraminos");
        setSize(LARGO, ALTO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crea el panel con GridLayout para los botones
        JPanel gridPanelPC = new JPanel(new GridLayout(FILAS, COLUMNAS));
        // Crea la matriz de botones y las casillas correspondientes
        botonesPC = new JButton[FILAS][COLUMNAS]; //Crea el array de los botones
        casillasPC = new Casilla[FILAS][COLUMNAS]; //Crea el array de las casillas
        for (int i = 0; i < FILAS; i++) { // For para ir asignando lo que va a hacercada boton en el array botones
            for (int j = 0; j < COLUMNAS; j++) {
                casillasPC[i][j] = new Casilla(i, j); //Se le asigana la X y la Y a la casilla en la posicion x e y
                botonesPC[i][j] = new JButton(); // Se crean los botones según van avanzando los if
                botonesPC[i][j].setEnabled(false);
                botonesPC[i][j].setName(Integer.toString(i) + Integer.toString(j)); //Se les asigna el nombre a los botones
                botonesPC[i][j].setPreferredSize(new Dimension(50, 50)); //se les asigna un tamaña a los botones
                botonesPC[i][j].addActionListener(new ActionListener() { // Aqui se condigura la accion del boton al pulsarlo
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton boton = (JButton) e.getSource();
                        String nombreBoton = boton.getName();
                        int fila = Integer.parseInt(nombreBoton.substring(0, 1)); //se obtiene la fila en la que está el botón
                        int columna = Integer.parseInt(nombreBoton.substring(1, 2)); //se obtiene la columna en la que está el botón

                        if (casillasPC[fila][columna].isBarco()) { // Aquí se comprueba si la casilla que corresponde al boton seleccionado, tiene o no un barco encima
                            boton.setBackground(Color.RED); // Si lo tiene se marca como rojo de tocado
                            vidaPC = vidaPC -1; //Se le resta 1 de vida al PC
                            if (vidaPC == 0){ // Y si la vida llega a 0
                                dispose(); // se cierra la ventana
                                System.out.println("Has ganado"); // y se imprime un mensaje diciendo que el jugador ha ganado
                            }
                        } else {
                            boton.setBackground(Color.BLUE);// Si por otra parte la casilla está vacia, se vuelve azul de que es agua
                            atacarJugador(casillasJugador, botonesJugador, yaAtacado);
                            if(vidaJugador == 0){
                                dispose();
                                System.out.println("Has perdido");
                            }
                        }
                        boton.setEnabled(false); // el boton se desactiva para no poder volver a pulsarlo
                    }
                });
                gridPanelPC.add(botonesPC[i][j]); // Agrega el botón al panel de GridLayout
            }
        }

        add(gridPanelPC, BorderLayout.EAST); // Agrega el panel de GridLayout al centro del JFrame

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);// Se define un separador vertical para los dos tableros
        separator.setPreferredSize(new Dimension(5, 600)); // define el tamaño del borde
        add(separator, BorderLayout.CENTER); // agrega el borde al centro de la ventana

        setVisible(true);
        pack();
        asignarBarcoPC(casillasPC);
        tetraminosComboBox = new JComboBox<String>(new String[]{"Recta", "Zeta", "Zeta Invertida", "Ele", "Ele Invertida", "T", "Cuadrado"});
        rotacionesComboBox = new JComboBox<String>(new String[]{"0 grados", "90 grados", "180 grados", "270 grados"});

        // Agrega las listas de selección a un panel en la parte superior del tablero
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new FlowLayout());
        selectionPanel.add(new JLabel("Seleccione un Tetramino:"));
        selectionPanel.add(tetraminosComboBox);
        selectionPanel.add(new JLabel("Seleccione una Rotación:"));
        selectionPanel.add(rotacionesComboBox);
        add(selectionPanel, BorderLayout.NORTH);

        // Crea el panel con GridLayout para los botones
        JPanel gridPanelJugador = new JPanel(new GridLayout(FILAS, COLUMNAS));
        // Crea la matriz de botones y las casillas correspondientes
        botonesJugador = new JButton[FILAS][COLUMNAS];
        casillasJugador = new Casilla[FILAS][COLUMNAS];
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                casillasJugador[i][j] = new Casilla(i, j);
                botonesJugador[i][j] = new JButton();
                botonesJugador[i][j].setName(Integer.toString(i) + Integer.toString(j));
                botonesJugador[i][j].setPreferredSize(new Dimension(50, 50));
                botonesJugador[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String tetraminoSeleccionado = (String) tetraminosComboBox.getSelectedItem();
                        String rotacionSeleccionada = (String) rotacionesComboBox.getSelectedItem();
                        JButton boton = (JButton) e.getSource();
                        String nombreBoton = boton.getName();
                        int fila = Integer.parseInt(nombreBoton.substring(0, 1));
                        int columna = Integer.parseInt(nombreBoton.substring(1, 2));
                        asignarBarcoJugador(tetraminoSeleccionado, rotacionSeleccionada, casillasJugador, fila, columna);
                        boton.setEnabled(false);
                        NUM_FICHASJUGADOR++;
                        if (NUM_FICHASJUGADOR == MAX_NUM_FICHAS){
                            vidaJugador = 16;
                            vidaPC = 16;
                            for (int i = 0; i < FILAS; i++) {
                                for (int j = 0; j < COLUMNAS; j++) {
                                    botonesJugador[i][j].setEnabled(false);
                                    botonesPC[i][j].setEnabled(true);
                                }
                            }
                        }

                    }
                });
                gridPanelJugador.add(botonesJugador[i][j]); // Agrega el botón al panel de GridLayout
            }

        }

        add(gridPanelJugador, BorderLayout.WEST); // Agrega el panel de GridLayout al centro del JFrame

        setVisible(true);
        pack();

    }

    /**
     * Metodo para asignar de manera fija los tetraminos del Array para el PC
     * @param casillas Se pasa el array de casillas para asignar barcos a las debidas
     */
    public void asignarBarcoPC(Casilla[][] casillas){ //Metodo para asignar los barcos

        int [] posicionesxCuadrado = new int[]{3,3,4,4};  // Se guardan las posiciones corresppondientes a la x del cuadrado
        int [] posicionesyCuadrado = new int []{3,4,3,4}; // Se guardan las posiciones corresppondientes a la y del cuadrado
        Tetramino cuadrado =  new Tetramino("Cuadrado",posicionesxCuadrado, posicionesyCuadrado); //se crea el tetramino y los
        tetraminosPC[0] = cuadrado;
        NUM_FICHASPC = ++NUM_FICHASPC;

        casillas[3][3].setBarco(true);
        casillas[3][4].setBarco(true);
        casillas[4][3].setBarco(true);
        casillas[4][4].setBarco(true);


        int [] posicionesxRecta = new int[]{2,2,2,2};
        int [] posicionesyRecta = new int []{6,7,8,9};
        Tetramino recta = new Tetramino("Recta", posicionesxRecta, posicionesyRecta);
        tetraminosPC[1] = recta;
        NUM_FICHASPC = ++NUM_FICHASPC;

        casillas[2][6].setBarco(true);
        casillas[2][7].setBarco(true);
        casillas[2][8].setBarco(true);
        casillas[2][9].setBarco(true);


        int [] posicionesxT = new int[]{7,8,8,8};
        int [] posicionesyT = new int[]{1,0,1,2};
        Tetramino t = new Tetramino("T", posicionesxT, posicionesyT);
        tetraminosPC[2] = t;
        NUM_FICHASPC = ++NUM_FICHASPC;

        casillas[7][1].setBarco(true);
        casillas[8][0].setBarco(true);
        casillas[8][1].setBarco(true);
        casillas[8][2].setBarco(true);


        int [] posicionesxZeta = new int[]{8,8,9,9};
        int [] posicionesyZeta = new int[]{7,8,8,9};
        Tetramino zeta = new Tetramino("Zeta", posicionesxZeta, posicionesyZeta);
        tetraminosPC[3] = zeta;
        NUM_FICHASPC = ++NUM_FICHASPC;

        casillas[8][7].setBarco(true);
        casillas[8][8].setBarco(true);
        casillas[9][8].setBarco(true);
        casillas[9][9].setBarco(true);


    }

    /**
     * Metodo para que el jugador asigne hasta 4 tetraminos
     * @param tetraminoSeleccionado El tetramino que el jugador ha escogido
     * @param rotacion La rotacion que el jugador le ha dado
     * @param casillas El array de casillas para asignarle un barco a las debidas
     * @param fila La fila inicial donde se ha colocado el tetramino
     * @param columna La columna inicial donde se ha colocado el tetramino
     */
    public void asignarBarcoJugador(String tetraminoSeleccionado, String rotacion, Casilla[][] casillas, int fila, int columna){

        //Se asgina el barco y se cambia el color de la primera posicion seleccionado
        tetraminos[NUM_FICHASJUGADOR] =  new Tetramino(tetraminoSeleccionado, fila, columna);
        botonesJugador[fila][columna].setBackground(Color.GREEN);
        casillas[fila][columna].setBarco(true);
        int indice = 0;
                 tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice);
            tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice);

            //A partir de aquí, segun las opciones seleccionadas, respecto a la primera casilla, se colocaran el resto de casillas

            //colocar cuadrado
            if (tetraminoSeleccionado.equals("Cuadrado")){

                //Esquina arriba derecha
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+1);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+1);
                botonesJugador[fila][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila][columna+1].setEnabled(false);
                casillas[fila][columna+1].setBarco(true);

                //Esquina abajo izquierda
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+2);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+2);
                botonesJugador[fila+1][columna].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna].setEnabled(false);
                casillas[fila+1][columna].setBarco(true);

                //Esquina abajo derecha
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+3);
                botonesJugador[fila+1][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna+1].setEnabled(false);
                casillas[fila+1][columna+1].setBarco(true);
        }

        //colocar recta
        if (tetraminoSeleccionado.equals("Recta")){//cogemos el tetramino recta
            if (rotacion.equals("0 grados") || rotacion.equals("180 grados")){//le decimos la condición de que para cuando rote 0º o 180º que se colore de forma horizontal en el tablero
                for(int i = 1; i < 4; i++){//le decimos que se pinten de verde las casillas en donde se coloquen las fichas
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+i, indice+i);
                    botonesJugador[fila][columna+i].setBackground(Color.GREEN);
                    botonesJugador[fila][columna+i].setEnabled(false);
                    casillas[fila][columna+i].setBarco(true);
                }
            }
            if (rotacion.equals("90 grados") || rotacion.equals("270 grados")){//le decimos que para 90º o 270º se coloque de forma vertical en el tablero
                for(int i = 1; i < 4; i++){
                    //coloreamos de color verde las casillas donde se coloque la ficha
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+i, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+i);
                    botonesJugador[fila+i][columna].setBackground(Color.GREEN);
                    botonesJugador[fila+i][columna].setEnabled(false);
                    casillas[fila+i][columna].setBarco(true);
                }
            }
        }

        //colocar zeta
        if (tetraminoSeleccionado.equals("Zeta")){

            if (rotacion.equals("0 grados") || rotacion.equals("180 grados")){
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+1);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+1);
                botonesJugador[fila][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila][columna+1].setEnabled(false);
                casillas[fila][columna+1].setBarco(true);
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+2);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+2);
                botonesJugador[fila+1][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna+1].setEnabled(false);
                casillas[fila+1][columna+1].setBarco(true);
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+2, indice+3);
                botonesJugador[fila+1][columna+2].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna+2].setEnabled(false);
                casillas[fila+1][columna+2].setBarco(true);
            }

            if (rotacion.equals("90 grados") || rotacion.equals("270 grados")){
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+1);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+1);
                botonesJugador[fila+1][columna].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna].setEnabled(false);
                casillas[fila+1][columna].setBarco(true);
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+2);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna-1, indice+2);
                botonesJugador[fila+1][columna-1].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna-1].setEnabled(false);
                casillas[fila+1][columna-1].setBarco(true);
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+2, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna-1, indice+3);
                botonesJugador[fila+2][columna-1].setBackground(Color.GREEN);
                botonesJugador[fila+2][columna-1].setEnabled(false);
                casillas[fila+2][columna-1].setBarco(true);
            }
        }

        //colocar zeta invertida
        if (tetraminoSeleccionado.equals("Zeta Invertida")){

            if (rotacion.equals("0 grados") || rotacion.equals("180 grados")){
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+1);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+1);
                botonesJugador[fila][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila][columna+1].setEnabled(false);
                casillas[fila][columna+1].setBarco(true);
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila-1, indice+2);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+2);
                botonesJugador[fila-1][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila-1][columna+1].setEnabled(false);
                casillas[fila-1][columna+1].setBarco(true);
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila-1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+2, indice+3);
                botonesJugador[fila-1][columna+2].setBackground(Color.GREEN);
                botonesJugador[fila-1][columna+2].setEnabled(false);
                casillas[fila-1][columna+2].setBarco(true);
            }

            if (rotacion.equals("90 grados") || rotacion.equals("270 grados")){
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+1);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+1);
                botonesJugador[fila+1][columna].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna].setEnabled(false);
                casillas[fila+1][columna].setBarco(true);
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+2);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+2);
                botonesJugador[fila+1][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna+1].setEnabled(false);
                casillas[fila+1][columna+1].setBarco(true);
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+2, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna-1, indice+3);
                botonesJugador[fila+2][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila+2][columna+1].setEnabled(false);
                casillas[fila+2][columna+1].setBarco(true);
            }
        }

        //colocar T

        if(tetraminoSeleccionado.equals("T")){
            if (rotacion.equals("0 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+i, indice+i);
                    botonesJugador[fila][columna+i].setBackground(Color.GREEN);
                    botonesJugador[fila][columna+i].setEnabled(false);
                    casillas[fila][columna+i].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+3);
                botonesJugador[fila+1][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna+1].setEnabled(false);
                casillas[fila+1][columna+1].setBarco(true);
            }

            if (rotacion.equals("90 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+i, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+i);
                    botonesJugador[fila+i][columna].setBackground(Color.GREEN);
                    botonesJugador[fila+i][columna].setEnabled(false);
                    casillas[fila+i][columna].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna-1, indice+3);
                botonesJugador[fila+1][columna-1].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna-1].setEnabled(false);
                casillas[fila+1][columna-1].setBarco(true);
            }

            if (rotacion.equals("180 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+i, indice+i);
                    botonesJugador[fila][columna+i].setBackground(Color.GREEN);
                    botonesJugador[fila][columna+i].setEnabled(false);
                    casillas[fila][columna+i].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila-1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+3);
                botonesJugador[fila-1][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila-1][columna+1].setEnabled(false);
                casillas[fila-1][columna+1].setBarco(true);
            }

            if (rotacion.equals("270 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+i, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+i);
                    botonesJugador[fila+i][columna].setBackground(Color.GREEN);
                    botonesJugador[fila+i][columna].setEnabled(false);
                    casillas[fila+i][columna].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+3);
                botonesJugador[fila+1][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna+1].setEnabled(false);
                casillas[fila+1][columna+1].setBarco(true);
            }
        }

        //Colocar Ele
        if (tetraminoSeleccionado.equals("Ele")){
            if (rotacion.equals("0 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+i, indice+i);
                    botonesJugador[fila][columna+i].setBackground(Color.GREEN);
                    botonesJugador[fila][columna+i].setEnabled(false);
                    casillas[fila][columna+i].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+3);
                botonesJugador[fila+1][columna].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna].setEnabled(false);
                casillas[fila+1][columna].setBarco(true);
            }

            if (rotacion.equals("90 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+i, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+i);
                    botonesJugador[fila+i][columna].setBackground(Color.GREEN);
                    botonesJugador[fila+i][columna].setEnabled(false);
                    casillas[fila+i][columna].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna-1, indice+3);
                botonesJugador[fila][columna-1].setBackground(Color.GREEN);
                botonesJugador[fila][columna-1].setEnabled(false);
                casillas[fila][columna-1].setBarco(true);
            }
            if (rotacion.equals("180 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+i, indice+i);
                    botonesJugador[fila][columna+i].setBackground(Color.GREEN);
                    botonesJugador[fila][columna+i].setEnabled(false);
                    casillas[fila][columna+i].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila-1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+2, indice+3);
                botonesJugador[fila-1][columna+2].setBackground(Color.GREEN);
                botonesJugador[fila-1][columna+2].setEnabled(false);
                casillas[fila-1][columna+2].setBarco(true);
            }

            if (rotacion.equals("270 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+i, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+i);
                    botonesJugador[fila+i][columna].setBackground(Color.GREEN);
                    botonesJugador[fila+i][columna].setEnabled(false);
                    casillas[fila+i][columna].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+2, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+3);
                botonesJugador[fila+2][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila+2][columna+1].setEnabled(false);
                casillas[fila+2][columna+1].setBarco(true);
            }
        }

        // Colocar Ele invertida
        if (tetraminoSeleccionado.equals("Ele Invertida")){
            if (rotacion.equals("0 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+i, indice+i);
                    botonesJugador[fila][columna+i].setBackground(Color.GREEN);
                    botonesJugador[fila][columna+i].setEnabled(false);
                    casillas[fila][columna+i].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+2, indice+3);
                botonesJugador[fila+1][columna+2].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna+2].setEnabled(false);
                casillas[fila+1][columna+2].setBarco(true);
            }

            if (rotacion.equals("90 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+i, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+i);
                    botonesJugador[fila+i][columna].setBackground(Color.GREEN);
                    botonesJugador[fila+i][columna].setEnabled(false);
                    casillas[fila+i][columna].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+2, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna-1, indice+3);
                botonesJugador[fila+2][columna-1].setBackground(Color.GREEN);
                botonesJugador[fila+2][columna-1].setEnabled(false);
                casillas[fila+2][columna-1].setBarco(true);
            }
            if (rotacion.equals("180 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+i, indice+i);
                    botonesJugador[fila+1][columna+i].setBackground(Color.GREEN);
                    botonesJugador[fila+1][columna+i].setEnabled(false);
                    casillas[fila+1][columna+i].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+1, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+1);
                botonesJugador[fila+1][columna].setBackground(Color.GREEN);
                botonesJugador[fila+1][columna].setEnabled(false);
                casillas[fila+1][columna].setBarco(true);
            }

            if (rotacion.equals("270 grados")){
                for (int i=1; i<=2; i++){
                    tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila+i, indice+i);
                    tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna, indice+i);
                    botonesJugador[fila+i][columna].setBackground(Color.GREEN);
                    botonesJugador[fila+i][columna].setEnabled(false);
                    casillas[fila+i][columna].setBarco(true);
                }
                tetraminos[NUM_FICHASJUGADOR].setPosicionx(fila, indice+3);
                tetraminos[NUM_FICHASJUGADOR].setPosiciony(columna+1, indice+3);
                botonesJugador[fila][columna+1].setBackground(Color.GREEN);
                botonesJugador[fila][columna+1].setEnabled(false);
                casillas[fila][columna+1].setBarco(true);
            }
        }

    }


    /**
     * Metodo para que tras el ataque incicial del jugador, ataque el Pc aleatoriamente al jugador
     * @param casillas se le pasa el array de casillas a comprobar si tiene barco
     * @param botones se le pasa el array de botones para cambiar el color si se ha comprobado que la casilla tiene barco
     * @param yaAtacado se le pasa el array de posciones ya atacadas para que no se repitan los ataques
     */
    public void atacarJugador(Casilla[][] casillas, JButton[][] botones, boolean[][] yaAtacado){

        int fila, columna;

        do {
            //se seleccionan aleatoriamente las posiciones a atacar
            fila = (int) (Math.random() * 10);
            columna = (int) (Math.random() * 10);
        } while (yaAtacado[fila][columna]); //si estas ya han sido atacadas, el programa volverá a generar otras posiciones
        yaAtacado[fila][columna]=true; //se establece la casilla a true por que ya ha sido atacada

        if (casillas[fila][columna].isBarco()){ //si hay n barco en la casilla a atacar, el boton correspondiente se volverá rojo y se le restará vida al jugador
            botones[fila][columna].setBackground(Color.RED);
            vidaJugador = --vidaJugador;
        }
        else { //si no, azul
            botones[fila][columna].setBackground(Color.BLUE);
        }

    }


    /**
     * Getter de los bootnes del tablero del PC
     * @return devulve los botones del PC
     */
    public JButton[][] getBotonesPC() {
        return botonesPC;
    }

    /**
     * Setter de los botones del PC
     * @param botonesPC Se pasa el array de los botones
     */
    public void setBotonesPC(JButton[][] botonesPC) {
        this.botonesPC = botonesPC;
    }

    /**
     * Getter de las casillas del PC
     * @return Devuelve las casillas del PC
     */
    public Casilla[][] getCasillasPC() {
        return casillasPC;
    }

    /**
     * Setter de las casillas del PC
     * @param casillasPC Se pasa el array de las casillas
     */
    public void setCasillasPC(Casilla[][] casillasPC) {
        this.casillasPC = casillasPC;
    }

    /**
     * Getter de los tetraminos del PC
     * @return Devuelve el array de los tetraminos
     */
    public Tetramino[] getTetraminosPC() {
        return tetraminosPC;
    }

    /**
     * Setter de los tetraminos del PC
     * @param tetraminosPC Se pasa el array de los tetraminos
     */
    public void setTetraminosPC(Tetramino[] tetraminosPC) {
        this.tetraminosPC = tetraminosPC;
    }

    /**
     * Getter de la vida restante del PC
     * @return Devuelve la vida del PC
     */
    public int getVidaPC() {
        return vidaPC;
    }

    /**
     * Setter de la vida del PC
     * @param vidaPC Se le pasa la cantidad de vida a asignar al Pc
     */
    public void setVidaPC(int vidaPC) {
        this.vidaPC = vidaPC;
    }

    /**
     * Getter del numero de fichas del jugadoor
     * @return devuelve el numero de fichas que tiene el jugador
     */
    public int getNUM_FICHASJUGADOR() {
        return NUM_FICHASJUGADOR;
    }

    /**
     * Setter del numero de fichas del jugador
     * @param NUM_FICHASJUGADOR Se pasa el numero a asignar como fichas del jugador
     */
    public void setNUM_FICHASJUGADOR(int NUM_FICHASJUGADOR) {
        this.NUM_FICHASJUGADOR = NUM_FICHASJUGADOR;
    }

    /**
     * Getter de la vida del Jugador
     * @return Devuelve la vida del jugador
     */
    public int getVidaJugador() {
        return vidaJugador;
    }

    /**
     * Setter de la vida del jugador
     * @param vidaJugador Establece la vida del jugador
     */
    public void setVidaJugador(int vidaJugador) {
        this.vidaJugador = vidaJugador;
    }

    /**
     * Getter de los botones del tablero del jugador
     * @return Devuelve el array de los botones del jugador
     */
    public JButton[][] getBotonesJugador() {
        return botonesJugador;
    }

    /**
     * Setter de los botones del jugador
     * @param botonesJugador Se pasa un array de los botones
     */
    public void setBotonesJugador(JButton[][] botonesJugador) {
        this.botonesJugador = botonesJugador;
    }

    /**
     * Getter del array de casillas del jugador
     * @return Se devuelve el array de las casillas
     */
    public Casilla[][] getCasillasJugador() {
        return casillasJugador;
    }

    /**
     * Setter de las casillas del jugador
     * @param casillasJugador Se pasa el array de casillas
     */
    public void setCasillasJugador(Casilla[][] casillasJugador) {
        this.casillasJugador = casillasJugador;
    }

    /**
     * Getter de las opciones a elegir como tetraminos
     * @return Devuelve el combo box de Strings de los tetraminos
     */
    public JComboBox<String> getTetraminosComboBox() {
        return tetraminosComboBox;
    }

    /**
     * Setter de las opciones a elegir como tetraminos
     * @param tetraminosComboBox Se pasa una seleccion de opciones
     */
    public void setTetraminosComboBox(JComboBox<String> tetraminosComboBox) {
        this.tetraminosComboBox = tetraminosComboBox;
    }

    /**
     * Getter de las opciones a elegir como rotaciones
     * @return Devuelve el combo box de Strings de las rotaciones
     */
    public JComboBox<String> getRotacionesComboBox() {
        return rotacionesComboBox;
    }

    /**
     * Setter de las opciones a elegir como rotaciones
     * @param rotacionesComboBox Se pasa una seleccion de rotaciones
     */
    public void setRotacionesComboBox(JComboBox<String> rotacionesComboBox) {
        this.rotacionesComboBox = rotacionesComboBox;
    }

    /**
     * Getter del array de posiciones ya atacadas
     * @return Devuelve el array de las posiciones previamente atacadas
     */
    public boolean[][] getYaAtacado() {
        return yaAtacado;
    }

    /**
     * Setter de las posiciones ya atacadas
     * @param yaAtacado Se pasa el array de las posiciones atacadas
     */
    public void setYaAtacado(boolean[][] yaAtacado) {
        this.yaAtacado = yaAtacado;
    }
}
