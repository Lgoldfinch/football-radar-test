
object Fudge extends App {

  // We need to iterate through the string
  // We need to accumulate a count of characters
  // When a duplicate is found, we need to reset the substring count down but continue on.
  // If currentSubstring contains next letter, then reset the substring but keep the count
  def lengthOfLongestSubstring(s: String): Int = {
     val res =  s.toCharArray.foldLeft((0, "")){
       case ((longestSubstring, currentSubstring), nextChar) =>
         println("RUN")
         println(s"Longest sub: $longestSubstring")
         println(s"Longest sub: $currentSubstring")
         println(s"Longest sub: $nextChar")
         println()
            if(!currentSubstring.contains(nextChar))
              (longestSubstring + 1, s"$currentSubstring$nextChar")
            else (longestSubstring, "")
  }
      res._1
  }

  println(lengthOfLongestSubstring("abcabcbb"))
//    @main def hello(): Unit = {

//  },
}
