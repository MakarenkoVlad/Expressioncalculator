package utils

/**
 * @return first is value, second is first index, third is last index
 */
fun MatchResult.decomposeMatchResult(): Triple<String, Int, Int> {
    return Triple(value, range.first, range.last)
}