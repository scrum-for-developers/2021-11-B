Feature: Adding a new book to the library

    Scenario Outline: Adding various books

        Given an empty library

        When a librarian adds a random book and the "<property>" of that book is "<value>"

        Then the booklist shows that book with "<property>" as "<value>"

        Examples:
            | property     | value           |
            | title        | Sourcery        |
            | author       | Terry Pratchett |
            | year         | 1989            |
            | isbn         | 123456789X      |
            | isbn         | 9783866400016   |
            | description  | Sourcery is a fantasy novel by British writer Terry Pratchett, the fifth book in his Discworld series, published in 1988. |

    Scenario: Adding books with special characters

      Given an empty library

      When a librarian adds a random book and the "title" of that book is "  X  "

      Then the booklist shows that book with "title" as "X"


    Scenario Outline: There can be multiple copies of the same book with the same ISBN

        Given an empty library

        When a librarian adds a book with "<title>", "<author>", <edition>, "<year>" and "<isbn>"
        And a librarian adds another book with "<title2>", "<author2>", <edition>, "<year>" and "<isbn>"

        Then the booklist contains a book with "<title>", "<author>", "<year>", <edition> and "<isbn>"
        And the library contains <nr> copies of the book with "<isbn>"

        Examples:

            | title    | author          | edition | year | isbn       | author2                | title2               | nr |
            | Sourcery | Terry Pratchett | 1       | 1989 | 0552131075 | Terry Pratchett        | Sourcery             | 2  |
            | Sourcery | Terry Pratchett | 1       | 1989 | 0552131075 | XX_DIFFERENT_AUTHOR_XX | Sourcery             | 1  |
            | Sourcery | Terry Pratchett | 1       | 1989 | 0552131075 | Terry Pratchett        | XX_DIFERENT_TITLE_XX | 1  |

    Scenario: Adding books with a description special characters

        Given an empty library

        When a librarian adds a random book and the "description" of that book is "/*-+_@&$#%) in a string?"

        Then the booklist shows that book with "description" as "/*-+_@&$#%) in a string?"


    Scenario: Adding books with no description

        Given an empty library

        When a librarian adds a random book and the "description" of that book is ""

        Then the booklist shows that book with "description" as ""

    Scenario: Adding books with longer than allowed description

        Given an empty library

        When a librarian adds a random book and the "description" of that book is "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-Blablablablabla"

        Then the booklist shows that book with "description" as "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"