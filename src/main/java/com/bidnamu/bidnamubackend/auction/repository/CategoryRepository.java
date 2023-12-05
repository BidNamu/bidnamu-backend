package com.bidnamu.bidnamubackend.auction.repository;

import com.bidnamu.bidnamubackend.auction.domain.Category;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findAllByParentIsNull();
}
