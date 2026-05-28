package com.hotelmanager.model;//Dados que um quarto possui e como esses dados serão salvos no banco.

import com.hotelmanager.enums.StatusQuarto;
import com.hotelmanager.enums.TipoQuarto;
    //Transformar a classe Java em tabela do banco.
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
    //Regra aplicada a banco.
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
    //Lombok reduz código repetitivo.
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor//Cria um construtor vazio.
@AllArgsConstructor//Cria um construtor com todos os atributos.
@Entity//Classe que representa uma tabela no banco de dados.
@Table(name = "quartos")//Nome da tabela no banco
public class Quarto {

    @Id//Gerar ID de acordo com o Banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O numero do quarto e obrigatorio.")
    private String numero;

    @NotNull(message = "O tipo do quarto e obrigatorio.")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 40)
    private TipoQuarto tipo;

    @NotNull(message = "A capacidade do quarto e obrigatoria.")
    @Min(value = 1, message = "A capacidade deve ser maior que zero.")
    private Integer capacidade;

    @NotNull(message = "O valor da diaria e obrigatorio.")
    @DecimalMin(value = "0.01", message = "O valor da diaria deve ser maior que zero.")
    private BigDecimal valorDiaria;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 40)
    private StatusQuarto status;

    private String observacoes;

    private Boolean ativo;
}
