# 🏡 Terminal Crossing

> Proyecto de programación orientada a objetos en Java para el grado superior en Desarrollo de Aplicaciones Web (DAW).

📖 **Guía completa del proyecto:** [https://terminalcrossingdoc.netlify.app/#/](https://terminalcrossingdoc.netlify.app/#/)

---

## 🎬 Demo

> El jugador crea su personaje en la ventana de inicio, entra a la aldea y explora el mapa en la terminal. En el vídeo se puede ver el movimiento por el mapa ASCII, que se redibuja cada vez que se pulsa la tecla enter después de cada acción, posicionando los elementos según las decisiones del jugador (moverse, interactuar, abrir un menú, etc). También se muestra a modo de ejemplo la interacción con árboles para recoger fruta, la captura de un insecto con la red y la consulta de la mochila y la recuperación de puntos de energía gastados tras comer una manzana.


https://github.com/user-attachments/assets/03123ec9-7f52-4728-82ba-812a1f5baa53


---

## 🛠️ Tecnologías utilizadas

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| **Java** | 24 | Lenguaje principal del proyecto |
| **Java Swing** | JDK built-in | Interfaz gráfica para la creación del personaje (`VentanaInicio`, `VentanaResumen`) |
| **org.json** | 20231013 | Serialización y lectura de archivos JSON |
| **JUnit Jupiter** | 5.10.0 | Framework de tests unitarios |
| **IntelliJ IDEA** | Ultimate | IDE de desarrollo |
| **Javadoc** | JDK built-in | Generación automática de documentación |
| **Docsify** | — | Generación de la guía del proyecto como sitio web |

---

## 📌 Descripción general

**Terminal Crossing** es un videojuego de simulación de vida que se ejecuta en la terminal. El jugador crea su personaje mediante una ventana gráfica (Swing), explora un mapa ASCII, recolecta frutas sacudiendo árboles, pesca con una caña, captura insectos con una red, gestiona su mochila y lleva el registro de las especies descubiertas en una enciclopedia. El juego guarda y lee el progreso del personaje en formato **JSON**.

---

## 🗂️ Estructura de paquetes

```
src/
├── Main.java
├── VentanaInicio.java
├── VentanaResumen.java
├── ConfiguracionJSON.java
├── coleccionables/
│   ├── Objeto.java          ← clase padre
│   ├── Fruta.java
│   ├── Insecto.java
│   ├── Pez.java
│   ├── Herramienta.java
│   ├── ArbolFrutal.java
│   ├── Mochila.java
│   └── TipoHerramienta.java ← enum
├── personajes/
│   ├── Personaje.java       ← clase padre abstracta
│   ├── Pj.java
│   ├── Npc.java
│   └── EstadoPersonaje.java ← enum
├── controlador/
│   ├── Controlador.java
│   ├── Catalogo.java
│   └── InventarioControlador.java
├── mundo/
│   ├── Mundo.java
│   └── Celda.java
└── excepciones/
    ├── NombreInvalidoException.java
    ├── MochilaLlenaException.java
    ├── HerramientaRotaException.java
    └── EnergiaInsuficienteException.java

test/
├── ObjetoTest.java
├── HerramientaTest.java
├── MochilaTest.java
└── PjTest.java
```

---

## ✅ Cumplimiento de requisitos

### Requisitos generales

| Requisito | Cumplimiento |
|-----------|-------------|
| Proyecto con sentido realista | ✅ Simulación de vida tipo *Animal Crossing* |
| Al menos 10 clases diferentes | ✅ 15 clases + 2 enums |
| Al menos 2 paquetes | ✅ `coleccionables`, `personajes`, `controlador`, `mundo`, `excepciones` |
| Cada clase con ≥4 atributos, constructores, getters/setters y toString | ✅ Todas las clases lo implementan |
| ≥2 atributos enteros, decimales, booleanos y String en el proyecto | ✅ Presentes en múltiples clases |
| ≥2 atributos enumerados | ✅ `TipoHerramienta` y `EstadoPersonaje` |
| ≥2 clases padre y ≥4 hijas | ✅ `Objeto` → `Fruta`, `Insecto`, `Pez`, `Herramienta`, `ArbolFrutal`, `Mochila` / `Personaje` → `Pj`, `Npc` |
| ≥2 objetos de cada clase en `Main` | ✅ Se instancian 2 objetos de cada clase |

---

### UD3 — Estructuras de control

| Requisito | Cumplimiento |
|-----------|-------------|
| `equals` y `hashCode` en todas las clases | ✅ Implementados en `Objeto`, `Herramienta`, `Mochila`, `Fruta`, `Insecto`, `Pez`, `ArbolFrutal`, `Personaje`, `Pj`, `Npc` |
| ≥4 atributos de tipo lista (`ArrayList` / `HashSet`) | ✅ `Mochila` usa `LinkedHashMap<String, List<Objeto>>`; `Controlador` usa `List<String> mensajes`; `Catalogo` tiene 2 `LinkedHashMap` |
| Métodos para buscar, añadir, borrar y modificar en listas | ✅ `guardar()`, `sacar()`, `contiene()`, `getNombresGrupos()` en `Mochila`; `registrarInsecto()`, `registrarPez()` en `Catalogo` |
| ≥2 atributos `HashMap` | ✅ `insectosRegistrados` y `pecesRegistrados` en `Catalogo` (ambos `LinkedHashMap`) |
| Métodos para gestionar los HashMap | ✅ `registrarInsecto()`, `registrarPez()`, `getLineasCatalogo()` en `Catalogo` |
| ≥4 estructuras condicionales | ✅ Múltiples `if/else` y `switch` en `Controlador`, `Celda`, `Mochila`, `Pj` y otros |
| ≥4 bucles | ✅ Bucles `for`, `while` y `for-each` en `Controlador`, `Mundo`, `Mochila`, `InventarioControlador` y otros |

---

### UD4 — Depuración y pruebas

| Requisito | Cumplimiento |
|-----------|-------------|
| ≥4 excepciones personalizadas | ✅ `NombreInvalidoException`, `MochilaLlenaException`, `HerramientaRotaException`, `EnergiaInsuficienteException` |
| Cada excepción se lanza y captura | ✅ Las 4 se lanzan y capturan en `Main` y en sus clases correspondientes |
| ≥4 clases con ≥5 tests cada una | ✅ `ObjetoTest`, `HerramientaTest`, `MochilaTest`, `PjTest` |
| ≥5 casos negativos en total | ✅ Tests que verifican el lanzamiento de excepciones en casos inválidos |

---

### UD5 — Interfaces gráficas y ficheros

| Requisito | Cumplimiento |
|-----------|-------------|
| Ventana con formulario | ✅ `VentanaInicio`: formulario con campo de nombre y selector de personaje |
| Segunda ventana que procesa los datos del formulario | ✅ `VentanaResumen`: muestra el personaje elegido leído desde el JSON y lanza el juego |
| Escritura de archivo JSON | ✅ `ConfiguracionJSON.guardar()` escribe `saves/personaje.json` con la librería `org.json` |
| Lectura de archivo JSON | ✅ `ConfiguracionJSON.leer()` lee y parsea `saves/personaje.json` |

---

## 🧩 Clases y herencia

### Jerarquía de coleccionables

```
Objeto  (padre)
├── Fruta
├── Insecto
├── Pez
├── Herramienta
├── ArbolFrutal
└── Mochila
```

### Jerarquía de personajes

```
Personaje  (padre abstracto)
├── Pj      (jugador controlado por el usuario)
└── Npc     (personajes del pueblo)
```

---

## 🔢 Enumerados

| Enum | Valores | Uso |
|------|---------|-----|
| `TipoHerramienta` | `RED`, `CAÑA`, `PALA`, `DESCONOCIDO` | Tipo de herramienta que lleva el jugador |
| `EstadoPersonaje` | `ACTIVO`, `DORMIDO`, `AGOTADO`, `AUSENTE` | Estado actual de cualquier personaje |

---

## ⚠️ Excepciones personalizadas

| Excepción | Se lanza cuando... |
|-----------|-------------------|
| `NombreInvalidoException` | Se crea un objeto o personaje con nombre vacío o nulo |
| `MochilaLlenaException` | Se intenta guardar un objeto en una mochila sin espacio |
| `HerramientaRotaException` | Se intenta usar una herramienta con durabilidad 0 |
| `EnergiaInsuficienteException` | El personaje intenta actuar con energía insuficiente |

---

## 🧪 Tests

| Clase de test | Clases probadas | Casos negativos incluidos |
|---------------|-----------------|--------------------------|
| `ObjetoTest` | `Objeto` | ✅ Nombre inválido, precio negativo |
| `HerramientaTest` | `Herramienta` | ✅ Uso con durabilidad 0, nombre vacío |
| `MochilaTest` | `Mochila` | ✅ Mochila llena, guardar con capacidad 0 |
| `PjTest` | `Pj`, `Personaje` | ✅ Movimiento sin energía, nombre nulo |

---

## 🖥️ Interfaz gráfica

El flujo de inicio del juego es el siguiente:

1. **`VentanaInicio`** — formulario Swing donde el jugador introduce su nombre y elige su skin (emoji). Al confirmar, los datos se guardan en `saves/personaje.json`.
2. **`VentanaResumen`** — ventana que lee el JSON guardado, muestra el personaje elegido y un botón para lanzar el juego en la terminal.
3. Al pulsar "¡A jugar!", se cierra la ventana y arranca el `Controlador` con el mapa en la terminal.

---

## 💾 Persistencia JSON

Los datos del personaje se guardan automáticamente al iniciar una partida. El archivo generado tiene el siguiente formato:

```json
{
  "nombre": "Alex",
  "skin": "🦝"
}
```

La clase `ConfiguracionJSON` gestiona la escritura y lectura usando la librería **org.json**.

---

## 🎮 Controles del juego

| Tecla | Acción |
|-------|--------|
| `W` / `A` / `S` / `D` | Mover al personaje |
| `F` | Interactuar con el entorno (recoger, pescar, sacudir árbol...) |
| `E` | Comer una fruta de la mochila |
| `I` | Abrir la mochila |
| `C` | Abrir la enciclopedia |
| `Q` | Salir del juego |

---

## 📚 Dependencias

| Librería | Versión | Uso |
|----------|---------|-----|
| `org.json` | 20231013 | Lectura y escritura de JSON |
| `JUnit Jupiter` | 5.10.0 | Tests unitarios |

---

## ▶️ Cómo ejecutar

1. Abre el proyecto en **IntelliJ IDEA**.
2. Asegúrate de que las librerías de la carpeta `lib/` están añadidas al classpath.
3. Ejecuta la clase `Main`.
4. Se abrirá la ventana de creación de personaje. ¡A jugar!

---


## 👤Autores

- [Daaniel-Sans](https://github.com/Daaniel-Sans)
- [deterry69](https://github.com/deterry69)
- [diegojosealonso](https://github.com/diegojosealonso)
- [whoischabola](https://github.com/whoischabola)

