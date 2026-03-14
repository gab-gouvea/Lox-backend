# Lox Backend — Especificacao Completa da API

Este documento descreve todas as entidades, DTOs e endpoints que o backend Java precisa implementar para atender o frontend.

---

## 1. Entidades JPA

### 1.1 Proprietario

```java
@Entity
@Table(name = "proprietarios")
public class Proprietario {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;                    // "123.456.789-00"

    private String rg;                     // nullable

    private LocalDate dataNascimento;      // nullable

    private String profissao;              // nullable

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;       // nullable

    private String endereco;               // nullable

    private String email;                  // nullable

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;              // set on create

    @Column(nullable = false)
    private Instant atualizadoEm;          // updated on every save
}
```

**Enum EstadoCivil:**
```java
public enum EstadoCivil {
    solteiro, casado, divorciado, viuvo, separado, uniao_estavel
}
```

---

### 1.2 Property (Propriedade)

```java
@Entity
@Table(name = "propriedades")
public class Property {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nome;

    private String endereco;               // nullable

    private String proprietarioId;         // FK para Proprietario.id (nullable)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType tipo;

    @Column(nullable = false)
    private Integer quartos;               // >= 0

    private String fotoCapa;               // URL nullable

    @Column(nullable = false)
    private BigDecimal percentualComissao;  // 0-100

    private BigDecimal taxaLimpeza;         // >= 0, nullable

    @Column(nullable = false)
    private Boolean temHobbyBox;

    private String acessoPredio;           // nullable

    private String acessoApartamento;      // nullable

    @Column(nullable = false)
    private Boolean ativo;                 // default true

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(nullable = false)
    private Instant atualizadoEm;
}
```

**Enum PropertyType:**
```java
public enum PropertyType {
    apartamento, casa, studio, chale, flat, outro
}
```

---

### 1.3 Reservation (Reserva)

```java
@Entity
@Table(name = "reservas")
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;          // FK para Property.id

    @Column(nullable = false)
    private String nomeHospede;

    @Column(nullable = false)
    private Instant checkIn;               // ISO 8601

    @Column(nullable = false)
    private Instant checkOut;              // ISO 8601

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private BigDecimal precoTotal;         // >= 0, nullable

    @Column(columnDefinition = "TEXT")
    private String notas;                  // nullable

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationSource fonte;

    @Column(nullable = false)
    private Integer numHospedes;           // >= 1

    @Enumerated(EnumType.STRING)
    private FaxinaStatus faxinaStatus;     // nullable

    @Column(nullable = false)
    private Boolean faxinaPorMim;          // true = eu limpo, false = empresa

    private BigDecimal custoEmpresaFaxina; // >= 0, nullable

    private Boolean faxinaPaga;            // nullable

    private Instant faxinaData;            // nullable

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reservation_id")
    private List<Despesa> despesas;        // pode ser vazio

    private BigDecimal valorRecebidoCancelamento; // >= 0, nullable

    @Column(nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(nullable = false)
    private Instant atualizadoEm;
}
```

**Enum ReservationStatus:**
```java
public enum ReservationStatus {
    pendente, confirmada, em_andamento, concluida, cancelada
}
```
> Nota: no frontend o valor é `"em andamento"` (com espaco). Decidir se no backend usa `em_andamento` e converte no frontend, ou se usa `@JsonValue` para serializar com espaco.

**Enum ReservationSource:**
```java
public enum ReservationSource {
    airbnb, booking, direto, outro
}
```

**Enum FaxinaStatus:**
```java
public enum FaxinaStatus {
    nao_agendada, agendada, concluida
}
```

---

### 1.4 Despesa (Embeddable ou Entity)

```java
@Entity
@Table(name = "despesas")
public class Despesa {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;              // >= 0

    @Column(nullable = false)
    private Boolean reembolsavel;
}
```

> Alternativa: pode ser `@Embeddable` com `@ElementCollection` na Reservation. Mas como o frontend manipula despesas individualmente (add/remove), Entity separada com `orphanRemoval = true` facilita.

---

### 1.5 PropertyComponent (Componente de Manutencao)

```java
@Entity
@Table(name = "componentes")
public class PropertyComponent {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;          // FK para Property.id

    @Column(nullable = false)
    private String nome;                   // ex: "Ar Condicionado"

    @Column(nullable = false)
    private LocalDate ultimaManutencao;

    @Column(nullable = false)
    private LocalDate proximaManutencao;

    @Column(nullable = false)
    private Integer intervaloDias;         // >= 1

    private String prestador;             // nullable, ex: "Clean House"

    @Column(columnDefinition = "TEXT")
    private String observacoes;            // nullable
}
```

> `status` ("em_dia" | "atrasado") e calculado no frontend comparando `proximaManutencao` com a data atual. Nao precisa persistir.

---

### 1.6 MaintenanceRecord (Registro de Manutencao)

```java
@Entity
@Table(name = "registros_manutencao")
public class MaintenanceRecord {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;          // FK para Property.id

    @Column(nullable = false)
    private String componenteId;           // FK para PropertyComponent.id

    @Column(nullable = false)
    private String nomeServico;            // ex: "Higienizacao Geral"

    private String prestador;              // nullable

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private BigDecimal valor;              // >= 0

    @Column(nullable = false)
    private Boolean pago;
}
```

---

### 1.7 InventoryItem (Item de Inventario)

```java
@Entity
@Table(name = "itens_inventario")
public class InventoryItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String propriedadeId;          // FK para Property.id

    @Column(nullable = false)
    private String comodo;                  // ex: "Cozinha", "Banheiro", "Quarto"

    @Column(nullable = false)
    private String nome;                   // ex: "Toalha de Banho"

    @Column(nullable = false)
    private Integer quantidade;            // >= 0

    private String descricao;             // nullable

    private String imagemUrl;             // URL, nullable

    @Column(nullable = false)
    private Instant atualizadoEm;         // updated on every save
}
```

---

## 2. DTOs (Records Java)

### 2.1 Proprietario DTOs

```java
// POST /api/proprietarios
public record CreateProprietarioDTO(
    @NotBlank String nomeCompleto,
    @NotBlank @Size(min = 11) String cpf,
    String rg,                             // nullable
    LocalDate dataNascimento,              // nullable
    String profissao,                      // nullable
    EstadoCivil estadoCivil,               // nullable
    String endereco,                       // nullable
    @Email String email                    // nullable
) {}

// PUT /api/proprietarios/:id
public record UpdateProprietarioDTO(
    String nomeCompleto,                   // todos opcionais
    String cpf,
    String rg,
    LocalDate dataNascimento,
    String profissao,
    EstadoCivil estadoCivil,
    String endereco,
    String email
) {}
```

### 2.2 Property DTOs

```java
// POST /api/properties
public record CreatePropertyDTO(
    @NotBlank String nome,
    String endereco,
    String proprietarioId,
    @NotNull PropertyType tipo,
    @NotNull @Min(0) Integer quartos,
    String fotoCapa,                       // URL
    @NotNull @DecimalMin("0") @DecimalMax("100") BigDecimal percentualComissao,
    @DecimalMin("0") BigDecimal taxaLimpeza,
    @NotNull Boolean temHobbyBox,
    String acessoPredio,
    String acessoApartamento,
    @NotNull Boolean ativo
) {}

// PUT /api/properties/:id
public record UpdatePropertyDTO(
    String nome,                           // todos opcionais
    String endereco,
    String proprietarioId,
    PropertyType tipo,
    Integer quartos,
    String fotoCapa,
    BigDecimal percentualComissao,
    BigDecimal taxaLimpeza,
    Boolean temHobbyBox,
    String acessoPredio,
    String acessoApartamento,
    Boolean ativo
) {}
```

### 2.3 Reservation DTOs

```java
// Subdto para despesas
public record DespesaDTO(
    @NotBlank String descricao,
    @NotNull @DecimalMin("0") BigDecimal valor,
    @NotNull Boolean reembolsavel
) {}

// POST /api/reservations
public record CreateReservationDTO(
    @NotBlank String propriedadeId,
    @NotBlank String nomeHospede,
    @NotNull Instant checkIn,
    @NotNull Instant checkOut,
    @NotNull ReservationStatus status,
    @DecimalMin("0") BigDecimal precoTotal,
    String notas,
    @NotNull ReservationSource fonte,
    @NotNull @Min(1) Integer numHospedes,
    FaxinaStatus faxinaStatus,
    @NotNull Boolean faxinaPorMim,
    @DecimalMin("0") BigDecimal custoEmpresaFaxina,
    Boolean faxinaPaga,
    Instant faxinaData,
    List<DespesaDTO> despesas
) {}

// PUT /api/reservations/:id
// IMPORTANTE: aceita todos os campos opcionais + valorRecebidoCancelamento
// porque o frontend faz updates parciais (ex: so muda faxinaPaga)
public record UpdateReservationDTO(
    String propriedadeId,
    String nomeHospede,
    Instant checkIn,
    Instant checkOut,
    ReservationStatus status,
    BigDecimal precoTotal,
    String notas,
    ReservationSource fonte,
    Integer numHospedes,
    FaxinaStatus faxinaStatus,
    Boolean faxinaPorMim,
    BigDecimal custoEmpresaFaxina,
    Boolean faxinaPaga,
    Instant faxinaData,
    List<DespesaDTO> despesas,
    BigDecimal valorRecebidoCancelamento   // so no update, nao no create
) {}
```

### 2.4 PropertyComponent DTOs

```java
// POST /api/properties/:propertyId/components
public record CreateComponentDTO(
    @NotBlank String nome,
    @NotNull LocalDate ultimaManutencao,
    LocalDate proximaManutencao,           // opcional no form (pode ser calculado)
    @NotNull @Min(1) Integer intervaloDias,
    String prestador,
    String observacoes
) {}

// PUT /api/components/:id
public record UpdateComponentDTO(
    String nome,
    LocalDate ultimaManutencao,
    LocalDate proximaManutencao,
    Integer intervaloDias,
    String prestador,
    String observacoes
) {}
```

### 2.5 MaintenanceRecord DTOs

```java
// POST /api/maintenance-records
public record CreateMaintenanceRecordDTO(
    @NotBlank String propriedadeId,
    @NotBlank String componenteId,
    @NotBlank String nomeServico,
    String prestador,
    @NotNull LocalDate data,
    @NotNull @DecimalMin("0") BigDecimal valor,
    @NotNull Boolean pago
) {}

// PUT /api/maintenance-records/:id
public record UpdateMaintenanceRecordDTO(
    String propriedadeId,
    String componenteId,
    String nomeServico,
    String prestador,
    LocalDate data,
    BigDecimal valor,
    Boolean pago
) {}
```

### 2.6 InventoryItem DTOs

```java
// POST /api/properties/:propertyId/inventory
public record CreateInventoryItemDTO(
    @NotBlank String comodo,
    @NotBlank String nome,
    @NotNull @Min(0) Integer quantidade,
    String descricao,
    @URL String imagemUrl
) {}

// PUT /api/inventory/:id
public record UpdateInventoryItemDTO(
    String comodo,
    String nome,
    Integer quantidade,
    String descricao,
    String imagemUrl
) {}
```

---

## 3. Endpoints

### 3.1 Proprietarios

| Metodo | Path | Request Body | Response | Status |
|--------|------|-------------|----------|--------|
| GET | `/api/proprietarios` | — | `List<Proprietario>` | 200 |
| GET | `/api/proprietarios/:id` | — | `Proprietario` | 200 |
| POST | `/api/proprietarios` | `CreateProprietarioDTO` | `Proprietario` | 201 |
| PUT | `/api/proprietarios/:id` | `UpdateProprietarioDTO` | `Proprietario` | 200 |
| DELETE | `/api/proprietarios/:id` | — | — | 204 |

**Regras:**
- CPF deve ser unico
- `criadoEm` setado no create, `atualizadoEm` atualizado em toda operacao

---

### 3.2 Properties

| Metodo | Path | Request Body | Response | Status |
|--------|------|-------------|----------|--------|
| GET | `/api/properties` | — | `List<Property>` | 200 |
| GET | `/api/properties/:id` | — | `Property` | 200 |
| POST | `/api/properties` | `CreatePropertyDTO` | `Property` | 201 |
| PUT | `/api/properties/:id` | `UpdatePropertyDTO` | `Property` | 200 |
| DELETE | `/api/properties/:id` | — | — | 204 |

**Regras:**
- `proprietarioId` pode ser null (propriedade sem dono cadastrado)
- `criadoEm` setado no create, `atualizadoEm` atualizado em toda operacao

---

### 3.3 Reservations

| Metodo | Path | Request Body | Response | Status |
|--------|------|-------------|----------|--------|
| GET | `/api/reservations` | — | `List<Reservation>` | 200 |
| GET | `/api/reservations/:id` | — | `Reservation` | 200 |
| GET | `/api/reservations?propertyId=X` | — | `List<Reservation>` | 200 |
| GET | `/api/reservations?start=X&end=Y` | — | `List<Reservation>` | 200 |
| POST | `/api/reservations` | `CreateReservationDTO` | `Reservation` | 201 |
| PUT | `/api/reservations/:id` | `UpdateReservationDTO` | `Reservation` | 200 |
| DELETE | `/api/reservations/:id` | — | — | 204 |

**Regras:**
- Filtro por date range: retorna reservas onde `checkOut > start AND checkIn < end` (overlap)
- Filtro por propertyId: retorna reservas da propriedade
- Update aceita campos parciais (so o que vier no body e atualizado)
- `despesas` no update substitui o array inteiro (orphanRemoval)
- `criadoEm` setado no create, `atualizadoEm` atualizado em toda operacao
- `valorRecebidoCancelamento` so aceito via update (nao via create)

**Auto-status (pode ser no backend ou frontend):**
- `confirmada` → `em_andamento` quando hoje >= checkIn
- `confirmada`/`em_andamento` → `concluida` quando hoje >= checkOut
- `cancelada`, `concluida`, `pendente` nunca mudam automaticamente

---

### 3.4 Property Components

| Metodo | Path | Request Body | Response | Status |
|--------|------|-------------|----------|--------|
| GET | `/api/properties/:propertyId/components` | — | `List<PropertyComponent>` | 200 |
| GET | `/api/components` | — | `List<PropertyComponent>` | 200 |
| POST | `/api/properties/:propertyId/components` | `CreateComponentDTO` | `PropertyComponent` | 201 |
| PUT | `/api/components/:id` | `UpdateComponentDTO` | `PropertyComponent` | 200 |
| DELETE | `/api/components/:id` | — | — | 204 |

**Regras:**
- `GET /api/components` retorna TODOS de todas as propriedades (usado pelo dashboard para alertas)
- `POST` seta o `propriedadeId` a partir do path param
- Se `proximaManutencao` nao vier no create, pode calcular: `ultimaManutencao + intervaloDias`

---

### 3.5 Maintenance Records

| Metodo | Path | Request Body | Response | Status |
|--------|------|-------------|----------|--------|
| GET | `/api/maintenance-records` | — | `List<MaintenanceRecord>` | 200 |
| GET | `/api/maintenance-records?startDate=X&endDate=Y&propertyId=Z` | — | `List<MaintenanceRecord>` | 200 |
| POST | `/api/maintenance-records` | `CreateMaintenanceRecordDTO` | `MaintenanceRecord` | 201 |
| PUT | `/api/maintenance-records/:id` | `UpdateMaintenanceRecordDTO` | `MaintenanceRecord` | 200 |
| DELETE | `/api/maintenance-records/:id` | — | — | 204 |

**Regras:**
- Todos os query params sao opcionais
- `startDate`: filtra `data >= startDate`
- `endDate`: filtra `data <= endDate`
- `propertyId`: filtra `propriedadeId == propertyId`
- Os filtros sao combinados (AND)

---

### 3.6 Inventory Items

| Metodo | Path | Request Body | Response | Status |
|--------|------|-------------|----------|--------|
| GET | `/api/properties/:propertyId/inventory` | — | `List<InventoryItem>` | 200 |
| POST | `/api/properties/:propertyId/inventory` | `CreateInventoryItemDTO` | `InventoryItem` | 201 |
| PUT | `/api/inventory/:id` | `UpdateInventoryItemDTO` | `InventoryItem` | 200 |
| DELETE | `/api/inventory/:id` | — | — | 204 |

**Regras:**
- `POST` seta `propriedadeId` e `atualizadoEm` automaticamente
- `PUT` atualiza `atualizadoEm` automaticamente
- Strings vazias para `descricao` e `imagemUrl` devem ser tratadas como null

---

## 4. Entidades que podem ser REMOVIDAS do backend atual

| Entidade atual | Motivo |
|---------------|--------|
| `Category` | Frontend nao usa categorias separadas. Inventario usa `comodo` (String) |
| `Cleaning` (entidade separada) | Faxina virou campos dentro de `Reservation` |
| `CheckIn` (entidade separada) | Nao existe no frontend, checkIn e um campo da reserva |
| `Item` (separado de Inventory) | Virou `InventoryItem` flat com `comodo` |
| `Inventory` (container) | Nao existe como container, cada item e independente |
| `Alert` (entidade) | Alertas sao calculados no frontend (hook `useAlerts`) |

---

## 5. Resumo das Diferencas Backend Atual → Backend Necessario

| Area | O que mudar |
|------|------------|
| **Rental → Reservation** | Renomear + adicionar 12 campos (status, fonte, notas, faxina*, despesas, valorRecebidoCancelamento) |
| **Property** | Adicionar 8 campos (tipo, quartos, percentualComissao, taxaLimpeza, temHobbyBox, acessoPredio, acessoApartamento, fotoCapa) |
| **Owner → Proprietario** | Adicionar 5 campos (rg, dataNascimento, profissao, estadoCivil, endereco) |
| **Component** | Mudar de ManyToMany para pertencer a 1 propriedade + adicionar intervaloDias, prestador, ultimaManutencao, proximaManutencao |
| **MaintenanceRecord** | Criar do zero (entidade + controller + service + repository) |
| **InventoryItem** | Recriar como entidade flat com comodo (sem Category/Inventory containers) |
| **Endpoints** | Adicionar filtro por date range em reservas, filtros em maintenance-records |
| **Limpeza** | Remover Category, Cleaning, CheckIn, Item, Inventory, Alert entities |
