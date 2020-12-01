package hu.bridgesoft.axon.library.repository

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class BookEntity {
    @Id
     var isbn: String? = null

    @Column
    var libraryId = 0

    @Column
    var title: String? = null
}