import cats.effect.{ExitCode, IO, IOApp, Resource}

import scala.io.Source
import io.circe.parser.parse
import cats.syntax.all.*
import io.circe.generic.auto.*

object Main extends IOApp {

  final case class Result(gameId: Int, homeTeam: String, awayTeam: String, kickoff: String, seasonId: Int, homeGoals: Int, awayGoals: Int)

  // Read from the file
  // turn into json
  // turn into result format.

//  foldLeft to exhaust all the list. 
//  foldLeft(Map[])()  
  final case class FinalResult(gamesPlayed: Int = 0, teamName: String, gamesWon: Int =0, gamesDrawn: Int=0, gamesLost: Int=0, goalsScored: Int=0, goalsAgainst: Int=0, totalPoints: Int=0)

  override def run(args: List[String]): IO[ExitCode] = {

    val source = IO(Source.fromFile("src/main/scala/football.json"))

    val resource = Resource.make(source)(a =>  IO(a.close()))

    for {
       json <- resource.use {
         source =>
           parse(source.getLines().mkString).liftTo[IO]
       }
       results <- json.as[List[Result]].liftTo[IO]
        _ <- IO.println(getTotalPoints(results))
    } yield ExitCode.Success
  }
  
  private def getTotalPoints(results: List[Result]): List[FinalResult] =
    val homeGames = results.groupBy(_.homeTeam)
    val awayGames = results.groupBy(_.awayTeam)

    val resultsByTeam = homeGames ++ awayGames

    resultsByTeam.map {
      case (teamName, results) => 
       results.foldLeft(FinalResult(teamName = teamName)){
          case (accumulatedResult, Result(gameId, `teamName`, awayTeam, kickoff, seasonId, homeGoals, awayGoals)) => 
            if (homeGoals > awayGoals) // home team wins
              FinalResult(gamesPlayed = accumulatedResult.gamesPlayed + 1, teamName = teamName, accumulatedResult.gamesWon + 1, accumulatedResult.gamesDrawn, accumulatedResult.gamesLost, accumulatedResult.goalsScored + homeGoals, accumulatedResult.goalsAgainst + awayGoals, accumulatedResult.totalPoints + 3)
            else if (awayGoals > homeGoals) // away team wins team wins
              FinalResult(gamesPlayed = accumulatedResult.gamesPlayed + 1, teamName = teamName, accumulatedResult.gamesWon,  accumulatedResult.gamesDrawn, accumulatedResult.gamesLost + 1, accumulatedResult.goalsScored + homeGoals, accumulatedResult.goalsAgainst + awayGoals, accumulatedResult.totalPoints)
            else 
              FinalResult(gamesPlayed = accumulatedResult.gamesPlayed + 1, teamName = teamName, accumulatedResult.gamesWon,  accumulatedResult.gamesDrawn, accumulatedResult.gamesLost, accumulatedResult.goalsScored + homeGoals, accumulatedResult.goalsAgainst + awayGoals, accumulatedResult.totalPoints + 1)
          
          case (accumulatedResult, Result(gameId, teamName, `teamName`, kickoff, seasonId, homeGoals, awayGoals)) =>
            if (homeGoals > awayGoals) // home team wins
              FinalResult(gamesPlayed = accumulatedResult.gamesPlayed + 1, teamName = teamName, accumulatedResult.gamesWon, accumulatedResult.gamesDrawn, accumulatedResult.gamesLost + 1, accumulatedResult.goalsScored + homeGoals, accumulatedResult.goalsAgainst + awayGoals, accumulatedResult.totalPoints)
            else if (awayGoals > homeGoals) // away team wins team wins
              FinalResult(gamesPlayed = accumulatedResult.gamesPlayed + 1, teamName = teamName, accumulatedResult.gamesWon + 1, accumulatedResult.gamesDrawn, accumulatedResult.gamesLost, accumulatedResult.goalsScored + homeGoals, accumulatedResult.goalsAgainst + awayGoals, accumulatedResult.totalPoints + 3) 
            else
              FinalResult(gamesPlayed = accumulatedResult.gamesPlayed + 1, teamName = teamName, accumulatedResult.gamesWon, accumulatedResult.gamesDrawn, accumulatedResult.gamesLost, accumulatedResult.goalsScored + homeGoals, accumulatedResult.goalsAgainst + awayGoals, accumulatedResult.totalPoints + 1)

          case (accumulatedResult, result) => throw new Exception("Shouldn't be here")
        }
    }.toList
}
