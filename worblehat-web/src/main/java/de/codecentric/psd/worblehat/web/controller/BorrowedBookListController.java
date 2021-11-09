package de.codecentric.psd.worblehat.web.controller;

import de.codecentric.psd.worblehat.domain.Book;
import de.codecentric.psd.worblehat.domain.BookService;
import de.codecentric.psd.worblehat.web.formdata.ReturnAllBooksFormData;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Controller class for the */
@Controller
@RequestMapping("/borrowedBookList")
public class BorrowedBookListController {

  private BookService bookService;

  @GetMapping
  public String setupForm(ModelMap modelMap, String email) {
    List<Book> books = bookService.findAllBooks();
    List<Book> borrowedBooks = null;
    for (Book book : books) {
      if(book.getBorrowing().getBorrowerEmailAddress() == email) {
        borrowedBooks.add(book);
      }
    }
    Collections.sort(borrowedBooks, new Comparator<Book>() {
      @Override
      public int compare(final Book object1, final Book object2) {
        return object1.getTitle().compareTo(object2.getTitle());
      }
    });
    modelMap.addAttribute("borrowedBookList", borrowedBooks);
    return "borrowedBookList";
  }
}