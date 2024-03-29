package de.codecentric.psd.worblehat.acceptancetests.step.business;

import de.codecentric.psd.worblehat.domain.Book;

public class DemoBookFactory {

  private Book book;

  private DemoBookFactory() {
    this.book = new Book("A book title", "A book author", "1", "123456789X", 2013, "Description");
  }

  public static DemoBookFactory createDemoBook() {
    return new DemoBookFactory();
  }

  public DemoBookFactory withTitle(String title) {
    this.book.setTitle(title);
    return this;
  }

  public DemoBookFactory withAuthor(String author) {
    this.book.setAuthor(author);
    return this;
  }

  public DemoBookFactory withEdition(String edition) {
    this.book.setEdition(edition);
    return this;
  }

  public DemoBookFactory withISBN(String isbn) {
    this.book.setIsbn(isbn);
    return this;
  }

  public DemoBookFactory withYearOfPublication(String year) {
    this.book.setYearOfPublication(Integer.parseInt(year));
    return this;
  }

  public DemoBookFactory withYearOfPublication(int yearOfPublication) {
    this.book.setYearOfPublication(yearOfPublication);
    return this;
  }

  public DemoBookFactory withDescription(String description) {
    this.book.setDescription(description);
    return this;
  }

  public Book build() {
    return book;
  }
}
