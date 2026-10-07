package dio.budgeting.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String category;
    private final String description;
    private final LocalDateTime occurredAt;

    public Transaction(UUID id, TransactionType type, BigDecimal amount, String category,
                       String description, LocalDateTime occurredAt) {
        if (type == null) throw new IllegalArgumentException("O tipo da transação é obrigatório.");
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("O valor deve ser maior que zero.");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("A categoria é obrigatória.");

        this.id = id == null ? UUID.randomUUID() : id;
        this.type = type;
        this.amount = amount;
        this.category = category.trim();
        this.description = description == null ? "" : description.trim();
        this.occurredAt = occurredAt == null ? LocalDateTime.now() : occurredAt;
    }

    public static Transaction create(TransactionType type, BigDecimal amount, String category, String description) {
        return new Transaction(null, type, amount, category, description, LocalDateTime.now());
    }

    public UUID getId() { return id; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDateTime getOccurredAt() { return occurredAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
