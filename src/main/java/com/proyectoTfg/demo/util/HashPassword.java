package com.proyectoTfg.demo.util;

import org.mindrot.jbcrypt.BCrypt;

public class HashPassword {

    public static String hashPassword(String contraseña) {
        return BCrypt.hashpw(contraseña, BCrypt.gensalt());
    }

    public static boolean checkPassword(String contraseña, String contraseñaHasheada) {
        return BCrypt.checkpw(contraseña,contraseñaHasheada);
    }
}
