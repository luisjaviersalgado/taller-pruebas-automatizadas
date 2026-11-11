package co.edu.taller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    private static final Pattern CORREO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final Map<Long, Usuario> usuarios = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @GetMapping("/saludo")
    public ResponseEntity<String> saludo(@RequestParam(defaultValue = "mundo") String nombre) {
        return ResponseEntity.ok("Hola " + nombre);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Object> crear(@RequestBody UsuarioRequest solicitud) {
        List<String> errores = validar(solicitud);
        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errores", errores));
        }
        Usuario nuevo = new Usuario(secuencia.getAndIncrement(), solicitud.nombre().trim(), solicitud.correo());
        usuarios.put(nuevo.id(), nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Object> obtener(@PathVariable Long id) {
        Usuario encontrado = usuarios.get(id);
        if (encontrado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(encontrado);
    }

    private List<String> validar(UsuarioRequest s) {
        List<String> errores = new ArrayList<>();
        if (s.nombre() == null || s.nombre().isBlank()) {
            errores.add("El nombre es obligatorio");
        }
        if (s.correo() == null || !CORREO.matcher(s.correo()).matches()) {
            errores.add("El correo no tiene un formato válido");
        }
        if (s.clave() == null || s.clave().length() < 8) {
            errores.add("La clave debe tener al menos 8 caracteres");
        }
        return errores;
    }
}
