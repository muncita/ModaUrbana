
# MODA URBANA APP

---

INTEGRANTES DEL EQUIPO

* Enzo Gabrielli
* Monserratt Aldea
* [Nombre Integrante 3]


---

DESCRIPCIÓN GENERAL DEL PROYECTO
Moda Urbana es una aplicación móvil Android desarrollada en Kotlin, orientada a la autenticación y gestión de perfil de usuario como parte de una propuesta de e-commerce de moda urbana.

El proyecto implementa el flujo completo de registro, inicio de sesión y cierre de sesión, utilizando autenticación basada en tokens JWT, consumo de una API REST externa y persistencia de sesión local. Además, incorpora el uso de recursos nativos del dispositivo como cámara y galería para la gestión de imagen de perfil.

---

- **Stack principal:**  
Aplicación móvil:

* Kotlin
* Jetpack Compose (Material 3)
* Navigation Compose
* ViewModel + StateFlow
* Coroutines
* Retrofit + Gson
* DataStore Preferences
* Coil (carga de imágenes)

Backend / API:

* API REST externa (Xano)
* Autenticación JWT

- **Instalación:**
  ```bash
  git clone https://github.com/tuusuario/modaurbana.git
  cd modaurbana
  '''
## Estructura del proyecto
java/com/example/modaurbana/
├── data/
│   ├── local/
│   │   └── SessionManager.kt
│   └── remote/
│       ├── ApiService.kt
│       ├── AuthInterceptor.kt
│       └── RetrofitClient.kt
│
├── models/
│   └── Models.kt
│
├── repository/
│   ├── AuthRepository.kt
│   └── UserRepository.kt
│
├── ui/
│   ├── components/
│   │   ├── ImagenInteligente.kt
│   │   └── ImagePickerDialog.kt
│   ├── navigation/
│   │   ├── AppNavGraph.kt
│   │   ├── AppNavigation.kt
│   │   └── Routes.kt
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── LoginScreen.kt
│   │   ├── ProfileScreen.kt
│   │   └── RegisterScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── utils/
│   └── ValidationUtils.kt
│
├── viewmodel/
│   ├── AuthViewModel.kt
│   ├── HomeViewModel.kt
│   ├── LoginViewModel.kt
│   ├── ProfileViewModel.kt
│   └── RegisterViewModel.kt
│
├── AppDependencies.kt
└── MainActivity.kt

##. Funcionalidades

**Formulario validado (login y registro)**  

* Registro de usuario con validaciones de nombre, email y contraseña
* Inicio de sesión con control de errores
* Persistencia de sesión mediante token JWT
* 
- Validación de campos: nombre, email, contraseña y confirmación.  
- Errores visuales dinámicos con `ValidationUtils.kt`.

**Navegación y backstack**  
-* Flujo completo entre pantallas: Login, Registro, Home y Perfil
* Manejo del backstack con NavHostController
- Flujo completo con `NavHostController`.  
- Navegación limpia entre pantallas y control de retorno.

**Gestión de estado (carga/éxito/error)**  
- `StateFlow` + `ViewModelScope` para manejar estados reactivos.  
- Se muestran `CircularProgressIndicator` y mensajes de error en pantalla.
- * Manejo de estados de carga, éxito y error
* Implementación con StateFlow y ViewModelScope
* Indicadores visuales de carga y mensajes de error


**Persistencia local (DataStore)**  
* Uso de DataStore para guardar el token JWT
* Almacenamiento de la imagen de perfil como URI local
* Limpieza de sesión al cerrar sesión
* 
**Almacenamiento de imagen de perfil**  
- Opción de tomar foto o elegir desde galería (`ProfileScreen.kt`).  
- Integración con `FileProvider` para capturas seguras.  
- Imagen persistente guardada como URI local.

**Recursos nativos**  
- Uso de permisos (`Manifest.permission.CAMERA`).  
- `ImagePickerDialog.kt` permite seleccionar cámara o galería.  
- Manejo de resultados con `ActivityResultContracts`.
- * Captura de imagen mediante cámara
* Selección de imagen desde galería
* Manejo de permisos de cámara
* Uso de FileProvider para capturas seguras


**Consumo de API (/auth/signup, /auth/login, /auth/me)**  
- Peticiones reales con **Retrofit** hacia la API de **Xano**.  
- Token JWT agregado automáticamente por `AuthInterceptor.kt`.  
- `UserRepository` obtiene los datos del usuario autenticado.
  
##. Endpoints

Base URL:https://x8ki-letl-twmt.n7.xano.io/api:Rfm_61dW

| Método | Ruta | Body | Respuesta |
|--------|------|------|-----------|
| `POST` | `/auth/signup` | `{ "name", "email", "password" }` | `201 { token, user: { id, name, email } }` |
| `POST` | `/auth/login` | `{ "email", "password" }` | `200 { token, user: { id, name, email } }` |
| `GET`  | `/auth/me` | Header: `Authorization: Bearer <token>` | `200 { id, name, email }` |

##.Flujo Princiapl
1. El usuario abre la app → **LoginScreen**.  
2. Si no tiene cuenta, pasa a **RegisterScreen**.  
3. Se validan los datos → se envían a la API (Xano).  
4. Si el login o registro es exitoso → se guarda el token JWT en DataStore.  
5. Se redirige a **HomeScreen**.  
6. Desde el Home puede entrar a su **ProfileScreen**.  
7. En el perfil puede cambiar su imagen o cerrar sesión.

## Casos de error
- Email o contraseña inválidos → mensaje en pantalla.  
- Token expirado → redirección al login.  
- Error de conexión → mensaje visible con fallback seguro.

#justificacion de roles:
- hay una cantidad baja de roles debido a que solamente se necesitan 2, uno para la configuracion y administracion de la app, y otro para el usuario.
