package co.edu.taller.web.util;

import org.openqa.selenium.WebDriver;

/** Permite a la extensión de capturas obtener el navegador de la prueba en curso. */
public interface ConNavegador {
    WebDriver navegador();
}
