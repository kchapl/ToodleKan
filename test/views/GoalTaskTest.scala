package views

import model.Task
import org.scalatest.{FlatSpec, Matchers}

class GoalTaskTest extends FlatSpec with Matchers {

  it should "contain a span with highlighted class if task is starred" in {
    val task = Task(1, "title", 0, 0, None, hasStar = true)
    val view = views.html.goalTask(task)
    view.toString should include("""<span class="highlighted">title</span>""")
  }
}
