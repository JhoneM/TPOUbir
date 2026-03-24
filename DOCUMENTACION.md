# Ubir -- Documentacion del Proyecto

**Materia:** Patrones de Diseno de Software
**Universidad Argentina de la Empresa (UADE)**
**Lenguaje:** Java

---

## 1. Descripcion del problema

La empresa de servicios de transporte **Ubir** solicita el diseno y desarrollo de una aplicacion que permita a los usuarios realizar viajes de manera eficiente, conectando pasajeros con choferes disponibles en la plataforma.

Las funcionalidades requeridas por el enunciado son las siguientes:

- **Registro y login de usuarios:** cada usuario puede crear una cuenta como Pasajero o como Chofer. Para los choferes es indispensable contar con una licencia de conducir valida.
- **Billetera virtual:** sirve como medio de pago para los viajes. En el caso de los choferes, alli se acredita el total del viaje menos la comision de la plataforma (15%).
- **Recarga de billetera:** puede realizarse mediante tarjeta de credito o PayPal.
- **Registro de vehiculos (choferes):** es obligatorio ingresar patente, fecha de VTV, marca y modelo del vehiculo.
- **Solicitud de viaje (pasajeros):** el pasajero indica origen y destino. El sistema busca choferes dentro de un rango de distancia en kilometros. Cada chofer notificado puede aceptar o rechazar la solicitud. Si todos la rechazan, el pasajero debe volver a solicitarla (eventualmente con un rango ampliado).

El objetivo academico del ejercicio es aplicar **patrones de diseno** (Strategy, Observer, Facade) y **principios de programacion orientada a objetos** (abstraccion, encapsulamiento, herencia, polimorfismo) junto con los **principios GRASP** para lograr un diseno cohesivo, desacoplado y extensible.

---

## 2. Diagrama de clases (descripcion textual)

### 2.1 Jerarquia de herencia

```
Usuario (abstracta)
  |-- Pasajero
  |-- Chofer  (implementa ObservadorViaje)
```

`Usuario` es la clase abstracta base que contiene los atributos comunes (nombre, email, contrasenia, billetera) y los metodos `pagarViaje()` y `cobrarViaje()`. `Pasajero` y `Chofer` la extienden, anadiendo cada uno sus atributos y comportamientos especificos.

### 2.2 Implementacion de interfaces

| Interfaz | Implementaciones concretas |
|---|---|
| `MetodoPago` (paquete `payment`) | `RecargaTarjetaCredito`, `RecargaPayPal` |
| `ObservadorViaje` (paquete `observer`) | `Chofer` (paquete `model`) |

### 2.3 Asociaciones principales

- **Usuario** tiene-una **Billetera** (composicion: se crea en el constructor de `Usuario`).
- **Chofer** tiene-un **Vehiculo** (asociacion: puede ser `null` hasta que se registre).
- **Chofer** tiene-un **Viaje** pendiente (`viajePendiente`) que recibe via notificacion.
- **Viaje** tiene-un **Pasajero** (quien lo solicita) y tiene-un **Chofer** (quien lo acepta; `null` hasta la aceptacion).
- **Viaje** mantiene una lista de **Chofer** rechazantes (`chofersRechazantes`).
- **Billetera** usa-un **MetodoPago** como parametro en `recargar()` (dependencia, no atributo).
- **NotificadorViajes** mantiene una lista de **ObservadorViaje** suscriptos.

### 2.4 Capa de servicios (Facade)

```
SistemaUbir (Facade)
  |-- UsuarioService    (registro y autenticacion)
  |-- VehiculoService   (registro de vehiculos)
  |-- ViajeService      (ciclo de vida de viajes)
```

`SistemaUbir` es el punto de entrada unico. Delega cada operacion al servicio correspondiente. `Main` interactua exclusivamente con `SistemaUbir`.

### 2.5 Organizacion por paquetes

```
src/
  model/        --> Usuario, Pasajero, Chofer, Viaje, Vehiculo, Billetera, EstadoViaje
  payment/      --> MetodoPago, RecargaTarjetaCredito, RecargaPayPal
  observer/     --> ObservadorViaje, NotificadorViajes
  service/      --> SistemaUbir, UsuarioService, VehiculoService, ViajeService
  Main.java     --> Punto de entrada y demo
```

---

## 3. Patrones de diseno aplicados

### 3.1 Strategy Pattern -- Metodos de recarga de billetera

**Problema que resuelve:**
La billetera virtual necesita soportar multiples medios de recarga (tarjeta de credito, PayPal) y debe poder incorporar nuevos medios en el futuro sin modificar el codigo existente.

**Participantes:**

| Rol en el patron | Clase/Interfaz |
|---|---|
| Strategy (interfaz) | `MetodoPago` |
| ConcreteStrategy A | `RecargaTarjetaCredito` |
| ConcreteStrategy B | `RecargaPayPal` |
| Context | `Billetera` |

**Justificacion:**
El patron Strategy permite encapsular cada algoritmo de recarga en su propia clase, desacoplando a `Billetera` del conocimiento de como se procesa cada tipo de pago. Si en el futuro se agrega un nuevo medio (por ejemplo, MercadoPago), basta con crear una nueva clase que implemente `MetodoPago`, sin modificar `Billetera` ni las estrategias existentes. Esto respeta el principio Open/Closed (abierto a la extension, cerrado a la modificacion).

Uso desde `Main`:

```java
MetodoPago tarjeta = new RecargaTarjetaCredito("4111111111111111", "Ana Garcia");
ana.getBilletera().recargar(500.0, tarjeta);

MetodoPago paypal = new RecargaPayPal("ana.garcia@paypal.com");
ana.getBilletera().recargar(300.0, paypal);
```

---

### 3.2 Observer Pattern -- Notificacion de viajes a choferes

**Problema que resuelve:**
Cuando un pasajero solicita un viaje, el sistema debe notificar a todos los choferes disponibles dentro de un rango de distancia. Se requiere que los choferes puedan suscribirse y desuscribirse dinamicamente, y que el pasajero no necesite conocer a los choferes individuales.

**Participantes:**

| Rol en el patron | Clase/Interfaz |
|---|---|
| Observer (interfaz) | `ObservadorViaje` |
| ConcreteObserver | `Chofer` |
| Subject | `NotificadorViajes` |

**Justificacion:**
El patron Observer desacopla al emisor de la notificacion (el sistema, representado por `NotificadorViajes`) de los receptores (los choferes). Permite agregar o quitar choferes del pool de notificacion en tiempo de ejecucion sin modificar la logica de solicitud de viaje.

Una alternativa seria iterar directamente sobre la lista de choferes en `ViajeService`, pero esto acoplaria fuertemente al servicio con la clase `Chofer` y eliminaria la posibilidad de tener otros tipos de observadores en el futuro (por ejemplo, un sistema de monitoreo).


---

### 3.3 Facade Pattern -- SistemaUbir

**Problema que resuelve:**
El sistema tiene multiples servicios internos (`UsuarioService`, `VehiculoService`, `ViajeService`) con diferentes responsabilidades. El cliente no debe conocer ni depender de la estructura interna de estos servicios.

**Participantes:**

| Rol en el patron | Clase |
|---|---|
| Facade | `SistemaUbir` |
| Subsistemas | `UsuarioService`, `VehiculoService`, `ViajeService` |
| Client | `Main` |

**Justificacion:**
El patron Facade proporciona una interfaz simplificada y unificada sobre los subsistemas. `Main` interactua exclusivamente con `SistemaUbir`, sin necesidad de instanciar ni coordinar servicios individuales. Esto reduce el acoplamiento del cliente con la capa de servicios y facilita la evolucion independiente de cada subsistema.

Sin Facade, `Main` tendria que instanciar cada servicio, pasarles dependencias manualmente y conocer la API de cada uno, lo cual produciria un alto acoplamiento.

---

## 4. Principios de Programacion Orientada a Objetos aplicados

### 4.1 Abstraccion

La clase `Usuario` es **abstracta** y define el contrato comun para todos los tipos de usuario del sistema: atributos compartidos (`nombre`, `email`, `contrasenia`, `billetera`) y comportamientos genericos (`pagarViaje()`, `cobrarViaje()`, `verificarContrasenia()`).

Las subclases `Pasajero` y `Chofer` no necesitan reimplementar la logica de pagos; solo agregan los atributos y comportamientos propios de su rol.

### 4.2 Encapsulamiento

Los campos de todas las clases son **privados** (o `protected` en `Usuario`, accesible solo por subclases). El acceso se controla mediante metodos especificos que expresan la intencion del negocio:

- `Viaje.setEstado()` es **privado**: el estado solo cambia a traves de los metodos `aceptar()` y `finalizar()`, que validan las transiciones permitidas.
- `Chofer` no expone un metodo `setDisponible(boolean)`. En su lugar ofrece `marcarOcupado()` y `marcarDisponible()`, que comunican la semantica del negocio y evitan asignaciones arbitrarias.

### 4.3 Herencia

La jerarquia `Usuario -> Pasajero / Chofer` permite reutilizar el codigo comun (gestion de billetera, verificacion de credenciales) y especializar el comportamiento en cada subclase:

- `Pasajero` hereda la totalidad de `Usuario` sin agregar logica adicional significativa.
- `Chofer` hereda de `Usuario` y ademas implementa la interfaz `ObservadorViaje`, agrega atributos como `licenciaConducir`, `vehiculo`, `ubicacionKm` y `disponible`.

### 4.4 Polimorfismo

Se aplica polimorfismo en dos puntos centrales:

**En el Strategy Pattern:** `Billetera.recargar()` recibe un parametro de tipo `MetodoPago` (la interfaz). En tiempo de ejecucion, se ejecuta la implementacion concreta (`RecargaTarjetaCredito` o `RecargaPayPal`) sin que `Billetera` conozca cual es.

**En el Observer Pattern:** `NotificadorViajes.notificarTodos()` itera sobre una lista de `ObservadorViaje` (la interfaz). Cada elemento es un `Chofer` concreto, pero el `NotificadorViajes` no depende de la clase `Chofer`.

---

## 5. Principios GRASP aplicados

### 5.1 Information Expert (Experto en informacion)

*"Asignar la responsabilidad a la clase que tiene la informacion necesaria para cumplirla."*

- **`Viaje`** controla sus propias transiciones de estado mediante los metodos `aceptar()` y `finalizar()`. Es la clase que conoce su estado actual y puede validar si la transicion es valida.
- **`Usuario`** (y por herencia, `Pasajero` y `Chofer`) controla `pagarViaje()` y `cobrarViaje()`, ya que posee la referencia a su propia `Billetera`.
- **`Billetera`** controla las operaciones `recargar()`, `debitar()` y `acreditar()`, ya que es la clase que conoce el `saldo`.

### 5.2 Creator (Creador)

*"Asignar la creacion de un objeto a la clase que tiene la informacion necesaria para inicializarlo."*

- **`ViajeService`** crea instancias de `Viaje`, ya que posee el contexto necesario: el pasajero solicitante, el origen, destino y monto.
- **`UsuarioService`** crea instancias de `Pasajero` y `Chofer`, ya que es el servicio que gestiona el registro y valida precondiciones (email duplicado, licencia).
- **`VehiculoService`** crea instancias de `Vehiculo` y lo asigna al chofer correspondiente.

### 5.3 Controller

*"Asignar la responsabilidad de recibir y coordinar las operaciones del sistema a un objeto que represente el sistema o un caso de uso."*

- **`SistemaUbir`** actua como controlador (y Facade). Recibe las solicitudes del cliente (`Main`) y las delega al servicio correspondiente. `Main` no interactua directamente con ningun servicio ni con la logica de negocio.

### 5.4 Low Coupling (Bajo acoplamiento)

*"Minimizar las dependencias entre clases."*

- Cada servicio depende unicamente de lo que necesita: `VehiculoService` solo conoce `Chofer` y `Vehiculo`; `ViajeService` conoce `Viaje`, `Chofer`, `Pasajero` y `NotificadorViajes`.
- `Billetera` depende de la interfaz `MetodoPago`, no de las implementaciones concretas.
- `NotificadorViajes` depende de la interfaz `ObservadorViaje`, no de `Chofer` directamente.
- `Main` solo depende de `SistemaUbir` (la Facade).

### 5.5 High Cohesion (Alta cohesion)

*"Cada clase debe tener una responsabilidad clara y bien definida."*

| Clase/Servicio | Responsabilidad unica |
|---|---|
| `UsuarioService` | Registro y autenticacion de usuarios |
| `VehiculoService` | Registro de vehiculos |
| `ViajeService` | Ciclo de vida de viajes (solicitud, aceptacion, rechazo, finalizacion) |
| `Billetera` | Gestion de saldo (recargas, debitos, acreditaciones) |
| `NotificadorViajes` | Gestion de observadores y despacho de notificaciones |

### 5.6 Polymorphism (Polimorfismo)

*"Cuando el comportamiento varia segun el tipo, usar polimorfismo en lugar de condicionales."*

- Las diferentes formas de recarga de billetera se resuelven mediante la interfaz `MetodoPago` (Strategy Pattern), no con `if`/`switch` sobre el tipo de pago.
- Las notificaciones se despachan a traves de la interfaz `ObservadorViaje` (Observer Pattern), permitiendo que cualquier clase que implemente la interfaz reciba notificaciones.

### 5.7 Pure Fabrication (Fabricacion pura)

*"Crear clases que no representan un concepto del dominio pero mejoran la cohesion y reducen el acoplamiento."*

- **`NotificadorViajes`** no existe en el dominio del negocio de transporte; es una fabricacion pura que encapsula la logica del patron Observer (gestion de suscripciones y despacho de eventos).
- **`UsuarioService`**, **`VehiculoService`** y **`ViajeService`** son fabricaciones puras que encapsulan la logica de negocio, separandola del modelo de dominio.

### 5.8 Protected Variations (Variaciones protegidas)

*"Proteger al sistema de las variaciones encerrando la inestabilidad detras de una interfaz."*

- **`Viaje.aceptar()` y `Viaje.finalizar()`** protegen contra transiciones de estado invalidas lanzando `IllegalStateException`. Ningun cliente externo puede poner un viaje en un estado inconsistente.

- La interfaz `MetodoPago` protege a `Billetera` de las variaciones en los metodos de pago: agregar un nuevo medio no impacta en `Billetera`.
- La interfaz `ObservadorViaje` protege a `NotificadorViajes` de las variaciones en los tipos de observador.

---

## 6. Instrucciones de compilacion y ejecucion

### Salida esperada

La demo ejecuta el siguiente flujo completo:

1. Registra un pasajero (Ana) y dos choferes (Carlos a 3.5 km, Marta a 7.0 km).
2. Registra los vehiculos de ambos choferes.
3. Recarga la billetera de Ana: `$500` con tarjeta de credito y `$300` con PayPal (Strategy Pattern).
4. Ana solicita un viaje con rango de 5 km (solo Carlos es notificado -- Observer Pattern).
5. Carlos rechaza el viaje.
6. Ana re-solicita con rango de 10 km (Carlos y Marta son notificados).
7. Marta acepta el viaje.
8. El viaje se finaliza: se debitan `$150` a Ana, se acreditan `$127.50` a Marta (15% de comision para la plataforma).
9. Se muestran los saldos finales.
