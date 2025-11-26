# ECOGAMES   
Minijuegos de reciclaje para Android



## Descripción del proyecto

**ECOGAMES** es una aplicación móvil desarrollada en **Kotlin (Android)** cuyo objetivo es enseñar a clasificar correctamente los residuos en los tachos adecuados, combinando juego y educación ambiental.

Actualmente la app incluye:

- **EcoMemory**: juego de memoria donde se deben emparejar residuos con su tacho correspondiente.  
- **Semáforo de Tachos**: aparece un residuo en pantalla y el jugador debe elegir el tacho correcto (orgánico, reciclable, no reciclable o peligroso).  

Además, se guardan **historiales de partidas** (puntajes, tiempo, movimientos, etc.), para que el usuario pueda ver su progreso y tratar de superarse.


## Funcionalidades principales

- Menú principal con acceso a:
  - EcoMemory  
  - Semáforo de Tachos  
  - Reglas de los minijuegos  
  - Salir de la aplicación

- **EcoMemory**
  - Tablero tipo memoria (cartas boca abajo).
  - Parejas formadas por: residuo + tacho correcto.
  - Contador de movimientos, tiempo y puntaje.
  - Pantalla de resultados con historial y mejor puntaje.

- **Semáforo de Tachos**
  - Muestra un residuo y cuatro botones de tacho (marrón, verde, negro, peligroso).
  - Puntuación por aciertos y penalización por errores.
  - Temporizador y pantalla de resultados con historial.

- **Historiales**
  - Guardado local de partidas usando repositorios (`GameResultRepository`, `MemoryResultRepository`).
  - Listado de partidas anteriores.
  - Posibilidad de borrar el historial desde la interfaz.


## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin  
- **Entorno:** Android Studio  
- **Arquitectura básica:** Fragments + Navigation Component  
- **Almacenamiento local:** SharedPreferences con JSON para historial de resultados  
- **Interfaz:** RecyclerView, AlertDialog, ConstraintLayout / GridLayoutManager  


## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener:

- **Android Studio** instalado (versión reciente recomendada).  
- **GitHub Desktop** instalado para clonar el repositorio de manera sencilla.  
- Un emulador o dispositivo físico Android configurado.


## Cómo clonar y ejecutar el proyecto

A continuación se describen los pasos para clonar el repositorio con **GitHub Desktop** y luego abrirlo en **Android Studio**.

### 1. Clonar el repositorio con GitHub Desktop

1. Abrir **GitHub Desktop**.  
2. Ir a **File → Clone repository…**  
3. En la pestaña **URL**, pegar la URL del repositorio:  
   `https://github.com/JuanHuamaniVasquez/ProyectoFinal-Ecogames.git`  
4. Elegir una carpeta local donde se descargará el proyecto.  
5. Hacer clic en **Clone** y esperar a que termine la descarga.


### 2. Abrir el proyecto en Android Studio

1. Abrir **Android Studio**.  
2. Seleccionar **“Open an Existing Project”** (Abrir proyecto existente).  
3. Navegar hasta la carpeta donde GitHub Desktop clonó el repositorio y seleccionar la carpeta del proyecto.  
4. Esperar a que Android Studio:
   - Importe el proyecto.
   - Ejecute la sincronización de Gradle.
5. Una vez cargado, verificar que no haya errores de compilación.

### 3. Ejecutar la aplicación

1. Conectar un dispositivo Android físico con modo desarrollador habilitado o iniciar un emulador desde Android Studio.  
2. En la parte superior de Android Studio, elegir el dispositivo/emulador donde se va a ejecutar la app.  
3. Hacer clic en el botón **Run (Shift + F10)**.  
4. Esperar a que se instale y se abra la aplicación en el dispositivo elegido.

Una vez abierta la app, verás el **menú principal de ECOGAMES**, desde donde podrás probar:

- **EcoMemory**  
- **Semáforo de Tachos**  
- Ver las reglas  
- Salir de la aplicación


## Estructura básica del proyecto (resumen)

- `MainActivity.kt`  
  Activity principal que actúa como contenedor de los fragments.

- `MenuFragment.kt`  
  Menú principal con acceso a los minijuegos y reglas.

- `GameFragment.kt`  
  Lógica y UI del minijuego **Semáforo de Tachos**.

- `MemoryGameFragment.kt`  
  Lógica y UI del minijuego **EcoMemory**.

- `ResultFragment.kt` y `MemoryResultFragment.kt`  
  Pantallas de resultados e historiales para cada minijuego.

- `data/`  
  - Modelos de datos (`GameResult`, `MemoryResult`, `MemoryCard`, etc.).  
  - Repositorios (`GameResultRepository`, `MemoryResultRepository`) para manejar el almacenamiento local.

- `adapter/`  
  Adaptadores para RecyclerView (historiales, cartas de memoria, etc.).


## Mantenimiento del README

Este archivo **README.md** se mantendrá actualizado a medida que:

- Se agreguen nuevos minijuegos (por ejemplo, **Cinta Eco**).  
- Cambie la URL del repositorio o la estructura del proyecto.  
- Se modifiquen los pasos de ejecución o los requisitos.

La idea es que cualquier persona que llegue al repositorio pueda:

1. Entender rápidamente de qué trata **ECOGAMES**.  
2. Saber cómo clonar el proyecto.  
3. Ejecutarlo sin perderse en la configuración.

