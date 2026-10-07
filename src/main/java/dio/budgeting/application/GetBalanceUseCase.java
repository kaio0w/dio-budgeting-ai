package dio.budgeting.application;

import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import dio.budgeting.domain.TransactionType;

import java.math.BigDecimal;

public class GetBalanceUseCase {
    private final TransactionRepository repository;

    public GetBalanceUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public BigDecimal execute() {
        return repository.findAll().stream()
                .map(this::signedValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal signedValue(Transaction transaction) {
        return transaction.getType() == TransactionType.INCOME
                ? transaction.getAmount()
                : transaction.getAmount().negate();
    }
}
