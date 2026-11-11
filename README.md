# taller-pruebas-automatizadas

Taller AA1 – Prueba y Calidad de Software (Unidad 2): scripts de prueba automatizados para una API REST y un formulario web.

## Contenido

- **Aplicación de práctica** (Spring Boot 3): servicio REST de usuarios (`/api`) y formulario `registro.html`.
- **Pruebas de la API**: JUnit 5 + `TestRestTemplate` (`UsuarioApiTest`).
- **Pruebas del formulario**: Selenium WebDriver con Page Object Model, esperas explícitas y datos en CSV (`RegistroFormularioTest`).
- **Evidencias**: capturas automáticas en `target/capturas` y reportes en `target/surefire-reports`.

## Requisitos

JDK 17 o superior, Maven 3.9 o superior y Google Chrome.

## Cómo ejecutar

```
mvn spring-boot:run                 # levanta la app en http://localhost:8080/registro.html
mvn test                            # todas las pruebas (13)
mvn test -Dtest=UsuarioApiTest      # solo la API
mvn test -Dtest=RegistroFormularioTest -Dheadless=true   # solo el formulario, sin ventana
```

## Autor

Luis Javier Salgado Guzmán – Ingeniería de Software.
