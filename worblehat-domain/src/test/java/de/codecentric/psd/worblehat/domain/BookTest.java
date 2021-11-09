package de.codecentric.psd.worblehat.domain;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookTest {

  Book BOOK;
  Book BOOK2;

  @BeforeEach
  public void setup() {
    BOOK = new Book("Titel", "Author", "2", "1", 1234, "description");
  }

  @Test
  void shouldReturnFalseWhenAuthorisDifferent() {
    Book anotherCopy =
        new Book(
            BOOK.getTitle(),
            BOOK.getAuthor(),
            BOOK.getEdition(),
            BOOK.getIsbn(),
            BOOK.getYearOfPublication());
    anotherCopy.setAuthor("Bene");
    assertThat(BOOK.isSameCopy(anotherCopy), is(false));
  }

  @Test
  void shouldAllowBookWithDescription() {
    Book anotherCopy =
        new Book(
            BOOK.getTitle(),
            BOOK.getAuthor(),
            BOOK.getEdition(),
            BOOK.getIsbn(),
            BOOK.getYearOfPublication(),
            BOOK.getDescription());
    assertThat(BOOK.isSameCopy(anotherCopy), is(true));
  }

  @Test
  void shouldReturnFalseWhenTitleisDifferent() {
    Book anotherCopy =
        new Book(
            BOOK.getTitle(),
            BOOK.getAuthor(),
            BOOK.getEdition(),
            BOOK.getIsbn(),
            BOOK.getYearOfPublication());
    anotherCopy.setTitle("Lord of the Rings");
    assertThat(BOOK.isSameCopy(anotherCopy), is(false));
  }

  @Test
  void shouldReturnTrueWhenAllButTitleAndAuthorAreDifferent() {
    Book anotherCopy =
        new Book(
            BOOK.getTitle(),
            BOOK.getAuthor(),
            BOOK.getEdition(),
            BOOK.getIsbn(),
            BOOK.getYearOfPublication());
    anotherCopy.setEdition("2000");
    anotherCopy.setIsbn("123456789X");
    anotherCopy.setYearOfPublication(2010);
    assertThat(BOOK.isSameCopy(anotherCopy), is(true));
  }

  @Test
  void shouldBeBorrowable() {
    BOOK.borrowNowByBorrower("a@bc.de");
    assertThat(BOOK.getBorrowing().getBorrowerEmailAddress(), is("a@bc.de"));
  }

  @Test
  void shouldIgnoreNewBorrowWhenBorrowed() {
    BOOK.borrowNowByBorrower("a@bc.de");
    BOOK.borrowNowByBorrower("a@bc.ru");
    assertThat(BOOK.getBorrowing().getBorrowerEmailAddress(), is("a@bc.de"));
  }

  @Test
  void shouldReturnRelevatInfoInToString() {
    String borrowingAsString = BOOK.toString();
    assertThat(borrowingAsString, containsString("title"));
    assertThat(borrowingAsString, containsString("author"));
    assertThat(borrowingAsString, containsString("edition"));
    assertThat(borrowingAsString, containsString("isbn"));
    assertThat(borrowingAsString, containsString("yearOfPublication"));
  }
}
