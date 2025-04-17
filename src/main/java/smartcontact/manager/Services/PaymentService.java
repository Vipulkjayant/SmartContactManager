package smartcontact.manager.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import smartcontact.manager.Entity.CustomerData;


@Service
public class PaymentService {
    
    @Value("${stripe.apiKey}")
    private String secret_key;

    public CustomerData create(CustomerData customerData)throws Exception
    {
        Stripe.apiKey=secret_key;
        Map<String,Object> map=new HashMap<>();
        map.put("name",customerData.getName());
        map.put("email",customerData.getEmail());

        Customer customer=Customer.create(map);
        customerData.setCustomer_Id(customer.getId()); //stripe is creating customer-id corresponding to user

        return customerData;
         
    }

    public String doPayment(Long amount, String currency, String paymentMethod,String customerId) {

        Stripe.apiKey=secret_key;
        try {
            
             PaymentIntentCreateParams paymentIntentCreateParams=PaymentIntentCreateParams.builder()
               .setAmount(amount*100)
               .setCurrency(currency)
               .setPaymentMethod(paymentMethod)
               .setCustomer(customerId)
               .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
               .setConfirm(false)
               .build();

               PaymentIntent paymentIntent=PaymentIntent.create(paymentIntentCreateParams);
               String clientSecret=paymentIntent.getClientSecret();
               System.out.println("Payment Successful....");

               return clientSecret;

        } catch (Exception e) {
           e.printStackTrace();
        }

        return "error";

    }
    
}
