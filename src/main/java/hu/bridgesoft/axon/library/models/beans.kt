package hu.bridgesoft.axon.library.models

class BookBean(var isbn: String? = null, var title: String? = null)
class LibraryBean(var libraryId: Int? = null, var name: String? = null, var isbnBooks: List<String?>? = null)