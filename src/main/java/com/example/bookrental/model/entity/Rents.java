package com.example.bookrental.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "Rent")
public class Rents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "bookId", nullable = false)
    private Books book;

    @Column(name = "RentDate", nullable = false)
    private LocalDateTime RentDate;

    @Column(name = "returnDate", nullable = false)
    private LocalDateTime ReturnDate;

    @PrePersist
    public void prePersist() {
        if (RentDate == null) {
            RentDate = LocalDateTime.now();
        }
        if (ReturnDate == null) {
            ReturnDate = LocalDateTime.now();
        }
    }
}

