package hu.bridgesoft.axon.library.aggregate;

import java.util.ArrayList;
import java.util.List;

import hu.bridgesoft.axon.library.api.commands.RegisterBookCommand;
import hu.bridgesoft.axon.library.api.events.BookCreatedEvent;
import hu.bridgesoft.axon.library.api.events.LibraryCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import hu.bridgesoft.axon.library.api.commands.RegisterLibraryCommand;
import org.springframework.util.Assert;

@Aggregate
public class Library {

	@AggregateIdentifier
	private Integer libraryId;

	private String name;

	private List<String> isbnBooks;

	protected Library() {
		// For Axon instantiation
	}

	@CommandHandler
	public Library(RegisterLibraryCommand cmd) {
		Assert.notNull(cmd.getLibraryId(), "ID should not be null");
		Assert.notNull(cmd.getName(), "Name should not be null");

		AggregateLifecycle.apply(new LibraryCreatedEvent(cmd.getLibraryId(), cmd.getName()));
	}

	public Integer getLibraryId() {
		return libraryId;
	}

	public String getName() {
		return name;
	}

	public List<String> getIsbnBooks() {
		return isbnBooks;
	}

	@CommandHandler
	public void addBook(RegisterBookCommand cmd) {
		Assert.notNull(cmd.getLibraryId(), "ID should not be null");
		Assert.notNull(cmd.getIsbn(), "Book ISBN should not be null");

		AggregateLifecycle.apply(new BookCreatedEvent(cmd.getLibraryId(), cmd.getIsbn(), cmd.getTitle()));
	}

	@EventSourcingHandler
	private void handleCreatedEvent(LibraryCreatedEvent event) {
		libraryId = event.getLibraryId();
		name = event.getName();
		isbnBooks = new ArrayList<>();
	}

	@EventSourcingHandler
	private void addBook(BookCreatedEvent event) {
		isbnBooks.add(event.getIsbn());
	}

}