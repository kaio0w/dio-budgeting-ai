package dio.budgeting.application;

import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import dio.budgeting.domain.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public class GetExpensesByCategoryUseCase {
    private final TransactionRepository repository;

    public GetExpensesByCategoryUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public Result execute(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }

        List<Transaction> transactions = repository.findByCategoryIgnoreCase(category).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .toList();

        BigDecimal total = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Result(category, total, transactions.size(), transactions);
    }

    public record Result(String category, BigDecimal total, int quantity, List<Transaction> transactions) {}
}
