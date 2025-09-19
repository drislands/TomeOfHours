import com.islands.toh.SQLManager
import spock.lang.Specification

class DataTesting extends Specification {
    def setupSpec() {
        SQLManager.initialize("library.sqlite3")
    }

    def "just true"() {
        given:
        true

    }
}
