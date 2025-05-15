package com.proyectoTfg.demo.DTO;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarjetaDTO {

    @NotBlank(message = "El número de tarjeta es obligatorio.")
    @Pattern(regexp = "^\\d{16}$", message = "El número de tarjeta debe tener 16 dígitos.")
    private String numeroTarjeta;

    @NotBlank(message = "La fecha de caducidad es obligatoria.")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Formato inválido (MM/YY).")
    private String fechaCaducidad;

    @NotBlank(message = "El código de seguridad es obligatorio.")
    @Pattern(regexp = "^\\d{3}$", message = "El código de seguridad debe tener 3 dígitos.")
    private String codigoSeguridad;

    @NotBlank(message = "El tipo de tarjeta es obligatorio.")
    @Size(min = 4, max = 10, message = "El tipo de tarjeta debe ser válido (Visa, MasterCard, Amex).")
    private String tipoTarjeta;

}

