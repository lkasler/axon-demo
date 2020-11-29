package hu.bridgesoft.axon.library.api.events;

import lombok.Data;

@Data
public class LibraryCreatedEvent {
	private final Integer libraryId;
	private final String name;
}
