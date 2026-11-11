package co.edu.taller.web.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Page Object del formulario de registro: encapsula localizadores y acciones. */
public class RegistroPage {

    private final WebDriver driver;
    private final WebDriverWait espera;

    private final By campoNombre = By.id("nombre");
    private final By campoCorreo = By.id("correo");
    private final By campoClave = By.id("clave");
    private final By botonRegistrar = By.id("btnRegistrar");
    private final By mensajeExito = By.id("mensajeExito");

    public RegistroPage(WebDriver driver) {
        this.driver = driver;
        this.espera = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public RegistroPage abrir(String urlBase) {
        driver.get(urlBase + "/registro.html");
        espera.until(ExpectedConditions.visibilityOfElementLocated(campoNombre));
        return this;
    }

    public RegistroPage registrar(String nombre, String correo, String clave) {
        escribir(campoNombre, nombre);
        escribir(campoCorreo, correo);
        escribir(campoClave, clave);
        driver.findElement(botonRegistrar).click();
        return this;
    }

    /** Espera a que aparezca el mensaje de éxito y devuelve su texto. */
    public String mensajeExito() {
        return espera.until(ExpectedConditions.visibilityOfElementLocated(mensajeExito)).getText();
    }

    public boolean hayMensajeExito() {
        return driver.findElement(mensajeExito).isDisplayed();
    }

    /** Devuelve el texto de error de un campo ("nombre", "correo" o "clave"). */
    public String error(String campo) {
        String sufijo = Character.toUpperCase(campo.charAt(0)) + campo.substring(1);
        By localizador = By.id("error" + sufijo);
        espera.until(d -> !d.findElement(localizador).getText().isBlank());
        return driver.findElement(localizador).getText();
    }

    private void escribir(By localizador, String texto) {
        WebElement elemento = driver.findElement(localizador);
        elemento.clear();
        if (texto != null) {
            elemento.sendKeys(texto);
        }
    }
}
