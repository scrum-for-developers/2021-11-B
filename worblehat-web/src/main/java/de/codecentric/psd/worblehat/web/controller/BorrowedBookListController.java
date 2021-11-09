package de.codecentric.psd.worblehat.web.controller;

import de.codecentric.psd.worblehat.domain.Book;
import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.web.formdata.GetBorrowedBookListFormData;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/** Controller class for the */
@Controller
@RequestMapping("/borrowedBookList")
public class BorrowedBookListController {

  private BookService bookService;

  @Autowired
  public BorrowedBookListController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping
  public String setupForm(ModelMap modelMap) {
    modelMap.put("getBorrowedBookListFormData", new GetBorrowedBookListFormData());

    // das hier muss noch mal extra passieren, hier ist die falsche Stelle dafür
    // List<Book> borrowedBooks = getBorrowedBooks(emailAddress);
    // modelMap.addAttribute("borrowerEmail", emailAddress);
    // modelMap.addAttribute("borrowedList", borrowedBooks);
    return "borrowedList";
  }

  public List<Book> getBorrowedBooks(
      @ModelAttribute("getBorrowedBookListFormData") @Valid GetBorrowedBookListFormData formData) {
    List<Book> books = bookService.findAllBooks();
    List<Book> borrowedBooks = null;
    for (Book book : books) {
      if (book.getBorrowing().getBorrowerEmailAddress() == formData.getEmailAddress()) {
        borrowedBooks.add(book);
      }
    }
    Collections.sort(
        borrowedBooks,
        new Comparator<Book>() {
          @Override
          public int compare(final Book object1, final Book object2) {
            int c = 0;
            c = object1.getTitle().compareTo(object2.getTitle());
            if (c == 0) {
              c = object1.getIsbn().compareTo(object2.getIsbn());
            }
            return c;
          }
        });
    return borrowedBooks;
  }
}
