package hu.bridgesoft.axon.rest;

import hu.bridgesoft.axon.library.api.GetBooksQuery;
import hu.bridgesoft.axon.library.api.GetLibraryQuery;
import hu.bridgesoft.axon.library.api.RegisterBookCommand;
import hu.bridgesoft.axon.library.api.RegisterLibraryCommand;
import hu.bridgesoft.axon.library.models.BookBean;
import hu.bridgesoft.axon.library.models.LibraryBean;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.axonframework.messaging.responsetypes.ResponseTypes.instanceOf;
import static org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf;

@RestController
@Profile("rest")
public class LibraryRestController {

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;

	@Autowired
	public LibraryRestController(CommandGateway commandGateway, QueryGateway queryGateway) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}

	@PostMapping("/api/library")
	public String addLibrary(@RequestBody LibraryBean library) {
		commandGateway.send(new RegisterLibraryCommand(library.getLibraryId(), library.getName()));
		return "Saved";
	}

	@GetMapping("/api/library/{library}")
	public LibraryBean getLibrary(@PathVariable Integer library) throws InterruptedException, ExecutionException {
		CompletableFuture<LibraryBean> future = queryGateway.query(new GetLibraryQuery(library), LibraryBean.class);
		return future.get();
	}

	@PostMapping("/api/library/{library}/book")
	public String addBook(@PathVariable Integer library, @RequestBody BookBean book) {
		commandGateway.send(new RegisterBookCommand(library, book.getIsbn(), book.getTitle()));
		return "Saved";
	}

	@GetMapping("/api/library/{library}/book")
	public List<BookBean> getBook(@PathVariable Integer library) throws InterruptedException, ExecutionException {
		return queryGateway.query(new GetBooksQuery(library), ResponseTypes.multipleInstancesOf(BookBean.class)).get();
	}

	@GetMapping(value = "/api/library/{library}/book/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<BookBean> subscribeRoomMessages(@PathVariable Integer library) {
		GetBooksQuery query = new GetBooksQuery(library);
		SubscriptionQueryResult<List<BookBean>, BookBean> result = queryGateway.subscriptionQuery(
				query, multipleInstancesOf(BookBean.class), instanceOf(BookBean.class)
		);
        /* If you only want to send new messages to the client, you could simply do:
                return result.updates();
           However, in our implementation we want to provide both existing messages and new ones,
           so we combine the initial result and the updates in a single flux. */
		Flux<BookBean> initialResult = result.initialResult().flatMapMany(Flux::fromIterable);
		return Flux.concat(initialResult, result.updates());
	}

}
