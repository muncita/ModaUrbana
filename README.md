## <Moda Urbana>
- **Caso:** Moda Urbana 
- **Alcance:**  
  ModaUrbana es una aplicación móvil enfocada en la **autenticación y gestión de perfil de usuario**, pensada como parte de una propuesta de e-commerce para moda urbana  
  Se desarrolló todo el flujo de **registro**, **inicio de sesión**, **persistencia de sesión con token JWT**, **validaciones**, **gestión de estado**, **navegación entre pantallas**, y el uso de **recursos nativos del dispositivo** como cámara y galería para el manejo de imágenes de perfil.
## Requisitos y ejecución

- **Stack principal:**  
  - Kotlin  
  - Jetpack Compose (Material 3)  
  - Navigation Compose  
  - ViewModel + StateFlow  
  - Retrofit + GsonConverter  
  - DataStore Preferences  
  - Coil (para carga de imágenes)  
  - Coroutines (asincronía)  

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
- Validación de campos: nombre, email, contraseña y confirmación.  
- Errores visuales dinámicos con `ValidationUtils.kt`.

**Navegación y backstack**  
- Flujo completo con `NavHostController`.  
- Navegación limpia entre pantallas y control de retorno.

**Gestión de estado (carga/éxito/error)**  
- `StateFlow` + `ViewModelScope` para manejar estados reactivos.  
- Se muestran `CircularProgressIndicator` y mensajes de error en pantalla.

**Persistencia local (DataStore)**  
- `SessionManager.kt` guarda el token JWT y la URI del avatar.  
- Limpieza de sesión al hacer logout.

**Almacenamiento de imagen de perfil**  
- Opción de tomar foto o elegir desde galería (`ProfileScreen.kt`).  
- Integración con `FileProvider` para capturas seguras.  
- Imagen persistente guardada como URI local.

**Recursos nativos**  
- Uso de permisos (`Manifest.permission.CAMERA`).  
- `ImagePickerDialog.kt` permite seleccionar cámara o galería.  
- Manejo de resultados con `ActivityResultContracts`.

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
