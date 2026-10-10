package Modelo;
//es la pose que tiene una entidad combatible pensada para que la
//vista elija la animacion que le corresponde. 
    public enum PoseEntidadCombatible {
    NORMAL(false),
    SENTADO(false),
    LEVANTANDOSE(true);   // animacion de una sola pasada (transicion)
 
    private final boolean transicion;
 
    PoseEntidadCombatible(boolean transicion) {
        this.transicion = transicion;
    }
 
    public boolean esTransicion() {
        return transicion;
    }
}
 
