package com.bidnamu.bidnamubackend.user.repository;

import com.bidnamu.bidnamubackend.user.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
