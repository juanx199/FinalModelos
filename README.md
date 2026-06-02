# 🚗 CarMotor - Sistema de Gestión de Concesionarios y Vehículos

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Database](https://img.shields.io/badge/Database-H2%20(Embedded)-blue.svg)](https://www.h2database.com/)
[![Build](https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)

**CarMotor** es una aplicación web robusta desarrollada en **Java 21** y **Spring Boot** para la administración integral de concesionarios de vehículos. El sistema integra operaciones comerciales como la gestión de inventario, administración de sedes físicas, reservas de pruebas de ruta (*test drives*), simulación de financiamiento bancario, cotizaciones de pólizas de seguros y flujo de negociación de precios.

Este proyecto ha sido diseñado bajo un enfoque académico/arquitectónico avanzado, implementando una gran variedad de **Patrones de Diseño de Software** (creacionales, estructurales y de comportamiento) para garantizar una arquitectura limpia, desacoplada y altamente mantenible.

---

## 🛠️ Tecnologías y Dependencias

- **Lenguaje:** Java 21
- **Framework Principal:** Spring Boot 4.0.6 (Web, Data JPA, Thymeleaf)
- **Base de Datos:** H2 Database (Persistencia local en `./data/carmotordb`)
- **Gestor de Dependencias:** Maven
- **Librería de Utilidades:** Lombok (para la reducción de código repetitivo)
- **Frontend:** HTML5, CSS3 (Vanilla CSS para estilos personalizados), Thymeleaf como motor de plantillas

---

## 📐 Patrones de Diseño Implementados

El núcleo de la aplicación está estructurado en torno a patrones de diseño clásicos de GoF (Gang of Four), organizados en las siguientes categorías dentro del paquete `com.mycompany.carmotor.model.patterns`:

### 1. Patrones Creacionales
*   **Singleton (`BranchSingleton`):** Controla el acceso a la jerarquía organizacional única de sedes del concesionario, asegurando que exista una única instancia de inicialización en memoria a partir de los datos en base de datos.
*   **Builder (`VehicleBuilder`):** Proporciona una interfaz fluida para construir paso a paso objetos de tipo `IVehicle`, abstrayendo la instanciación de vehículos con datos básicos, especificaciones técnicas y galería de fotos.
*   **Factory Method (`InsuranceFactory`):** Centraliza e instancia dinámicamente objetos de aseguradoras (`SuraInsurer`, `MapfreInsurer`, `LibertyInsurer`, `AxaColpatriaInsurer`, `FalabellaInsurer`) que implementan la interfaz `IInsurer`, basándose en el nombre de la compañía solicitado.

### 2. Patrones Estructurales
*   **Composite (`BranchComposite`):** Permite estructurar las sucursales del concesionario en forma de árbol jerárquico (sedes principales y sub-sedes), donde cada nodo composite puede contener tanto una lista de vehículos disponibles como otras sub-sedes, facilitando operaciones uniformes sobre la jerarquía.
*   **Adapter (`FinancialAdapter`):** Adapta la interfaz de simulación de cuotas de una entidad bancaria (`BankEntity`) a la interfaz requerida por el sistema (`IFinancialAdapter`), permitiendo realizar simulaciones de crédito sin acoplar directamente el dominio.
*   **Facade (`NegotiationFacade`):** Ofrece una interfaz simplificada y unificada para simular una negociación integral (cálculo de cuotas de crédito bancario, cálculo de pólizas de seguro e inspección de estado del vehículo) reduciendo la complejidad de interacción directa con múltiples subsistemas.
*   **Proxy (`VehicleProxy`):** Actúa como un intermediario o sustituto para la entidad pesada `Vehicle`, implementando carga perezosa (*lazy loading*). Carga el vehículo real desde la base de datos solo cuando se invocan métodos que requieren información detallada del dominio.

### 3. Patrones de Comportamiento
*   **State (`VehicleState`):** Gestiona el ciclo de vida y los estados de un vehículo en el concesionario. Las transiciones de comportamiento se delegan en clases concretas correspondientes a cada estado:
    *   `AvailableState` (Disponible para la venta o prueba)
    *   `InNegotiationState` (Bajo proceso de oferta de precio)
    *   `SoldState` (Vendido, bloqueando acciones comerciales)
*   **Iterator (`VehicleIterator`):** Define un mecanismo uniforme para recorrer colecciones de vehículos. Se implementan de forma personalizada:
    *   `BranchVehicleIterator` (recorrido secuencial estándar por sede)
    *   `FilteredVehicleIterator` (recorrido condicional sobre un subconjunto filtrado)
*   **Strategy (Búsqueda y Ordenamiento):** Permite cambiar dinámicamente el criterio de búsqueda en el catálogo. `VehicleSearchContext` utiliza estrategias concretas que implementan `VehicleCriteriaStrategy`:
    *   `SearchByBrandStrategy` (Filtro por marca)
    *   `SearchByTypeStrategy` (Filtro por tipo de vehículo: Carro, Moto, SUV, etc.)
    *   `SearchByPlateStrategy` (Filtro por último dígito de placa)
    *   `SortByPriceStrategy` (Ordenamiento por precio de menor a mayor)
*   **Template Method (`InsuranceCalculator`):** Define el esqueleto del algoritmo de cálculo para cotizar una póliza de seguro (`calculatePolicy()`). Las aseguradoras concretas heredan de esta clase y sobrescriben los métodos gancho/abstractos (`assessRisk()`, `applyDiscount()`, `getBaseRate()`) para inyectar sus propias reglas de negocio y factores de riesgo.
*   **Command (`TestDriveCommand`):** Encapsula las solicitudes de reservas de pruebas de conducción (`ScheduleTestDriveCmd`, `CancelTestDriveCmd`, `RescheduleTestDriveCmd`) permitiendo parametrizar las acciones del usuario, mantener un historial de operaciones y habilitar la funcionalidad de deshacer (*undo*).
*   **Observer (`TestDriveObserver`):** Implementa un esquema de suscripción donde `TestDriveScheduler` (que actúa como `TestDriveSubject`) notifica eventos a múltiples observadores registrados cuando ocurre un cambio en una prueba de conducción. Los observadores concretos incluyen:
    *   `SlotAvailabilityUpdater` (Actualiza el estado de disponibilidad del slot)
    *   `ClientConfirmationSender` (Simula el envío de una confirmación al cliente)
    *   `AdvisorNotifier` (Notifica al asesor asignado sobre el evento)

---

## 🗺️ Diagrama UML del Sistema

El siguiente diagrama de clases representa la arquitectura completa del proyecto, organizada por capas (Controladores, Servicios, Repositorios, Configuración y Dominio/Patrones):

<details>
<summary><b>🔍 Hacer clic para desplegar el código PlantUML</b></summary>

```plantuml
@startuml CarMotor

skinparam classAttributeIconSize 0
skinparam packageStyle rectangle
skinparam linetype ortho

package "domain" #E8F4FD {

    interface IVehicle {
        +showDetail()
        +getPrice() : double
        +getBrand() : String
        +getModel() : int
        +getType() : VehicleType
        +getPassengerCapacity() : int
        +getLastDigitPlate() : int
        +getPhotoPaths() : List<String>
        +getAdvisor() : Advisor
        +setAdvisor(Advisor)
        +getMaintenanceHistory() : List<MaintenanceRecord>
        +addMaintenance(MaintenanceRecord)
        +setState(VehicleState)
        +getState() : VehicleState
        +startNegotiation()
        +confirmSale()
        +cancelNegotiation()
        +getStateName() : String
        +isInsurable() : boolean
    }

    interface IInsurer {
        +calculatePolicy(price: double) : double
        +getEntityName() : String
    }

    enum VehicleType {
        CAR
        TRUCK
        MOTORCYCLE
        SUV
    }

    enum PhotoCategory {
        FRONT
        REAR
        INTERIOR
        GENERAL
    }

    class Vehicle {
        -id : Long
        -brand : String
        -model : int
        -price : double
        -type : VehicleType
        -passengerCapacity : int
        -lastDigitPlate : int
        -stateName : String
        -insurable : boolean
        -state : VehicleState
        -photos : List<Photo>
        -maintenanceHistory : List<MaintenanceRecord>
        -advisor : Advisor
        -branch : Branch
    }

    class Advisor {
        -id : Long
        -nombre : String
        -fotoAsesor : String
        -datosContacto : String
        -vehicles : List<Vehicle>
    }

    class Branch {
        -id : Long
        -name : String
        -address : String
        -phone : String
        -businessHours : String
        -testDriveSlots : List<String>
        -vehicles : List<Vehicle>
    }

    class BankEntity {
        -id : Long
        -nombre : String
        -logo : String
        -telefonoAsesor : String
    }

    class Photo {
        -id : Long
        -area : String
        -filePath : String
        -vehicle : Vehicle
    }

    class MaintenanceRecord {
        -id : Long
        -date : LocalDate
        -workshop : String
        -oilChange : boolean
        -brakeInspection : boolean
        -vehicle : Vehicle
    }

    class TestDriveBooking {
        -id : Long
        -vehicleId : Long
        -branchName : String
        -slotId : String
        -clientName : String
        -clientEmail : String
        -clientPhone : String
        -bookingTime : LocalDateTime
    }

    class TestDriveSlot {
        -slotId : String
        -dateTime : String
        -reserved : boolean
        -vehicle : IVehicle
        +reserve()
        +release()
    }

    class AxaColpatriaInsurer {
        +calculatePolicy(price: double) : double
        +getEntityName() : String
    }

    class FalabellaInsurer {
        +calculatePolicy(price: double) : double
        +getEntityName() : String
    }

    class LibertyInsurer {
        +calculatePolicy(price: double) : double
        +getEntityName() : String
    }

    class MapfreInsurer {
        +calculatePolicy(price: double) : double
        +getEntityName() : String
    }

    class SuraInsurer {
        +calculatePolicy(price: double) : double
        +getEntityName() : String
    }

    Vehicle ..|> IVehicle
    AxaColpatriaInsurer ..|> IInsurer
    FalabellaInsurer ..|> IInsurer
    LibertyInsurer ..|> IInsurer
    MapfreInsurer ..|> IInsurer
    SuraInsurer ..|> IInsurer

    Vehicle "many" --> "1" Advisor : advisor
    Vehicle "many" --> "1" Branch : branch
    Vehicle "1" *--> "many" Photo
    Vehicle "1" *--> "many" MaintenanceRecord
    Vehicle --> VehicleType
    Advisor "1" --> "many" Vehicle
    Branch "1" --> "many" Vehicle
    Photo --> PhotoCategory
}

package "patterns.state" #FFF3CD {

    interface VehicleState {
        +startNegotiation(v: Vehicle)
        +confirmSale(v: Vehicle)
        +cancelNegotiation(v: Vehicle)
        +getStateName() : String
    }

    class AvailableState {
        +startNegotiation(v: Vehicle)
        +confirmSale(v: Vehicle)
        +cancelNegotiation(v: Vehicle)
        +getStateName() : String
    }

    class InNegotiationState {
        +startNegotiation(v: Vehicle)
        +confirmSale(v: Vehicle)
        +cancelNegotiation(v: Vehicle)
        +getStateName() : String
    }

    class SoldState {
        +startNegotiation(v: Vehicle)
        +confirmSale(v: Vehicle)
        +cancelNegotiation(v: Vehicle)
        +getStateName() : String
    }

    AvailableState ..|> VehicleState
    InNegotiationState ..|> VehicleState
    SoldState ..|> VehicleState
    Vehicle --> VehicleState : state
}

package "patterns.creational" #D4EDDA {

    class BranchSingleton {
        -{static} instance : BranchSingleton
        -rootBranch : BranchComposite
        -companyName : String
        -{static} +getInstance(companyName: String) : BranchSingleton
        +getRootBranch() : BranchComposite
        +initSystem()
    }

    class VehicleBuilder {
        -brand : String
        -year : String
        -price : Double
        -type : VehicleType
        -passengerCapacity : int
        -lastDigitPlate : int
        -photoPaths : List<String>
        +withBasicData(brand, year, lastDigitPlate) : VehicleBuilder
        +withSpecs(type, capacity, price) : VehicleBuilder
        +addPhoto(path) : VehicleBuilder
        +build() : IVehicle
        +reset()
    }

    class InsuranceFactory {
        +{static} getInsurer(type: String) : IInsurer
        +{static} listAvailableInsurers() : List<String>
    }

    VehicleBuilder ..> VehicleProxy : creates
    InsuranceFactory ..> IInsurer : creates
    BranchSingleton --> BranchComposite : holds
}

package "patterns.structural" #F8D7DA {

    interface IFinancialAdapter {
        +getSimulatedInstallment(value: double, installments: int) : double
    }

    class FinancialAdapter {
        -bank : BankEntity
        +getSimulatedInstallment(value: double, installments: int) : double
    }

    class NegotiationFacade {
        +simulateFullNegotiation(vehicle, bank, installments, insurerName)
    }

    class BranchComposite {
        -name : String
        -address : String
        -phone : String
        -businessHours : String
        -testDriveSlots : List<String>
        -subBranches : List<BranchComposite>
        -vehicles : List<IVehicle>
        +add(branch: BranchComposite)
        +remove(branch: BranchComposite)
        +addVehicle(vehicle: IVehicle)
        +addTestDriveSlot(slot: String)
        +showStructure(prefix: String)
    }

    class VehicleProxy {
        -vehicleReal : Vehicle
        -brand : String
        -year : int
        -basePrice : double
        -type : VehicleType
        -passengerCapacity : int
        -lastDigitPlate : int
        -originalPaths : List<String>
        -ensureVehicleLoaded()
    }

    FinancialAdapter ..|> IFinancialAdapter
    FinancialAdapter --> BankEntity
    VehicleProxy ..|> IVehicle
    VehicleProxy --> Vehicle : wraps (lazy)
    BranchComposite "1" o--> "many" BranchComposite : subBranches
    BranchComposite --> IVehicle
    NegotiationFacade --> FinancialAdapter
    NegotiationFacade --> InsuranceFactory
}


package "patterns.catalog" #E2D9F3 {

    interface VehicleCatalog {
        +getIterator() : VehicleIterator
    }

    interface VehicleIterator {
        +hasNext() : boolean
        +next() : IVehicle
    }

    interface VehicleCriteriaStrategy {
        +apply(vehicles: List<IVehicle>, criteria: String) : List<IVehicle>
    }

    class BranchCatalog {
        -vehicles : List<IVehicle>
        +getIterator() : VehicleIterator
    }

    class BranchVehicleIterator {
        -vehicles : List<IVehicle>
        -index : int
        +hasNext() : boolean
        +next() : IVehicle
    }

    class FilteredVehicleIterator {
        -source : List<IVehicle>
        -filtered : List<IVehicle>
        -index : int
        +hasNext() : boolean
        +next() : IVehicle
    }

    class VehicleSearchContext {
        -strategy : VehicleCriteriaStrategy
        -vehicles : List<IVehicle>
        +setStrategy(strategy: VehicleCriteriaStrategy)
        +executeSearch(criteria: String) : List<IVehicle>
    }

    class SearchByBrandStrategy {
        +apply(vehicles, criteria) : List<IVehicle>
    }

    class SearchByTypeStrategy {
        +apply(vehicles, criteria) : List<IVehicle>
    }

    class SearchByPlateStrategy {
        +apply(vehicles, criteria) : List<IVehicle>
    }

    class SortByPriceStrategy {
        +apply(vehicles, criteria) : List<IVehicle>
    }

    BranchCatalog ..|> VehicleCatalog
    BranchVehicleIterator ..|> VehicleIterator
    FilteredVehicleIterator ..|> VehicleIterator
    SearchByBrandStrategy ..|> VehicleCriteriaStrategy
    SearchByTypeStrategy ..|> VehicleCriteriaStrategy
    SearchByPlateStrategy ..|> VehicleCriteriaStrategy
    SortByPriceStrategy ..|> VehicleCriteriaStrategy
    BranchCatalog ..> BranchVehicleIterator : creates
    VehicleSearchContext --> VehicleCriteriaStrategy
    VehicleSearchContext --> IVehicle
}


package "patterns.negotiation" #FFE8CC {

    abstract class NegotiationHandler {
        #nextHandler : NegotiationHandler
        #maxDiscountPct : double
        +setNext(next: NegotiationHandler) : NegotiationHandler
        +process(req: NegotiationRequest) : NegotiationResult
        #canHandle(req: NegotiationRequest) : boolean
        #handle(req: NegotiationRequest) : NegotiationResult
        #passToNext(req: NegotiationRequest) : NegotiationResult
    }

    class AdvisorHandler {
        +canHandle(req) : boolean
        +handle(req) : NegotiationResult
    }

    class BranchManagerHandler {
        +canHandle(req) : boolean
        +handle(req) : NegotiationResult
    }

    class GeneralDirectorHandler {
        +canHandle(req) : boolean
        +handle(req) : NegotiationResult
    }

    class NegotiationRequest {
        -vehiclePrice : double
        -requestedPrice : double
        -discountPct : double
        -clientName : String
        +getDiscountPct() : double
    }

    class NegotiationResult {
        -approved : boolean
        -finalPrice : double
        -approvedBy : String
        -message : String
    }

    AdvisorHandler --|> NegotiationHandler
    BranchManagerHandler --|> NegotiationHandler
    GeneralDirectorHandler --|> NegotiationHandler
    NegotiationHandler --> NegotiationHandler : nextHandler
    NegotiationHandler --> NegotiationRequest
    NegotiationHandler --> NegotiationResult
}


package "patterns.insurance" #D1ECF1 {

    abstract class InsuranceCalculator {
        #insurerName : String
        #vehicle : IVehicle
        +calculatePolicy() : InsuranceQuote
        +validateVehicle() : boolean
        +buildQuote(...) : InsuranceQuote
        #assessRisk() : double
        #applyDiscount() : double
        #getBaseRate() : double
    }

    class SuraCalculator {
        #assessRisk() : double
        #applyDiscount() : double
        #getBaseRate() : double
    }

    class MapfreCalculator {
        #assessRisk() : double
        #applyDiscount() : double
        #getBaseRate() : double
    }

    class LibertyCalculator {
        #assessRisk() : double
        #applyDiscount() : double
        #getBaseRate() : double
    }

    class AxaColpatriaCalculator {
        #assessRisk() : double
        #applyDiscount() : double
        #getBaseRate() : double
    }

    class DummyVehicle {
        -brand : String
        -price : double
        -type : VehicleType
    }

    class InsuranceQuote {
        -insurerName : String
        -baseCost : double
        -riskCost : double
        -discount : double
        -totalPremium : double
    }

    SuraCalculator --|> InsuranceCalculator
    MapfreCalculator --|> InsuranceCalculator
    LibertyCalculator --|> InsuranceCalculator
    AxaColpatriaCalculator --|> InsuranceCalculator
    InsuranceCalculator --> IVehicle
    InsuranceCalculator --> InsuranceQuote
    DummyVehicle ..|> IVehicle
}


package "patterns.testdrive" #FCE4EC {

    interface TestDriveCommand {
        +execute()
        +undo()
    }

    interface TestDriveObserver {
        +update(event: TestDriveEvent)
    }

    interface TestDriveSubject {
        +subscribe(observer: TestDriveObserver)
        +unsubscribe(observer: TestDriveObserver)
        +notifyObservers()
    }

    class TestDriveScheduler {
        -observers : List<TestDriveObserver>
        -slots : List<TestDriveSlot>
        -lastEvent : TestDriveEvent
        +scheduleTestDrive(slot, vehicle)
        +cancelTestDrive(slotId, reason)
        +addSlot(slot: TestDriveSlot)
        +findSlot(slotId: String) : TestDriveSlot
    }

    class TestDriveInvoker {
        -history : List<TestDriveCommand>
        -current : TestDriveCommand
        +setCommand(command: TestDriveCommand)
        +executeCommand()
        +undoLast()
    }

    class TestDriveEvent {
        -type : String
        -slot : TestDriveSlot
        -vehicle : IVehicle
    }

    class ScheduleTestDriveCmd {
        -scheduler : TestDriveScheduler
        -slot : TestDriveSlot
        -vehicle : IVehicle
        +execute()
        +undo()
    }

    class CancelTestDriveCmd {
        -scheduler : TestDriveScheduler
        -slotId : String
        -reason : String
        +execute()
        +undo()
    }

    class RescheduleTestDriveCmd {
        -scheduler : TestDriveScheduler
        -oldSlotId : String
        -newSlot : TestDriveSlot
        -vehicle : IVehicle
        +execute()
        +undo()
    }

    class SlotAvailabilityUpdater {
        +update(event: TestDriveEvent)
    }

    class ClientConfirmationSender {
        +update(event: TestDriveEvent)
    }

    class AdvisorNotifier {
        +update(event: TestDriveEvent)
    }

    TestDriveScheduler ..|> TestDriveSubject
    ScheduleTestDriveCmd ..|> TestDriveCommand
    CancelTestDriveCmd ..|> TestDriveCommand
    RescheduleTestDriveCmd ..|> TestDriveCommand
    SlotAvailabilityUpdater ..|> TestDriveObserver
    ClientConfirmationSender ..|> TestDriveObserver
    AdvisorNotifier ..|> TestDriveObserver

    TestDriveScheduler "1" --> "many" TestDriveObserver : notifies
    TestDriveScheduler --> TestDriveEvent
    TestDriveScheduler --> TestDriveSlot
    TestDriveInvoker --> TestDriveCommand
    ScheduleTestDriveCmd --> TestDriveScheduler
    CancelTestDriveCmd --> TestDriveScheduler
    RescheduleTestDriveCmd --> TestDriveScheduler
    TestDriveEvent --> TestDriveSlot
    TestDriveEvent --> IVehicle
}


package "service" #F5F5F5 {

    class VehicleService {
        +getAllVehicles() : List<Vehicle>
        +getVehicleById(id) : Vehicle
        +saveVehicle(vehicle) : Vehicle
        +deleteVehicle(id)
        +updateVehicle(id, data) : Vehicle
    }

    class BranchService {
        +getAllBranches() : List<BranchComposite>
        +getBranchByName(name) : BranchComposite
        +refreshFromDB()
    }

    class TestDriveService {
        +bookTestDrive(request) : TestDriveBooking
        +cancelTestDrive(bookingId)
        +getBookingsForBranch(branchName) : List<TestDriveBooking>
    }

    class AdvisorService {
        +getAllAdvisors() : List<Advisor>
        +getAdvisorById(id) : Advisor
    }

    class BankEntityService {
        +getAllBanks() : List<BankEntity>
        +getBankById(id) : BankEntity
    }

    VehicleService --> VehicleBuilder
    VehicleService --> VehicleProxy
    VehicleService --> NegotiationFacade
    BranchService --> BranchSingleton
    BranchService --> BranchComposite
    TestDriveService --> TestDriveInvoker
    TestDriveService --> TestDriveScheduler
    TestDriveService --> TestDriveBooking
}


package "repository" #EEEEEE {

    interface VehicleRepository
    interface BranchRepository
    interface AdvisorRepository
    interface PhotoRepository
    interface MaintenanceRecordRepository
    interface TestDriveBookingRepository
    interface BankEntityRepository

    VehicleService --> VehicleRepository
    BranchService --> BranchRepository
    AdvisorService --> AdvisorRepository
    TestDriveService --> TestDriveBookingRepository
    BankEntityService --> BankEntityRepository
}


package "controller" #DDEEFF {

    class VehicleController {
        +listVehicles() : ModelAndView
        +vehicleDetail(id) : ModelAndView
        +bookTestDrive(request) : ResponseEntity
        +simulateInsurance(id, insurer) : ResponseEntity
        +simulateNegotiation(request) : ResponseEntity
    }

    class AdminVehicleController {
        +listVehicles() : ModelAndView
        +newVehicleForm() : ModelAndView
        +createVehicle(form) : RedirectView
        +editVehicleForm(id) : ModelAndView
        +updateVehicle(id, form) : RedirectView
        +deleteVehicle(id) : RedirectView
    }

    class AdminBranchController {
        +listBranches() : ModelAndView
        +branchDetail(name) : ModelAndView
    }

    VehicleController --> VehicleService
    VehicleController --> BranchService
    VehicleController --> BankEntityService
    VehicleController --> TestDriveService
    AdminVehicleController --> VehicleService
    AdminVehicleController --> AdvisorService
    AdminVehicleController --> BranchService
    AdminBranchController --> BranchService
}



package "config" #E8EAF6 {

    class WebConfig {
        +addResourceHandlers(registry: ResourceHandlerRegistry)
    }

    WebConfig ..|> WebMvcConfigurer
}

@enduml
```

</details>

*Nota: Para visualizar y editar este diagrama de forma gráfica, puedes instalar la extensión **PlantUML** en tu IDE (como VS Code) o copiar el contenido del bloque anterior e ingresarlo en el servidor oficial de [PlantUML Online Server](https://www.plantuml.com).*

---

## ⚡ Guía de Ejecución y Configuración

Sigue estos pasos para compilar y ejecutar el proyecto localmente:

### Prerrequisitos
- **Java Development Kit (JDK) 21** instalado y configurado en la variable de entorno `JAVA_HOME`.
- **Apache Maven** (u utilizar el wrapper de Maven `./mvnw` incluido).

### Instalación y Compilación
1. Clona este repositorio o ubícate en la carpeta raíz del proyecto.
2. Compila el código del proyecto y descarga las dependencias ejecutando:
   ```bash
   mvn clean install
   ```

### Ejecutar la Aplicación
Para iniciar el servidor embebido Tomcat (por defecto configurado en el puerto **8081**), ejecuta:
```bash
./mvnw spring-boot:run
```
Una vez que el servidor se inicie correctamente, abre tu navegador web y accede a:
- **Aplicación Principal:** [http://localhost:8081](http://localhost:8081)
- **Consola de Base de Datos H2:** [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
  - **JDBC URL:** `jdbc:h2:file:./data/carmotordb`
  - **Usuario:** `sa`
  - **Contraseña:** *(dejar vacío)*

---

## 👥 Autores del Proyecto

A continuación se listan los autores principales que han contribuido al desarrollo, diseño e implementación del sistema **CarMotor**:

| Nombre | Código |
|---|---|
| Juan Daniel Palomino García | 20232020065 |
| Juan Camilo Carvajal Camargo | 20232020026 |
| Josep Emmanuel Leon Joya | 20231020160 |

---
