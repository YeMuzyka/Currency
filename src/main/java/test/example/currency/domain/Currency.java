package test.example.currency.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "currency")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "r030")
    private Integer r030;

    @Column(name = "txt")
    private String txt;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "cc")
    private String cc;

    @Column(name = "exchangeDate")
    private LocalDate exchangeDate;

    @CreatedDate
    @Column(name = "createdDate", updatable = false)
    private LocalDateTime createdDate;
}
