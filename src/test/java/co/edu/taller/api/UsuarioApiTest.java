package co.edu.taller.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Pruebas automatizadas del servicio REST con JUnit 5 y TestRestTemplate. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioApiTest {

    @Autowired
    private TestRestTemplate rest;

    /** Método reutilizable: envía un POST /api/usuarios con los datos dados. */
    @SuppressWarnings("rawtypes")
    private ResponseEntity<Map> crearUsuario(String nombre, String correo, String clave) {
        return rest.postForEntity("/api/usuarios", new UsuarioRequest(nombre, correo, clave), Map.class);
    }

    @Test
    @DisplayName("CP-API-01 GET /api/saludo con nombre devuelve 200 y el saludo")
    void saludoConNombre() {
        ResponseEntity<String> r = rest.getForEntity("/api/saludo?nombre=Luis", String.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals("Hola Luis", r.getBody());
    }

    @Test
    @DisplayName("CP-API-02 GET /api/saludo sin parámetro usa el valor por defecto")
    void saludoPorDefecto() {
        ResponseEntity<String> r = rest.getForEntity("/api/saludo", String.class);
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals("Hola mundo", r.getBody());
    }

    @Test
    @DisplayName("CP-API-03 POST /api/usuarios con datos válidos devuelve 201")
    @SuppressWarnings("rawtypes")
    void crearUsuarioValido() {
        ResponseEntity<Map> r = crearUsuario("Luis Salgado", "luis@correo.com", "Clave1234");
        assertEquals(HttpStatus.CREATED, r.getStatusCode());
        assertNotNull(r.getBody().get("id"));
        assertEquals("Luis Salgado", r.getBody().get("nombre"));
        assertEquals("luis@correo.com", r.getBody().get("correo"));
        assertFalse(r.getBody().containsKey("clave"), "La clave no debe exponerse en la respuesta");
    }

    @Test
    @DisplayName("CP-API-04 GET /api/usuarios/{id} devuelve el usuario creado")
    @SuppressWarnings("rawtypes")
    void consultarUsuarioCreado() {
        ResponseEntity<Map> creado = crearUsuario("Maria Lopez", "maria@correo.com", "Clave1234");
        Long id = ((Number) creado.getBody().get("id")).longValue();

        ResponseEntity<Map> consulta = rest.getForEntity("/api/usuarios/" + id, Map.class);

        assertEquals(HttpStatus.OK, consulta.getStatusCode());
        assertEquals("maria@correo.com", consulta.getBody().get("correo"));
    }

    @Test
    @DisplayName("CP-API-05 GET /api/usuarios/{id} inexistente devuelve 404")
    @SuppressWarnings("rawtypes")
    void usuarioInexistente() {
        ResponseEntity<Map> r = rest.getForEntity("/api/usuarios/99999", Map.class);
        assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
        assertEquals("Usuario no encontrado", r.getBody().get("error"));
    }

    @ParameterizedTest(name = "CP-API-06 datos inválidos -> {3}")
    @CsvSource(delimiter = '|', value = {
        "''        | ana@correo.com | Clave1234 | El nombre es obligatorio",
        "Ana Perez | anacorreo.com  | Clave1234 | El correo no tiene un formato válido",
        "Ana Perez | ana@correo.com | abc123    | La clave debe tener al menos 8 caracteres"
    })
    @SuppressWarnings({"rawtypes", "unchecked"})
    void datosInvalidosDevuelven400(String nombre, String correo, String clave,
                                    String mensajeEsperado) {
        ResponseEntity<Map> r = crearUsuario(nombre, correo, clave);

        assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
        List<String> errores = (List<String>) r.getBody().get("errores");
        assertTrue(errores.contains(mensajeEsperado), "Errores recibidos: " + errores);
    }
}
