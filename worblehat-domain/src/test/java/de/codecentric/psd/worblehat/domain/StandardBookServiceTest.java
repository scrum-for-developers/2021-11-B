package de.codecentric.psd.worblehat.domain;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

class StandardBookServiceTest {

  @Mock private BorrowingRepository borrowingRepository;

  @Mock private BookRepository bookRepository;

  private BookService bookService;

  private static final String BORROWER_EMAIL = "someone@codecentric.de";

  private static final LocalDate NOW = LocalDate.now();

  private static final String VALID_2K_CHARACTER_STRING =
      "wyou7J77ykHf35UDH7QnuNpax9QUhzPkURWcFRIAzS0Z2Stf1e1o2HwSeD2etahILsxxSTTx6LgKp3VnX0sioxWGfEypKEc3csNI6Hax3EnY1EaIa4CBL3vSbTgugs9zDtXj5xlpj6xNeDzUgEdDK61EvRxLlMckvS6W3971yLFBOoKO4LPSrNl1NNIjpBzp0iwDzVJv4WacDCCiM6EZTxNkzx8UIPKeZlYne99gZUCcJfSIPc27PwkLrD4e3gYYs4g0vlizoO1ttfVj7a6w4tS8lzNP4fqZNtDKag2AX765CEJJHaTYy9GfQbqP9IKTsDXIT3vjjRvMRrPIxKOEHeA5xqqopRGjHa9Uo94O6NhAfL9Rp60mpK7XZmVAFqxMwBxBefm49XYZGqkphY9W9mu0nxLAbon4m0LXmlLre1Zyyg7GnPfoS6hBO4nZVTI97ckJBRKZj1cuuSQ2wYtI0vyxYIAyz7cetfKivslMbVgn2OZb2rvPjNzC6d07x5Yva5SSt0rHme7eJHWF5RbAxUbqRkqwo5zDJPxaB2MkaZPQYHSecu9HTceDe9RAqxguM4lKWUeHT3NydfIXrXQrgUOwFyDbkTiXYpvOU2VUx9jaTC15EVmDnFUx5R2Dxl5F6ydNXFKGIqtYeiRvFHDB45zYQrda2q3B13abxLTMjrNRwhaf3DK3KEGURDJxG7VBWpGa21xonSmYf8aqeQkSdAHvB1OCWxHdT75UI4D2ixCAabQ0NkgAxPyI0P6GUPwlGY2UgR0mwHZGjQ5E72vDrDv5oJQ9oauGxxviaqvColqscvR9TwB9mstknIX498wkzKBTRefYVmOCdK90Bwu43EElT2w9n95n9QksivX8YWouHaF1IxREsdeaUsMGu5wm3GU16xFnym9KPu1FbLrOiqukDqF0l5cILSk9qWvm1wSJ2Gg4PC9nXdjIEzETEDplOQS8K9yxXpHXJmgIdAsSA1jIqkOk68plJySh8cCXosXNFB6bMrKPTPDWfzMtPAzH4KcCFsNFQfKt8SFTwgOKBGQEwDZcyi68iAgLLIqZRvWS1zpJmRkNN6lqJSLjUrmdZc5uHl82HN2Dkhei1HxWye8T7Aw5IX9a4o4WnqJ8jOKah2ZbWOJcAe4PRUMUaky461RJbsozcU9smz9duiZ5MVSrCpdbvRTQZuXanlUCQH11ZHrqZbW9hk9lUITNkpXOp29FB4ABkiwsp2RAoy5I5li6WS9J44PbtSsFpnXyY03MYKSmWIN1qu61fvAT33tiVFNc4YSyZEGwwxilZRfK1kIoDibObbvWkECqrhxuidCyFspgBVdD44TSquvDnFOtOzYwZWBI3MEIcVUzlQKCeUg8uzNnOJwBAqfeKZUxcngQYFhcLXggMLrPzRP7YLdxcsVwvwipQsWbvUNywdIqwmSacMTBBCHpvLe5UDqEzEm3IPo1fijoSyf80lz7XB7W1slXEzwZMWPkdQsCHLTgY7GKILynmP9G8OmKlTlceLQMx6y0m2BOmnpTtrSEra90VmBgArvKLwEHFhispzplIi0OEYAUB6qssX5Ta0pI5xDPqhgmMJUd7bZjO6xdMpW7smkn0nxys5ZfRKWmojB8dF5pUniYpdb2uLGMutpQ2ThcjBGksuhxYtFShZn9fOuIZSgiU4qKAbOQ786ZFLpamYKQ9eEA1K0oLu151S59GLIoyHDjooCsyNnmStR1Tnsz8yC6u7HSKNQZL90o4FDEYDgKQQOf2nC9PdLiLZesIYxkve7mdETEpCzOCAzvUfALApuDJPlnKXT5POYj4UycbAzCKRyHevUVmrF0PYD8it2p23KRbLODdFsazaHsrF8WQ9YpsZl2n7ZgUIgq8YMdKT1J6mbZjmKgYpAn5D2z8LN4noGXBgjh1dEbK5AKU9r3v9ZakKTwj637dwexYKtYJVGjbRBEjgsu6jEZc5Q4oB8emQfAz461pd5V6PG2iv1VeTjedDtktG05AFiR";

  private Book aBook, aCopyofBook, whitespaceBook;
  private Book aBorrowedBook, aCopyofBorrowedBook, anotherBorrowedBook;
  private Borrowing aBorrowing, aBorrowingOfCopy, anotherBorrowing;

  private static final String TITLE = "title";
  private static final String AUTHOR = "author";
  private static final String EDITION = "edition";
  private static final String ISBN = "title";
  private static final String DESCRIPTION = "title";

  @BeforeEach
  void setup() {
    initMocks(this);

    aBook = new Book("title", "author", "edition", "isbn", 2016, "description");
    aCopyofBook = new Book("title", "author", "edition", "isbn", 2016, "description");
    whitespaceBook =
        new Book(
            "    " + TITLE + "    ",
            "      " + AUTHOR + "      ",
            "      " + EDITION + "      ",
            "      " + ISBN + "      ",
            2016,
            "      " + DESCRIPTION + "      ");

    aBorrowedBook = new Book("title", "author", "edition", "isbn", 2016, "description");
    aBorrowing = new Borrowing(aBorrowedBook, BORROWER_EMAIL, NOW);
    aBorrowedBook.borrowNowByBorrower(BORROWER_EMAIL);

    aCopyofBorrowedBook = new Book("title", "author", "edition", "isbn", 2016, "description");
    aBorrowingOfCopy = new Borrowing(aCopyofBorrowedBook, BORROWER_EMAIL, NOW);
    aCopyofBorrowedBook.borrowNowByBorrower(BORROWER_EMAIL);

    anotherBorrowedBook = new Book("title2", "author2", "edition2", "isbn2", 2016, "description");
    anotherBorrowing = new Borrowing(anotherBorrowedBook, BORROWER_EMAIL, NOW);
    anotherBorrowedBook.borrowNowByBorrower(BORROWER_EMAIL);

    when(borrowingRepository.findBorrowingsByBorrower(BORROWER_EMAIL))
        .thenReturn(Arrays.asList(aBorrowing, anotherBorrowing));

    when(borrowingRepository.findBorrowingForBook(aBook)).thenReturn(null);

    bookService = new StandardBookService(borrowingRepository, bookRepository);
  }

  private void givenALibraryWith(Book... books) {
    Map<String, Set<Book>> bookCopies = new HashMap<>();
    for (Book book : books) {
      if (!bookCopies.containsKey(book.getIsbn())) {
        bookCopies.put(book.getIsbn(), new HashSet<>());
      }
      bookCopies.get(book.getIsbn()).add(book);
    }
    for (Map.Entry<String, Set<Book>> entry : bookCopies.entrySet()) {
      when(bookRepository.findByIsbn(entry.getKey())).thenReturn(entry.getValue());
      when(bookRepository.findTopByIsbn(entry.getKey()))
          .thenReturn(Optional.of(entry.getValue().iterator().next()));
    }
  }

  @Test
  void shouldReturnAllBooksOfOnePerson() {
    bookService.returnAllBooksByBorrower(BORROWER_EMAIL);
    verify(borrowingRepository).delete(anotherBorrowing);
  }

  @Test
  void shouldSaveBorrowingWithBorrowerEmail() {
    givenALibraryWith(aBook);
    ArgumentCaptor<Borrowing> borrowingArgumentCaptor = ArgumentCaptor.forClass(Borrowing.class);
    bookService.borrowBook(aBook.getIsbn(), BORROWER_EMAIL);
    verify(borrowingRepository).save(borrowingArgumentCaptor.capture());
    assertThat(
        borrowingArgumentCaptor.getValue().getBorrowerEmailAddress(), equalTo(BORROWER_EMAIL));
  }

  @Test()
  void shouldNotBorrowWhenBookAlreadyBorrowed() {
    givenALibraryWith(aBorrowedBook);
    Optional<Borrowing> borrowing = bookService.borrowBook(aBorrowedBook.getIsbn(), BORROWER_EMAIL);
    assertTrue(!borrowing.isPresent());
  }

  @Test
  void shouldSelectOneOfTwoBooksWhenBothAreNotBorrowed() {
    givenALibraryWith(aBook, aCopyofBook);
    ArgumentCaptor<Borrowing> borrowingArgumentCaptor = ArgumentCaptor.forClass(Borrowing.class);
    bookService.borrowBook(aBook.getIsbn(), BORROWER_EMAIL);
    verify(borrowingRepository).save(borrowingArgumentCaptor.capture());
    assertThat(borrowingArgumentCaptor.getValue().getBorrowerEmailAddress(), is(BORROWER_EMAIL));
    assertThat(
        borrowingArgumentCaptor.getValue().getBorrowedBook(),
        either(is(aBook)).or(is(aCopyofBook)));
  }

  @Test
  void shouldSelectUnborrowedOfTwoBooksWhenOneIsBorrowed() {
    givenALibraryWith(aBook, aBorrowedBook);
    ArgumentCaptor<Borrowing> borrowingArgumentCaptor = ArgumentCaptor.forClass(Borrowing.class);
    bookService.borrowBook(aBook.getIsbn(), BORROWER_EMAIL);
    verify(borrowingRepository).save(borrowingArgumentCaptor.capture());
    assertThat(borrowingArgumentCaptor.getValue().getBorrowerEmailAddress(), is(BORROWER_EMAIL));
    assertThat(borrowingArgumentCaptor.getValue().getBorrowedBook(), is(aBook));
  }

  @Test
  void shouldThrowExceptionWhenAllBooksAreBorrowedRightNow() {
    givenALibraryWith(aBorrowedBook, aCopyofBorrowedBook);
    ArgumentCaptor<Borrowing> borrowingArgumentCaptor = ArgumentCaptor.forClass(Borrowing.class);
    Optional<Borrowing> borrowing = bookService.borrowBook(aBorrowedBook.getIsbn(), BORROWER_EMAIL);
    assertThat(borrowing, isEmpty());
    verify(borrowingRepository, never()).save(any(Borrowing.class));
  }

  @Test
  void shouldCreateBook() {
    when(bookRepository.save(any(Book.class))).thenReturn(aBook);
    bookService.createBook(
        aBook.getTitle(),
        aBook.getAuthor(),
        aBook.getEdition(),
        aBook.getIsbn(),
        aBook.getYearOfPublication(),
        aBook.getDescription());

    // assert that book was saved to repository
    ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
    verify(bookRepository).save(bookArgumentCaptor.capture());

    // assert that the information was passed correctly to create the book
    assertThat(bookArgumentCaptor.getValue().getTitle(), is(aBook.getTitle()));
    assertThat(bookArgumentCaptor.getValue().getAuthor(), is(aBook.getAuthor()));
    assertThat(bookArgumentCaptor.getValue().getEdition(), is(aBook.getEdition()));
    assertThat(bookArgumentCaptor.getValue().getIsbn(), is(aBook.getIsbn()));
    assertThat(
        bookArgumentCaptor.getValue().getYearOfPublication(), is(aBook.getYearOfPublication()));
  }

  @Test
  void shouldCreateBookWithoutWhitespace() {
    when(bookRepository.save(any(Book.class))).thenReturn(aBook);
    bookService.createBook(
        whitespaceBook.getTitle(),
        whitespaceBook.getAuthor(),
        whitespaceBook.getEdition(),
        whitespaceBook.getIsbn(),
        whitespaceBook.getYearOfPublication(),
        whitespaceBook.getDescription());

    // assert that book was saved to repository
    ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
    verify(bookRepository).save(bookArgumentCaptor.capture());

    // assert that the information was passed correctly to create the book
    assertThat(bookArgumentCaptor.getValue().getTitle(), is(TITLE));
    assertThat(bookArgumentCaptor.getValue().getAuthor(), is(AUTHOR));
    assertThat(bookArgumentCaptor.getValue().getEdition(), is(EDITION));
    assertThat(bookArgumentCaptor.getValue().getIsbn(), is(ISBN));
    assertThat(
        bookArgumentCaptor.getValue().getYearOfPublication(), is(aBook.getYearOfPublication()));
    assertThat(bookArgumentCaptor.getValue().getDescription(), is(DESCRIPTION));
  }

  @Test
  void shouldCreateBookWithLimitedNumberOfCharacters() {
    when(bookRepository.save(any(Book.class))).thenReturn(aBook);
    bookService.createBook(
        aBook.getTitle(),
        aBook.getAuthor(),
        aBook.getEdition(),
        aBook.getIsbn(),
        aBook.getYearOfPublication(),
        VALID_2K_CHARACTER_STRING + "das sollte keiner sehen");

    // assert that book was saved to repository
    ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
    verify(bookRepository).save(bookArgumentCaptor.capture());

    // assert that the information was passed correctly to create the book
    assertThat(bookArgumentCaptor.getValue().getTitle(), is(aBook.getTitle()));
    assertThat(bookArgumentCaptor.getValue().getAuthor(), is(aBook.getAuthor()));
    assertThat(bookArgumentCaptor.getValue().getEdition(), is(aBook.getEdition()));
    assertThat(bookArgumentCaptor.getValue().getIsbn(), is(aBook.getIsbn()));
    assertThat(
        bookArgumentCaptor.getValue().getYearOfPublication(), is(aBook.getYearOfPublication()));
    assertThat(bookArgumentCaptor.getValue().getDescription(), is(VALID_2K_CHARACTER_STRING));
  }

  @Test
  void shouldCreateBookWithExactly2KCharacters() {
    when(bookRepository.save(any(Book.class))).thenReturn(aBook);
    bookService.createBook(
        aBook.getTitle(),
        aBook.getAuthor(),
        aBook.getEdition(),
        aBook.getIsbn(),
        aBook.getYearOfPublication(),
        VALID_2K_CHARACTER_STRING);

    // assert that book was saved to repository
    ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
    verify(bookRepository).save(bookArgumentCaptor.capture());

    // assert that the information was passed correctly to create the book
    assertThat(bookArgumentCaptor.getValue().getTitle(), is(aBook.getTitle()));
    assertThat(bookArgumentCaptor.getValue().getAuthor(), is(aBook.getAuthor()));
    assertThat(bookArgumentCaptor.getValue().getEdition(), is(aBook.getEdition()));
    assertThat(bookArgumentCaptor.getValue().getIsbn(), is(aBook.getIsbn()));
    assertThat(
        bookArgumentCaptor.getValue().getYearOfPublication(), is(aBook.getYearOfPublication()));
    assertThat(bookArgumentCaptor.getValue().getDescription(), is(VALID_2K_CHARACTER_STRING));
  }

  @Test
  void shouldCreateAnotherCopyOfExistingBook() {
    when(bookRepository.save(any(Book.class))).thenReturn(aBook);
    bookService.createBook(
        aBook.getTitle(),
        aBook.getAuthor(),
        aBook.getEdition(),
        aBook.getIsbn(),
        aBook.getYearOfPublication(),
        aBook.getDescription());
    verify(bookRepository, times(1)).save(any(Book.class));
  }

  @Test
  void shouldNotCreateAnotherCopyOfExistingBookWithDifferentTitle() {
    givenALibraryWith(aBook);
    bookService.createBook(
        aBook.getTitle() + "X",
        aBook.getAuthor(),
        aBook.getEdition(),
        aBook.getIsbn(),
        aBook.getYearOfPublication(),
        aBook.getDescription());
    verify(bookRepository, times(0)).save(any(Book.class));
  }

  @Test
  void shouldNotCreateAnotherCopyOfExistingBookWithDifferentAuthor() {
    givenALibraryWith(aBook);
    bookService.createBook(
        aBook.getTitle(),
        aBook.getAuthor() + "X",
        aBook.getEdition(),
        aBook.getIsbn(),
        aBook.getYearOfPublication(),
        aBook.getDescription());
    verify(bookRepository, times(0)).save(any(Book.class));
  }

  @Test
  void shouldFindAllBooks() {
    List<Book> expectedBooks = new ArrayList<>();
    expectedBooks.add(aBook);
    when(bookRepository.findAllByOrderByTitle()).thenReturn(expectedBooks);
    List<Book> actualBooks = bookService.findAllBooks();
    assertThat(actualBooks, is(expectedBooks));
  }

  @Test
  void shouldVerifyExistingBooks() {
    when(bookRepository.findByIsbn(aBook.getIsbn())).thenReturn(Collections.singleton(aBook));
    Boolean bookExists = bookService.bookExists(aBook.getIsbn());
    assertTrue(bookExists);
  }

  @Test
  void shouldVerifyNonexistingBooks() {
    when(bookRepository.findByIsbn(aBook.getIsbn())).thenReturn(Collections.emptySet());
    Boolean bookExists = bookService.bookExists(aBook.getIsbn());
    assertThat(bookExists, is(false));
  }

  @Test
  void shouldFindAllCopiesOfABook() {
    when(bookRepository.findByIsbn("isbn")).thenReturn(Set.of(aBook, aCopyofBook));
    Set<Book> booksByIsbn = bookService.findBooksByIsbn("isbn");
    assertThat(booksByIsbn, everyItem(hasProperty("isbn", is("isbn"))));
  }

  @Test
  void shouldDeleteAllBooksAndBorrowings() {
    bookService.deleteAllBooks();
    verify(bookRepository).deleteAll();
    verify(borrowingRepository).deleteAll();
  }

  @Test
  void shouldSaveUpdatedBookToRepo() {
    bookService.updateBook(aBook);
    verify(bookRepository).save(aBook);
  }
}
