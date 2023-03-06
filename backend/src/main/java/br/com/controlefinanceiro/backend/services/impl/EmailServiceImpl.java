package br.com.controlefinanceiro.backend.services.impl;

import java.time.LocalDateTime;
import java.util.HashMap;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.controlefinanceiro.backend.enuns.StatusEmail;
import br.com.controlefinanceiro.backend.enuns.TypeEmail;
import br.com.controlefinanceiro.backend.models.EmailModel;
import br.com.controlefinanceiro.backend.repositories.EmailRepository;
import br.com.controlefinanceiro.backend.services.EmailService;
import br.com.controlefinanceiro.backend.services.impl.UserServiceImpl.UserProducer;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

	@Autowired private EmailRepository emailRepository;
	@Autowired private JavaMailSender javaMailSender;
	private final Configuration fmConfiguration;
	@Value("kraken.mail.from")
	private String krakenMailFrom;
	
	@Override
	public EmailModel createEmail(EmailModel emailModel) {
		emailModel.setUpdatedAt(LocalDateTime.now());
		
		try {
        	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
 
            mimeMessageHelper.setSubject(emailModel.getSubject());
            mimeMessageHelper.setFrom(emailModel.getEmailFrom(), "Kraken");
            
            mimeMessageHelper.setTo(emailModel.getEmailTo());	            	            
            mimeMessageHelper.setText(emailModel.getBody(), emailModel.isHTML());
 
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            
            emailModel.setStatus(StatusEmail.SENT);
            emailModel.setSentAt(LocalDateTime.now());
		} catch (Exception e) {
			emailModel.setStatus(StatusEmail.ERROR);
        	e.printStackTrace();
		} finally {
			emailModel = emailRepository.save(emailModel);
		}
		
		return 	emailModel;
	}

	@Override
	public EmailModel sendEmail(EmailModel emailModel) {
		emailModel.setCreatedAt(LocalDateTime.now());
		emailModel.setUpdatedAt(LocalDateTime.now());
		emailModel.setStatus(StatusEmail.PROCESSING);
		
		return emailRepository.save(emailModel);
	}

	@Override
	public EmailModel sendEmail(UserProducer user, String templateName, String subject) {
		log.info("Sending Email to : {}", user.getEmail());
		
		var emailModel = new EmailModel();
		emailModel.setIntegrationId(user.getUserId());
		emailModel.setBody(genBody(user, templateName));
		
		emailModel.setEmailFrom(this.krakenMailFrom);
		emailModel.setEmailTo(user.getEmail());
		emailModel.setStatus(StatusEmail.PROCESSING);
		emailModel.setSubject(subject);
		emailModel.setType(TypeEmail.HTML);		
		
		emailModel = createEmail(emailModel);
		return sendEmail(emailModel);
	}
	
	private String genBody(UserProducer user, String templateName) { 
        StringBuffer content = new StringBuffer();
        ObjectMapper mapObject = new ObjectMapper();
        @SuppressWarnings("unchecked")
		HashMap<String, Object> map = mapObject.convertValue(user, HashMap.class);
        try {
            content.append(FreeMarkerTemplateUtils.processTemplateIntoString(fmConfiguration.getTemplate(templateName), map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

}
