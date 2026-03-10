# HundirFlota
Mi juego de hundir la flota con reglas personalizadas y con arquitectura tanto grafica como de red totalmente hechas a mano

# AGRADECIMIENTOS
Este proyecto ha sido el mayor proyecto que he hecho hasta la fecha 10/03/2026
no sera el mejor proyecto que vereis pero en este proyecto desarrollado desde el
05/11/2025 hasta ahora ha sido casi mi dia a dia y antes de contar sobre mi proyecto
quisiera dar las gracias a: Rocio Baez, Antonio, mi madre, mi padre y a mi pareja quienes por
desgracia tuvieron que aguantarme todo este tiempo sin saber hablar de otra cosa,
muchas gracias.

# PROYECTO HUNDIR LA FLOTA
Hundir la flota es un juego bastante conocido por todos pero yo lo quise llevar mas lejos
tuve que buscar la manera de poder conseguir un servidor en la nube gratuito ya que los
recursos economicos estan bastante limitados y gracias a Google Cloud pude subir mi servidor.

El codigo esta hecho totalmente por mi, no hubo cooperadores en este proyecto y la gran mayoria
de este proyecto esta hecho de forma autodidactica lo que son interfaces graficas, sockets e hilos,
pero no podria haber salido adelante sin las bases que me enseño mi profesora Rocio Baez, gracias a
sus insistencias en el clean code y muchas mas cosas cogi manias que permiten que sea un codigo legible
entonces gran mayoria que se encuentra en este proyecto fue construido mientras se aprendia.

La arquitectura de la aplicacion creo que es algo de lo que mas me costo, pero el funcionamiento es
sencillo, usted coge y se registra, haciendo un login bastante sencillo y normal en java con JBDC,
el cual controla bastantes aspectos como que el primer caracter de su nombre sea una letra, y algo
de lo mas importante el encriptamiento de la contraseña, actualmente uso md5 por la facilidad y el
nivel actual del proyecto si el proyecto siguiera escalando mejorare el metodo de encriptamiento, la
conexion con la base de datos la primera vez es la mas lenta ya que se esta creando esta misma pero
la segunda ya esta conectada y va mucho mas fluido, en el menu nos encontramos con unos niveles de 
bot, que el futuro actualizare el proyecto para que tenga una IA heuristica y pueda haber un singlePlayer
de momento no esta implementado, y tenemos la arquitectura de red el corazon de todo, uso logicamente un 
protocolo TCP para la comunicacion y lo que hago es una peticion al servidor diciendo si soy un host o un
hosteado y el servidor dependiendo de lo que sea me guarda en un mapa de las salas que hay con un codigo
de partida de 6 caracteres alphanumerico los cuales se generan de manera aleatoria cuando creas una partida
hace una comprobacion si existe esa sala cuando te quieres unir y los une a los 2 en un sistema de tunel 
bidireccional se lanzan 2 hilos en el que uno es el comunicador y en el otro eres el escuchador por lo que
el servidor solo hace un redireccionamiento de paquetes totalmente ciego si en el momento de la partida uno
de los jugadores se desconecta es decir que se va avisa al contrincante antes de irse y su rival lo llevara
al menu despues de un tiempo de cortesia unos 3 segundos.

Que te parece esto para el readme del proyecto
