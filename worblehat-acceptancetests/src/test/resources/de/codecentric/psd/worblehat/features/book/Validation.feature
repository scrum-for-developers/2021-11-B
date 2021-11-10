Feature: Testing for basic validation when adding new books

  Scenario Outline: Providing invalid input data for new books

    Given an empty library

    When a librarian adds a book with "<title>", "<author>", <edition>, "<year>" and "<isbn>"

    Then the page contains error message for field "<field>"
    And The library contains no books

    Examples:

      | isbn          | author          | title    | edition | year | field   |
      | 0XXXXXXXX5    | Terry Pratchett | Sourcery | 1       | 2001 | isbn    |
      | 0552131075    | Terry Pratchett | Sourcery | 1       | 1    | year    |
      | 9783866400017 | Terry Pratchett | Sourcery | 1       | 2004 | isbn    |

