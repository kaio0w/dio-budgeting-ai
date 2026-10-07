package dio.budgeting.infrastructure.persistence;

import dio.budgeting.domain.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class TransactionJpaEntity {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String category;

    private String description;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    protected TransactionJpaEntity() {}

    public TransactionJpaEntity(UUID id, TransactionType type, BigDecimal amount, String category,
                                String description, LocalDateTime occurredAt) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.occurredAt = occurredAt;
    }

    public UUID getId() { return id; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
}
