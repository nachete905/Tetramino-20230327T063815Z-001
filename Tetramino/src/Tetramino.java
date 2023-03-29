public class Tetramino {
    private static String []tipos ={"Recta", "Zeta", "Zeta Invertida", "Ele", "Ele Invertida", "T", "Cuadrado"};// un array con los tipos de fichas que tenemos
    private String tipo;
    private int vida = 4;
    private int posicionx[] = new int[4];//un array de la posicionX con un tamaño de 4
    private int posiciony[] = new int[4];//un array de la posicionY con un tamaño de 4


    /**
     * Constructor de tetramino por defecto
     */
    public Tetramino (){
        tipo = tipos[6];
    }// estos son todos los tipos de fichas que tenemos
    //constructor para asignar la posición 0

    /**
     * Construtor del Tetramino al que se le asigna la primera posicion
     * @param tipo Se pasa el tipo de tetramino que va a ser
     * @param posicionx se le pasa la posicon incicial que es donde se pulsa el boton
     * @param posiciony se le pasa la posicon incicial que es donde se pulsa el boton
     */
    public Tetramino(String tipo, int posicionx, int posiciony) {
        this.tipo = tipo;
        this.posicionx[0] = posicionx;
        this.posiciony[0] = posiciony;
        this.vida = 4;
    }
    //constructor para pasar un array con posiciones

    /**
     * Constructor del tetramino al que se le pasan ya todas las posiciones que va a ocupar
     * @param tipo Se pasa el tipo que va a ser
     * @param posicionx Se pasa el array de sus posiciones X
     * @param posiciony Se pasa el array de sus posiciones Y
     */
    public Tetramino(String tipo, int [] posicionx, int [] posiciony){
        this.tipo = tipo;
        this.posicionx = posicionx;
        this.posiciony = posiciony;
        this.vida = 4;
    }
    //setter y getter

    /**
     * Getter del tipo que es el tetramino
     * @return tipo Devuelve el tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Setter del tipo del tetramino
     * @param tipo Se pasa el tipo que va a ser
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Getter de la vida restante del tetramino
     * @return vida Devuelve la vida restante
     */

    public int getVida() {
        return vida;
    }

    /**
     * Setter de la vida del tetramino
     * @param vida Se le pasa la cantidad de vida
     */

    public void setVida(int vida) {
        this.vida = vida;
    }

    /**
     * Getter de las posiciones X del tetramino
     * @return posicionx Devuelve las posiciones X
     */

    public int[] getPosicionx() {
        return posicionx;
    }

    /**
     * Setter de las posiciones X del tetramino según la posicion el en array deseada
     * @param posicionx Se le pasa la posicion que va a tener
     * @param indice Se le pasa el lugar que esa posicion va a tener el en array
     */

    public void setPosicionx(int posicionx, int indice) {
        this.posicionx[indice] = posicionx;
    }

    /**
     * Setter de todas las posiciones X del tetramino
     * @param posicionx Se le pasa el array con las posiciones
     */

    public void setPosicionx(int [] posicionx){
        this.posicionx=posicionx;
    }

    /**
     * Getter de las posiciones Y del tetramino
     * @return posiciony Devuelve las posiciones Y
     */

    public int[] getPosiciony() {
        return posiciony;
    }

    /**
     * Setter de las posiciones Y del tetramino según la posicion el en array deseada
     * @param posiciony Se le pasa la posicion que va a tener
     * @param indice Se le pasa el lugar que esa posicion va a tener el en array
     */


    public void setPosiciony(int posiciony, int indice) {
        this.posiciony[indice] = posiciony;
    }

    /**
     * Setter de todas las posiciones Y del tetramino
     * @param posiciony Se le pasa el array con las posiciones
     */
    public void setPosiciony(int [] posiciony){
        this.posiciony=posiciony;
    }
}


