package io.muffin.inventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "doctor_code")
public class DoctorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="doctor_code")
    private String doctorCode;
    @Column(name="specialization")
    private String specialization;
    @Column(name="description")
    private String description;
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "modified")
    private LocalDateTime modified;
    @Column(name = "deleted")
    private boolean deleted;
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

}
