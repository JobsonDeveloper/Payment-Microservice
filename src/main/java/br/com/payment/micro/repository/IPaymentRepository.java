package br.com.payment.micro.repository;

import br.com.payment.micro.domain.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPaymentRepository extends MongoRepository<Payment, String> {
    public Optional<Payment> findBySaleId(String saleId);
}
