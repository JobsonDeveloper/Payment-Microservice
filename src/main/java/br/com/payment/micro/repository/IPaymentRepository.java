package br.com.payment.micro.repository;

import br.com.payment.micro.domain.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepository extends MongoRepository<Payment, String> {
}
