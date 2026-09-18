package org.example.maosaobra.model;

import jakarta.persistence.*;
import lombok.*;

// JPA
@Entity
@Table(name = "cliente")

// LOMBOK
@NoArgsConstructor
@Getter
@Setter
public class Cliente extends Pessoa{

}
