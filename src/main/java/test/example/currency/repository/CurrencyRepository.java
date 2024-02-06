package test.example.currency.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import test.example.currency.domain.Currency;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    boolean existsCurrencyByExchangeDate(LocalDate date);

    List<Currency> findAllByExchangeDate(LocalDate date);

    void deleteAllByExchangeDate(LocalDate date);
}
