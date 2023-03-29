public class Casilla {

    private boolean barco;
    private int fila;
    private int columna;

    /**
     * Constructor de Casilla
     * @param i Se le pasa la que va a ser la fila
     * @param j Se le pasa la que va a ser la columna
     */
    public Casilla(int i, int j) {
        fila = i;
        columna = j;
    }

    /**
     * Constructor por defecto de Casilla
     */
    public Casilla(){

    }

    /**
     * Getter de si la casilla tiene barco
     * @return Devuelve si la casilla tiene o no barco
     */
    public boolean isBarco() {
        return barco;
    }

    /**
     * Setter del barco en la casilla
     * @param barco Se pasa true si tiene o no barco
     */
    public void setBarco(boolean barco){
        this.barco = barco;
    }

    /**
     * Getter de la Fila de la casilla
     * @return Devuelve la fila de la casilla
     */
    public int getFila() {
        return fila;
    }

    /**
     * Setter de la fila de la casilla
     * @param fila Se pasa la fila donde va a estar
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * Getter de la columna de la casilla
     * @return Devuelve la columna de la casilla
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Setter de la columna de la casilla
     * @param columna Se pasa la columna donde va a estar
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }
}
