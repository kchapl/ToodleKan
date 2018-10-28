package model

object ToodledoUrl {

  def tasksFetch(accessToken: String): String =
    s"https://api.toodledo.com/3/tasks/get.php?access_token=$accessToken&fields=goal,duedate"
}
