package br.com.controlefinanceiro.backend.services;

import br.com.controlefinanceiro.backend.models.EmailModel;
import br.com.controlefinanceiro.backend.services.impl.UserServiceImpl.UserProducer;

public interface EmailService {

	public EmailModel createEmail(EmailModel emailModel);
	public EmailModel sendEmail(EmailModel emailModel);
	public EmailModel sendEmail(UserProducer user, String templateName, String subject);
}
