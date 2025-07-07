import cats.effect.{ExitCode, IO, IOApp, Resource}
import io.circe.parser.*
import cats.syntax.all.*

import scala.io.{BufferedSource, Source}

object AnotherJsonRead extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val teamOpt = args.headOption

    val result = Resource.make[IO, BufferedSource](IO.blocking(Source.fromFile("src/main/resources/test2.json")))(
      source => IO(source.close())
    ).use(
      source =>
        parse(source.getLines().mkString).flatMap(_.as[List[Result]]).liftTo[IO]
    )

    (for {
       team <- teamOpt.liftTo[IO](new RuntimeException("Hello"))
       res <- result
       referencedGames = res.foldLeft(List.empty[Result]) {
         case (acc, nextResult) if nextResult.homeTeam === team => acc.prepended(nextResult)
         case (acc, nextResult) if nextResult.awayTeam === team => acc.prepended(nextResult)
         case (acc, _) => acc
       }
       _ <- IO.println(referencedGames)
    } yield ()).as(ExitCode.Success)
   }
  }