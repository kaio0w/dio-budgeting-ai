package dio.budgeting.application;

import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionRepository;
import dio.budgeting.domain.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetExpensesByCategoryUseCaseTest {
    @Test
    void shouldSumOnlyExpensesFromCategory() {
        InMemoryRepository repository = new InMemoryRepository();
        repository.save(Transaction.create(TransactionType.EXPENSE, new BigDecimal("45.00"), "alimentacao", "Almoço"));
        repository.save(Transaction.create(TransactionType.EXPENSE, new BigDecimal("20.50"), "alimentacao", "Café"));
        repository.save(Transaction.create(TransactionType.INCOME, new BigDecimal("100.00"), "alimentacao", "Reembolso"));

        var result = new GetExpensesByCategoryUseCase(repository).execute("alimentacao");

        assertEquals(new BigDecimal("65.50"), result.total());
        assertEquals(2, result.quantity());
    }

    private static class InMemoryRepository implements TransactionRepository {
        private final List<Transaction> items = new ArrayList<>();

        public Transaction save(Transaction transaction) { items.add(transaction); return transaction; }
        public List<Transaction> findAll() { return List.copyOf(items); }
        public List<Transaction> findByCategoryIgnoreCase(String category) {
            return items.stream().filter(t -> t.getCategory().equalsIgnoreCase(category)).toList();
        }
    }
}
