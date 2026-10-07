package dio.budgeting.infrastructure.http;

import dio.budgeting.application.CreateTransactionUseCase;
import dio.budgeting.application.GetBalanceUseCase;
import dio.budgeting.application.GetExpensesByCategoryUseCase;
import dio.budgeting.application.ListTransactionsUseCase;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final CreateTransactionUseCase create;
    private final ListTransactionsUseCase list;
    private final GetExpensesByCategoryUseCase byCategory;
    private final GetBalanceUseCase balance;

    public TransactionController(CreateTransactionUseCase create, ListTransactionsUseCase list,
                                 GetExpensesByCategoryUseCase byCategory, GetBalanceUseCase balance) {
        this.create = create;
        this.list = list;
        this.byCategory = byCategory;
        this.balance = balance;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@Valid @RequestBody CreateTransactionRequest request) {
        return create.execute(request.type(), request.amount(), request.category(), request.description());
    }

    @GetMapping
    public List<Transaction> list() {
        return list.execute();
    }

    @GetMapping("/expenses/category/{category}")
    public GetExpensesByCategoryUseCase.Result expensesByCategory(@PathVariable String category) {
        return byCategory.execute(category);
    }

    @GetMapping("/balance")
    public BalanceResponse balance() {
        return new BalanceResponse(balance.execute());
    }

    public record CreateTransactionRequest(
            @NotNull TransactionType type,
            @NotNull @DecimalMin("0.01") BigDecimal amount,
            @NotBlank String category,
            String description) {}

    public record BalanceResponse(BigDecimal balance) {}
}
