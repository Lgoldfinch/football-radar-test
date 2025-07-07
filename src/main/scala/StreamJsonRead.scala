import cats.ApplicativeThrow
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all.*
import fs2.data.json.*
import fs2.data.json.circe.*
import fs2.io.file.{Files, Path}

import java.time.LocalDate

object  StreamJsonRead extends IOApp {

  final case class ResultResponse(goalsScored: Int = 0, goalsConceded: Int = 0, teamInvolvement: List[Result] = List.empty) {
    override def toString: String = s"Total goals scored $goalsScored. Total goals conceded $goalsConceded. Results: $teamInvolvement"
  }

  def resultStream(team: String): IO[Option[ResultResponse]] = Files[IO].readUtf8Lines(Path("src/main/resources/test.json"))
    .through(tokens)
    .through(codec.deserialize[IO, Result]).handleErrorWith {
      case e: JsonException =>
              fs2.Stream.eval(IO.println(s"Could not decode: ${e.inner.getMessage}")) >> fs2.Stream.unit
    }
    .fold(ResultResponse()) {
      case (resultResponse, res @ Result(_, `team`, awayTeam, homeGoals, awayGoals)) => resultResponse.copy(
      goalsScored = resultResponse.goalsScored + homeGoals,
      goalsConceded = resultResponse.goalsConceded + awayGoals,
      teamInvolvement = resultResponse.teamInvolvement.prepended(res)
    )
    case (resultResponse, res @ Result(_, homeTeam, `team`, homeGoals, awayGoals)) => resultResponse.copy(
      goalsScored = resultResponse.goalsScored + awayGoals,
      goalsConceded = resultResponse.goalsConceded + homeGoals,
      teamInvolvement = resultResponse.teamInvolvement.prepended(res)
    )
    case (resultResponse, _) => resultResponse
    }.compile.toList.map(_.headOption)

  override def run(args: List[String]): IO[ExitCode] = {
    val argsArray = args.toArray
    val team = argsArray.headOption

    (for {
      team <- team.liftTo[IO](new RuntimeException("Hello"))
      results <- resultStream(team)
      _ <- results.fold(ApplicativeThrow[IO].raiseError(new RuntimeException(s"No result found for $team")))(IO.println)
    } yield ()).as(ExitCode.Success)
  }
}