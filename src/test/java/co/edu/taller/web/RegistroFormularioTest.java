package co.edu.taller.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import co.edu.taller.web.pages.RegistroPage;
import co.edu.taller.web.util.Capturas;
import co.edu.taller.web.util.CapturaAlFallarExtension;
import co.edu.taller.web.util.ConNavegador;

/** Pruebas funcionales del formulario de registro con Selenium WebDriver. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(CapturaAlFallarExtension.class)
class RegistroFormularioTest implements ConNavegador {

    @LocalServerPort
    private int puerto;

    private WebDriver driver;
    private RegistroPage pagina;

    @Override
    public WebDriver navegador() {
        return driver;
    }

    @BeforeEach
    void iniciarNavegador() {
        ChromeOptions opciones = new ChromeOptions();
        opciones.addArguments("--window-size=1280,900");
        if (Boolean.getBoolean("headless")) {      // mvn test -Dheadless=true
            opciones.addArguments("--headless=new");
        }
        driver = new ChromeDriver(opciones);
        pagina = new RegistroPage(driver).abrir("http://localhost:" + puerto);
    }

    @AfterEach
    void cerrarNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("CP-WEB-01 registro exitoso con datos válidos")
    void registroExitoso() {
        pagina.registrar("Luis Salgado", "luis@correo.com", "Clave1234");

        String mensaje = pagina.mensajeExito();

        assertTrue(mensaje.contains("Registro exitoso"), "Mensaje recibido: " + mensaje);
        assertTrue(mensaje.contains("Luis Salgado"));
        Capturas.tomar(driver, "CP-WEB-01_registro_exitoso");
    }

    @ParameterizedTest(name = "{0}")
    @CsvFileSource(resources = "/datos/registro_invalido.csv",
                   numLinesToSkip = 1, delimiter = ';')
    void datosInvalidosMuestranError(String caso, String nombre, String correo, String clave,
                                     String campoError, String mensajeEsperado) {
        pagina.registrar(nombre, correo, clave);

        assertEquals(mensajeEsperado, pagina.error(campoError));
        assertFalse(pagina.hayMensajeExito(), "No debe mostrarse el mensaje de éxito");
        Capturas.tomar(driver, caso);
    }
}
