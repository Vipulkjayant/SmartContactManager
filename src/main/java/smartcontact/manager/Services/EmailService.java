package smartcontact.manager.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailServiceImpl{

    @Autowired
    private JavaMailSender javaMailSender;


    private Logger logger=LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendEmail(String to, String subject, String message) {
        
       SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
       simpleMailMessage.setTo(to);
       simpleMailMessage.setSubject(subject);
       simpleMailMessage.setText(message);
       simpleMailMessage.setFrom("testforlearn129@gmail.com");

       javaMailSender.send(simpleMailMessage);
       logger.info("OTP has been generated successfully");

    }
    

}
