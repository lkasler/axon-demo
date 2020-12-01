package hu.bridgesoft.axon.library.repository;

import hu.bridgesoft.axon.library.api.BookCreatedEvent;
import hu.bridgesoft.axon.library.api.GetBooksQuery;
import hu.bridgesoft.axon.library.models.BookBean;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookRepositoryProjector {

	private final BookRepository bookRepository;

	public BookRepositoryProjector(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@EventHandler
	public void addBook(BookCreatedEvent event) {
		BookEntity book = new BookEntity();
		book.setIsbn(event.getIsbn());
		book.setLibraryId(event.getLibraryId());
		book.setTitle(event.getTitle());
		bookRepository.save(book);
	}

	@QueryHandler
	public List<BookBean> getBooks(GetBooksQuery query) {
		return bookRepository.findByLibraryId(query.getLibraryId()).stream().map(toBook()).collect(Collectors.toList());
	}

	private Function<BookEntity, BookBean> toBook() {
		return e -> {
			BookBean book = new BookBean();
			book.setIsbn(e.getIsbn());
			book.setTitle(e.getTitle());
			return book;
		};
	}
}
