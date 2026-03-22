package br.com.payment.micro.repository;

import br.com.payment.micro.domain.Canceled;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICanceledRepository extends MongoRepository<Canceled, String> {
}
