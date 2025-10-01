
# Snake Game Multijugador

Este es un juego de Snake multijugador implementado en Java usando Sockets y Swing para la GUI.

## Estructura del Proyecto

El proyecto está organizado en los siguientes paquetes:

- `com.snakegame.main`: Contiene las clases principales para iniciar el servidor y el cliente.
- `com.snakegame.model`: Contiene las clases del modelo de datos del juego (Snake, Player, GameState, etc.).
- `com.snakegame.net`: Contiene las clases para la comunicación en red (servidor y cliente).
- `com.snakegame.gui`: Contiene las clases para la interfaz gráfica de usuario del cliente.

## Cómo Compilar

Para compilar el proyecto, puedes usar un IDE de Java (como IntelliJ IDEA o Eclipse) o compilarlo desde la línea de comandos. Asegúrate de tener el JDK 8 o superior instalado.

Desde la raíz del proyecto, ejecuta:

```bash
javac -d out src/main/java/com/snakegame/main/ServerMain.java src/main/java/com/snakegame/main/ClientMain.java
```

## Cómo Ejecutar

### 1. Iniciar el Servidor

Ejecuta el siguiente comando en la terminal:

```bash
java -cp out com.snakegame.main.ServerMain [puerto]
```

- `[puerto]` (opcional): El puerto en el que el servidor escuchará. Por defecto es `8080`.

### 2. Iniciar Clientes

Para cada jugador, abre una nueva terminal y ejecuta:

```bash
java -cp out com.snakegame.main.ClientMain [host] [puerto]
```

- `[host]` (opcional): La dirección IP del servidor. Por defecto es `localhost`.
- `[puerto]` (opcional): El puerto del servidor. Por defecto es `8080`.


## Cómo Jugar

- Usa las **flechas de dirección** (Arriba, Abajo, Izquierda, Derecha) para controlar la serpiente.
- El objetivo es comer la fruta (círculos rojos) para crecer y ganar puntos.
- El juego termina si una serpiente choca contra una pared, contra sí misma o contra otra serpiente.

