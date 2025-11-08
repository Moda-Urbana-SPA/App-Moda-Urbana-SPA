# App-Moda-Urbana-SPA

## 1. Caso elegido y alcance

- **Caso:** Tienda de Moda Urbana — Inicio de Sesión, registro de usuario, catálogo de prendas visual (home) y perfil de usuario.
- **Alcance EP3:** Diseño/UI, validaciones (a implementar por el alumno), navegación y backstack, gestión de estado, persistencia local (CRUD con Room), recursos nativos (cámara/galería), manejo de avatar y consumo de la API de autenticación.

**Resumen de objetivos**
1. Mostrar catálogo de productos y pantalla de detalle.  
2. Autenticación básica (registro/login) con token.  
3. CRUD local para entidades clave (usuarios, prendas).  
4. Selección y almacenamiento de avatar desde cámara/galería.  
5. Manejo de estados (carga/éxito/error) y navegación coherente.

---

## 2. Requisitos y ejecución

- **Pre-requisitos**
  - Android Studio
  - JDK
  - Dispositivo o emulador Android.

- **Instalación**
```bash
git clone https://github.com/Moda-Urbana-SPA/App-Moda-Urbana-SPA.git
cd App-Moda-Urbana-SPA
```

- **Abrir y compilar**
  - Abrir la carpeta del proyecto en Android Studio y dejar que Gradle sincronice las dependencias.
  - Para compilar desde línea de comandos:
```bash
# generar APK debug
./gradlew assembleDebug

# instalar en emulador o dispositivo conectado
./gradlew installDebug
```

---

## 3. Arquitectura y flujo

- **Estructura**
  - `app/src/main/java/com/example/modaurbanaprototipoapp/` — paquete raíz.  
  - `data/local/entity/` — entidades Room.  
  - `data/local/dao/` — DAOs.  
  - `data/local/database/` — `AppDatabase.kt`.  
  - `data/remote/`.  
  - `repository/`.  
  - `viewmodel/`.  
  - `ui/`.

- **Gestión de estado**
  - ViewModel para estado por pantalla + repositorios que abstraigan acceso local/remoto.
  - Uso de coroutines para llamadas asíncronas y manejo de resultados en estados: carga, éxito y error.
  - Tokens y preferencias se deben guardar de forma segura.

- **Navegación**
  - Flujo principal basado en:  Register -> Login -> Profile -> Home -> Catálogo.

---

## 4. Funcionalidades

- Formulario de registro/login.  
- Catálogo de prendas con soporte para búsqueda y filtros básicos (implementado en `HomeScreen` y `ClothingDao`). 
- Persistencia local con Room.  
- CRUD local para entidades principales (usuarios y prendas).  
- Gestión y almacenamiento de avatar: selección por cámara o galería y persistencia de la referencia.  
- Manejo de estados (carga, éxito, error) en operaciones remotas y locales.  
- Consumo de API remoto via Retrofit.  
- Integración de recursos nativos.  
- Repositorios para separar lógica de datos.

---

## 5. Endpoints

**Base URL (plantilla usada en proyecto):** `https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW`

| Método | Ruta | Body | Respuesta (resumen) |
| ------ | ---- | ---- | -------------------- |
| POST | `/auth/signup` | `{ email, password, name?, ... }` | 201 `{ authToken, user: { id, email, ... } }` |
| POST | `/auth/login`  | `{ email, password }` | 200 `{ authToken, user: { id, email, ... } }` |
| GET  | `/auth/me`     | — (Authorization: Bearer <token>) | 200 `{ id, email, name, avatarUrl?, ... }` |

---

## 6. User flows

### Flujo de autenticación
- **Registro:** POST a `/auth/signup` -> guardar `authToken` localmente -> navegar a pantalla principal.  
- **Login:** POST a `/auth/login` -> guardar `authToken` -> GET `/auth/me` para sincronizar perfil.  
- **Token inválido/expirado:** limpiar credenciales y redirigir a login.

### Gestión de perfil y avatar
- Desde perfil: opción para cambiar avatar → elegir Cámara o Galería → previsualizar → persistir referencia local y/o subir al servidor según el flujo.

### Casos de error
- Sin conexión: mostrar mensaje y permitir reintento o modo con datos locales.  
- Error en servidor: mostrar mensaje claro y opción de reintento.  
- Token inválido: forzar re-login.
