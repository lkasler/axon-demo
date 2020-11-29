package hu.bridgesoft.axon.library.repository;

import hu.bridgesoft.axon.library.aggregate.Library;
import hu.bridgesoft.axon.library.api.queries.GetLibraryQuery;
import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class LibraryProjector {

	@Autowired
	@Lazy
	/**
	 * AxonConfiguration initialized too late, so constructor injection unfortunately
	 * not working here.
	 */
	private Repository<Library> libraryRepository;


	@QueryHandler
	public Library getLibrary(GetLibraryQuery query) throws InterruptedException, ExecutionException {
		CompletableFuture<Library> future = new CompletableFuture<>();
		libraryRepository.load("" + query.getLibraryId()).execute(future::complete);
		return future.get();
	}

}
