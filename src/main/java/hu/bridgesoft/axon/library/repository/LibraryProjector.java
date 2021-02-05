package hu.bridgesoft.axon.library.repository;

import hu.bridgesoft.axon.library.aggregate.Library;
import hu.bridgesoft.axon.library.api.GetLibraryQuery;
import hu.bridgesoft.axon.library.models.LibraryBean;
import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Service
@Profile("command")
public class LibraryProjector {

	@Autowired
	@Lazy
	/**
	 * AxonConfiguration initialized too late, so constructor injection unfortunately
	 * not working here.
	 */
	private Repository<Library> libraryRepository;


	@QueryHandler
	public LibraryBean getLibrary(GetLibraryQuery query) throws InterruptedException, ExecutionException {
		CompletableFuture<Library> future = new CompletableFuture<>();
		libraryRepository.load("" + query.getLibraryId()).execute(future::complete);
		return toLibraryBean().apply(future.get());
	}

	private Function<Library, LibraryBean> toLibraryBean() {
		return library -> {
			LibraryBean libraryBean = new LibraryBean(library.getLibraryId(), library.getName(), library.getIsbnBooks());
			return libraryBean;
		};
	}

}
