package com.bidnamu.bidnamubackend.item.repository;

import com.bidnamu.bidnamubackend.item.domain.Category;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findAllByParentIsNull();
}
