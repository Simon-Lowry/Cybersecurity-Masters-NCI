package com.spfwproject.quotes.services;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.spfwproject.quotes.controllers.QuoteController;
import com.spfwproject.quotes.entities.QuoteEntity;
import com.spfwproject.quotes.repositories.QuoteRepository;
import com.spfwproject.quotes.repositories.QuoteRepository;

public class QuoteService {
	private final QuoteRepository quoteRepository;

	private Logger logger = LoggerFactory.getLogger(QuoteService.class);

	public QuoteService(QuoteRepository quoteRepo) {
		this.quoteRepository = quoteRepo;
	}

	public List<QuoteEntity> getQuote() {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName);

		List<QuoteEntity> allQuotes = quoteRepository.findAll();

		logger.info("Exiting method " + methodName + ".");
		return allQuotes;

	}

	public QuoteEntity getQuote( Long id) {
		final String methodName = "getQuote";
		logger.info("Entered " + methodName + ", retrieving Quote with id: " + id);

		QuoteEntity quote = quoteRepository.findById(id).orElseThrow(RuntimeException::new);

		logger.info("Exiting method " + methodName + ".");
		return quote;
	}

	@PostMapping
	public QuoteEntity createQuote( QuoteEntity Quote) throws URISyntaxException {
		final String methodName = "createQuote";
		logger.info("Entered " + methodName);

		QuoteEntity createdQuote = quoteRepository.save(Quote);

		logger.info("Exiting method " + methodName + ".");
		return createdQuote;
	}

	public QuoteEntity updateQuote( Long id, QuoteEntity quote) {
		final String methodName = "updateQuote";
		logger.info("Entered " + methodName);

		QuoteEntity quoteToBeUpdated = quoteRepository.findById(id).orElseThrow(RuntimeException::new);
		quoteToBeUpdated.setQuoteText(quote.getQuoteText());
		quoteToBeUpdated.setQuotePrivacySetting(quote.getQuotePrivacySetting());
		quoteToBeUpdated = quoteRepository.save(quote);

		logger.info("Exiting method " + methodName + ".");
		return quoteToBeUpdated;
	}

	public boolean deleteQuote( Long id) {
		final String methodName = "deleteQuote";
		logger.info("Entered " + methodName);

		quoteRepository.deleteById(id);

		logger.info("Exiting method " + methodName + ".");
		return true;
	}

}
