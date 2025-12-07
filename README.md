# ECOGAMES   
Minijuegos de reciclaje para Android

---

## Integrantes

Este proyecto fue hecho por:

- Juan José Huamaní Vásquez.  
- Gabriel Frank Zela Flores.  
- Melvin Jarred Yabar Carazas.

---

## Descripción del proyecto

**ECOGAMES** es una aplicación móvil desarrollada en **Kotlin (Android)** cuyo objetivo es enseñar a clasificar correctamente los residuos en los tachos adecuados, combinando juego y educación ambiental.

Actualmente la app incluye tres minijuegos:

- **Semáforo de Tachos**: aparece un residuo en pantalla y el jugador debe elegir el tacho correcto (orgánico, reciclable, no reciclable o peligroso).  
- **EcoMemory**: juego de memoria donde se deben emparejar residuos con el tacho del color correcto.  
- **Cinta Eco**: un residuo aparece sobre una “cinta transportadora” y el jugador debe arrastrarlo (drag & drop) hasta el tacho adecuado antes de que se acabe el tiempo.

Además, se guardan **historiales de partidas** (puntajes, tiempo, movimientos, aciertos, etc.) para que el usuario pueda ver su progreso y tratar de superarse.

---

## Funcionalidades principales

### Menú principal

- Acceso a:
  - **Semáforo de Tachos**  
  - **EcoMemory**  
  - **Cinta Eco**  
  - Reglas de los minijuegos (en un diálogo de ayuda)  
  - Salir de la aplicación

---

### Semáforo de Tachos

- Muestra un residuo y cuatro botones de tacho:
  - Marrón (orgánico)  
  - Verde (reciclable)  
  - Negro (no reciclable)  
  - Rojo (peligroso)
- Puntuación por aciertos y penalización por errores.
- Sistema de **vidas** y **tiempo limitado**.
- Pantalla de resultados que muestra:
  - Puntaje de la partida actual.  
  - Mejor puntaje histórico.  
  - Historial de partidas anteriores (RecyclerView).  
  - Opción para borrar el historial.

---

### EcoMemory

- Tablero tipo memoria con **cartas boca abajo**.
- Parejas formadas por:
  - Cartas de residuo.  
  - Cartas de tachos del color correcto.
- El jugador debe voltear cartas y encontrar parejas por **tipo de residuo**, no solo por imagen.
- Se lleva el registro de:
  - Movimientos realizados.  
  - Tiempo empleado.  
  - Puntaje final.
- Pantalla de resultados con:
  - Datos de la partida actual.  
  - Mejor puntaje histórico.  
  - Historial de partidas anteriores y opción para borrar ese historial.

---

### Cinta Eco

- Un residuo aparece en el centro de la pantalla sobre una “cinta”.
- El jugador debe **arrastrar (drag & drop)** el residuo hasta uno de los cuatro tachos:
  - Orgánico (marrón)  
  - Reciclable (verde)  
  - No reciclable (negro)  
  - Peligroso (rojo)
- Cada acierto:
  - Suma puntos.  
  - Aumenta una racha de aciertos.  
  - Hace que la cinta se **acelere** cada cierto número de aciertos.
- Cada error o residuo que se “escapa”:
  - Resta puntos.  
  - Resta una vida.
- Al quedarse sin vidas, se muestra una pantalla de resultados con:
  - Puntaje final.  
  - Número de aciertos.  
  - Vidas restantes.  
  - Historial de partidas de Cinta Eco y mejor puntaje histórico.  
  - Opción para borrar el historial.

---

### Historiales

- Guardado local de partidas usando repositorios:
  - `GameResultRepository` (Semáforo de Tachos)  
  - `MemoryResultRepository` (EcoMemory)  
  - `CintaResultRepository` (Cinta Eco)
- Uso de **SharedPreferences** + **JSON** para almacenar:
  - Puntaje.  
  - Fecha/hora (timestamp).  
  - Métricas específicas de cada juego (movimientos, tiempo, aciertos, vidas, etc.).
- Listado de partidas anteriores en pantallas de resultados mediante **RecyclerView**.
- Opción de **borrar historial** desde la interfaz de cada minijuego.

---

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin  
- **Entorno:** Android Studio  
- **Arquitectura básica:**
  - `MainActivity` como contenedor de fragments.  
  - **Fragments + Navigation Component** para la navegación entre pantallas.
- **Almacenamiento local:** SharedPreferences (con JSON) para historial de resultados.  
- **Interfaz:**
  - ConstraintLayout, GridLayout, CardView.  
  - RecyclerView para historiales y listas.  
  - AlertDialog para confirmaciones y reglas.  
- **Otras características:**
  - `CountDownTimer` para manejar tiempos de juego.  
  - Drag & Drop nativo de Android en **Cinta Eco** (`startDragAndDrop`, `OnDragListener`).

---

## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener:

- **Android Studio** instalado (versión reciente recomendada).  
- **GitHub Desktop** instalado para clonar el repositorio de manera sencilla.  
- Un emulador o dispositivo físico Android configurado (modo desarrollador activado).

---

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
5. Verificar que no haya errores de compilación.

### 3. Ejecutar la aplicación

1. Conectar un dispositivo Android físico o iniciar un emulador desde Android Studio.  
2. En la parte superior de Android Studio, elegir el dispositivo/emulador donde se va a ejecutar la app.  
3. Hacer clic en el botón **Run (Shift + F10)**.  
4. Esperar a que se instale y se abra la aplicación en el dispositivo elegido.

Una vez abierta la app, verás el **menú principal de ECOGAMES**, desde donde podrás probar:

- **Semáforo de Tachos**  
- **EcoMemory**  
- **Cinta Eco**  
- Ver las reglas  
- Salir de la aplicación

---

## Estructura básica del proyecto (resumen)

- `MainActivity.kt`  
  Activity principal que actúa como contenedor de los fragments.

- `MenuFragment.kt`  
  Menú principal con acceso a los minijuegos y reglas.

- `GameFragment.kt`  
  Lógica y UI del minijuego **Semáforo de Tachos**.

- `ResultFragment.kt`  
  Pantalla de resultados e historial de **Semáforo de Tachos**.

- `MemoryGameFragment.kt`  
  Lógica y UI del minijuego **EcoMemory**.

- `MemoryResultFragment.kt`  
  Pantalla de resultados e historial de **EcoMemory**.

- `CintaEcoFragment.kt`  
  Lógica y UI del minijuego **Cinta Eco** (drag & drop, temporizador, vidas, racha).

- `CintaResultFragment.kt`  
  Pantalla de resultados e historial de **Cinta Eco**.

- `data/`  
  - Repositorios para manejar el almacenamiento local:  
    - `GameResultRepository`  
    - `MemoryResultRepository`  
    - `CintaResultRepository`

- `adapter/`  
  - Adaptadores para RecyclerView (historial de partidas, resultados de Cinta Eco, cartas de memoria, etc.).

- `res/layout/`  
  - Layouts de fragments, ítems de RecyclerView y pantallas de resultados.



La idea es que cualquier persona que llegue al repositorio pueda:

1. Entender rápidamente de qué trata **ECOGAMES**.  
2. Saber cómo clonar el proyecto.  
3. Ejecutarlo sin perderse en la configuración.
