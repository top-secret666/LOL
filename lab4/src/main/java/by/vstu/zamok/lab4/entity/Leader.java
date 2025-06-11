package by.vstu.zamok.lab4.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "leaders")
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"students"})
public class Leader extends AbstractEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "position")
    private String position;

    @JsonManagedReference
    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;
}