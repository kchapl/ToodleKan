package model

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.{JsSuccess, Json}

class TaskTest extends FlatSpec with Matchers {

  "reads" should "read task with a due date correctly" in {
    val json =
      """{"id":53960,"title":"Green","modified":1540636860,"completed":1540641600,"goal":798207,"star":0,"duedate":1540555200}"""
    Task.reads.reads(Json.parse(json)) shouldBe JsSuccess(
      Task(
        id = 53960,
        title = "Green",
        goalId = 798207,
        completed = 1540641600,
        due = Some(LocalDate.of(2018, 10, 26)),
        hasStar = false
      ))
  }

  it should "read task without a due date correctly" in {
    val json =
      """{"id":53979,"title":"Vaccine","modified":1540811614,"completed":0,"goal":0,"star":0,"duedate":0}"""
    Task.reads.reads(Json.parse(json)) shouldBe JsSuccess(
      Task(
        id = 53979,
        title = "Vaccine",
        goalId = 0,
        completed = 0,
        due = None,
        hasStar = false
      ))
  }

  it should "read starred task correctly" in {
    val json =
      """{"id":53979,"title":"Vaccine","modified":1540811614,"completed":0,"goal":0,"star":1,"duedate":0}"""
    Task.reads.reads(Json.parse(json)) shouldBe JsSuccess(
      Task(
        id = 53979,
        title = "Vaccine",
        goalId = 0,
        completed = 0,
        due = None,
        hasStar = true
      ))
  }
}
