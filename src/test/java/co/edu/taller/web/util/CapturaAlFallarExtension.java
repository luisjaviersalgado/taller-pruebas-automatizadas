package co.edu.taller.web.util;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/** Si una prueba falla, toma automáticamente una captura antes de cerrar el navegador. */
public class CapturaAlFallarExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext contexto) {
        if (contexto.getExecutionException().isPresent()
                && contexto.getRequiredTestInstance() instanceof ConNavegador prueba) {
            Capturas.tomar(prueba.navegador(), "FALLO_" + contexto.getDisplayName());
        }
    }
}
