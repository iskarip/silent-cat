# Proyecto: Silent Cat

## 1. Integrantes del Equipo 

- Godoy, Aldana 
- Lacho, Melisa  
- Pardo, Iskari 
- Ugarte, Micaela

## 2. Dominio y Alcance del Sistema 

### Descripcion del problema 

Se buscara desarollar un videojuego de escritorio basandose en conceptos relacionados con el juego "Silent Hill". La tematica se basara en la eleccion de uno de los dos personajes que ofrecemos,junto con un tercer personaje, el cual sera su mascota - un gato -. Se buscara desarrollar un juego de estrategia y habilidades mediante la resolucion de acertijos para cumplir con el objetivo. 

## Objetivo del Sistema 

El sistema sera un juego que permitira al usuario la eleccion de uno de los dos personajes, teniendo como objetivo la busqueda del tercer personaje (su amigo gatuno) recorriendo un mapa - mansion terrorifica -. La finalidad es pasar 3 habitaciones a lo largo del juego, en donde el requisito para acceder al proximo nivel (un nivel por habitacion) es la resolucion de acertijos dinamicos. El usuario gana el juego cuando encuentra a su gato antes de perder sus 3 vidas. 

### Funcionalidades Principales (Features)

-**Personaje**

    -El personaje carga con una linterna con bateria limitada. 
    -El personaje tiene un artefacto para defensa personal en caso de ataque.
    -Se contara con una vida limitada, en donde el personaje tendra 3 intentos para lograr el objetivo.  
    -Se desarollara un "checkpoint" por habitacion / por nivel. (el progreso del jugador se guarda en caso de que su vida termine)

-**Enemigos** 

    -Caracteristicas del enemigo prontas a desarrollar. Por el momento hemos definidio: 
        -Objetivo: impedir que el personaje continue con su objetivo.
        -Personajes de aspecto feo y tenebrosos.
        -Dañaran la vida del personaje, decrementando los intentos que tiene para lograr el objetivo. 
        -Tiene vida limitada. 

-**Interfaz grafica (IGU)** 

    -Pantalla principal --> jugar, reiniciar desde checkpoint, salir. 
    -Pantalla de juego --> escenario de juego, personaje, puntos de vida, boton de salir. 

-**Persistencia** 

    -Estado de partida --> para guardar la partida y poder continuar en otro momento.  

## 3. Imagenes representativas de la idea planteada. 

![Prototipo de IGu](./imagen/pantalla_juego.jpg)

## 4. Diagrama de Clases UML (Conceptual)

Enlace a CANVA: https://canva.link/c4rd5agxm72r0ff

## 5. Implementacion de Herencia y Polimorfismo

### Herencia de nuestro código: ###

**ENTIDAD**

De la clase padre Entidad, sus clases hijas: Personaje y Enemigo. Estas clases comparten 2 atributos puntosVida, danioBase y un método Mover. Lo hicimos de ese modo porque vimos que tanto el personaje como los enemigos necesitan tener vida, poder atacar con cierta fuerza y moverse por el mapa.
Entonces Entidad maneja eso y las subclases (clases hijas) personaje y enemigo lo heredan. 

**Super clase Entidad.java**

    public Entidad (int puntosVida) {
        this.puntosVida = puntosVida;
    }

**SubClase Personaje.java**

    public Personaje (String nombrePersonaje, Arma arma, Linterna linterna){
    super(100); //reutiliza el constructor de entidad para asignar la vida this.nombrePersonaje = nombrePersonaje;
            this.nivelEstamina = 100;
            this.arma = arma;
            this.linterna = linterna;
            this.inventario = new Inventario();
    }

**SubClase Enemigo.java**

    public Enemigo(int puntosVida, int danioBase, int idEnemigo){
    super(puntosVida); // reutiliza el constructor de entidad para asignar vida
        this.danioBase= danioBase;
        this.idEnemigo= idEnemigo;
    }

**ITEM**

Otro ejemplo de herencia de nuestro codigo esta en la clase padre Item y sus subclases ItemBateria e ItemMedicina. Item comparte los atributos basicos: de nombre y recogido(booleano). Y los metodos getNombre, isRecogido, serRecogido(valor), recoger(), interactuar(Personaje p).

**Super Clase Item.java**

    public Item (String nombre){
        this.recogido = false;
        this.nombre = nombre;
    }

**SubClase ItemMedicina.java**

    public itemMedicina(String nombre,int curacion){
        super (nombre); //reutilixa el constructor de Item
    this.curacion = curacion;
    }

**Sub Clase Item.Bateria.java**

    public itemBateria(){
        super("Bateria"); //reutiliza el constructor de Item con un nombre fijo
        cantidadCarga = 10;
    }

**ACERTIJO**

Nuestra clase Acertijo (abstracta) se encarga de todo lo que es común a cualquier acertijo del juego: guardar si ya fue resuelto, mostrar el enunciado con mostrarEnunciado(), y validar la respuesta del jugador con validarRespuesta(). Pero este último método no compara nada por su cuenta, internamente llama a verificarRespuesta(String), que está declarado como abstracto, obligando a cada acertijo hijo a implementarlo a su manera.

 **Super Clase Acertijo.java**

    protected abstract boolean verificarRespuesta(String respuesta);


**SubClase AcertijoObjeto.java**

    @Override
    protected boolean verificarRespuesta(String respuesta) {
        return this.respuesta.equalsIgnoreCase(respuesta);
    }

**SubClase AcertijoNumerico.java**

    @Override
        protected boolean verificarRespuesta(String respuesta) {
        try {
            int intento = Integer.parseInt(respuesta);
            return intento == this.respuesta;
            }  catch (NumberFormatException error) { // para que el juego no se rompa si ingresan algo distinto a un numero
            return false;
            }        
        }

### Polimorfismo de nuestro código: ###

**Método atacar() compartido de Entidad a Personaje y Enemigo**

Nuestro metodo atacar( Entidad objetivo) declarado abstracto en nuestra clase padre Entidad, obliga a sus hijos a implementarlo de diferente manera.
Personaje.atacar calcula el daño a partir de Arma(arma.calcularDanio()) y solo golpea si entra al condicional if. Es decir si el arma lo permite (if (arma.usarArma())

En cambio Enemigo.atacar() golpea directamente con el danioBase, sin pasar por ningún arma. 

**Interactuar() en interfaz Interactuable**

Se comparte la interfaz Interactuable donde definimos un unico metodo interactuar(Personaje p) donde cada clase que implemente esa interfaz actua de diferente manera sobre ella. 
*Item.interactuar()  recoge el objeto y lo guarda en el inventario.
*Medicina.interactuar() sobreescribe eso y en cambio cura al personaje mediante (aplicarCuracion(p)).
*itemBateria.interactuar() sobreescribe eso y recarga la linterna(aplicarCarga(p)).
*Acertijo.interactuar() junto a sus hijos (acertijoObjeto y acertijoNumerico) muestra el enunciado del acertijo.

Esto quedaría demostrado al momento en el que el personaje interactue con el objeto, no le preguntaría que es, simplemente le decimos que interactue y cada clase responde, la medicina va a curar y la batería recarga. 

    package Modelo;


    public class Enemigo extends Entidad {
    
    //como vamos a tener diferentes enemigos definimos usar id enemigo
    //para diferenciar lo que hace cada uno.


    //--ATRIBUTOS--
    private int posicionX;
    private int posicionY;
    private int idEnemigo;
    private int direccion = 1;
    //--CONSTRUCTOR--


    public Enemigo(int puntosVida, int danioBase, int idEnemigo){
    super(puntosVida, danioBase);


    this.idEnemigo= idEnemigo;
    this.posicionX= posicionX;
    this.posicionY= posicionY;


    }


    //--GET Y SET--


    public int getIdEnemigo(){
    return this.idEnemigo;
    }


    public void setIdEnemigo(int idEnemigo){
    this.idEnemigo = idEnemigo;
    }


    //uso de override para los metodos heredados, atacar y recibirDanio


    @Override
    public void atacar(Entidad objetivo){
    if(objetivo !=null){
        System.out.println("El enemigo " + this.idEnemigo + " ataca y hace" + this.danioBase + " de daño. ");
        objetivo.recibirDanio(this.danioBase);
    }  
    }


    //metodo propio del enemigo (mover, patrullar)


    public void moverHaciaJugador(){
    System.out.println("El enemigo " + this.idEnemigo + " se mueve hacia la posicion del jugador. ");
    }


    public void patrullar(){
    System.out.println("El enemigo " + this.idEnemigo + "esta patrullando la zona cercana al jugador. ");
    }


    public void moverEnemigo(int deltaX,int deltaY){
    this.posicionX += deltaX;
    this.posicionY += deltaY;
    }

    }


















     








