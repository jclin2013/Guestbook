package guestbook;

import org.springframework.data.repository.CrudRepository;
// import org.springframework.web.bind.annotation.*;

// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.querydsl.QueryDslPredicateExecutor;
// import org.springframework.stereotype.Repository;
import java.util.List;

import guestbook.User;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

// @Repository
public interface UserRepository extends CrudRepository<User, Integer> {
  // @Query("SELECT * FROM user WHERE user.want_to_email = 0")
  // List<String > findByWantToEmail();

  List<User> findAllByWantToEmail(Integer status);
}
