package model

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.{JsSuccess, Json}

class TaskTest extends FlatSpec with Matchers {

  "reads" should "read task with a due date correctly" in {
    val json =
      """{"id":53960,"title":"Green","modified":1540636860,"completed":1540641600,"goal":798207,"duedate":1540555200}"""
    Task.reads.reads(Json.parse(json)) shouldBe JsSuccess(
      Task(53960, "Green", 798207, 1540641600, Some(LocalDate.of(2018, 10, 26)))
    )
  }

  "reads" should "read task without a due date correctly" in {
    val json =
      """{"id":53979,"title":"Vaccine","modified":1540811614,"completed":0,"goal":0,"duedate":0}"""
    Task.reads.reads(Json.parse(json)) shouldBe JsSuccess(Task(53979, "Vaccine", 0, 0, None))
  }
}
