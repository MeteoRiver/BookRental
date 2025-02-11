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

    @CreatedDate
    private LocalDateTime RentDate;

    @CreatedDate
    private LocalDateTime ReturnDate;
}

