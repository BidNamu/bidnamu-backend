package com.bidnamu.bidnamubackend.auth.repository;

import com.bidnamu.bidnamubackend.auth.domain.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {
}
