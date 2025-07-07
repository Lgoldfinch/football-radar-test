import cats.syntax.all.*

import io.circe.*
import io.circe.derivation.*
import io.circe.derivation.Configuration.default

import java.time.LocalDate

given configuration: Configuration = default

final case class Result(
                       date: LocalDate,
                       homeTeam: String,
                       awayTeam: String,
                       homeGoals: Int,
                       awayGoals: Int
                       ) derives ConfiguredDecoder