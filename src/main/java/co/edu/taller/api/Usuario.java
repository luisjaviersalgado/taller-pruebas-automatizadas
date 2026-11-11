package co.edu.taller.api;

/** Usuario registrado. La clave nunca se devuelve al cliente. */
public record Usuario(Long id, String nombre, String correo) { }
