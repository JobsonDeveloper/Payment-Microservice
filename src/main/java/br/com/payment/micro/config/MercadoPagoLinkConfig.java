package br.com.payment.micro.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoLinkConfig {
    @Value("${e-sale.access.token}")
    private String accessToken;

    @PostConstruct
    public void init()
    {
        MercadoPagoConfig.setAccessToken(this.accessToken);
    }
}
