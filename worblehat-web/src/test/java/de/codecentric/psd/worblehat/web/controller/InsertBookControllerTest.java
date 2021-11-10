package de.codecentric.psd.worblehat.web.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import de.codecentric.psd.worblehat.domain.Book;
import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.web.formdata.BookDataFormData;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

class InsertBookControllerTest {

  private InsertBookController insertBookController;

  private BookService bookService;

  private BookDataFormData bookDataFormData;

  private BindingResult bindingResult;

  private static final Book TEST_BOOK = new Book("title", "author", "edition", "isbn", 2016, "");

  private static final String ISBN10 = "3866400012";
  private static final String ISBN13 = "9783866400016";
  private static final String INVALID_ISBN = "123456";

  @BeforeEach
  public void setUp() {
    bookService = mock(BookService.class);
    insertBookController = new InsertBookController(bookService);
    bookDataFormData = new BookDataFormData();
    bindingResult = new MapBindingResult(new HashMap<>(), "");
  }

  @Test
  void shouldSetupForm() {
    ModelMap modelMap = new ModelMap();

    insertBookController.setupForm(modelMap);

    assertThat(modelMap.get("bookDataFormData"), is(not(nullValue())));
  }

  @Test
  void shouldRejectErrors() {
    bindingResult.addError(new ObjectError("", ""));

    String navigateTo = insertBookController.processSubmit(bookDataFormData, bindingResult);

    assertThat(navigateTo, is("insertBooks"));
  }

  @Test
  void shouldCreateBookAndNavigateToBookList() {
    setupFormData();
    when(bookService.createBook(any(), any(), any(), any(), anyInt(), any()))
        .thenReturn(Optional.of(TEST_BOOK));

    String navigateTo = insertBookController.processSubmit(bookDataFormData, bindingResult);

    verifyBookIsCreated();
    assertThat(navigateTo, is("redirect:bookList"));
  }

  @Test
  void shouldCreateBookWithValid10CharISBNAndNavigateToBookList() {
    setupFormData();
    bookDataFormData.setIsbn(ISBN10);
    when(bookService.createBook(any(), any(), any(), any(), anyInt(), any()))
        .thenReturn(Optional.of(TEST_BOOK));

    String navigateTo = insertBookController.processSubmit(bookDataFormData, bindingResult);

    verifyBookIsCreatedWithValid10CharISBN();
    assertThat(navigateTo, is("redirect:bookList"));
  }

  @Test
  void shouldCreateBookWithValid13CharISBNAndNavigateToBookList() {
    setupFormData();
    bookDataFormData.setIsbn(ISBN13);
    when(bookService.createBook(any(), any(), any(), any(), anyInt(), any()))
        .thenReturn(Optional.of(TEST_BOOK));

    String navigateTo = insertBookController.processSubmit(bookDataFormData, bindingResult);

    verifyBookIsCreatedWithValid13CharISBN();
    assertThat(navigateTo, is("redirect:bookList"));
  }

  @Test
  void shouldFailToCreateBookDueToInvalidISBN() {
    setupFormData();
    bookDataFormData.setIsbn(INVALID_ISBN);
    when(bookService.createBook(any(), any(), any(), any(), anyInt(), any()))
        .thenReturn(Optional.empty());

    String navigateTo = insertBookController.processSubmit(bookDataFormData, bindingResult);

    verifyBookIsCreatedWithInvalidISBN();
    assertThat(
        bindingResult.getGlobalErrors(),
        hasItem(hasProperty("codes", hasItemInArray("duplicateIsbn"))));
    assertThat(navigateTo, is("insertBooks"));
  }

  @Test
  void shouldStayOnInsertBookPageWhenCreatingBookFails() {
    setupFormData();
    when(bookService.createBook(any(), any(), any(), any(), anyInt(), any()))
        .thenReturn(Optional.empty());

    String navigateTo = insertBookController.processSubmit(bookDataFormData, bindingResult);

    verifyBookIsCreated();
    assertThat(
        bindingResult.getGlobalErrors(),
        hasItem(hasProperty("codes", hasItemInArray("duplicateIsbn"))));
    assertThat(navigateTo, is("insertBooks"));
  }

  private void verifyBookIsCreated() {
    verify(bookService)
        .createBook(
            TEST_BOOK.getTitle(),
            TEST_BOOK.getAuthor(),
            TEST_BOOK.getEdition(),
            TEST_BOOK.getIsbn(),
            TEST_BOOK.getYearOfPublication(),
            TEST_BOOK.getDescription());
  }

  private void verifyBookIsCreatedWithValid10CharISBN() {
    verify(bookService)
        .createBook(
            TEST_BOOK.getTitle(),
            TEST_BOOK.getAuthor(),
            TEST_BOOK.getEdition(),
            ISBN10,
            TEST_BOOK.getYearOfPublication(),
            TEST_BOOK.getDescription());
  }

  private void verifyBookIsCreatedWithValid13CharISBN() {
    verify(bookService)
        .createBook(
            TEST_BOOK.getTitle(),
            TEST_BOOK.getAuthor(),
            TEST_BOOK.getEdition(),
            ISBN13,
            TEST_BOOK.getYearOfPublication(),
            TEST_BOOK.getDescription());
  }

  private void verifyBookIsCreatedWithInvalidISBN() {
    verify(bookService)
        .createBook(
            TEST_BOOK.getTitle(),
            TEST_BOOK.getAuthor(),
            TEST_BOOK.getEdition(),
            INVALID_ISBN,
            TEST_BOOK.getYearOfPublication(),
            TEST_BOOK.getDescription());
  }

  private void setupFormData() {
    bookDataFormData.setTitle(TEST_BOOK.getTitle());
    bookDataFormData.setAuthor(TEST_BOOK.getAuthor());
    bookDataFormData.setEdition(TEST_BOOK.getEdition());
    bookDataFormData.setIsbn(TEST_BOOK.getIsbn());
    bookDataFormData.setYearOfPublication(String.valueOf(TEST_BOOK.getYearOfPublication()));
  }
}
