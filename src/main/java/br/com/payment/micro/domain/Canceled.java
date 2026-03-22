package br.com.payment.micro.domain;

import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@Document(collection = "canceled")
public class Canceled extends Payment {

}
