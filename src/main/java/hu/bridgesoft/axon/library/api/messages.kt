package hu.bridgesoft.axon.library.api

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RegisterBookCommand(@TargetAggregateIdentifier val libraryId: Int, val isbn: String, val title: String)
data class RegisterLibraryCommand(@TargetAggregateIdentifier val libraryId: Int, val name: String)

data class BookCreatedEvent(@TargetAggregateIdentifier val libraryId: Int, val isbn:String, val title: String)
data class LibraryCreatedEvent(val libraryId: Int, val name: String)

data class GetBooksQuery(val libraryId: Int)
data class GetLibraryQuery(val libraryId: Int)
