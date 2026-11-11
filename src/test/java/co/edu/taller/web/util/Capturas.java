package co.edu.taller.web.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/** Guarda capturas de pantalla en target/capturas como evidencia de la ejecución. */
public final class Capturas {

    private Capturas() { }

    public static Path tomar(WebDriver driver, String nombre) {
        try {
            File origen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destino = Path.of("target", "capturas", nombre.replaceAll("[^a-zA-Z0-9_-]", "_") + ".png");
            Files.createDirectories(destino.getParent());
            return Files.copy(origen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar la captura " + nombre, e);
        }
    }
}
