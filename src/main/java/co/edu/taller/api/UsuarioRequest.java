package co.edu.taller.api;

/** Datos que envía el cliente para registrar un usuario. */
public record UsuarioRequest(String nombre, String correo, String clave) { }
