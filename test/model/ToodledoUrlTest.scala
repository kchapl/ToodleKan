package model
import org.scalatest.{FlatSpec, Matchers}

class ToodledoUrlTest extends FlatSpec with Matchers {

  "tasksFetch" should "include correct optional fields" in {
    ToodledoUrl.tasksFetch("") should endWith("&fields=goal,duedate")
  }
}
